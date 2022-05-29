package com.tan.labbackend.entity;


import com.tan.labbackend.utils.MessageTypeEnum;
import lombok.Data;

import java.util.Set;


/**
 * websocket 通信消息类
 */
@Data
public class ChatMessage<T> {

    /**
     * 消息类型
     */
    private MessageTypeEnum type;

    /**
     * 消息发送者
     */
    private String sender;

    /**
     * 消息接收者
     */
    private Set<String> receivers;

    private T data;
}

