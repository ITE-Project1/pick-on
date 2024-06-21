package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.exception.CustomException;
import com.ite.pickon.exception.ErrorCode;
import com.ite.pickon.response.ListResponse;
import com.ite.pickon.response.LoginResponse;
import com.ite.pickon.validator.UserValidator;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Controller
public class UserController {
    private static final int USER_PAGE_SIZE = 10;

    private final UserService userService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserValidator validator;

    @Autowired
    public UserController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.validator = new UserValidator();
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> userAdd(@RequestBody UserVO user, BindingResult bindingResult, HttpSession session) {
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
            return ResponseEntity.status(500).body("An error occurred during login. Please try again.");
        }
    }

    @PostMapping("/user/logout")
    public ResponseEntity<?> userLogout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

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

    @PatchMapping(value = "/admin/users", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> userStatusModify(@RequestBody List<String> usernames) {
        userService.modifyUserListStatus(usernames);
        return ResponseEntity.ok("User blacked");
    }

    private Map<String, String> createErrorMap(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return errors;
    }

}
