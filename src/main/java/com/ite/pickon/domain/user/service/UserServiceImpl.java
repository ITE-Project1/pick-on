package com.ite.pickon.domain.user.service;

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

    /**
     * 회원가입 처리
     *
     * @param user 사용자 정보 객체
     */
    @Override
    @Transactional
    public void addUser(UserVO user) {
        userMapper.insertUser(user);
    }

    /**
     * 사용자명으로 사용자 정보 조회
     *
     * @param username 사용자명
     * @return 사용자 정보 객체
     * @throws CustomException 로그인 실패 시 예외 발생
     */
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

    /**
     * 키워드를 이용한 사용자 목록 조회 (관리자용)
     *
     * @param pageable 페이징 정보
     * @param keyword 검색 키워드
     * @param totalPage 전체 페이지 수
     * @return 사용자 목록 및 페이지 정보
     */
    @Override
    public ListResponse findUserList(Pageable pageable, String keyword, int totalPage) {
        List<UserAdminVO> userAdminVOList = userMapper.selectUserListByKeyword(pageable, keyword);
        if (userAdminVOList.isEmpty()) {
            throw new CustomException(ErrorCode.FIND_FAIL_USERS);
        }
        return new ListResponse(userAdminVOList, totalPage);
    }

    /**
     * 사용자 상태 변경
     *
     * @param userId 사용자 ID
     * @param userStatus 변경할 상태
     */
    @Override
    @Transactional
    public void modifyUserStatus(Long userId, UserStatus userStatus) {
        userMapper.updateUserStatus(userId, userStatus.getStatusCode());
    }

    /**
     * 사용자 상태 일괄 변경
     *
     * @param usernames 상태 변경할 사용자명 목록
     */
    @Override
    @Transactional
    public void modifyUserListStatus(List<String> usernames) {
        userMapper.updateUserListStatus(usernames);
    }

    /**
     * 키워드를 이용한 전체 사용자 페이지 수 조회
     *
     * @param keyword 검색 키워드
     * @param userPageSize 페이지 크기
     * @return 전체 페이지 수
     */
    @Override
    public int getTotalPage(String keyword, int userPageSize) {
        return userMapper.countTotalUserPages(keyword, userPageSize);
    }

    /**
     * 현재 세션의 사용자 ID 확인
     *
     * @param session 현재 세션
     * @return 사용자 ID
     * @throws CustomException 세션 만료 또는 비회원일 경우 예외 발생
     */
    @Override
    public Long checkCurrentUser(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            // 세션이 만료되었을 경우
            throw new CustomException(ErrorCode.INVALID_SESSION_ID);
        }

        // 현재 로그인 한 유저가 회원인지 확인
        if (userMapper.selectUserId(userId).equals(userId)) {
            return userId;
        } else {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
    }
}
