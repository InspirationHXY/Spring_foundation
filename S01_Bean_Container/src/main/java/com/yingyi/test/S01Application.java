package com.yingyi.test;

import com.yingyi.test.domain.Component1;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.DefaultSingletonBeanRegistry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.Map;

@SpringBootApplication
public class S01Application {


    //获取logger
    public static final Logger LOGGER = LoggerFactory.getLogger(S01Application.class);


    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException, IOException {

        /**
         * ApplicationContext 是一个子接口
         * BeanFactory 是父接口, Spring的核心容器
         */
        /**
         *
         *   context是一个spring容器 组合了BeanFactory 即包含一个beanFactory字段(DefaultListableBeanFactory)
         */
        ConfigurableApplicationContext context = SpringApplication.run(S01Application.class, args);

        //AnnotationConfigServletWebServerApplicationContext
        System.out.println(context);

        /**
         * 快捷键使用
         * ctrl+alt+B 或者 按住ctrl+alt 鼠标左键点击 ==> 跳到方法的实现处
         * ctrl+B ==> 方法的定义处
         */
        //context.getBean("aa");

        /**
         * 使用反射获取DefaultSingletonBeanRegistry类中的singletonObjects字段
         */
        Field singletonObjects = DefaultSingletonBeanRegistry.class.getDeclaredField("singletonObjects");
        //设置可以访问私有的属性
        singletonObjects.setAccessible(true);
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();

        Map<String, Object> map = (Map<String, Object>) singletonObjects.get(beanFactory);
        //void accept(T t, U u);
        map.forEach((k, v) -> System.out.println(k + "=" + v));

        System.out.println("===================================");

        //只输出component的结果
        //boolean test(T t);
        map.entrySet().stream().filter(e -> e.getKey().startsWith("component")).forEach(e -> System.out.println(e.getKey() + "=" + e.getValue()));


        /**
         * ApplicationContext 扩展BeanFactory的功能
         *
         * 1. 用于国际化，翻译等功能
         * 方法提供接口 MessageSource
         *   语言文件保存在以messages开头的文件中， messages.properties保存通用的信息
         */

        System.out.println(context.getMessage("hi", null, Locale.CHINA));
        System.out.println(context.getMessage("hi", null, Locale.JAPANESE));
        System.out.println(context.getMessage("hi", null, Locale.ENGLISH));

        /**
         *  2.通配符方式获取一组 Resource 资源 Resource即对资源文件的抽象
         *  方法提供接口 ResourcePatternResolver
         */
        Resource[] resources = context.getResources("classpath:application.yml");
        for (Resource resource : resources) {
            System.out.println(resource);
        }
        System.out.println("=============");
        //通配符匹配 classpath* 可以匹配jar中的文件
        Resource[] resources1 = context.getResources("classpath*:META-INF/spring.factories");
        for (Resource resource : resources1) {
            System.out.println(resource);
        }

        /**
         * 3.整合 Environment 环境
         * 接口 EnvironmentCapable
         * 获取配置环境变量
         */
        System.out.println(context.getEnvironment().getProperty("java_home"));
        System.out.println(context.getEnvironment().getProperty("server.port"));

        System.out.println("=============event=============");

        /**
         * 新的代码解耦方式
         * 4. 事件发布与监听
         * 方法定义接口 ApplicationEventPublisher
         * 事件类
         *
         *
         *  1. 事件定义
         *  2. 事件发布
         *  3. 事件的监听与接收
         *      任意的组件都可以监听并接收事件 @EventListener
         *
         */
        //发送事件
        //context.publishEvent(new UserRegisteredEvent(context));


        Component1 comp1 = context.getBean(Component1.class);
        //发送事件
        comp1.register();

        /**
         * 使用AOP实现解耦
         */

    }
}
