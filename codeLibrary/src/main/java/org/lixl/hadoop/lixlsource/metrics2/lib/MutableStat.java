package org.lixl.hadoop.lixlsource.metrics2.lib;

import org.apache.commons.lang.StringUtils;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;
import org.lixl.hadoop.lixlsource.metrics2.MetricsRecordBuilder;
import org.lixl.hadoop.lixlsource.metrics2.util.SampleStat;

import static org.lixl.hadoop.lixlsource.metrics2.lib.Interns.info;

/**
 * 一个可变指标 附带统计
 */
@InterfaceAudience.Public
@InterfaceStability.Evolving
public class MutableStat extends MutableMetric {
    private final MetricsInfo numInfo;
    private final MetricsInfo avgInfo;
    private final MetricsInfo stdevInfo;
    private final MetricsInfo iMinInfo;
    private final MetricsInfo iMaxInfo;
    private final MetricsInfo minInfo;
    private final MetricsInfo maxInfo;
    private final MetricsInfo iNumInfo;

    private final SampleStat intervalStat = new SampleStat();
    private final SampleStat prevStat = new SampleStat();
    private final SampleStat.MinMax minMax = new SampleStat.MinMax();
    private long numSamples = 0;
    private boolean extended = false;

    public MutableStat(String name, String description,
                       String sampleName, String valueName, boolean extended) {
        String ucName = StringUtils.capitalize(name);   //转为首字母大写
        String usName = StringUtils.capitalize(sampleName);
        String uvName = StringUtils.capitalize(valueName);
        String desc = StringUtils.uncapitalize(description);
        String lsName = StringUtils.uncapitalize(sampleName);
        String lvName = StringUtils.uncapitalize(valueName);
        numInfo = info(ucName + "Num" + usName, "Number of " + lsName + " for " + desc);
        iNumInfo = info(ucName + "INum" + usName, "Interval number of " + lsName + " for " + desc);
        avgInfo = info(String.format("%sAvg%s", ucName, uvName), String.format("Average %s for %s", lvName, desc));
        stdevInfo = info(String.format("%sStdev%s", ucName, uvName), String.format("Standard deviation of %s for %s", lvName, desc));
        iMinInfo = info(String.format("%sIMin%s", ucName, uvName), String.format("Interval min %s for %s", lvName, desc));
        iMaxInfo = info(String.format("%sIMax%s", ucName, uvName), String.format("Interval max %s for %s", lvName, desc));
        minInfo = info(String.format("%sMin%s", ucName, uvName), String.format("Min %s for %s", lvName, desc));
        maxInfo = info(String.format("%sMax%s", ucName, uvName), String.format("Max %s for %s", lvName, desc));
        this.extended = extended;
    }

    public MutableStat(String name, String description, String sampleName, String valueName) {
        this(name, description, sampleName, valueName, false);
    }

    public synchronized void setExtended(boolean extended) {
        this.extended = extended;
    }

    public synchronized void add(long numSamples, long sum) {
        intervalStat.add(numSamples, sum);
        setChanged();
    }

    public synchronized void add(long value) {
        intervalStat.add(value);
        minMax.add(value);
        setChanged();
    }

    @Override
    public synchronized void snapshot(MetricsRecordBuilder builder, boolean all) {
        if(all || changed()) {
            numSamples += intervalStat.numSamples();
            builder.addCounter(numInfo, numSamples).addGauge(avgInfo, lastStat().mean());
            if(extended) {
                builder.addGauge(stdevInfo, lastStat().stddev())
                        .addGauge(iMinInfo, lastStat().min())
                        .addGauge(iMaxInfo, lastStat().max())
                        .addGauge(minInfo, minMax.min())
                        .addGauge(maxInfo, minMax.max())
                        .addGauge(iNumInfo, lastStat().numSamples());
            }
            if(changed()) {
                if(numSamples > 0) {
                    intervalStat.copyTo(prevStat);
                    intervalStat.reset();
                }
                clearChanged();
            }
        }
    }

    /**
     * 返回一个统计样例对象
     * @return
     */
    public SampleStat lastStat() {
        return changed() ? intervalStat : prevStat;
    }

    public void resetMinMax() {
        minMax.reset();
    }

    @Override
    public String toString() {
        return lastStat().toString();
    }

}
