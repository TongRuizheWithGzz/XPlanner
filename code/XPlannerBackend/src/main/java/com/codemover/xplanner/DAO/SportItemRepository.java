package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Sportsitem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SportItemRepository extends JpaRepository<Sportsitem, Integer> {
    Sportsitem findBySportName(String sportname);

    @Override
    long count();

}
