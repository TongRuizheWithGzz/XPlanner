package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

@Transactional
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByRolename(String roleName);
}
