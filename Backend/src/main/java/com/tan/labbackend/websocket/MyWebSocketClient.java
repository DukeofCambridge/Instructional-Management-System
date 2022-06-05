package com.tan.labbackend.websocket;

import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

@Slf4j
public class MyWebSocketClient extends WebSocketClient {
    public MyWebSocketClient(URI uri){
        super(uri);
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        log.info("客户端连接成功");
    }

    @Override
    public void onMessage(String s) {
        log.info("客户端接收到消息："+s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("客户端关闭成功");
    }

    @Override
    public void onError(Exception e) {
        log.error(String.valueOf(e));
        log.error("客户端出错");
    }

    public static void main(String[] args) {
        try {
            MyWebSocketClient myWebSocketClient = new MyWebSocketClient(new URI("ws://localhost:8085/game/match/18"));
            myWebSocketClient.connect();
            while (!WebSocket.READYSTATE.OPEN.equals(myWebSocketClient.getReadyState())) {
                log.info("WebSocket客户端连接中，请稍等...");
                Thread.sleep(500);
            }
            myWebSocketClient.send("{\"module\":\"HEART_CHECK\",\"message\":\"请求心跳\"}");
            myWebSocketClient.close();
        } catch (Exception e) {
            log.error("error", e);
        }
    }

}
