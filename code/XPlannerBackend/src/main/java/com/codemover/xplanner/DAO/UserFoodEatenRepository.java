package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Model.Entity.UserFoodEaten;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface UserFoodEatenRepository extends JpaRepository<UserFoodEaten,Integer> {
    List<UserFoodEaten> findByUserAndAteTimeBetweenOrderByAteTime(User user, Timestamp beginning, Timestamp endding);
}
