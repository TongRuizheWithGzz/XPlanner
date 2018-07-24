package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "keeper_recommand", schema = "xplanner", catalog = "")
public class KeeperRecommand {
    private int recommand_id;
    private Timestamp start_time;
    private Timestamp end_time;
    private String title;
    private String description;
    private String address;
    private User user;

    @Id
    @Column(name = "recommand_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getRecommand_id(){return recommand_id;}

    public void setRecommand_id(int recommand_id) {
        this.recommand_id = recommand_id;
    }

    @Basic
    @Column(name="start_time")
    public Timestamp getStart_time(){return start_time;}

    public void setStart_time(Timestamp start_time) {
        this.start_time = start_time;
    }

    @Basic
    @Column(name="end_time")
    public Timestamp getEnd_time(){return end_time;}

    public void setEnd_time(Timestamp end_time) {
        this.end_time = end_time;
    }

    @Basic
    @Column(name="title")
    public String getTitle(){return title;}

    public void setTitle(String title) {
        this.title = title;
    }

    @Basic
    @Column(name="description")
    public String getDescription(){return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    @Basic
    @Column(name="address")
    public String getAddress(){return address;}

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
}
