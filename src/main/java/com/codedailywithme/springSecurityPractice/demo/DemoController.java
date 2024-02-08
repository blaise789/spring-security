package com.codedailywithme.springSecurityPractice.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/demo-controller")
public class DemoController {
    @GetMapping
    public String helloMessage(){
        return "hello it is blaise on the hit";
    }
}
