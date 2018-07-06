package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "user_food_eaten", schema = "xplanner", catalog = "")
public class UserFoodEaten {
    private int userFoodEatenId;
    private String foodName;
    private Integer calorie;
    private Timestamp ateTime;
    private User userByUserId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserFoodEaten that = (UserFoodEaten) o;
        return userFoodEatenId == that.userFoodEatenId &&
                Objects.equals(foodName, that.foodName) &&
                Objects.equals(calorie, that.calorie) &&
                Objects.equals(ateTime, that.ateTime);
    }

    @Override
    public int hashCode() {

        return Objects.hash(userFoodEatenId, foodName, calorie, ateTime);
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
