package com.tan.labbackend.entity;

import lombok.Data;

/**
 * websocket 响应消息类
 *
 */
@Data
public class MessageReply<T> {

    private Integer code;

    private String desc;

    private ChatMessage<T> chatMessage;
}
