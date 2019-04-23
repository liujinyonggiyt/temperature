package com.ljy.mrg;

import com.google.inject.Inject;
import com.ljy.misc.trigger.TriggerSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lishile
 */
public class TimerMrg extends TriggerSystem {

    private static final Logger logger = LoggerFactory
            .getLogger(TimerMrg.class);

    @Inject
    public TimerMrg() {
    }

}
