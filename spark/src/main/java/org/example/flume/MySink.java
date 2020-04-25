package org.example.flume;

import org.apache.flume.*;
import org.apache.flume.conf.Configurable;
import org.apache.flume.sink.AbstractSink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySink extends AbstractSink implements Configurable {

    private String prefix;
    private String suffix;
    private static Logger logger = LoggerFactory.getLogger(MySink.class);

    @Override
    public Status process() {
        Status status = null;
        Channel ch = getChannel();
        Transaction txn = ch.getTransaction();
        try {
            txn.begin();
            Event event = null;
            //需要一直判断event是否不为null
            while(true){
                event = ch.take();
                if (event != null){
                    break;
                }
            }
            String body = new String(event.getBody());
            body= body.replace("\r", "").replace("\n", "");
            logger.info(prefix +"-"+ body + suffix);
            txn.commit();
            status = Status.READY;
        } catch (Exception e) {
            txn.rollback();
            status = Status.BACKOFF;
        } finally {
            txn.close();
        }


        return status;
    }

    @Override
    public void configure(Context context) {
        prefix = context.getString("prefix");
        suffix = context.getString("suffix", "-JUMP");
    }
}
