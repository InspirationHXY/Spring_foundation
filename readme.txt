使用AOP方式实现代码解耦
    用户注册与发送消息或者发送邮件或者回电等方式

AOP的依赖jar包
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-aop</artifactId>
</dependency>

实现：
    1. 在service层定义一个用户注册方法
    2. 定义一个类，其中定义通知
    3. 测试


