package com.ite.pickon.domain.user.service;


import com.ite.pickon.domain.order.OrderStatus;
import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.exception.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_ORDER_ID;

@Service
public class UserServiceImpl implements UserService {

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
    public List<UserVO> findUserList(Pageable pageable, String keyword) {
        return userMapper.selectUserListByKeyword(pageable, keyword);
    }

    @Override
    public void modifyUserStatus(String username, UserStatus userStatus) {
        userMapper.updateUserStatus(username, userStatus.getStatusCode());
    }


}
