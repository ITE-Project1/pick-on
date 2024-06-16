package com.ite.pickon.domain.user.controller;

import com.ite.pickon.domain.user.dto.UserVO;
import com.ite.pickon.domain.user.service.UserService;
import com.ite.pickon.domain.user.service.UserServiceImpl;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Log
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String userAdd(@RequestBody UserVO user, Model model) {
        user.setRole("general");
        System.out.println(user);
        userService.addUser(user);

        return "redirect:/login";
    }

    @GetMapping("/log-in")
    public String login() {
        return "login";
    }


}
