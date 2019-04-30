package com.ljy;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ljy.misc.msg.SystemTimeMrg;
import com.ljy.mrg.*;

/**
 * Author:liujinyong
 * Date:2019/4/23
 * Time:17:40
 */
public class ModuleApp extends AbstractModule {
    protected void configure() {
        binder().requireExplicitBindings();

        bind(World.class).in(Singleton.class);
        bind(AcceptorMrg.class).in(Singleton.class);
        bind(TcpOuterMrg.class).in(Singleton.class);
        bind(EventGroupMrg.class).in(Singleton.class);
        bind(ChannelWriteMrg.class).in(Singleton.class);
        bind(SendMrg.class).in(Singleton.class);
        bind(TimerMrg.class).in(Singleton.class);
        bind(ClientProtoHandlerMrg.class).in(Singleton.class);
        bind(ClientSessionMrg.class).in(Singleton.class);
        bind(SystemTimeMrg.class).in(Singleton.class);
    }
}
