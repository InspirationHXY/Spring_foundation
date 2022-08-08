package com.yingyi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.mvc.Controller;

public class A02Application {
    private static final Logger log = LoggerFactory.getLogger(A02Application.class);



    public static void main(String[] args) {

        /*
            学到了什么
                a. 常见的 ApplicationContext 容器实现 (四种)
                   会加载bean的定义信息
                    1 ClassPathXmlApplicationContext
                    2 FileSystemXmlApplicationContext
                    3 AnnotationConfigApplicationContext 会添加额外的bean后处理器
                        org.springframework.context.annotation.internalConfigurationAnnotationProcessor
                        org.springframework.context.annotation.internalAutowiredAnnotationProcessor
                        org.springframework.context.annotation.internalCommonAnnotationProcessor
                        org.springframework.context.event.internalEventListenerProcessor
                        org.springframework.context.event.internalEventListenerFactory
                        与 xml中的<context:annotation-config/>标签功能相同
                    4 AnnotationConfigServletWebServerApplicationContext
                b. 内嵌容器、DispatcherServlet 的创建方法、作用
                    web环境搭建的核心
                   1 内嵌容器 tomcat
                   2 DispatcherServlet 前端控制器
                   3 将DispatcherServlet注册到tomcat中
                   4 请求处理器
         */


        //testClassPathXmlApplicationContext();
        //testFileSystemXmlApplicationContext();

        //testAnnotationConfigApplicationContext();
        testAnnotationConfigServletWebServerApplicationContext();
        /**
         *ClassPathXmlApplicationContext 底层如何创建、管理bean？
         *  读取bean的配置文件，转化为BeanDefinition
         *  创建bean
         */


        /*DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        System.out.println("读取之前。。。");
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println("读取之后。。。");

        *//**
         * new一个 XmlBeanDefinitionReader 参数为保存BeanDefinition的BeanFactory容器
         *//*
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);

        //reader.loadBeanDefinitions(new ClassPathResource("applicationContext.xml"));
        reader.loadBeanDefinitions(new FileSystemResource("S02_ApplicationContext_implement\\src\\main\\resources\\applicationContext.xml"));
        for (String name : beanFactory.getBeanDefinitionNames()) {
            System.out.println(name);
        }*/
    }

    // ⬇️较为经典的容器, 基于 classpath 下 xml 格式的配置文件来创建
    private static void testClassPathXmlApplicationContext(){
        ClassPathXmlApplicationContext applicationContext =
                new ClassPathXmlApplicationContext("applicationContext.xml");

        for (String name : applicationContext.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println(applicationContext.getBean(Bean2.class).getBean1());

    }

    // ⬇️基于磁盘路径下 xml 格式的配置文件来创建
    private static void testFileSystemXmlApplicationContext(){
        /*FileSystemXmlApplicationContext context
                = new FileSystemXmlApplicationContext("G:\\Java_Project\\Spring_foundation\\S02_ApplicationContext_implement\\src\\main\\resources\\applicationContext.xml");
        */
        FileSystemXmlApplicationContext context
                = new FileSystemXmlApplicationContext("S02_ApplicationContext_implement\\src\\main\\resources\\applicationContext.xml");
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println(context.getBean(Bean2.class).getBean1());
    }

    // ⬇️较为经典的容器, 基于 java 配置类来创建
    private static void testAnnotationConfigApplicationContext(){
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(Config.class);
        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }

        System.out.println(context.getBean(Bean2.class).getBean1());
    }


    // ⬇️较为经典的容器, 基于 java 配置类来创建, 用于 web 环境
    private static void testAnnotationConfigServletWebServerApplicationContext(){
        AnnotationConfigServletWebServerApplicationContext context = new AnnotationConfigServletWebServerApplicationContext(WebConfig.class);

        for (String name : context.getBeanDefinitionNames()) {
            System.out.println(name);
        }


    }


    /**
     * Web应用的核心配置
     *  1 内嵌容器 tomcat
     *  2 DispatcherServlet 前端控制器
     *  3 将DispatcherServlet注册到tomcat中
     *  4 请求处理器
     */
    @Configuration
    static class WebConfig{

        @Bean
        public ServletWebServerFactory servletWebServerFactory(){
            return new TomcatServletWebServerFactory();
        }

        @Bean
        public DispatcherServlet dispatcherServlet(){
            return new DispatcherServlet();
        }

        @Bean
        public DispatcherServletRegistrationBean registrationBean(DispatcherServlet dispatcherServlet){
            return new DispatcherServletRegistrationBean(dispatcherServlet,  "/");
        }

        @Bean("/hello")
        public Controller controller1(){
            return ((req, resp) -> {
                resp.getWriter().print("Hello");
               return  null;
            });
        }
    }

    @Configuration
    static class Config {

        @Bean
        public Bean1 bean1(){
            return new Bean1();
        }

        @Bean
        public Bean2 bean2(Bean1 bean1){
            Bean2 bean2 = new Bean2();
            bean2.setBean1(bean1);
            return bean2;
        }
    }

    static class Bean1 {
    }

    static class Bean2 {

        private Bean1 bean1;

        public void setBean1(Bean1 bean1) {
            this.bean1 = bean1;
        }

        public Bean1 getBean1() {
            return bean1;
        }
    }

}
