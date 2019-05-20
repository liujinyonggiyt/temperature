package com.ljy.mrg;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ljy.misc.net.ConnectionInfo;
import com.ljy.misc.net.broadcast.BroadcastChannelInitializer;
import com.ljy.misc.net.group.GroupBroadcastChannelInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:18:20
 */
public class TcpOuterMrg {
    private static final Logger logger = LoggerFactory.getLogger(TcpOuterMrg.class);
    private final ConnectionInfo outerConnectionInfo;
    @Inject
    public TcpOuterMrg(AcceptorMrg acceptorMrg, EventGroupMrg eventGroupMrg) {

        outerConnectionInfo = acceptorMrg.acceptRange(10001, 10001, new GroupBroadcastChannelInitializer(), eventGroupMrg.getBossGroup(), eventGroupMrg.getWorkGroup());
        Preconditions.checkArgument(null!=outerConnectionInfo);
        logger.info("server out tcp listen success!ip:{}, port:{}", outerConnectionInfo.getHost(), outerConnectionInfo.getPort());
    }
}
