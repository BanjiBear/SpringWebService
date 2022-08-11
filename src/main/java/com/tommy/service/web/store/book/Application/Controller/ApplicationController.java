package com.tommy.service.web.store.book.Application.Controller;

import java.util.ArrayList;
import java.util.HashMap;

// Spring dependencies
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.bind.annotation.DeleteMapping;
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
	public ResponseFactory getBooks(
			@RequestParam(name = "type", defaultValue = "") String type,
			@RequestParam(name = "query", defaultValue = "") String query){
		
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		RequestProcessor requestProcessor = (RequestProcessor) context.getBean(RequestProcessor.class);
		
		requestProcessor.setType(type); //System.out.println(bookQuery.getType());
		requestProcessor.setQuery(query); //System.out.println(bookQuery.getQuery());
		requestProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return requestProcessor.getBooks(requestProcessor.getQuery(), requestProcessor.getStorageReader().getStorage());
	}
	
	/*@GetMapping("/book")
	public ArrayList<BookInformation> getOnlyMatchedBooks(
			@RequestParam(required = true, name = "type") String type,
			@RequestParam(required = true, name = "query") String query){
		
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		requestProcessor requestProcessor = (requestProcessor) context.getBean(requestProcessor.class);
		
		requestProcessor.setType(type);
		requestProcessor.setQuery(query);
		requestProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return requestProcessor.getOnlyMatchedBooks(requestProcessor.getQuery(), requestProcessor.getStorageReader().getStorage());
	}*/
	
	@PostMapping("/books")
	public ResponseFactory postNewBook(@RequestBody BookInformation newBookInformation){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		RequestProcessor requestProcessor = (RequestProcessor) context.getBean(RequestProcessor.class);
		
		requestProcessor.setNewBook(new String[] {newBookInformation.getBook_name(), newBookInformation.getAuthor(), newBookInformation.getISBN(), newBookInformation.getPrice(), newBookInformation.getPublish_date()});
		requestProcessor.getStorageReader().setStorage();
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
		
		return requestProcessor.postNewBook(requestProcessor.getNewBook(), requestProcessor.getStorageReader().getStorage());
	}
	
	@PutMapping("/books/{isbn}")
	public ResponseFactory putNewBookInfo(
			@RequestBody BookInformation modefiedBookInformation, 
			@PathVariable(name = "isbn") String isbn){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		RequestProcessor requestProcessor = (RequestProcessor) context.getBean(RequestProcessor.class);
		
		requestProcessor.setQuery(isbn);
		requestProcessor.setNewBook(new String[] {modefiedBookInformation.getBook_name(), modefiedBookInformation.getAuthor(), modefiedBookInformation.getISBN(), modefiedBookInformation.getPrice(), modefiedBookInformation.getPublish_date()});
		requestProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return requestProcessor.putNewBookInfo(requestProcessor.getQuery(), requestProcessor.getNewBook(), requestProcessor.getStorageReader().getStorage());
	}
	
	@DeleteMapping("/books/{isbn}")
	public ResponseFactory deleteBook(@PathVariable String isbn){
		ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
		//((AnnotationConfigApplicationContext) context).refresh();
		RequestProcessor requestProcessor = (RequestProcessor) context.getBean(RequestProcessor.class);
		
		requestProcessor.setQuery("-"+isbn+"-");
		requestProcessor.getStorageReader().setStorage();
		((AnnotationConfigApplicationContext)context).close();
		
		return requestProcessor.deleteBook(requestProcessor.getQuery(), requestProcessor.getStorageReader().getStorage());
	}
}