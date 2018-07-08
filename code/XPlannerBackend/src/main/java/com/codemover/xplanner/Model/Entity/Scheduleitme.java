package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
public class Scheduleitme {
    private int scheduleItmeId;
    private Timestamp startTime;
    private Timestamp endTime;
    private String description;
    private String address;
    private User userByUserId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scheduleitme that = (Scheduleitme) o;
        return scheduleItmeId == that.scheduleItmeId &&
                Objects.equals(startTime, that.startTime) &&
                Objects.equals(endTime, that.endTime) &&
                Objects.equals(description, that.description) &&
                Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {

        return Objects.hash(scheduleItmeId, startTime, endTime, description, address);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    public User getUserByUserId() {
        return userByUserId;
    }

    public void setUserByUserId(User userByUserId) {
        this.userByUserId = userByUserId;
    }
}
