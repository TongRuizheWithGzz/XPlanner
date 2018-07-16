package com.codemover.xplanner.Security.Config;


import com.codemover.xplanner.DAO.RoleRepository;
import com.codemover.xplanner.DAO.UserRepository;
import com.codemover.xplanner.Model.Entity.Role;
import com.codemover.xplanner.Model.Entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Component
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUserName(username);
        if (user == null) {
            throw new UsernameNotFoundException("User Not Found");
        }
        Set<Role> roles = user.getRoles();
        Collection<GrantedAuthority> grantedAuthority = new ArrayList<>();

        for (Role role : roles) {
            grantedAuthority.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserName(),
                user.getUserPassword(), grantedAuthority);
    }

}
