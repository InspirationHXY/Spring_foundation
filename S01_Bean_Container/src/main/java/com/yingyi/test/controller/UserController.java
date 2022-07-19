package com.yingyi.test.controller;

import com.yingyi.test.domain.User;
import com.yingyi.test.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("/register")
    public String register(){
        userService.userRegister(new User());
        return "registered";
    }
}
