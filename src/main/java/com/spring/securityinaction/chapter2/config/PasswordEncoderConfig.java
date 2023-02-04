package com.spring.securityinaction.chapter2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class PasswordEncoderConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); // 이 PasswordEncoder는 운영 단계에서는 절대 사용하면 안 된다. 예제로 사용하기 편리하다. 암호화 x
    }
}
