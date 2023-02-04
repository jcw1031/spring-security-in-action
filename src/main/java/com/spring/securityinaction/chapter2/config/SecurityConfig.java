package com.spring.securityinaction.chapter2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * 모든 요청에 인증이 필요하도록 하는 설정
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated() // 모든 요청에 인증이 필요하다.
                .and().build();
    }*/

    /**
     * 모든 요청에 인증이 필요 없도록 하는 설정
     * @param httpSecurity
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().permitAll() // 모든 요청을 인증 없이 허가한다.
                .and().build();
    }
}
