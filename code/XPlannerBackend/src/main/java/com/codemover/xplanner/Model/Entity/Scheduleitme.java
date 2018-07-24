package com.codemover.xplanner.Model.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Scheduleitme {
    @JsonProperty("scheduleItem_id")
    private int scheduleItmeId;

    @JsonProperty("start_time")
    private Timestamp startTime;

    @JsonProperty("end_time")
    private Timestamp endTime;
    private String description;
    private String address;
    private String title;

    private boolean completed;

    @JsonIgnore
    private User user;


    @Id
    @Column(name = "scheduleItme_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getScheduleItmeId() {
        return scheduleItmeId;
    }

    public void setScheduleItmeId(int scheduleItmeId) {
        this.scheduleItmeId = scheduleItmeId;
    }

    @Basic
    @Column(name = "start_time")
    public Timestamp getStartTime() {
        return startTime;
    }


    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }

    @Basic
    @Column(name = "end_time")
    public Timestamp getEndTime() {
        return endTime;
    }

    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }





    @Basic
    @Column(name = "title")
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Basic
    @Column(name = "completed")
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }


}
