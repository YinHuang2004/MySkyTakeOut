package com.sky.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
//configurationProperties表示当前类是一个配置属性类：作用是封装配置文件中的配置项，封装的是springboot的配置项（两个配置文件application.properties和application.yml）
//prefix表示配置文件中的属性前缀，这里是sky.jwt，表示将配置文件（application-dev.yml）中的属性值注入到当前类的属性中时，需要将属性名前缀为sky.jwt的属性值注入到当前类的属性中
//例如：sky.jwt.adminSecretKey = 123456，将123456注入到adminSecretKey属性中
//将配置文件封装为java对象注入给controller使用，controller中可以直接使用adminSecretKey属性等，而不需要在controller中注入JwtProperties类的实例
//不用一个个的@value注解注入属性值，直接使用当前类的属性即可获取到配置文件中的属性值

@ConfigurationProperties(prefix = "sky.jwt")
@Data
public class JwtProperties {

    /**
     * 管理端员工生成jwt令牌相关配置
     */
    private String adminSecretKey;
    private long adminTtl;
    private String adminTokenName;

    /**
     * 用户端微信用户生成jwt令牌相关配置
     */
    private String userSecretKey;
    private long userTtl;
    private String userTokenName;

}
