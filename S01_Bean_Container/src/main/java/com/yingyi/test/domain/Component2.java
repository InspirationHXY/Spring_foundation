package com.yingyi.test.domain;

import com.yingyi.test.S01Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class Component2 {
    public static final Logger LOGGER = LoggerFactory.getLogger(S01Application.class);


    //声明为事件监听器, 用于接收事件
    @EventListener
    public void doEvent(UserRegisteredEvent event){
        LOGGER.debug("{}", event);
        LOGGER.debug("发送消息");
    }
}
