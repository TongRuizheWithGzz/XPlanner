package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Plannerstore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface PlannerStoreRepository extends JpaRepository<Plannerstore, Integer> {
}





