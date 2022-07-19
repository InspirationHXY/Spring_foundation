package com.yingyi.test.service;

import com.yingyi.test.domain.User;


public interface IUserService {

    /**
     * 用户注册方法
     *  切面添加的功能是：在用户注册成功之后在发送消息或者发送邮件等
     */
    Boolean userRegister(User user);
}
