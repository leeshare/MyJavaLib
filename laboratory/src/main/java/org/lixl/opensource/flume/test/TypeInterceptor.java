package org.lixl.opensource.flume.test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.List;
import java.util.Map;

public class TypeInterceptor implements Interceptor {
    @Override
    public Event intercept(Event event) {
        String body = new String(event.getBody());
        Map<String, String> headers = event.getHeaders();
        JSONObject json = JSON.parseObject(body);
        Integer type = json.getInteger("type");
        if(type == 1){
            headers.put("type", "student");
        }else if(type == 2){
            headers.put("type", "teacher");
        }
        event.setHeaders(headers);
        return event;
    }
    @Override
    public List<Event> intercept(List<Event> list) {
        for(Event event: list){
            intercept(event);
        }
        return list;
    }

    @Override
    public void initialize() {

    }

    @Override
    public void close() {

    }

    private TypeInterceptor(){}

    public static class Builder implements Interceptor.Builder {
        private static TypeInterceptor interceptor = new TypeInterceptor();

        @Override
        public Interceptor build() {
            return interceptor;
        }

        @Override
        public void configure(Context context) {

        }
    }
}
