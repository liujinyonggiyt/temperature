package com.ljy.misc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.ljy.ModuleApp;
import com.ljy.World;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liujinyong
 * Netty的多个网络IO线程与一个游戏逻辑线程通信的事件队列
 */
@SuppressWarnings("unchecked")
public class GlobalQueue {

    private static final int INIT_LOGIC_EVENT_CAPACITY = 1024 * 8;

    private static final Disruptor<LogicEvent> DISRUPTOR;

    public static final RingBuffer<LogicEvent> logicQueue;

    private static final SleepingWaitExtendStrategy strategy;

    static {
        //游戏逻辑线程，无论是客户端的消息还是服务器之间的消息都是在这个线程处理
        ExecutorService logicExecutor = Executors.newSingleThreadExecutor((
                Runnable r) -> new Thread(r, "LOGIC_THREAD"));
        //抽象工厂模式，各个服务器进程得到其对应的事件消费者，如S服是通过SceneEventConsumerFactory拿到的SceneLogicConsumer
        Injector injector=Guice.createInjector(new ModuleApp());
        World world=injector.getInstance(World.class);
        EventConsumer<LogicEvent> logicEventConsumer = world;
        //等待策略
        strategy = new SleepingWaitExtendStrategy(logicEventConsumer);
        //多生产者（Netty的多个网络IO线程），单消费者（一个逻辑线程）
        DISRUPTOR = new Disruptor<>(
                new LogicEventFactory(), INIT_LOGIC_EVENT_CAPACITY,
                logicExecutor, ProducerType.MULTI,
                strategy );
        //绑定网络逻辑事件处理，TCP、HTTP的连接、断开连接、发的消息处理等等
        DISRUPTOR.handleEventsWith(logicEventConsumer::onEventTemplate);

        logicQueue = DISRUPTOR.getRingBuffer();

        DISRUPTOR.start();
    }
    
    public static void setNeedSleep(boolean isNeedSleep){
    	strategy.setNeedSleep(isNeedSleep);
    }

}
