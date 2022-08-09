package com.tommy.service.web.store.book.Application.Controller;

public class BookInformation{
	
	private String bookName;
    private String author;
    private String publishDate;
    private String isbn;
    private String price;

    public BookInformation(){}

    public void setBookName(String bookName){ this.bookName = bookName; }
    public String getBookName(){ return this.bookName; }

    public void setAuthor(String author){ this.author = author; }
    public String getAuthor(){ return this.author; }

    public void setPublishDate(String publishDate){ this.publishDate = publishDate; }
    public String getPublishDate(){ return this.publishDate; }

    public void setISBN(String isbn){ this.isbn = isbn; }
    public String getISBN(){ return this.isbn; }

    public void setPrice(String price){ this.price = price; }
    public String getPrice(){ return this.price; }
    
}
