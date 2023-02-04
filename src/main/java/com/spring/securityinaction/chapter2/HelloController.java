package com.spring.securityinaction.chapter2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/hello")
    public String hello() {
        log.info("/hello 호출");
        return "Hello!";
    }
}
