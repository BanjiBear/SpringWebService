package com.tommy.service.web.store.book.spring.Application;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@EnableWebMvc
@Configuration
@ComponentScan("com.tommy")
public class WebConfig implements WebMvcConfigurer {

}