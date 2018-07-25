package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Food;
import com.codemover.xplanner.Model.Entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food,Integer>{
    public List<Food> findByDiningHallAndFoodTypeByFoodTypeId(String dininghall,FoodType food_type);

    public List<Food> findByDiningHall(String dininghall);
}
