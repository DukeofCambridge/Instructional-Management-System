package com.tan.labbackend;

import com.alibaba.fastjson.JSON;
import com.tan.labbackend.websocket.MyWebSocketClient;
import lombok.extern.slf4j.Slf4j;
import org.java_websocket.WebSocket;
import org.json.JSONObject;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

class WebsocketTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @BeforeEach
    public void before(){
        mockMvc= MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

// ...  等等

    /**
     * 创建 WebSocket 的客户端做测试
     * @throws Exception
     */
    @Test
    @Order(1)
    void websocketClient() throws Exception{

        // ADD USER

        MyWebSocketClient myWebSocketClient1 = new MyWebSocketClient(new URI("ws://localhost:8085/game/match/18"));
        myWebSocketClient1.connect();
        while (!WebSocket.READYSTATE.OPEN.equals(myWebSocketClient1.getReadyState())){
            log.info("WebSocket客户端连接中，请稍等...");
            Thread.sleep(500);
        }

//        JSONObject gameOver = new JSONObject();
//        gameOver.put("sender",18);
//        gameOver.put("data",60);
//        gameOver.put("type","GAME_OVER");
//
//        myWebSocketClient1.send(gameOver.toString());



        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sender",18);
        jsonObject.put("contestId",12);
        jsonObject.put("type","ADD_USER");


        String s = jsonObject.toString();
        System.out.println(s);
        myWebSocketClient1.send(s);

        Thread.sleep(500);


        MyWebSocketClient myWebSocketClient2 = new MyWebSocketClient(new URI("ws://localhost:8085/game/match/17"));
        myWebSocketClient2.connect();
        while (!WebSocket.READYSTATE.OPEN.equals(myWebSocketClient2.getReadyState())){
            log.info("WebSocket客户端连接中，请稍等...");
            Thread.sleep(500);
        }


//        gameOver.put("sender",17);
//        gameOver.put("data",60);
//        gameOver.put("type","GAME_OVER");
//
//        myWebSocketClient2.send(gameOver.toString());

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("sender",17);
        jsonObject2.put("contestId",12);
        jsonObject2.put("type","ADD_USER");

//        jsonObject.put("sender",17);


        String s2 = jsonObject2.toString();
        System.out.println(s2);
        myWebSocketClient2.send(s2);

        Thread.sleep(500);


        MyWebSocketClient myWebSocketClient3 = new MyWebSocketClient(new URI("ws://localhost:8085/game/match/15"));
        myWebSocketClient3.connect();
        while (!WebSocket.READYSTATE.OPEN.equals(myWebSocketClient3.getReadyState())){
            log.info("WebSocket客户端连接中，请稍等...");
            Thread.sleep(500);
        }

//        gameOver.put("sender",15);
//        gameOver.put("data",60);
//        gameOver.put("type","GAME_OVER");
//
//        myWebSocketClient3.send(gameOver.toString());

        JSONObject jsonObject3 = new JSONObject();
        jsonObject3.put("sender",15);
        jsonObject3.put("contestId",12);
        jsonObject3.put("type","ADD_USER");

//        jsonObject.put("sender",15);

        String s3 = jsonObject3.toString();
        System.out.println(s3);
        myWebSocketClient3.send(s3);

        Thread.sleep(500);


        // USER MATCH

        jsonObject.put("type","MATCH_USER");
        myWebSocketClient1.send(jsonObject.toString());


        Thread.sleep(500);

        jsonObject2.put("type","MATCH_USER");
        myWebSocketClient2.send(jsonObject2.toString());


        Thread.sleep(500);

        jsonObject3.put("type","MATCH_USER");
        myWebSocketClient3.send(jsonObject3.toString());


        // IN GAME

        JSONObject jsonObject4 = new JSONObject();
        jsonObject4.put("sender",18);
        jsonObject4.put("type","PLAY_GAME");
        jsonObject4.put("data",20);

        myWebSocketClient1.send(jsonObject4.toString());

        jsonObject4.put("data",40);

        myWebSocketClient1.send(jsonObject4.toString());

        JSONObject jsonObject5 = new JSONObject();
        jsonObject5.put("sender",17);
        jsonObject5.put("type","PLAY_GAME");
        jsonObject5.put("data",80);

        myWebSocketClient2.send(jsonObject5.toString());


        JSONObject jsonObject6 = new JSONObject();
        jsonObject6.put("sender",15);
        jsonObject6.put("type","PLAY_GAME");
        jsonObject6.put("data",100);

        myWebSocketClient3.send(jsonObject6.toString());


        // GAME OVER

        JSONObject over1 = new JSONObject();
        over1.put("sender",18);
        over1.put("type","GAME_OVER");
        over1.put("data",40);

        JSONObject over2 = new JSONObject();
        over2.put("sender",17);
        over2.put("type","GAME_OVER");
        over2.put("data",80);

        JSONObject over3 = new JSONObject();
        over3.put("sender",15);
        over3.put("type","GAME_OVER");
        over3.put("data",100);


        myWebSocketClient1.close();
        myWebSocketClient2.close();
        myWebSocketClient3.close();
    }
}
