package com.codemover.xplanner.Service;

import com.codemover.xplanner.Model.Entity.User;

import java.util.List;

public interface UserService {
    List<User> queryAllUser();
    void save(User user);
}
