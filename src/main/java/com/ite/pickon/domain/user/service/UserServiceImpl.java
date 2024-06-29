package com.ite.pickon.domain.user.service;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.mapper.UserMapper;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.security.JwtToken;
import com.ite.pickon.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final BCryptPasswordEncoder encoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final Set<String> blacklist = new HashSet<>();

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
     * 로그인 처리 및 JWT 토큰 생성
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @param userId 사용자 ID
     * @return 생성된 JWT 토큰
     */
    @Override
    public JwtToken login(String username, String password, Long userId) {

        // Authentication 객체 생성
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 검증된 인증 정보로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication, userId);

    }

    /**
     * 토큰을 블랙리스트에 추가
     *
     * @param token 블랙리스트에 추가할 토큰
     */
    @Override
    public void addTokenToBlacklist(String token) {
        blacklist.add(token);
    }

    /**
     * 토큰이 블랙리스트에 있는지 확인
     *
     * @param token 확인할 토큰
     * @return 블랙리스트에 포함 여부
     */
    @Override
    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

}
