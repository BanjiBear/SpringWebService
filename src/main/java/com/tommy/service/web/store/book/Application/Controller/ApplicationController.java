package com.tommy.service.web.store.book.Application.Controller;

import java.util.ArrayList;
import java.util.HashMap;

// Spring dependencies
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

// Package dependencies
import com.tommy.service.web.store.book.Application.Config.AppConfig;

@RestController
public class ApplicationController{
	
	/* Testing purposes */
    @GetMapping("/test/{str}")
    //@ResponseBody is included in the @RestController annotation
    public String test(@PathVariable String str) {
    	String testStr = "The service is available, testing string: " + str;
		return testStr;
    }
	
	@GetMapping("/books")
	public ArrayList<BookInformation> getBooks(
			@RequestParam(name = "type", defaultValue = "") String type,
			@RequestParam(name = "query", defaultValue = "") String query){
		
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		LogicProcessor logicProcessor = (LogicProcessor) context.getBean(LogicProcessor.class);
		
		logicProcessor.setType(type); //System.out.println(bookQuery.getType());
		logicProcessor.setQuery(query); //System.out.println(bookQuery.getQuery());
		logicProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return logicProcessor.getBooks(logicProcessor.getQuery(), logicProcessor.getStorageReader().getStorage());
	}
	
	@GetMapping("/book")
	public ArrayList<BookInformation> getOnlyMatchedBooks(
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "query") String query){
		
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		LogicProcessor logicProcessor = (LogicProcessor) context.getBean(LogicProcessor.class);
		
		logicProcessor.setType(type);
		logicProcessor.setQuery(query);
		logicProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return logicProcessor.getOnlyMatchedBooks(logicProcessor.getQuery(), logicProcessor.getStorageReader().getStorage());
	}
	
	@PostMapping("/create")
	public HashMap<String, BookInformation> postNewBook(@RequestBody BookInformation newBookInformation){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		LogicProcessor logicProcessor = (LogicProcessor) context.getBean(LogicProcessor.class);
		
		logicProcessor.setNewBook(new String[] {newBookInformation.getBookName(), newBookInformation.getAuthor(), newBookInformation.getISBN(), newBookInformation.getPrice(), newBookInformation.getPublishDate()});
		logicProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		/*
		 * curl command:
		 * curl -X POST localhost:8080/SpringRestJwt/create 
		 * -H "Content-type:application/json" 
		 * -d "{
		 * 		\"bookName\": \"SpongeBob Square Pants\", 
		 * 		\"author\": \"Mr. Kong the big Kong\", 
		 * 		\"isbn\": \"-0000000000002-\", 
		 * 		\"price\": \"255\", 
		 * 		\"publishDate\": \"2015-10-11\"
		 * 	}"
		 */
		
		return logicProcessor.postNewBook(logicProcessor.getNewBook(), logicProcessor.getStorageReader().getStorage());
	}
	
	@PutMapping("/book/update")
	public HashMap<String, BookInformation> putNewBookInfo(@RequestBody BookInformation modefiedBookInformation, @RequestParam(required = true, name = "isbn") String isbn){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		LogicProcessor logicProcessor = (LogicProcessor) context.getBean(LogicProcessor.class);
		
		logicProcessor.setQuery(isbn);
		logicProcessor.setNewBook(new String[] {modefiedBookInformation.getBookName(), modefiedBookInformation.getAuthor(), modefiedBookInformation.getISBN(), modefiedBookInformation.getPrice(), modefiedBookInformation.getPublishDate()});
		logicProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return logicProcessor.putNewBookInfo(logicProcessor.getQuery(), logicProcessor.getNewBook(), logicProcessor.getStorageReader().getStorage());
	}
}