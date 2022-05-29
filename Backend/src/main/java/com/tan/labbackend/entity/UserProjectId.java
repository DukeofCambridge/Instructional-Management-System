package com.tan.labbackend.entity;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class UserProjectId implements Serializable {

    private Integer projectId;

    private Integer studentId;

}
