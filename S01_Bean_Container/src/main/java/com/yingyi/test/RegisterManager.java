package com.yingyi.test;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RegisterManager {
    /**
     * AOP使用三要素：
     * 何时、何地、何事
     */

    //切点定义
    @Pointcut("execution(* com.yingyi.service.impl.*.*(..))")
    public void pointCut(){}
    //前置通知

    //后置通知

    //异常通知

    //最终通知

    //环绕通知
    @Around("pointCut()")
    public void handler(ProceedingJoinPoint pjp){
        try {
            Boolean registered = (Boolean) pjp.proceed();

            //添加后置操作
            if(registered){
                System.out.println("用户注册成功！");
            }else{
                System.out.println("用户注册失败！");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
