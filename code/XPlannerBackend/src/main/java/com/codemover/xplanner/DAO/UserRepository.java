package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.User;

import javax.transaction.Transactional;

@Transactional

public interface UserRepository extends UserBaseRepository<User> {

}
