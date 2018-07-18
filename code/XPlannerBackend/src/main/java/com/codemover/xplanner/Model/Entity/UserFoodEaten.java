package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "user_food_eaten", schema = "xplanner", catalog = "")
public class UserFoodEaten {
    private int userFoodEatenId;
    private String foodName;
    private Integer calorie;
    private Timestamp ateTime;
    private User user;

    @Id
    @Column(name = "user_food_eaten_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getUserFoodEatenId() {
        return userFoodEatenId;
    }

    public void setUserFoodEatenId(int userFoodEatenId) {
        this.userFoodEatenId = userFoodEatenId;
    }

    @Basic
    @Column(name = "food_name")
    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    @Basic
    @Column(name = "calorie")
    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    @Basic
    @Column(name = "ate_time")
    public Timestamp getAteTime() {
        return ateTime;
    }

    public void setAteTime(Timestamp ateTime) {
        this.ateTime = ateTime;
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
