package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Integer> {
    public T findByUserName(String username);

}
