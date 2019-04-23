package com.ljy.misc.trigger;

/**
 * 触发器接口
 * Created by lishile on 2016/3/8.
 */
public interface TriggerInterface {
    /**
     * 添加一个计时器
     * @param timer
     */
    void addTimer(Timer timer);
    /**
     * 刷帧更新触发器
     */
    void tickTrigger();
}
