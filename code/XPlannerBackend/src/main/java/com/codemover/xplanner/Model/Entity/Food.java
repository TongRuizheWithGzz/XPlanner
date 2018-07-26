package com.codemover.xplanner.Model.Entity;

import com.codemover.xplanner.Model.DTO.FoodDTO;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Food {

    private int foodId;
    private String foodName;
    private Integer calorie;
    private FoodType foodTypeByFoodTypeId;
    private String diningHall;

    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getFoodId() {
        return foodId;
    }

    public void setFoodId(int foodId) {
        this.foodId = foodId;
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
    public String getDiningHall() {
        return diningHall;
    }

    public void setDiningHall(String diningHall) {
        this.diningHall = diningHall;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return foodId == food.foodId &&
                Objects.equals(foodName, food.foodName) &&
                Objects.equals(calorie, food.calorie) &&
                Objects.equals(diningHall, food.diningHall);
    }

    @Override
    public int hashCode() {

        return Objects.hash(foodId, foodName, calorie, diningHall);
    }

    @ManyToOne
    @JoinColumn(name = "food_type_id", referencedColumnName = "food_type_id")
    public FoodType getFoodTypeByFoodTypeId() {
        return foodTypeByFoodTypeId;
    }

    public void setFoodTypeByFoodTypeId(FoodType foodTypeByFoodTypeId) {
        this.foodTypeByFoodTypeId = foodTypeByFoodTypeId;
    }


    public FoodDTO toFoodDTO() {
        FoodDTO foodDTO = new FoodDTO();
        foodDTO.calorie = this.calorie;
        foodDTO.dininghall = this.diningHall;
        foodDTO.food_id = this.foodId;
        foodDTO.food_name = this.foodName;
        foodDTO.food_type_id = this.foodTypeByFoodTypeId == null ? null : this.foodTypeByFoodTypeId.getFoodTypeId();

        return foodDTO;
    }
}
