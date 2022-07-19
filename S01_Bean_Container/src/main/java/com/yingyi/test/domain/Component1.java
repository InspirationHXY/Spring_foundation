package com.yingyi.test.domain;

import com.yingyi.test.S01Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class Component1 {
    public static final Logger LOGGER = LoggerFactory.getLogger(S01Application.class);

    //获取事件发布器用于发送事件
    @Autowired
    private ApplicationEventPublisher event;

    public void register(){
        LOGGER.debug("用户注册");

        //发布事件
        event.publishEvent(new UserRegisteredEvent(this));
    }
}
