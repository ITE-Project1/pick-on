package com.ite.pickon.domain.user.service;


import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public void addUser(UserVO user) {
        userMapper.insertUser(user);
    }

    @Override
    public UserVO findByUsername(String username) {
        return userMapper.selectUser(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO user = userMapper.selectUser(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if ("admin".equals(user.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("general".equals(user.getRole())) {
            authorities.add(new SimpleGrantedAuthority("ROLE_GENERAL"));
        }

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
}
