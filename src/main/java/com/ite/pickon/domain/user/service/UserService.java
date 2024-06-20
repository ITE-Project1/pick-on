package com.ite.pickon.domain.user.service;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.response.ListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface UserService {

    void addUser(UserVO user);
    UserVO findByUsername(String username);
    ListResponse findUserList(Pageable pageable, String keyword, int totalPage);
    void modifyUserStatus(Long userId, UserStatus userStatus);
    Long checkCurrentUser(HttpSession session);
    void modifyUserListStatus(List<String> usernames);

    int getTotalPage(String keyword, int userPageSize);
}
