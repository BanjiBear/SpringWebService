package com.tommy.service.web.store.book.Application.Controller;

public enum Status{
	
	BOOK_ISBN_EXIST (99, ""),
	BOOK_NOT_FOUND (80, ""),
	INVALID_QUERY (70, ""),
	INVALID_TYPE (60, ""),
	BOOK_DELETED (3, ""),
	BOOK_UPDATED (2, ""),
	BOOK_CREATED (1, ""),
	CORRESPONDING_RESULT_FOUND (0, "");
	
	private int status_code;
	private String status_message;
	
	Status(int status_code, String status_message) {
		this.status_code = status_code;
		this.status_message = status_message;
	}
	
	//public void setStatus_code(int status_code){ this.status_code = status_code; }
	public int getStatus_code(){ return this.status_code; }
	
	//public void setStatus_message(String status_message){ this.status_message = status_message; }
	public String getStatus_message(){ return this.status_message; }
	
}