package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.KeeperRecommand;
import com.codemover.xplanner.Model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface KeeperRecommandRepository extends JpaRepository<KeeperRecommand,Integer> {
    List<KeeperRecommand> findByUser(User user);
    void deleteByUser(User user);
    void removeByUser(User user);
}
