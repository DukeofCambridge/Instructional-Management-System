package com.tan.labbackend.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class ContestExerciseId implements Serializable {
    private static final long serialVersionUID = 4928523984042163856L;
    @Column(name = "contest_id", nullable = false)
    private Integer contestId;
    @Column(name = "exercise_id", nullable = false)
    private Integer exerciseId;

    public Integer getExerciseId() {
        return exerciseId;
    }

    public void setExerciseId(Integer exerciseId) {
        this.exerciseId = exerciseId;
    }

    public Integer getContestId() {
        return contestId;
    }

    public void setContestId(Integer contestId) {
        this.contestId = contestId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contestId, exerciseId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ContestExerciseId entity = (ContestExerciseId) o;
        return Objects.equals(this.contestId, entity.contestId) &&
                Objects.equals(this.exerciseId, entity.exerciseId);
    }
}