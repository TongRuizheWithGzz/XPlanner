package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserName(String username);
}
