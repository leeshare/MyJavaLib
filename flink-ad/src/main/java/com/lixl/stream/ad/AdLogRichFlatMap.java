package com.lixl.stream.ad;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lixl.stream.entity.AdLog;
import com.lixl.stream.entity.dto.AdLogDTO;
import com.lixl.stream.utils.ETLUtils;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class AdLogRichFlatMap extends RichFlatMapFunction<AdLog, String> {
    ObjectMapper objectMapper;

    @Override
    public void open(Configuration parameters) throws Exception {
        objectMapper = new ObjectMapper();
        super.open(parameters);
    }

    @Override
    public void flatMap(AdLog adLog, Collector<String> collector) throws Exception {
        AdLogDTO adLogDTO = ETLUtils.buildAdLogDTO(adLog);
        collector.collect(objectMapper.writeValueAsString(adLogDTO));
    }
}
