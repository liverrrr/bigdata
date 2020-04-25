package org.example.flume;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.PollableSource;
import org.apache.flume.conf.Configurable;
import org.apache.flume.event.SimpleEvent;
import org.apache.flume.source.AbstractSource;

import java.util.Random;

public class MySource extends AbstractSource implements Configurable, PollableSource {

    private String prefix;
    private String suffix;

    /**
     * 真正处理逻辑
     */
    @Override
    public Status process() {
        Status status = null;
        Random random = new Random();
        try {
            for (int i = 1; i < 10; i++) {
                Event event = new SimpleEvent();
                String body = prefix + i + suffix;
                event.setBody(body.getBytes());
                getChannelProcessor().processEvent(event);
            }
            status = Status.READY;
        } catch (Exception e) {
            e.printStackTrace();
            status = Status.BACKOFF;
        }

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return status;
    }

    @Override
    public long getBackOffSleepIncrement() {
        return 0;
    }

    @Override
    public long getMaxBackOffSleepInterval() {
        return 0;
    }

    /**
     * 设置配置信息
     */
    @Override
    public void configure(Context context) {
        prefix = context.getString("prefix", "ruozedata-");
        suffix = context.getString("suffix");
    }


}
