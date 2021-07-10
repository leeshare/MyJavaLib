package flink.dataset;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

public class WordCount {
    public static void main(String[] args) throws Exception {
        //一、获取离线程序入口
        ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        String inputPath = "";
        //二、获取数据源
        DataSource<String> dataSet = env.readTextFile(inputPath);
        //三、数据处理
        //dataSet.map()

    }
}
