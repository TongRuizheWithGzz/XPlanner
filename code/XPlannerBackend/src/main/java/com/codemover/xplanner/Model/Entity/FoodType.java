package com.codemover.xplanner.Model.Entity;

import javax.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
@Table(name = "food_type", schema = "xplanner", catalog = "")
public class FoodType {
    private int foodTypeId;
    private String foodTypeName;
    private Collection<Food> foodsByFoodTypeId;

    @Id
    @Column(name = "food_type_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getFoodTypeId() {
        return foodTypeId;
    }

    public void setFoodTypeId(int foodTypeId) {
        this.foodTypeId = foodTypeId;
    }

    @Basic
    @Column(name = "food_type_name")
    public String getFoodTypeName() {
        return foodTypeName;
    }

    public void setFoodTypeName(String foodTypeName) {
        this.foodTypeName = foodTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FoodType foodType = (FoodType) o;
        return foodTypeId == foodType.foodTypeId &&
                Objects.equals(foodTypeName, foodType.foodTypeName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(foodTypeId, foodTypeName);
    }

    @OneToMany(mappedBy = "foodTypeByFoodTypeId")
    public Collection<Food> getFoodsByFoodTypeId() {
        return foodsByFoodTypeId;
    }

    public void setFoodsByFoodTypeId(Collection<Food> foodsByFoodTypeId) {
        this.foodsByFoodTypeId = foodsByFoodTypeId;
    }
}
