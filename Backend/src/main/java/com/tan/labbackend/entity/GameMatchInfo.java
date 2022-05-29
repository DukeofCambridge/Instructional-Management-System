package com.tan.labbackend.entity;

import lombok.Data;

import java.util.List;

/**
 * @author yeeq
 * @date 2021/4/4
 */
@Data
public class GameMatchInfo {

    private UserMatchInfo selfInfo;
    private List<UserMatchInfo> opponentInfo;
    private List<Exercise> exercises;
}
