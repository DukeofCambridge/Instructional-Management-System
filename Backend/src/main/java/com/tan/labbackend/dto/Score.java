package com.tan.labbackend.dto;

import lombok.Data;

import java.util.List;

/**
 * @Author Sir Lancelot
 * @Description 专为数据传输使用，显示课程所有学生的所有项目成绩及总成绩
 */
@Data
public class Score {
    private String username;
    private String name;
    List<Double> grades;
    private Double overall;
}
