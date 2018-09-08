package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.JAccountUser;

import javax.transaction.Transactional;

@Transactional
public interface JAccountUserRepository extends UserBaseRepository<JAccountUser> {
    JAccountUser findByOpenId(String openId);
}
