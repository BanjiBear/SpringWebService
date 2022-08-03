package com.tommy.service.web.store.book.spring.Application;

import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
 
@RestController
public class GreetController {
 
 
    @GetMapping("/greet/{name}")
    public String greet(@PathVariable String name, ModelMap model) {
        String greet = "Hello!!! " + name + " How are You?";
        return greet;
    }
}