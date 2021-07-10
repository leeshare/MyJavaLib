package org.lixl.hadoop.lixlsource.metrics2.util;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;

import java.util.*;

/**
 * 分位数示例
 * Created by lxl on 20/2/2.
 */
@InterfaceAudience.Private
public class SampleQuantiles implements QuantileEstimator {

    /**
     * 在流中项的总数
     */
    private long count = 0;

    /**
     * 当前列表的样品项,维护在已排序 误差范围
     */
    private LinkedList<SampleItem> samples;

    /**
     * 缓存收入项作为批量插入. 项是线性插入缓存的.但缓存满时, 它全部冲进样品数组
     */
    private long[] buffer = new long[500];
    private int bufferCount = 0;

    /**
     * 我们关注的分位数数组, 随同期望误差
     */
    private final Quantile quantiles[];

    public SampleQuantiles(Quantile[] quantiles) {
        this.quantiles = quantiles;
        this.samples = new LinkedList<SampleItem>();
    }

    /**
     * 制定允许误差范围,依赖成为目标的分位数
     *
     * @param rank
     * @return
     */
    private double allowableError(int rank) {
        int size = samples.size();
        double minError = size + 1;
        for (Quantile q : quantiles) {
            double error;
            if (rank <= q.quantile * size) {
                error = (2.0 * q.error * (size - rank)) / (1.0 - q.quantile);
            } else {
                error = (2.0 * q.error * rank) / q.quantile;
            }
            if (error < minError) {
                minError = error;
            }
        }
        return minError;
    }

    /**
     * 从流中添加一个新值
     *
     * @param v
     */
    synchronized public void insert(long v) {
        buffer[bufferCount] = v;
        bufferCount++;

        count++;

        if (bufferCount == buffer.length) {
            insertBatch();
            compress();
        }
    }

    /**
     * 在一次通过 从缓存中合并项到样本数组
     * 这更高效的比每次插入一个
     */
    private void insertBatch() {
        if (bufferCount == 0) {
            return;
        }

        Arrays.sort(buffer, 0, bufferCount);

        int start = 0;
        if (samples.size() == 0) {
            SampleItem newItem = new SampleItem(buffer[0], 1, 0);
            samples.add(newItem);
            start++;
        }

        ListIterator<SampleItem> it = samples.listIterator();
        SampleItem item = it.next();
        for (int i = start; i < bufferCount; i++) {
            long v = buffer[i];
            while (it.nextIndex() < samples.size() && item.value < v) {
                item = it.next();
            }
            //如果我们找到更大的项, 备份这样在它之前我们插入自己
            if (item.value > v) {
                it.previous();
            }
            //
            int delta;
            if (it.previousIndex() == 0 || it.nextIndex() == samples.size()) {
                delta = 0;
            } else {
                delta = ((int) Math.floor(allowableError(it.nextIndex()))) - 1;
            }
            SampleItem newItem = new SampleItem(v, 1, delta);
            it.add(newItem);
            item = newItem;
        }

        bufferCount = 0;
    }

    /**
     * 从样例项中,试着移除无效项.这个用于检查如果一个项是否不是必须的期望误差,
     * 并且用临近项合并它
     */
    private void compress() {
        if (samples.size() < 2) {
            return;
        }

        ListIterator<SampleItem> it = samples.listIterator();
        SampleItem prev = null;
        SampleItem next = it.next();

        while (it.hasNext()) {
            prev = next;
            next = it.next();
            if (prev.g + next.g + next.delta <= allowableError(it.previousIndex())) {
                next.g += prev.g;
                // Remove prev.
                it.previous();
                it.previous();
                it.remove();
                //
                it.next();
            }
        }
    }

    /**
     * 在指定分位数 获取估算值
     *
     * @param quantile 比如 0.50 或 0.99
     * @return
     */
    private long query(double quantile) {
        Preconditions.checkState(!samples.isEmpty(), "在估算器没有数据");

        int rankMin = 0;
        int desired = (int) (quantile * count);

        ListIterator<SampleItem> it = samples.listIterator();
        SampleItem prev = null;
        SampleItem cur = it.next();
        for (int i = 1; i < samples.size(); i++) {
            prev = cur;
            cur = it.next();

            rankMin += prev.g;

            if (rankMin + cur.g + cur.delta > desired + (allowableError(i) / 2)) {
                return prev.value;
            }
        }
        //想要的最大值的边缘案例
        return samples.get(samples.size() - 1).value;
    }

    synchronized public Map<Quantile, Long> snapshot() {
        //为了最好的结果首先刷新缓存
        insertBatch();

        if (samples.isEmpty()) {
            return null;
        }

        Map<Quantile, Long> values = new TreeMap<>();
        for (int i = 0; i < quantiles.length; i++) {
            values.put(quantiles[i], query(quantiles[i].quantile));
        }
        return values;
    }

    synchronized public long getCount() {
        return count;
    }

    @VisibleForTesting
    synchronized public int getSampleCount() {
        return samples.size();
    }

    synchronized public void clear() {
        count = 0;
        bufferCount = 0;
        samples.clear();
    }

    @Override
    synchronized public String toString() {
        Map<Quantile, Long> data = snapshot();
        if (data == null) {
            return "[无样例]";
        } else {
            return Joiner.on("\n").withKeyValueSeparator(": ").join(data);
        }
    }


    /**
     * 描述一个测量值传给估值器,跟踪CKMS算法需要的附加信息
     */
    private static class SampleItem {

        /**
         * 样例的值(如 一个测量的潜在值)
         */
        public final long value;
        /**
         * 在上一个最小可能范围和这个最小可能范围间的差.
         * g的合值:所有前项
         */
        public int g;
        /**
         * 项最大可能范围和最小可能范围的差
         */
        public final int delta;

        public SampleItem(long value, int lowerDelta, int delta) {
            this.value = value;
            this.g = lowerDelta;
            this.delta = delta;
        }

        @Override
        public String toString() {
            return String.format("%d, %d, %d", value, g, delta);
        }
    }

}
