package com.tan.labbackend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCourseId implements Serializable {

    private Integer courseId;

    private Integer userId;
}