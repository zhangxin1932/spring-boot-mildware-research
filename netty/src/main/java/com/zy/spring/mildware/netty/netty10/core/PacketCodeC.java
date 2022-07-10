package com.zy.spring.mildware.netty.netty10.core;

import java.util.HashMap;
import java.util.Map;

import static com.zy.spring.mildware.netty.netty10.core.Command.*;

/**
 * Packet 的工厂类，用于生成 Payload 和 Header
 */
public class PacketCodeC {
    /**
     * 魔数
     */
    public static final int MAGIC_NUMBER = 0x88888888;
    private static final PacketCodeC INSTANCE = new PacketCodeC();

    private PacketCodeC() {
    }

    /**
     * 采用单例模式
     */
    public static PacketCodeC getInstance() {
        return INSTANCE;
    }

    private static final Map<Byte, Class<? extends Packet>> PACKET_TYPE_MAP = new HashMap<>();

    static {
        PACKET_TYPE_MAP.put(HEART_BEAT, HeartBeatPacket.class);
        /*packetTypeMap.put(LOGIN_REQUEST, LoginRequestPacket.class);
        packetTypeMap.put(LOGIN_RESPONSE, LoginResponsePacket.class);
        packetTypeMap.put(MESSAGE_REQUEST, MessageRequestPacket.class);
        packetTypeMap.put(MESSAGE_RESPONSE, MessageResponsePacket.class);*/
    }

}
