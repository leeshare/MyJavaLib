package org.lixl.hadoop.lixlsource.metrics2.impl;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.metrics2.MetricsCollector;
import org.lixl.hadoop.lixlsource.metrics2.MetricsFilter;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;

import java.util.Iterator;
import java.util.List;

import static org.lixl.hadoop.lixlsource.metrics2.lib.Interns.info;

@InterfaceAudience.Private
@VisibleForTesting
public class MetricsCollectorImpl implements MetricsCollector, Iterable<MetricsRecordBuilderImpl> {
    private final List<MetricsRecordBuilderImpl> rbs = Lists.newArrayList();
    private MetricsFilter recordFilter, metricFilter;

    @Override
    public MetricsRecordBuilderImpl addRecord(MetricsInfo info) {
        boolean acceptable = recordFilter == null || recordFilter.accepts(info.name());
        MetricsRecordBuilderImpl rb = new MetricsRecordBuilderImpl(this, info, recordFilter, metricFilter, acceptable);
        if (acceptable) {
            rbs.add(rb);
        }
        return rb;
    }

    @Override
    public MetricsRecordBuilderImpl addRecord(String name) {
        return addRecord(info(name, name + " record"));
    }

    public List<MetricsRecordImpl> getRecords() {
        List<MetricsRecordImpl> recs = Lists.newArrayListWithCapacity(rbs.size());
        for (MetricsRecordBuilderImpl rb : rbs) {
            MetricsRecordImpl mr = rb.getRecord();
            if (mr != null) {
                recs.add(mr);
            }
        }
        return recs;
    }

    @Override
    public Iterator<MetricsRecordBuilderImpl> iterator() {
        return rbs.iterator();
    }

    @InterfaceAudience.Private
    public void clear() {
        rbs.clear();
    }

    MetricsCollectorImpl setRecordFilter(MetricsFilter rf) {
        recordFilter = rf;
        return this;
    }

    MetricsCollectorImpl setMetricsFilter(MetricsFilter mf) {
        metricFilter = mf;
        return this;
    }

}
