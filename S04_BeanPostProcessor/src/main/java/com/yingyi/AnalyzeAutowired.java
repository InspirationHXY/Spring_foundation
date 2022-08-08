package com.yingyi;


import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.DependencyDescriptor;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.ContextAnnotationAutowireCandidateResolver;
import org.springframework.core.MethodParameter;
import org.springframework.core.env.StandardEnvironment;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

//AutowiredAnnotationBeanPostProcessor 运行分析
public class AnalyzeAutowired {
    public static void main(String[] args) throws Throwable {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        // 认为bean是已经创建好的（成品），可以直接使用（放入beanFactory中直接使用）， 不会有创建、依赖注入、初始化过程
        beanFactory.registerSingleton("bean2", new Bean2());
        beanFactory.registerSingleton("bean3", new Bean3());

        //@Value 获取@Value中的值
        beanFactory.setAutowireCandidateResolver(new ContextAnnotationAutowireCandidateResolver());

        //  ${} 的解析器 (Lambda表达式的对象引用)
        beanFactory.addEmbeddedValueResolver(new StandardEnvironment()::resolvePlaceholders);
        /**
         * 深入分析AutowiredAnnotationBeanPostProcessor
         * 单独拿出来分析在beanFactory中它的运行流程
         */
        AutowiredAnnotationBeanPostProcessor processor = new AutowiredAnnotationBeanPostProcessor();

        /**
         * 绑定一个beanFactory用于查找需要注入的对象的依赖bean(在beanFactory中)
         */
        processor.setBeanFactory(beanFactory);

        Bean1 bean1 = new Bean1();
        System.out.println(bean1);
//
//        //执行依赖注入 @Autowired @Value
//        processor.postProcessProperties(null, bean1, "bean1");
//        System.out.println(bean1);

        /**
         * 分析processor.postProcessProperties()方法
         */
        // 1. 查找哪些属性、方法加了 @Autowired, 这称之为 InjectionMetadata
        Method findAutowiringMetadata = AutowiredAnnotationBeanPostProcessor.class.getDeclaredMethod("findAutowiringMetadata", String.class, Class.class, PropertyValues.class);
        findAutowiringMetadata.setAccessible(true);
        InjectionMetadata metadata = (InjectionMetadata) findAutowiringMetadata.invoke(processor, "bean1", Bean1.class, null);
        System.out.println(metadata);


        // 2. 调用 InjectionMetadata 来进行依赖注入, 注入时按类型查找值
//        metadata.inject(bean1, "bean1", null);
//        System.out.println(bean1);

        /**
         * metadata.inject()方法的分析
         *  找到加了 @Autowired 的成员变量、方法
         *  封装为 DependencyDescriptor 参数：方法、成员变量， 是否是一定注入（没有找到对应的bean时是否报错）
         *  beanFactory.doResolveDependency() 根据成员变量类型找对应要注入的bean值(beanFactory中)
         */
        // 3. 如何按类型查找值

        //成员变量依赖注入
        Field bean3 = Bean1.class.getDeclaredField("bean3");
        System.out.println(bean3.getType());
        DependencyDescriptor dd1 = new DependencyDescriptor(bean3, false);
        Object o1 = beanFactory.doResolveDependency(dd1, null, null, null);
        System.out.println(o1);

        bean3.setAccessible(true);
        bean3.set(bean1, o1);
        bean3.setAccessible(false);
        //bean3.set(bean1, o1);



        //方法参数的注入
        Method setBean2 = Bean1.class.getDeclaredMethod("setBean2", Bean2.class);
        DependencyDescriptor dd2 = new DependencyDescriptor(new MethodParameter(setBean2, 0), false);
        Object o2 = beanFactory.doResolveDependency(dd2, null, null, null);


        System.out.println(o2);
        bean1.setBean2((Bean2) o2);

        //@Value值获取
        Method setHome = Bean1.class.getDeclaredMethod("setHome", String.class);
        DependencyDescriptor dd3 = new DependencyDescriptor(new MethodParameter(setHome, 0), false);
        Object o3 = beanFactory.doResolveDependency(dd3, null, null, null);
        System.out.println(o3);
        bean1.setHome((String) o3);

        System.out.println(bean1);
    }
}
