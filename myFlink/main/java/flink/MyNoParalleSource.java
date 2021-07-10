package flink;

import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * 自定义单并行度数据源
 * 功能：每秒产生一条数据
 * <p>
 * 单并行度数据源 SourceFunction<T>
 */
public class MyNoParalleSource implements SourceFunction<Long> {
    private long number = 1L;
    private boolean isRunning = true;

    @Override
    public void run(SourceContext<Long> sourceContext) throws Exception {
        while (isRunning) {
            sourceContext.collect(number);
            number++;
            //每秒生成一条数据
            Thread.sleep(1000);
        }
    }

    @Override
    public void cancel() {
        isRunning = false;
    }
}
