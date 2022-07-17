package com.zy.spring.mildware.netty.netty13;

import lombok.Data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 协议信息
 */
@Data
public class Message {
    /**
     * 魔数: 防止服务被恶意或无意调用
     */
    private int magicNumber;
    /**
     * 主版本号
     */
    private byte mainVersion;
    /**
     * 次版本号
     */
    private byte subVersion;
    /**
     * 修订版本号
     */
    private byte modifyVersion;
    /**
     * 会话 id
     */
    private String sessionId;
    /**
     * 消息类型
     */
    private MessageTypeEnum messageType;
    /**
     * 附加数据
     * 附加消息是字符串类型的键值对来表示的，
     * 这里首先使用2个字节记录键值对的个数，
     * 然后对于每个键和值，都首先使用4个字节记录其长度，然后是具体的数据，其形式如:
     * 键值对个数+键长度+键数据+值长度+值数据...
     */
    private Map<String, String> attachments = new HashMap<>();
    /**
     * 消息体
     */
    private String body;

    public Map<String, String> getAttachments() {
        return Collections.unmodifiableMap(attachments);
    }

    public void setAttachments(Map<String, String> attachments) {
        this.attachments.clear();
        if (null != attachments) {
            this.attachments.putAll(attachments);
        }
    }

    public void addAttachment(String key, String value) {
        attachments.put(key, value);
    }

}
