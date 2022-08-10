package com.tommy.service.web.store.book.Application.Controller;

public class BookInformation{
	
	private String book_name;
    private String author;
    private String publish_date;
    private String isbn;
    private String price;

    public BookInformation(){}

    public void setBook_name(String book_name){ this.book_name = book_name; }
    public String getBook_name(){ return this.book_name; }

    public void setAuthor(String author){ this.author = author; }
    public String getAuthor(){ return this.author; }

    public void setPublish_date(String publish_date){ this.publish_date = publish_date; }
    public String getPublish_date(){ return this.publish_date; }

    public void setISBN(String isbn){ this.isbn = isbn; }
    public String getISBN(){ return this.isbn; }

    public void setPrice(String price){ this.price = price; }
    public String getPrice(){ return this.price; }
    
}
