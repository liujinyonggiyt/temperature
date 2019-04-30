package com.ljy;

import com.google.inject.Inject;
import com.ljy.misc.AsynchronizedEventHandler;
import com.ljy.misc.EventConsumer;
import com.ljy.misc.LogicEvent;
import com.ljy.misc.LogicEventType;
import com.ljy.misc.msg.ClientRequest;
import com.ljy.misc.msg.SystemTimeMrg;
import com.ljy.misc.session.ClientSession;
import com.ljy.misc.utils.AnyUtils;
import com.ljy.mrg.ClientProtoHandlerMrg;
import com.ljy.mrg.ClientSessionMrg;
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
    private final ClientProtoHandlerMrg clientProtoHandlerMrg;
    private final ClientSessionMrg clientSessionMrg;
    private final SystemTimeMrg systemTimeMrg;

    protected final Map<LogicEventType,AsynchronizedEventHandler> logicEventMrg = new EnumMap<>(
            LogicEventType.class);
    @Inject
    public World(TimerMrg timerMrg, ClientProtoHandlerMrg clientProtoHandlerMrg, ClientSessionMrg clientSessionMrg, SystemTimeMrg systemTimeMrg) {
        this.timerMrg = timerMrg;
        this.clientProtoHandlerMrg = clientProtoHandlerMrg;
        this.clientSessionMrg = clientSessionMrg;
        this.systemTimeMrg = systemTimeMrg;
    }

    protected void initWhenThreadStart() throws Exception {
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_REGISTING, this::AS_on_client_register);
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_DISCONNECT,this::AS_on_client_disconnect);
        logicEventMrg.put(LogicEventType.LOGIC_CLIENT_PROTO_COMMING_EVENT,this::AS_on_client_proto_come);

        registProtoHandlers();
    }

    private void registProtoHandlers(){

    }

    /**
     * 客户端链接服务器的主线程事件
     *
     * @param data
     */
    public final void AS_on_client_register(LogicEvent data) throws Exception {
        ClientSession clientSession = new ClientSession(data.getChannel(), systemTimeMrg.getSysSecTime());
        clientSessionMrg.addSession(clientSession);

    }

    /**
     * 客户端断开连接异步事件
     *
     * @param logicEvent
     */
    public void AS_on_client_disconnect(LogicEvent logicEvent) throws Exception {
        try {
            ClientSession clientSession = clientSessionMrg.getSessionByChannel(logicEvent.getChannel());
            if (null == clientSession) {
                return;
            }
           clientSessionMrg.removeSession(logicEvent.getChannel());

            //TODO
        } finally {
            AnyUtils.closeQuietly(logicEvent.getChannel());
        }
    }

    public final void AS_on_client_proto_come(LogicEvent data) throws Throwable {
        ClientRequest clientRequest = (ClientRequest) data.getParamA();
        ProtoEnum protoEnum = ProtoEnum.values()[clientRequest.getMsgCode()];//索引
        clientProtoHandlerMrg.handleClientProto(protoEnum,
                data.getChannel(), clientRequest);
    }

    public void loop() {
        //刷新游戏时间戳
        int escapedMillTime = systemTimeMrg.update();
        //还未到每一帧的毫秒时间，直接return不刷帧更新
        if (0 == escapedMillTime) {
            return;
        }

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
