package com.yingyi.test.domain;


import org.springframework.context.ApplicationEvent;

public class UserRegisteredEvent extends ApplicationEvent {

    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     *               事件源
     */
    public UserRegisteredEvent(Object source) {
        super(source);
    }
}
