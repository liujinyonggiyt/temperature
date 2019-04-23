package com.ljy;

import com.google.inject.Inject;
import com.ljy.misc.AsynchronizedEventHandler;
import com.ljy.misc.EventConsumer;
import com.ljy.misc.LogicEvent;
import com.ljy.misc.LogicEventType;
import com.ljy.mrg.TimerMrg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumMap;
import java.util.Map;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:17:53
 */
public class World extends EventConsumer<LogicEvent> {
    private static final Logger logger = LoggerFactory.getLogger(World.class);

    private final TimerMrg timerMrg;
    protected final Map<LogicEventType,AsynchronizedEventHandler> logicEventMrg = new EnumMap<>(
            LogicEventType.class);
    @Inject
    public World(TimerMrg timerMrg) {
        this.timerMrg = timerMrg;
    }

    protected void initWhenThreadStart() throws Exception {
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_REGISTING, this::AS_on_client_register);
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_DISCONNECT,this::AS_on_client_disconnect);
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_PROTO_COMMING_EVENT,this::AS_on_client_proto_come);
    }

    /**
     * 客户端链接服务器的主线程事件
     *
     * @param data
     */
    public final void AS_on_client_register(LogicEvent data) throws Exception {

        ClientTempSession clientTempSession = new ClientTempSession(
                data.getChannel(), sysTimeMrg.getSysSecTime());

        clientSessionMrg.addTempSession(clientTempSession);

    }

    /**
     * 客户端断开连接异步事件
     *
     * @param logicEvent
     */
    public void AS_on_client_disconnect(LogicEvent logicEvent) throws Exception {
        try {
            ClientTempSession clientTempSession = clientSessionMrg
                    .removeTempSession(logicEvent.getChannel());
            if (null != clientTempSession) {
                return;
            }
            ClientSession clientSession = clientSessionMrg
                    .getSessionByChannel(logicEvent.getChannel());
            if (null == clientSession) {
                return;
            }
            //这里一定不能执行这一条代码,要不然在session管理器中有一个Map<channel,clientsession> key对象永久丢失内存泄露
            //clientSession.setChannel(null);
            sceneMrg.notifySMPlayerOffline(clientSession.getPlayer().getGuid(), clientSession.getPlayer().getSmServerSession(),"socket close");
        } finally {
            AnyUtils.closeQuietly(logicEvent.getChannel());
        }
    }

    public final void AS_on_client_proto_come(LogicEvent data) throws Throwable {
        sceneProtoHandlerMrg.handleClientProto(data.getProtoEnum(),
                data.getChannel(), data.getProto());
    }

    public void loop() {
        timerMrg.tickTrigger();
    }

    protected void onEvent(LogicEvent event, long sequence, boolean endOfBatch) throws Throwable {
        LogicEventType logicEventType = event.getLogicEventType();

        AsynchronizedEventHandler handler = logicEventMrg.get(logicEventType);

        if(null == handler)
        {

            throw new NullPointerException(
                    "opreation exception , no asynchronizedEventHandler matchs the logic event type: "
                            + logicEventType.toString());

        }
        handler.onEvent(event);
    }


}