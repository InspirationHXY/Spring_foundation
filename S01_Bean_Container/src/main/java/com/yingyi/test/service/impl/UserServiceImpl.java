package com.yingyi.test.service.impl;

import com.yingyi.test.domain.User;
import com.yingyi.test.service.IUserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {
    @Override
    public Boolean userRegister(User user) {
        System.out.println("user registration function called");
        return true;
    }
}
