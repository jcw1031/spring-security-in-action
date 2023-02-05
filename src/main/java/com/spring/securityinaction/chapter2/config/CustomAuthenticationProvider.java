package com.spring.securityinaction.chapter2.config;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.Arrays;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Override // 인증의 전체 논리를 나타내는 메서드
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        // 해당 조건문은 UserDetailsService 및 PasswordEncoder의 책임을 대체한다.
        if (username.equals("woopaca") && password.equals("woopaca")) {
            return new UsernamePasswordAuthenticationToken(username, password, Arrays.asList());
        }

        throw new AuthenticationCredentialsNotFoundException("Error in authentication!");
    }

    @Override // Authentication 형식의 구현을 추가하는 메서드
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class
                .isAssignableFrom(authentication);
    }
}
