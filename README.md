# java-spring-boot2-security-jwt-demo
java-spring-boot2-security-jwt-demo

自定义密码 方便与其他语言通信

java 11

mysql 8.x

spring-boot2 2.1.0.RELEASE

#  注册 

POST JSON /auth/register

```shell
{"username":"admin","password":"admin"}
```

# 登录 

POST JSON /auth/login

```shell
{"username":"admin","password":"admin"}
```

# sql
请自行创建数据库 `test`，然后执行下面sql创建表

```SQL
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255)  DEFAULT NULL,
  `password` varchar(255)  DEFAULT NULL,
  `role` varchar(255)  DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```