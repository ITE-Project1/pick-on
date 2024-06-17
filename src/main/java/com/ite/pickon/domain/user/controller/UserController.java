package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Log
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> userAdd(@RequestBody UserVO user) {
        try {
            // 사용자 등록
            user.setRole("general");
            user.setStatus(UserStatus.ACTIVE);
            userService.addUser(user);
            return ResponseEntity.ok("Registration successful! Please login.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred during registration. Please try again.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserVO user, HttpSession session) {
        String password = userService.findByUsername(user.getUsername()).getPassword();
        if (password.equals(user.getPassword())) {
            session.setAttribute("user", user);
            return ResponseEntity.ok("Registration successful! Please login.");
        } else {
            return ResponseEntity.status(500).body("An error occurred during login. Please try again.");
        }
    }

}
