package com.ite.pickon.domain.user.service;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {

    void addUser(UserVO user);
    UserVO findByUsername(String username);
    List<UserAdminVO> findUserList(Pageable pageable, String keyword);
    void modifyUserStatus(String username, UserStatus userStatus);
    void modifyUserListStatus(List<String> usernames);
}
