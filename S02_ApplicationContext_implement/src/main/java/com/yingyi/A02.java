package com.yingyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

public class A02 {


    public static void main(String[] args) {

        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        /**
         * 创建一个BeanFactory容器， 让他管理bean（bean的创建、使用、销毁等）， 需要告诉他bean的定义信息
         * 然后他才能管理
         * bean 的定义信息： class、 scope（单例、多例）、初始化信息等
         */
        AbstractBeanDefinition beanDefinition =
                BeanDefinitionBuilder.genericBeanDefinition(Config.class).setScope("singleton").getBeanDefinition();

        //注册BeanDefinition
        beanFactory.registerBeanDefinition("config", beanDefinition);


        //verify that  bean definition
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

    }
    //@Configuration 使用Java Config来代替applicationContext.xml相当于xml中的<beans></beans>标签
    //@Configuration注解的类中，使用@Bean注解标注的方法，返回的类型都会直接注册为bean。
    @Configuration
    static class Config{
        @Bean
        public Bean1 bean1() {
            return new Bean1();
        }

        @Bean
        public Bean2 bean2() {
            return new Bean2();
        }


    }


    interface Inter {

    }



    //Bean 注解 用在方法上，用于告诉方法，产生一个Bean对象，然后这个Bean对象交给Spring管理
    static class Bean1 {
        private static final Logger log = LoggerFactory.getLogger(Bean1.class);

        public Bean1() {
            log.debug("构造 Bean1()");
        }

        @Autowired
        private Bean2 bean2;

        public Bean2 getBean2() {
            return bean2;
        }

    }

    static class Bean2 {
        private static final Logger log = LoggerFactory.getLogger(Bean2.class);

        public Bean2() {
            log.debug("构造 Bean2()");
        }
    }
}
