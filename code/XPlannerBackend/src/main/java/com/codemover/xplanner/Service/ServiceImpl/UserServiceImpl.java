package com.codemover.xplanner.Service.ServiceImpl;

import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.User;
import com.codemover.xplanner.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    private UserRepository userRepository;


    @Override
    public List<User> queryAllUser() {
        return userRepository.findAll();
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
