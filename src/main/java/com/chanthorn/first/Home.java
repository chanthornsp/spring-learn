package com.chanthorn.first;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1")
public class Home {

    @GetMapping("/greeting")
    String home(){
        return "welcome to Spring Boot Application";
    }
}
