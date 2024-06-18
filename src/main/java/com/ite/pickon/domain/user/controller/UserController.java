package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserAdminVO;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
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

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log
@Controller
public class UserController {

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
    public ResponseEntity<?> userLogin(@RequestBody UserVO user, HttpSession session) {
        String password = userService.findByUsername(user.getUsername()).getPassword();

        // 패스워드 일치 여부 확인
        if (bCryptPasswordEncoder.matches(user.getPassword(), password)) {
            session.setAttribute("user", user);
            return ResponseEntity.ok("Login successful! Please login.");
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
    public ResponseEntity<?> userRemove(@RequestBody UserVO user, HttpSession session) {
        //UserVO user = (UserVO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        userService.modifyUserStatus(user.getUsername(), UserStatus.INACTIVE);
        session.invalidate();
        return ResponseEntity.ok("User deactivated");
    }

    @GetMapping("/admin/users")
    public ResponseEntity<List<UserAdminVO>> getUserList(@RequestParam int page,
                                                         @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, 10);
        List<UserAdminVO> userList = userService.findUserList(pageable, keyword);
        return ResponseEntity.ok(userList);
    }

    @PatchMapping(value = "/admin/users", produces = "application/json; charset=UTF-8")
    public ResponseEntity<String> userStatusModify(@RequestBody List<String> usernames) {
        userService.modifyUserListStatus(usernames, UserStatus.BLACK);
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
