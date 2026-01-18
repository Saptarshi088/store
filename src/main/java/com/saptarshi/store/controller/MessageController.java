package com.saptarshi.store.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/hello")
public class MessageController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
