package com.tan.labbackend.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class Result {
    /**
     *  响应状态码
     */
    private int code;
    /**
     *  响应状态消息
     */
    private String msg;
    /**
     *  响应结果对象
     */
    private Object object;


    Result (int code,String msg,Object data) {
        this.code = code;
        this.msg = msg;
        this.object = data;
    }
}

