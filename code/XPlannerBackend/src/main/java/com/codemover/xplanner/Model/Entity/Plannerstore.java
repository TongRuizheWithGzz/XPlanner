package com.codemover.xplanner.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Plannerstore {
    @JsonProperty(value="planner_id")
    private int plannerId;
    @JsonProperty(value="planner_name")
    private String plannerName;
    @JsonProperty(value="picture_path_name")
    private Integer picturePathName;

    @JsonProperty(value="description")
    private String description;


    @Id
    @Column(name = "planner_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getPlannerId() {
        return plannerId;
    }

    public void setPlannerId(int plannerId) {
        this.plannerId = plannerId;
    }

    @Basic
    @Column(name = "planner_name")
    public String getPlannerName() {
        return plannerName;
    }

    public void setPlannerName(String plannerName) {
        this.plannerName = plannerName;
    }

    @Basic
    @Column(name = "picture_path_name")
    public Integer getPicturePathName() {
        return picturePathName;
    }

    public void setPicturePathName(Integer picturePathName) {
        this.picturePathName = picturePathName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plannerstore that = (Plannerstore) o;
        return plannerId == that.plannerId &&
                Objects.equals(plannerName, that.plannerName) &&
                Objects.equals(picturePathName, that.picturePathName) &&
                Objects.equals(description, that.description);
    }


    @Override
    public int hashCode() {

        return Objects.hash(plannerId, plannerName, picturePathName, description);
    }
}
