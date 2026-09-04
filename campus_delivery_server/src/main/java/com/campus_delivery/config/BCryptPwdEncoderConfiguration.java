package com.campus_delivery.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
//告诉 Spring，这个类是一个配置类，Spring 在启动时会扫描并加载它里面定义的所有 @Bean 方法。
public class BCryptPwdEncoderConfiguration {
    @Bean
    //告诉 Spring，这个方法的返回值（即 BCryptPasswordEncoder 对象）需要交给 Spring 容器管理。之后在项目的任何地方，只要通过 @Autowired 注入，就能获得这个实例。
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
