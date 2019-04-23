package com.ljy.mrg;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.ljy.misc.net.ConnectionInfo;
import com.ljy.misc.net.Server2ClientChannelInitializer;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:18:20
 */
public class TcpOuterMrg {
    private final ConnectionInfo outerConnectionInfo;
    @Inject
    public TcpOuterMrg(AcceptorMrg acceptorMrg, EventGroupMrg eventGroupMrg) {

        outerConnectionInfo = acceptorMrg.acceptRange(10001, 10001, new Server2ClientChannelInitializer(), eventGroupMrg.getBossGroup(), eventGroupMrg.getWorkGroup());
        Preconditions.checkArgument(null!=outerConnectionInfo);
    }
}
