package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Scheduleitme;
import com.codemover.xplanner.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.List;

@Transactional
public interface ScheduleItemRepository extends JpaRepository<Scheduleitme, Integer> {
    List<Scheduleitme> findByUserAndStartTimeBetween(User user, Timestamp beginning, Timestamp endding);

    List<Scheduleitme> findByUserAndStartTimeBetweenOrderByStartTimeAsc(User user, Timestamp beginning, Timestamp endding);


}
