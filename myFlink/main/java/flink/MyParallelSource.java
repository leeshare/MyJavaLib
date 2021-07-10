package flink;

import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;

/**
 * 自定义支持并行度的数据源
 * <p>
 * 多并行度数据源 实现自 ParallelSourceFunction<T>
 */
public class MyParallelSource implements ParallelSourceFunction<Long> {
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
