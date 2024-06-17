package com.ite.pickon.domain.user.mapper;

import com.ite.pickon.domain.user.dto.UserVO;

public interface UserMapper {

    void insertUser(UserVO user);
    UserVO selectUser(String username);
    void deleteUser(String username);
}
