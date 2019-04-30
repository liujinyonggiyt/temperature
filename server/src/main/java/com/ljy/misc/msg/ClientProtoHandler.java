package com.ljy.misc.msg;

import com.ljy.misc.session.ClientSession;
import io.netty.channel.Channel;

/**
 * @author lishile
 */
public interface ClientProtoHandler {
    /**
     *
     * @param clientSession
     * @param proto
     * @return
     */
    boolean handle(ClientSession clientSession, ClientRequest clientRequest) throws Exception;
}
