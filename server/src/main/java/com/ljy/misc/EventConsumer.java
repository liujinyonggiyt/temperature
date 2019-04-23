package com.ljy.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liujinyong
 */
public abstract class EventConsumer<T extends Cleanable> {

    private static final Logger logger = LoggerFactory
            .getLogger(EventConsumer.class);

    private static final int LOOP_PER_EVENT_TIMES = 10000;

    private int eventTimeIndex = 0;

    private boolean isFirstLoop = true;

    /** 刷帧处理 */
    public final void loopTemplate() {

        try {
            ensureThreadStart();

            loop();

            eventTimeIndex = 0;

        } catch (Throwable e) {

            logger.error("", e);

            if (e instanceof OutOfMemoryError) {

                System.exit(-1);

            }

        }

    }

    private void ensureThreadStart() {

        if (isFirstLoop) {

            logger.info("logic thread starting");

            isFirstLoop = false;

            try {

                initWhenThreadStart();

            } catch (Throwable e) {

                logger.error("", e);

                System.exit(-1);

            }

            logger.info("logic thread started");
        }

    }

    public abstract void loop();

    protected abstract void onEvent(T event, long sequence, boolean endOfBatch)
            throws Throwable;

    public final void onEventTemplate(T event, long sequence, boolean endOfBatch)
            throws Exception {

        try {
            ensureThreadStart();

            try {
                //处理网络逻辑事件，连接、断开连接、发的消息处理等等
                onEvent(event, sequence, endOfBatch);
            } finally {
                event.clean();

                //对处理事件次数计数，
                ++eventTimeIndex;
                if (eventTimeIndex == LOOP_PER_EVENT_TIMES) {
                    //当消息特别多的时候，强行执行一次刷帧函数，保证游戏刷帧的处理
                    eventTimeIndex = 0;
                    //刷帧
                    loopTemplate();
                    return;
                }

                if (eventTimeIndex > LOOP_PER_EVENT_TIMES) {
                    throw new UnsupportedOperationException(
                            "an impossible event happen , I think we need stop the world");
                }
            }

        } catch (Throwable e) {
            logger.error("", e);

            if (e instanceof OutOfMemoryError) {
            	logger.error("",e);
                System.exit(-1);
            }
        }
    }

    protected abstract void initWhenThreadStart() throws
            Exception;

}
