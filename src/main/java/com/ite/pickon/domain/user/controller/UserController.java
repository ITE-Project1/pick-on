package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.security.JwtToken;
import com.ite.pickon.security.JwtTokenProvider;
import com.ite.pickon.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Controller
@RequiredArgsConstructor
public class UserController {
    private static final int USER_PAGE_SIZE = 10;
    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserValidator validator;
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 회원가입 처리
     *
     * @param user 사용자 정보 객체
     * @param bindingResult 입력값 검증 결과
     * @return 응답 객체
     */
    @PostMapping("/user/register")
    public ResponseEntity<?> userAdd(@RequestBody UserVO user, BindingResult bindingResult) {
        validator.validate(user, bindingResult);

        // 입력값 검증 로직 추가
        if (bindingResult.hasErrors()) {
            return new ResponseEntity<>(createErrorMap(bindingResult), HttpStatus.BAD_REQUEST);
        }
        try {
            // 사용자 등록
            user.setRole("general");
            user.setStatus(UserStatus.ACTIVE);

            // 패스워드 인코딩 설정 추가
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userService.addUser(user);
            return ResponseEntity.ok("Registration successful! Please login.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during registration. Please try again.");
        }
    }

    /**
     * 로그인 처리
     *
     * @param user 사용자 정보 객체
     * @return 응답 객체
     */
    @PostMapping("/user/login")
    public ResponseEntity<JwtToken> loginSuccess(@RequestBody UserVO user) {
        try {
            UserVO userinfo = userService.findByUsername(user.getUsername());
            JwtToken token = userService.login(user.getUsername(), user.getPassword(), userinfo.getUser_id());
            return ResponseEntity.ok(token);
        } catch(AuthenticationException e) {
            return ResponseEntity.status(401).build();
        }

    }

    /**
     * 리프레시 토큰을 이용한 액세스 토큰 갱신
     *
     * @param token 리프레시 토큰
     * @return 새로운 액세스 토큰
     */
    @PostMapping("/user/refresh")
    public ResponseEntity<JwtToken> refresh(@RequestHeader("Authorization") String token) {
        String refreshToken = token.replace("Bearer ", "");
        JwtToken newAccessToken = jwtTokenProvider.generateAccessTokenFromRefreshToken(refreshToken);
        return ResponseEntity.ok(newAccessToken);
    }

    /**
     * 로그아웃 처리
     *
     * @param token 액세스 토큰
     * @return 응답 객체
     */
    @PostMapping("/user/logout")
    public ResponseEntity<?> userLogout(@RequestHeader("Authorization") String token) {
        String authToken = token.replace("Bearer ", "");
        userService.addTokenToBlacklist(authToken);
        return ResponseEntity.ok("Logout successful");
    }

    /**
     * 계정 비활성화 처리
     *
     * @param token 액세스 토큰
     * @return 응답 객체
     */
    @PatchMapping("/user/sign-out")
    public ResponseEntity<?> userRemove(@RequestHeader("Authorization") String token) {
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userService.modifyUserStatus(userId, UserStatus.INACTIVE);
        userService.addTokenToBlacklist(token.replace("Bearer", ""));
        return ResponseEntity.ok("User deactivated");
    }

    /**
     * 사용자 목록 조회 (관리자용)
     *
     * @param page 페이지 번호
     * @param keyword 검색 키워드 (선택 사항)
     * @return 사용자 목록 응답 객체
     */
    @GetMapping("/admin/users")
    public ResponseEntity<ListResponse> getUserList(@RequestParam int page,
                                                    @RequestParam(required = false) String keyword) {

        int totalPage = userService.getTotalPage(keyword, USER_PAGE_SIZE);

        // 전체 페이지 개수를 넘는 요청을 보내면 예외 처리
        if (page >= totalPage) {
            throw new CustomException(ErrorCode.FIND_FAIL_USERS);
        }

        Pageable pageable = PageRequest.of(page, 10);
        return ResponseEntity.ok(userService.findUserList(pageable, keyword, totalPage));
    }

    /**
     * 사용자 상태 변경 (관리자용)
     *
     * @param usernames 상태 변경할 사용자명 목록
     * @return 응답 메시지
     */
    @PatchMapping(value = "/admin/users", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> userStatusModify(@RequestBody List<String> usernames) {
        userService.modifyUserListStatus(usernames);
        return ResponseEntity.ok("User blacked");
    }

    /**
     * 입력값 검증 오류 메시지 생성
     *
     * @param bindingResult 입력값 검증 결과
     * @return Error Message Map 객체
     */
    private Map<String, String> createErrorMap(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
