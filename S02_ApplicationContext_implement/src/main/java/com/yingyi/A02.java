package com.yingyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

public class A02 {


    public static void main(String[] args) {

        /**
         * DefaultListableBeanFactory 是BeanFactory的一个实现类
         */
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

        /**
         * org.springframework.context.annotation.internalConfigurationAnnotationProcessor
         * org.springframework.context.annotation.internalAutowiredAnnotationProcessor
         * org.springframework.context.annotation.internalCommonAnnotationProcessor
         * org.springframework.context.event.internalEventListenerProcessor
         * org.springframework.context.event.internalEventListenerFactory
         */
        //给BeanFactory中 添加一些常用的后处理器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(beanFactory);

        /**
         * BeanFactory后处理器主要功能，补充一些bean的定义
         */
        Map<String, BeanFactoryPostProcessor> beansOfType = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        beansOfType.values().forEach(beanFactoryPostProcessor -> beanFactoryPostProcessor.postProcessBeanFactory(beanFactory));



        //Bean 后处理器， 针对 bean 的生命周期的各个阶段提供扩展 例如：使@Autowired @Resource生效等
        //区别于BeanFactory的后处理器， Bean 后处理器是对每个bean相关的
        //beanFactory::addBeanPostProcessor 给每个bean对象绑定后处理器
        //void accept(T t);
        beanFactory.getBeansOfType(BeanPostProcessor.class).values().forEach(beanFactory::addBeanPostProcessor);

        //verify that  bean definition
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        beanFactory.preInstantiateSingletons(); //准备好所有单例对象

        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        //验证bean对象的创建
        System.out.println(beanFactory.getBean(Bean1.class).getBean2());

        /**
         * 小结：
         * a. beanFactory 不会做的事情
         *      1. 不会主动调用 BeanFactory 后处理器
         *      2. 不会主动添加 Bean 的后处理器
         *      3. 不会主动初始化单例对象
         *      4. 不会解析beanFactory 也不会解析 ${ } 和 #{ }
         *
         * b. bean 后处理器会有排序的逻辑
         */
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
