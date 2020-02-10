package org.lixl.hadoop.lixlsource.metrics2.impl;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.metrics2.MetricsInfo;

import java.util.StringJoiner;

@InterfaceAudience.Private
public enum MsInfo implements MetricsInfo {
    NumActiveSources("Number of active metrics source"),
    NumAllSources("Number of all registered metrics sources"),
    NumActiveSinks("Number of active metrics sinks"),
    NumAllSinks("Number of all registered metrics sinks"),
    Context("Metrics context"),
    Hostname("Local hostname"),
    SessionId("Session ID"),
    ProcessName("Process name");

    private final String desc;

    MsInfo(String desc) {
        this.desc = desc;
    }

    @Override
    public String description() {
        return desc;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", this.getClass().getSimpleName() + "{", "}")
                .add("name=" + name())
                .add("description=" + desc)
                .toString();
    }
}
