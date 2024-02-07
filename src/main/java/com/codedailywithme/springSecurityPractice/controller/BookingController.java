package com.codedailywithme.springSecurityPractice.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/hello")
public class BookingController {
    public ResponseEntity<String> helloMessage(){
        return ResponseEntity.ok("hello from secured point world");
    }

}
