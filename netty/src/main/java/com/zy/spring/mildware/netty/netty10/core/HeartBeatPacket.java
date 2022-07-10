package com.zy.spring.mildware.netty.netty10.core;

import static com.zy.spring.mildware.netty.netty10.core.Command.HEART_BEAT;

public class HeartBeatPacket extends Packet {
    private String msg = "ping-pong";
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
    @Override
    public Byte getCommand() {
        return HEART_BEAT;
    }
}