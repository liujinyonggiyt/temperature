package com.ljy.misc;

import io.netty.channel.Channel;

/**
 * Author:liujinyong
 * Date:2019/2/21
 * Time:11:30
 */
public class LogicEvent implements Cleanable{
    private LogicEventType logicEventType;
    private Object paramA;
    private long longParamA;
    private long longParamB;
    private long longParamC;
    private int intParamA;
    private Channel channel;
    private boolean boolParamA;

    public LogicEvent() {
    }

    public void clean() {
        this.channel = null;
        this.paramA = null;
    }

    public LogicEventType getLogicEventType() {
        return this.logicEventType;
    }

    public void setLogicEventType(LogicEventType logicEventType) {
        this.logicEventType = logicEventType;
    }

    public Object getParamA() {
        return this.paramA;
    }

    public void setParamA(Object paramA) {
        this.paramA = paramA;
    }

    public Channel getChannel() {
        return this.channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public long getLongParamA() {
        return this.longParamA;
    }

    public void setLongParamA(long longParamA) {
        this.longParamA = longParamA;
    }

    public int getIntParamA() {
        return this.intParamA;
    }

    public void setIntParamA(int intParamA) {
        this.intParamA = intParamA;
    }

    public long getLongParamB() {
        return this.longParamB;
    }

    public void setLongParamB(long longParamB) {
        this.longParamB = longParamB;
    }

    public boolean isBoolParamA() {
        return this.boolParamA;
    }

    public void setBoolParamA(boolean boolParamA) {
        this.boolParamA = boolParamA;
    }

    public long getLongParamC() {
        return this.longParamC;
    }

    public void setLongParamC(long longParamC) {
        this.longParamC = longParamC;
    }
}
