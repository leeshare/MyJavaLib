package product;

import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.state.ListState;
import org.apache.flink.api.common.state.ListStateDescriptor;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import scala.collection.mutable.ListBuffer;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.util.*;

/**
 * 需求：每隔5分钟，统计最近1小时热门商品
 *
 * 步骤：读取数据》
 *      添加水位》
 *      过滤用户行为》
 *      按商品分组》
 *      统计窗口数据》
 *      根据窗口分组》
 *      商品TopN排序》
 *      打印输出
 */
public class HotProduct {
    
    private static class UserBehavior {
        public Long userId;
        public Long productId;
        public Integer categoryId;
        public String beahvior;
        public Long timestamp;

        public UserBehavior(Long userId, Long productId, Integer categoryId, String behavior, Long timestamp) {
            this.userId = userId;
            this.productId = productId;
            this.categoryId = categoryId;
            this.beahvior = behavior;
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            return "UserBehavior{" +
                    "userId=" + userId +
                    ", productId=" + productId +
                    ", categoryId=" + categoryId +
                    ", beahvior='" + beahvior + '\'' +
                    ", timestamp=" + timestamp +
                    '}';
        }
    }
    
    private static class ProductViewCount //implements Comparable<ProductViewCount>
    {
        public Long productId;
        public Long windowEnd;
        public Long count;

        public ProductViewCount(Long productId, Long windowEnd, Long count) {
            this.productId = productId;
            this.windowEnd = windowEnd;
            this.count = count;
        }

        @Override
        public String toString() {
            return "ProductViewCount{" +
                    "productId=" + productId +
                    ", windowEnd=" + windowEnd +
                    ", count=" + count +
                    '}';
        }

        /*@Override
        public int compareTo(ProductViewCount o) {
            return o.count > this.count ? 1 : -1;
        }*/
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setParallelism(1);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //一、读取数据
        env.readTextFile("C:\\BigData\\data\\resources\\data1.csv").map(new MapFunction<String, UserBehavior>() {
            @Override
            public UserBehavior map(String s) throws Exception {
                String[] fields = s.split(",");
                UserBehavior userBehavior = new UserBehavior(Long.valueOf(fields[0]),
                        Long.valueOf(fields[1]),
                        Integer.valueOf(fields[2]),
                        fields[3],
                        Long.valueOf(fields[4]));
                return userBehavior;
            }
            //二、添加水位
        }).assignTimestampsAndWatermarks(new ProductEventTimeExtractor())
                //三、过滤用户行为
                .filter(new FilterFunction<UserBehavior>() {
                    @Override
                    public boolean filter(UserBehavior userBehavior) throws Exception {
                        return userBehavior.beahvior.equals("pv");
                    }
                })
                //四、按商品分组
                .keyBy(line -> line.productId)
                //四、按商品分组
                .timeWindow(Time.hours(1), Time.minutes(5))
                //五、统计窗口数据
                //sum() 用sum 返回的信息量不够，所以使用整合 aggregate
                .aggregate(new ProductCount(), new WindowResult())
                //六、商品TopN
                //按窗口进行分组
                .keyBy(line -> line.windowEnd)
                .process(new TopHotProduct())
                //七、打印输出
                .print();

        env.execute(HotProduct.class.getSimpleName());


    }

    /**
     *
     * key：商品Id
     * 输入：ProductViewCount
     * 输出：一个字符串
     */
    private static class TopHotProduct extends KeyedProcessFunction<Long, ProductViewCount, String> {
        private ListState<ProductViewCount> productState;

        @Override
        public void open(Configuration parameters) throws Exception {
            //super.open(parameters);
            productState = getRuntimeContext().getListState(
                    new ListStateDescriptor<ProductViewCount>("product-state", ProductViewCount.class)
            );
        }

        //里面逻辑很简单，目的就是求 TopN
        @Override
        public void processElement(ProductViewCount productViewCount, Context context, Collector<String> collector) throws Exception {
            productState.add(productViewCount);
            //注册一个定时器：那么何时触发呢？ 答：只要ProductViewCount的数据到期了即可。
            context.timerService().registerEventTimeTimer(productViewCount.windowEnd + 1);
        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
            //super.onTimer(timestamp, ctx, out);
            List<ProductViewCount> allProduct = new ArrayList<ProductViewCount>();
            Iterator<ProductViewCount> iterator = productState.get().iterator();
            while(iterator.hasNext()) {
                allProduct.add(iterator.next());
            }
            //allProduct.sort(ProductViewCount::compareTo);
            //List<ProductViewCount> sortedProduct = allProduct.subList(0, 4);
            Collections.sort(allProduct, new Comparator<ProductViewCount>() {
                @Override
                public int compare(ProductViewCount o1, ProductViewCount o2) {
                    return o2.count.compareTo(o1.count);
                }
            });
            List<ProductViewCount> sortedProduct = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                sortedProduct.add(allProduct.get(i));
            }
            allProduct.clear();
            //把结果拼接成字符串，输出去
            StringBuilder sb = new StringBuilder();
            sb.append("时间：").append(new Timestamp(timestamp - 1)).append("\n");
            for (ProductViewCount product : sortedProduct) {
                sb.append(" 商品ID：").append(product.productId)
                        .append(" 商品浏览量=").append(product.count)
                        //.append(" 商品浏览量=").append(product.count)
                        .append("\n");
            }
            sb.append("=====================");
            out.collect(sb.toString());
        }
    }

    /**
     *
     * 输入（是ProductCount的输出，即累加值），
     * 输出（ProductViewCount），
     * key（productId），
     * window类型
     */
    private static class WindowResult implements WindowFunction<Long, ProductViewCount, Long, TimeWindow> {

        @Override
        public void apply(Long key, TimeWindow timeWindow, Iterable<Long> input, Collector<ProductViewCount> out) throws Exception {
            out.collect(new ProductViewCount(key, timeWindow.getEnd(), input.iterator().next()));
        }
    }


    /**
     * 实现累加的效果
     *
     * aggregate 整合
     * 输入，辅助累加变量，输出
     */
    private static class ProductCount implements AggregateFunction<UserBehavior, Long, Long> {

        //创建一个辅助遍历，然后赋上初始值
        @Override
        public Long createAccumulator() {
            return 0L;
        }

        @Override
        public Long add(UserBehavior userBehavior, Long acc) {
            return acc+1;
        }

        @Override
        public Long getResult(Long acc) {
            return acc;
        }

        @Override
        public Long merge(Long acc, Long acc1) {
            return acc + acc1;
        }
    }

    //计算我们的watermark
    private static class ProductEventTimeExtractor implements AssignerWithPeriodicWatermarks<UserBehavior> {
        Long currentMaxEventTime = 0L;
        Long maxOutputOrderness = 10000L;

        //获取watermark
        @Nullable
        @Override
        public Watermark getCurrentWatermark() {
            return new Watermark(currentMaxEventTime - maxOutputOrderness);
        }

        //设置最大窗口时间
        @Override
        public long extractTimestamp(UserBehavior userBehavior, long l) {
            currentMaxEventTime = Math.max(userBehavior.timestamp, currentMaxEventTime);
            //由于数据是精确到秒，而watermark要精确到毫秒，所以 *1000
            Long timestamp = userBehavior.timestamp * 1000;
            return timestamp;
        }
    }
}
