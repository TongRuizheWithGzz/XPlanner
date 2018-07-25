package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Food;
import com.codemover.xplanner.Model.Entity.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodTypeRepository extends JpaRepository<FoodType,Integer> {
    public FoodType findByFoodTypeName(String food_type_name);
}
