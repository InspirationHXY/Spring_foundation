package com.yingyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Component
public class LifeCycleBean {
    private static final Logger log = LoggerFactory.getLogger(LifeCycleBean.class);

    /**
     * bean对象的生命周期
     *  构造
     *  依赖注入
     *  初始化
     *  销毁
     */
    public LifeCycleBean() {
        log.debug("构造");
    }

    @Autowired
    //@Value 注入数据的方式之一【值注入】，当给String这类基本数据类型注入值的时候可使用 需要配合@Autowired使用
    public void autowire(@Value("${JAVA_HOME}") String home) {
        log.debug("依赖注入: {}", home);
    }

    //当依赖注入完成后用于执行初始化的方法，并且只会被执行一次
    //初始化前执行
    @PostConstruct
    public void init() {
        log.debug("初始化");
    }

    //当Bean在容器销毁之前，调用被@PreDestroy注解的方法
    @PreDestroy
    public void destroy() {
        log.debug("销毁");
    }
}
