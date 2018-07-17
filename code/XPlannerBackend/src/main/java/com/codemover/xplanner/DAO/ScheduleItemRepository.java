package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface ScheduleItemRepository extends JpaRepository<Scheduleitme, Integer> {

    List<Scheduleitme> findByUserId(Integer userId);

}
