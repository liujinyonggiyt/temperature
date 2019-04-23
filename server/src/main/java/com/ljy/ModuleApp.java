package com.ljy;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.ljy.mrg.AcceptorMrg;
import com.ljy.mrg.EventGroupMrg;
import com.ljy.mrg.TcpOuterMrg;

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
    }
}
