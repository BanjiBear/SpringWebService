package com.tommy.service.web.store.book.Application.Config;

import com.tommy.service.web.store.book.Application.Controller.LogicProcessor;
import com.tommy.service.web.store.book.Application.Controller.StorageReader;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

// Root Configuration
@Configuration
@ComponentScan(basePackages = "com.tommy.service.web.store.book.Application.Controller")
public class AppConfig {
	
	 @Bean
	 public LogicProcessor bookQuery(){ return new LogicProcessor(storageReader()); }
	 
	 @Bean
	 public StorageReader storageReader(){ return new StorageReader(); }
	 
}