package com.deke.mall.aop;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Slf4j
@TestAnnotation
public class TestController {

    @GetMapping("/1")
    public String log1(){
        log.info("111111111");
        return "1";
    }

    @GetMapping("/2")
    public String log2(){
        log.info("22222");
        return "2";
    }
}
