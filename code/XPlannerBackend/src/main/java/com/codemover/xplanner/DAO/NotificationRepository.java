package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface NotificationRepository extends PagingAndSortingRepository<Notification, Integer> {
   // Page<Notification> findAll(Pageable pageable);


}




