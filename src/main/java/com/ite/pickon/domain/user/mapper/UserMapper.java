package com.ite.pickon.domain.user.mapper;

import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserMapper {

    /**
     * 회원가입 처리
     *
     * @param user 사용자 정보 객체
     */
    void insertUser(UserVO user);

    /**
     * 사용자 정보 조회
     *
     * @param username 사용자명
     * @return 사용자 정보 객체
     */
    UserVO selectUser(String username);

    /**
     * 사용자명을 통해 사용자 목록 조회 (관리자용)
     *
     * @param pageable 페이징 정보
     * @param keyword 검색 키워드
     * @return 사용자 목록
     */
    List<UserAdminVO> selectUserListByKeyword(@Param("pageable") Pageable pageable, @Param("keyword") String keyword);

    /**
     * 사용자 상태 변경
     *
     * @param user_id 사용자 ID
     * @param statusCode 상태 코드
     */
    void updateUserStatus(@Param("user_id") Long user_id, @Param("statusCode") int statusCode);

    /**
     * 사용자 상태 일괄 변경
     *
     * @param usernames 상태 변경할 사용자명 목록
     */
    void updateUserListStatus(@Param("usernames") List<String> usernames);

    /**
     * 사용자 ID 조회
     *
     * @param user_id 사용자 ID
     * @return 사용자 ID
     */
    Long selectUserId(@Param("user_id") Long user_id);

    /**
     * 키워드를 이용한 전체 사용자 페이지 수 조회
     *
     * @param keyword 검색 키워드
     * @param pageSize 페이지 크기
     * @return 전체 페이지 수
     */
    int countTotalUserPages(@Param("keyword") String keyword, @Param("pageSize") int pageSize);
}
