package com.ite.pickon.domain.user.service;


import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
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

        if (username == null) {
            throw new CustomException(ErrorCode.FAIL_TO_LOGIN);
        }
        UserVO user = userMapper.selectUser(username);
        if (user == null) {
            throw new CustomException(ErrorCode.FAIL_TO_LOGIN);
        }
        return user;
    }

    @Override
    public ListResponse findUserList(Pageable pageable, String keyword, int totalPage) {
        List<UserAdminVO> userAdminVOList = userMapper.selectUserListByKeyword(pageable, keyword);

        if (userAdminVOList.size() == 0) {
            throw new CustomException(ErrorCode.FIND_FAIL_USERS);
        }

        return new ListResponse(userAdminVOList, totalPage);
    }

    @Override
    @Transactional
    public void modifyUserStatus(Long userId, UserStatus userStatus) {
        userMapper.updateUserStatus(userId, userStatus.getStatusCode());
    }

    @Override
    @Transactional
    public void modifyUserListStatus(List<String> usernames) {
        userMapper.updateUserListStatus(usernames);
    }

    @Override
    public int getTotalPage(String keyword, int userPageSize) {
        return userMapper.countTotalUserPages(keyword, userPageSize);
    }

    @Override
    public Long checkCurrentUser(HttpSession session){
        Long userId = (Long)session.getAttribute("userId");
        if(userId == null){
            //세션이 만료되었을 경우
            throw new CustomException(ErrorCode.INVALID_SESSION_ID);
        }

        // 현재 로그인 한 유저가 회원인지 확인
        if (userMapper.selectUserId(userId).equals(userId)){
            return userId;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
