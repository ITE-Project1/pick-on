package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.UserStatus;
import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<?> userLogin(@RequestBody UserVO user, HttpSession session) {
        String password = userService.findByUsername(user.getUsername()).getPassword();
        if (password.equals(user.getPassword())) {
            session.setAttribute("user", user);
            return ResponseEntity.ok("Registration successful! Please login.");
        } else {
            return ResponseEntity.status(500).body("An error occurred during login. Please try again.");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> userLogout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Logout successful");
    }

    @PatchMapping("/sign-out")
    public ResponseEntity<?> userRemove(@RequestBody UserVO user, HttpSession session) {
        //UserVO user = (UserVO) session.getAttribute("user");
        if (user == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println(user.getUsername());
        userService.removeUser(user.getUsername());
        session.invalidate();
        return ResponseEntity.ok("User deactivated");
    }
}
