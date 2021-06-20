package product;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class LoginCheckWithCEP {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        KeyedStream<LoginEvent, Long> loginEventStream = env.readTextFile("C:\\BigData\\data\\resources\\data4.csv")
                .map(data -> {
                    String[] fields = data.split(",");
                    LoginEvent loginEvent = new LoginEvent(Long.valueOf(fields[0]),
                            fields[1].trim(),
                            fields[2].trim(),
                            Long.valueOf(fields[3].trim()));
                    return loginEvent;
                }).assignTimestampsAndWatermarks(new LoginCheckEventTimeExtractor())
                .keyBy(data -> data.userId);

        //步骤二、定义匹配模式
        Pattern<LoginEvent, LoginEvent> loginFailPattern = Pattern.<LoginEvent>begin("begin")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equalsIgnoreCase("fail");
                    }
                }).next("next")
                .where(new SimpleCondition<LoginEvent>() {
                    @Override
                    public boolean filter(LoginEvent loginEvent) throws Exception {
                        return loginEvent.eventType.equalsIgnoreCase("fail");
                    }
                }).within(Time.seconds(3));

        //步骤三、在事件流上应用模式，得到一个pattern stream
        PatternStream<LoginEvent> patternStream = CEP.pattern(loginEventStream, loginFailPattern);

        //步骤四、从pattern stream上应用select function，检出匹配事件序列
        SingleOutputStreamOperator<Warning> loginFailDataStream = patternStream.select(new LoginFailMatch());
        loginFailDataStream.print();
        env.execute("login fail check ......");

    }

    private static class LoginEvent {
        private Long userId;
        private String ip;
        private String eventType;
        private Long eventTime;

        public LoginEvent(Long userId, String ip, String eventType, Long eventTime) {
            this.userId = userId;
            this.ip = ip;
            this.eventType = eventType;
            this.eventTime = eventTime;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getEventType() {
            return eventType;
        }

        public void setEventType(String eventType) {
            this.eventType = eventType;
        }

        public Long getEventTime() {
            return eventTime;
        }

        public void setEventTime(Long eventTime) {
            this.eventTime = eventTime;
        }

    }

    private static class Warning {
        private Long userId;
        private Long firstFailTime;
        private Long lastFailTime;
        private String warningMsg;

        public Warning(Long userId, Long firstFailTime, Long lastFailTime, String warningMsg) {
            this.userId = userId;
            this.firstFailTime = firstFailTime;
            this.lastFailTime = lastFailTime;
            this.warningMsg = warningMsg;
        }

        public Long getUserId() {
            return userId;
        }

        public void setUserId(Long userId) {
            this.userId = userId;
        }

        public Long getFirstFailTime() {
            return firstFailTime;
        }

        public void setFirstFailTime(Long firstFailTime) {
            this.firstFailTime = firstFailTime;
        }

        public Long getLastFailTime() {
            return lastFailTime;
        }

        public void setLastFailTime(Long lastFailTime) {
            this.lastFailTime = lastFailTime;
        }

        public String getWarningMsg() {
            return warningMsg;
        }

        public void setWarningMsg(String warningMsg) {
            this.warningMsg = warningMsg;
        }

        @Override
        public String toString() {
            return "Warning{" +
                    "userId=" + userId +
                    ", firstFailTime=" + firstFailTime +
                    ", lastFailTime=" + lastFailTime +
                    ", warningMsg='" + warningMsg + '\'' +
                    '}';
        }
    }

    private static class LoginCheckEventTimeExtractor implements AssignerWithPeriodicWatermarks<LoginEvent> {
        Long currentMaxEventTime = 0L;
        Long maxOutOfOrderness = 10000L;

        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOutOfOrderness);
        }

        @Override
        public long extractTimestamp(LoginEvent loginEvent, long l) {
            Long timestamp = loginEvent.eventTime * 1000;
            currentMaxEventTime = Math.max(currentMaxEventTime, timestamp);
            return timestamp;
        }
    }

    /**
     * 第一个参数 LoginEvent是 输入类型；
     * 第二个参数Warning是 输出类型
     */
    private static class LoginFailMatch implements PatternSelectFunction<LoginEvent, Warning> {

        @Override
        public Warning select(Map<String, List<LoginEvent>> map) throws Exception {
            LoginEvent firstFail = map.get("begin").iterator().next();
            LoginEvent lastFail = map.get("next").iterator().next();
            return new Warning(firstFail.userId, firstFail.eventTime, lastFail.eventTime, "login fail!");
        }
    }

}
