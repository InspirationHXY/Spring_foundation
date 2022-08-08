package com.yingyi.test;

import java.util.ArrayList;
import java.util.List;

public class TestMethodTemplate {
    public static void main(String[] args) {

        MyBeanFactory beanFactory = new MyBeanFactory();


        //动态添加扩展的功能
        beanFactory.addBeanPostProcessor(beanPostProcessor -> {
            System.out.println("解析@Autowired");
        });

        beanFactory.addBeanPostProcessor(beanPostProcessor -> {
            System.out.println("解析@Resource");
        });
        beanFactory.getBean();

    }


    //模板方法 Template Method Pattern

    /**
     * 程序设计思想：
     *  程序中包含特定的步骤，需要做一些扩展，如何在不改变原有代码的基础上扩展？
     *  程序： 静态固定步骤 + 动态可扩展的操作
     *  思路：将可扩展的地方定义为接口，然后在程序中动态的调用接口实现类的方法
     */

    static class MyBeanFactory{
        //用于存放BeanPostProcessor接口实现类的集合
        private List<BeanPostProcessor> processors = new ArrayList<>();
        public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor){
            processors.add(beanPostProcessor);
        }


        public Object getBean(){
            Object bean = new Object();

            System.out.println("构造" + bean);
            System.out.println("依赖注入" + bean); //@Autowired @Resource
            //依赖注入的功能扩展
            for (BeanPostProcessor processor : processors) {
                processor.inject(bean);
            }

            System.out.println("初始化" + bean);

            return bean;
        }
    }

    //功能接口
    static interface BeanPostProcessor{
        public void inject(Object bean); //对注入阶段的扩展
    }
}
