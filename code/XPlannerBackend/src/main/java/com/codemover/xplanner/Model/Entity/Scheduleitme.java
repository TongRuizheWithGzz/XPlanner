package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
public class Scheduleitme {
    private int scheduleItmeId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
    private String address;
    private String title;
    private int has_known_concrete_time;

    private User user;

    private Integer userId;

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

    @Basic
    @Column(name= "title")
    public String getTitle(){return title;}

    public void setTitle(String title){this.title = title;}

    @Basic
    @Column
    public int getHas_known_concrete_time(){return has_known_concrete_time;}

    public void setHas_known_concrete_time(int has_known_concrete_time){this.has_known_concrete_time = has_known_concrete_time;}


    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id",insertable = false,updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name="user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
