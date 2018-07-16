package com.codemover.xplanner.DAO;

import com.codemover.xplanner.Model.Entity.WeixinUser;

import javax.transaction.Transactional;

@Transactional
public interface WeixinUserRepository extends UserBaseRepository<WeixinUser> {
}
