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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

import static com.ite.pickon.exception.ErrorCode.FIND_FAIL_USER_ID;

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
    @Transactional
    public void modifyUserListStatus(List<String> usernames) {
        userMapper.updateUserListStatus(usernames);
    }

    @Override
    public Long checkCurrentUser(@SessionAttribute(name ="userId", required = false) Long userId){
        if(userId == null){
            //세션이 만료되었을 경우
            throw new CustomException(ErrorCode.INVALID_SESSION_ID);
        }

        // 현재 로그인 한 유저가 회원인지 확인
        if (userMapper.selectUserId(userId).equals(userId)){
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        };
        return userId;

    }
}
