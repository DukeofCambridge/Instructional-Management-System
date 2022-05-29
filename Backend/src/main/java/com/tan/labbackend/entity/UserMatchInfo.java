package com.tan.labbackend.entity;

import lombok.Data;

import java.time.Instant;

/**
 * @author yeeq
 * @date 2021/3/29
 */
@Data
public class UserMatchInfo implements Comparable<UserMatchInfo>{

    private String userId;
    private String contestId;
    private Integer score;
    private Instant time;

    @Override
    public int compareTo(UserMatchInfo o){
        Integer x = this.score.compareTo(o.getScore());
        if (x!=0){
            return o.time.compareTo(this.time);
        }
        return x;
    }
}