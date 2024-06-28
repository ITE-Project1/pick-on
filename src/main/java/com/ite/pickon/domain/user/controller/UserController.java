package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
     * @param session 세션 객체
     * @param response 응답 객체
     * @return 응답 객체
     */
    @PostMapping("/user/login")
    public ResponseEntity<?> userLogin(@RequestBody UserVO user, HttpSession session, HttpServletResponse response) {
        UserVO userinfo = userService.findByUsername(user.getUsername());
        String password = userinfo.getPassword();

        // 패스워드 일치 및 활성 여부 확인
        if (bCryptPasswordEncoder.matches(user.getPassword(), password) && userinfo.getStatus() == UserStatus.ACTIVE) {
            // 세션 데이터 저장
            session.setAttribute("userId", userinfo.getUser_id());
            session.setMaxInactiveInterval(1800);

            // 프론트에 넘겨줄 정보 저장
            Map<String, Object> sessionData = new HashMap<>();
            sessionData.put("userId", userinfo.getUser_id());
            sessionData.put("role", userinfo.getRole());
            sessionData.put("username", user.getUsername());

            // 쿠키 설정
            String cookieValue = "userId=" + userinfo.getUser_id() + "; Path=/; Max-Age=-1; SameSite=None";
            response.addHeader("Set-Cookie", cookieValue);
            return ResponseEntity.ok(sessionData);
        } else {
            // 로그인 실패 시 ErrorCode 반환
            throw new CustomException(ErrorCode.FAIL_TO_LOGIN);
        }
    }

    /**
     * 로그아웃 처리
     *
     * @param session 세션 객체
     * @return 응답 객체
     */
    @PostMapping("/user/logout")
    public ResponseEntity<?> userLogout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    /**
     * 계정 비활성화 처리
     *
     * @param session 세션 객체
     * @return 응답 객체
     */
    @PatchMapping("/user/sign-out")
    public ResponseEntity<?> userRemove(HttpSession session) {
        Long userId = userService.checkCurrentUser(session);
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.modifyUserStatus(userId, UserStatus.INACTIVE);
        session.invalidate();
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
