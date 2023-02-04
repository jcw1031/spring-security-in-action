package com.spring.securityinaction.chapter2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetailsManager userDetailsService = new InMemoryUserDetailsManager();

        // 주어진 사용자 이름, 암호, 권한 목록으로 사용자를 생성한다.
        UserDetails user = User.withUsername("woopaca")
                .password("woopaca")
                .authorities("read")
                .build();
        userDetailsService.createUser(user); // UserDetailsService에서 관리하도록 사용자 추가

        return userDetailsService;
    }
}
