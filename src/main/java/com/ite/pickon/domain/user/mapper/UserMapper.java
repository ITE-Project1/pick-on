package com.ite.pickon.domain.user.mapper;

import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserMapper {

    void insertUser(UserVO user);

    UserVO selectUser(String username);

    List<UserAdminVO> selectUserListByKeyword(@Param("pageable") Pageable pageable, @Param("keyword") String keyword);

    void updateUserStatus(@Param("username") String usernames, @Param("statusCode") int statusCode);

    void updateUserListStatus(@Param("usernames") List<String> usernames);

    Long selectUserId(@Param("user_id") Long user_id);

    int countTotalUserPages(@Param("keword") String keyword, @Param("pageSize") int pageSize);
}
