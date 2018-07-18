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

    private String imageUrl;
    private boolean hasKnownConcreteTime;

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
    @Column(name = "imageUrl")
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
    @Column(name = "has_known_concrete_time")
    public boolean isHasKnownConcreteTime() {
        return hasKnownConcreteTime;
    }

    public void setHasKnownConcreteTime(boolean hasKnownConcreteTime) {
        this.hasKnownConcreteTime = hasKnownConcreteTime;
    }
}
