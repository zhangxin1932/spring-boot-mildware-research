package com.zy.spring.mildware.netty.netty10.core;

/**
 * Serializer，用来指定序列化算法，用于序列化对象
 * 序列化接口，每种序列化方式需要一个相应的实现
 */
public interface Serializer {
    /**
     * @return 序列化算法
     */
    byte getSerializerAlgorithm();
    /**
     * 将对象序列化成二进制
     *
     */
    byte[] serialize(Object object);
    /**
     * 将二进制反序列化为对象
     */
    <T> T deSerialize(Class<T> clazz, byte[] bytes);

    /**
     * 心跳包的内容很小，可以使用 JSON 进行解析。
     * 但是对于图片、视频、日志文件等比较大的内容，可能需要使用 Java 自带的序列化方式，
     * 或由 Kryo、Hessian、FST、Protobuf 等框架实现对象的序列化和反序列化。
     */
    interface SerializeAlgorithm {
        /**
         * json序列化标识
         */
        byte json = 1;
        byte binary = 2;
        byte fst = 3;
    }
}
