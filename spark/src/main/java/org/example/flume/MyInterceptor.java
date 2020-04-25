package org.example.flume;

import com.google.common.collect.Lists;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;

import java.util.HashMap;
import java.util.List;

public class MyInterceptor implements Interceptor {

    @Override
    public void initialize() {

    }

    @Override
    public Event intercept(Event event) {
        HashMap<String, String> header = new HashMap<>();
        String body = new String(event.getBody());
        if (body.contains("gifshow")) {
            header.put("type", "gifshow");
            event.setHeaders(header);
        } else {
            header.put("type", "other");
            event.setHeaders(header);
        }
        return event;
    }

    @Override
    public List<Event> intercept(List<Event> events) {
        List<Event> out = Lists.newArrayList();
        for (Event event : events) {
            Event outEvent = intercept(event);
            out.add(outEvent);
        }
        return out;
    }

    @Override
    public void close() {

    }

    public static class Builder implements Interceptor.Builder {

        @Override
        public Interceptor build() {
            return new MyInterceptor();
        }

        @Override
        public void configure(Context context) {

        }
    }
}
