package org.lixl.opensource.flink.stream;

import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.co.RichCoFlatMapFunction;
import org.apache.flink.util.Collector;

import static org.lixl.opensource.flink.stream.FileSource.OrderInfo1.string2OrderInfo1;
import static org.lixl.opensource.flink.stream.FileSource.OrderInfo2.string2OrderInfo2;

public class OrderStream {
    public static void main(String[] args) throws Exception {
        //步骤一 获取执行环境
        StreamExecutionEnvironment env =
                StreamExecutionEnvironment.getExecutionEnvironment();
        //步骤二 读取数据源 读数据
        DataStreamSource<String> info1 = env.addSource(new
                FileSource(FileSource.Constants.ORDER_INFO1_PATH));
        DataStreamSource<String> info2 = env.addSource(new
                FileSource(FileSource.Constants.ORDER_INFO2_PATH));
        KeyedStream<FileSource.OrderInfo1, Long> orderInfo1Stream = info1.map(line ->
                string2OrderInfo1(line))
                .keyBy(orderInfo1 -> orderInfo1.getOrderId());
        KeyedStream<FileSource.OrderInfo2, Long> orderInfo2Stream = info2.map(line ->
                string2OrderInfo2(line))
                .keyBy(orderInfo2 -> orderInfo2.getOrderId());
        // 把两个流 拼接在一起
        orderInfo1Stream.connect(orderInfo2Stream)
                .flatMap(new EnrichmentFunction())
                .print();
        env.execute("OrderStream");
    }
    /**
     * IN1, 第一个流的输入的数据类型
     IN2, 第二个流的输入的数据类型
     OUT，输出的数据类型
     */
    public static class EnrichmentFunction extends
            RichCoFlatMapFunction<FileSource.OrderInfo1, FileSource.OrderInfo2, Tuple2<FileSource.OrderInfo1, FileSource.OrderInfo2>> {
        //定义第一个流 key对应的state
        private ValueState<FileSource.OrderInfo1> orderInfo1State;
        //定义第二个流 key对应的state
        private ValueState<FileSource.OrderInfo2> orderInfo2State;
        @Override
        public void open(Configuration parameters) {
            //注册state
            orderInfo1State = getRuntimeContext()
                    .getState(new ValueStateDescriptor<FileSource.OrderInfo1>("info1",
                            FileSource.OrderInfo1.class));
            orderInfo2State = getRuntimeContext()
                    .getState(new ValueStateDescriptor<FileSource.OrderInfo2>
                            ("info2", FileSource.OrderInfo2.class));
        }

        //orderInfo1 = 123
        @Override
        public void flatMap1(FileSource.OrderInfo1 orderInfo1, Collector<Tuple2<FileSource.OrderInfo1,
                        FileSource.OrderInfo2>> out) throws Exception {
            //去 orderInfo2
            FileSource.OrderInfo2 value2 = orderInfo2State.value();
            if(value2 != null){
                //有数据，说明 key相同？？？？？
                orderInfo2State.clear();
                out.collect(Tuple2.of(orderInfo1,value2));
            }else{
                //放到state里面，等一等
                orderInfo1State.update(orderInfo1);
            }
        }
        @Override
        public void flatMap2(FileSource.OrderInfo2 orderInfo2, Collector<Tuple2<FileSource.OrderInfo1,
                FileSource.OrderInfo2>> out)throws Exception {
            FileSource.OrderInfo1 value1 = orderInfo1State.value();
            if(value1 != null){
                orderInfo1State.clear();
                out.collect(Tuple2.of(value1,orderInfo2));
            }else{
                orderInfo2State.update(orderInfo2);
            }
        }
    }
}
