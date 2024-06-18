package com.ite.pickon.domain.user.mapper;

import com.ite.pickon.domain.product.dto.ProductAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserMapper {

    void insertUser(UserVO user);
    UserVO selectUser(String username);
    void deleteUser(String username);
    List<UserVO> selectUserListByKeyword(@Param("pageable")Pageable pageable, @Param("keyword")String keyword);
}
