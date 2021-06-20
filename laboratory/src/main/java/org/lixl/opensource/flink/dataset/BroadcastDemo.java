package org.lixl.opensource.flink.dataset;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.api.java.operators.MapOperator;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 广播变量 broadcast
 * 需求：
 *  flink会从数据源中获取用户名
 *  最终需要把用户的姓名和年龄打印出来
 * 分析：
 *  所需要在中间map处理的时候获取用户的年龄信息
 *  建立把用户关系数据集使用广播变量进行处理
 */
public class BroadcastDemo {
    public static void main(String[] args) throws Exception {
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        ArrayList<Tuple2<String, Integer>> broadData = new ArrayList<>();
        broadData.add(new Tuple2<>("张三", 18));
        broadData.add(new Tuple2<>("李四", 20));
        broadData.add(new Tuple2<>("王五", 17));
        //
        DataSource<Tuple2<String, Integer>> tupleData = env.fromCollection(broadData);
        MapOperator<Tuple2<String, Integer>, HashMap<String, Integer>> toBroadcast = tupleData.map(new MapFunction<Tuple2<String, Integer>, HashMap<String, Integer>>() {
            @Override
            public HashMap<String, Integer> map(Tuple2<String, Integer> value) throws Exception {
                HashMap<String, Integer> res = new HashMap<>();
                res.put(value.f0, value.f1);
                return res;
            }
        });
        //源数据
        DataSource<String> data = env.fromElements("张三", "李四", "王五");
        //用 RichMapFunction获取广播变量
        // RichMapFunction 比 MapFunction多了比如 open方法 等
        MapOperator<String, String> result = data.map(new RichMapFunction<String, String>() {

            List<HashMap<String, Integer>> boradcastMap = new ArrayList<>();
            HashMap<String, Integer> allMap = new HashMap<>();

            @Override
            public void open(Configuration params) throws Exception {
                super.open(params);
                this.boradcastMap = getRuntimeContext().getBroadcastVariable("broadcastMapName");
                for (HashMap map : boradcastMap) {
                    allMap.putAll(map);
                }
            }

            @Override
            public String map(String value) throws Exception {
                Integer age = allMap.get(value);
                return value + "," + age;
            }
        }).withBroadcastSet(toBroadcast, "broadcastMapName");

        result.print();

    }
}
