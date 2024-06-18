package com.ite.pickon.domain.user.service;


import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;

    @Override
    @Transactional
    public void addUser(UserVO user) {
        userMapper.insertUser(user);
    }

    @Override
    public UserVO findByUsername(String username) {
        return userMapper.selectUser(username);
    }

    @Override
    public List<UserAdminVO> findUserList(Pageable pageable, String keyword) {
        List<UserAdminVO> userAdminVOList = userMapper.selectUserListByKeyword(pageable, keyword);
        if (userAdminVOList == null) {
            throw new CustomException(ErrorCode.FIND_FAIL_USER_ID);
        }
        return userAdminVOList;
    }

    @Override
    @Transactional
    public void modifyUserStatus(String username, UserStatus userStatus) {
        userMapper.updateUserStatus(username, userStatus.getStatusCode());
    }

    @Override
    public void modifyUserListStatus(List<String> usernames, UserStatus userStatus) {
        userMapper.updateUserListStatus(usernames, userStatus.getStatusCode());
    }


}
