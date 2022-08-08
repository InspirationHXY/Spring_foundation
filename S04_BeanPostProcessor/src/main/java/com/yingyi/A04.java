package com.yingyi;

import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.CommonAnnotationBeanPostProcessor;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.context.support.GenericApplicationContext;

/*
    bean 后处理器的作用
 */
public class A04 {
    public static void main(String[] args) {
        // ⬇️GenericApplicationContext 是一个【干净】的容器
        GenericApplicationContext context = new GenericApplicationContext();

        // ⬇️用原始方法注册三个 bean
        context.registerBean("bean1", Bean1.class);
        context.registerBean("bean2", Bean2.class);
        context.registerBean("bean3", Bean3.class);
        context.registerBean("bean4", Bean4.class);


        //报错：No qualifying bean of type 'java.lang.String' available
        /**
         * 原因：无法获取@Value注解要注入的值
         * 解决：更换一个解析器
         */
        context.getDefaultListableBeanFactory().setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        //@Autowired @Value
        context.registerBean(AutowiredAnnotationBeanPostProcessor.class);

        //@Resource @PostConstruct @PreDestroy
        //注意@Resource和@Autowired的执行顺序
        context.registerBean(CommonAnnotationBeanPostProcessor.class);

        //@ConfigurationProperties
        ConfigurationPropertiesBindingPostProcessor.register(context.getDefaultListableBeanFactory());


        // ⬇️初始化容器
        context.refresh(); //执行beanFactory后处理器，添加bean后处理器，初始化所有单例

        System.out.println(context.getBean(Bean4.class));


        // ⬇️销毁容器
        context.close();

        /*
            学到了什么
                a. @Autowired 等注解的解析属于 bean 生命周期阶段(依赖注入, 初始化)的扩展功能
                b. 这些扩展功能由 bean 后处理器来完成
         */
    }
}
