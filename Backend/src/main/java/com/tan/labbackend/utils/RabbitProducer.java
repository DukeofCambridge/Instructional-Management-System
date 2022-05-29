package com.tan.labbackend.utils;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class RabbitProducer {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void sendFanout(String msg) {
        System.out.println("[fanout] send msg:" + msg);
        // 第一个参数是我们交换机的名称 ，第二个参数是routerKey空着就可以，第三个是要发送的消息
        this.rabbitTemplate.convertAndSend("fanoutExchange", "", msg);
    }
}
