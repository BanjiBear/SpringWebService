package com.tommy.service.web.store.book.Application.Controller;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class RequestProcessor {
	private String query;
	private String type;
	private StorageReader storageReader;
	private String[] newBook;
	
	//For Bean Private Use
	private Status status = null;
	private ArrayList<BookInformation> book_data = new ArrayList<>();
	//private ArrayList<Object> response = new ArrayList<>();
	private final String[] TYPE = new String[] {"bookname", "author", "isbn", "price", "publishdate"};
	
	// Response Factory: return type to form name-value pair JSON format
	private ResponseFactory response_factory = new ResponseFactory();
	
	public RequestProcessor(StorageReader storageReader){ this.storageReader = storageReader; }
	
	public final void setType(String type){ this.type = type.toLowerCase(); }
	public final String getType(){ return this.type; }
	
	public final void setQuery(String query){ this.query = query.toLowerCase(); }
	public final String getQuery(){ return this.query; }
	
	//public final void setStorageReader(StorageReader storagereader){ this.storagereader = storagereader; }
	public final StorageReader getStorageReader(){ return this.storageReader; }
	
	public final void setNewBook(String[] newBook){ this.newBook = newBook; }
	public final String[] getNewBook(){ return this.newBook; }
	
	public final ResponseFactory getBooks(String query, ArrayList<String[]> storage){
		if(query.equals("")){
			for(int i = 1; i < storage.size(); i++){
				this.book_data.add(bookFormation(storage.get(i)));
			}
			this.status = Status.CORRESPONDING_RESULT_FOUND;
		}
		else {
			for(int i = 0; i < this.TYPE.length; i++){
				this.status = Status.INVALID_TYPE;
				if(this.type.equals(this.TYPE[i])){
					this.status = Status.INVALID_QUERY;
					for(int j = 1; j < storage.size(); j++){
						if(storage.get(j)[i].contains(query)) {
							this.book_data.add(bookFormation(storage.get(j)));
							this.status = Status.CORRESPONDING_RESULT_FOUND;
						}
					}
					break;
				}
			}
		}
		
		//response.add(this.status);
		//response.add(this.result);
		this.response_factory = formResponse(this.status, this.book_data);
		return this.response_factory;
	}
	
	/*public final ArrayList<BookInformation> getOnlyMatchedBooks(String query, ArrayList<String[]> storage){
		for(int i = 0; i < this.TYPE.length; i++){
			if(this.type.equals(this.TYPE[i])){
				for(int j = 1; j < storage.size(); j++){
					if(storage.get(j)[i].equals(query)){
						bookFormation(storage.get(j));
					}
				}
				break;
			}
		}
		//return this.queryResult;
	}*/
	
	public final ResponseFactory postNewBook(String[] newBook, ArrayList<String[]> storage){
		//Write to CSV: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
		this.book_data.add(bookFormation(newBook));
		if((storage.size() > 1 && isUnique(newBook[2], storage)) || storage.size() <= 1){
			try {		  
		    	// create CSVWriter object filewriter object as parameter
		    	CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(this.storageReader.getPath()), true));
		    	csvWriter.writeNext(newBook);
		    	// closing writer connection
		    	csvWriter.close();
		    }
		    catch (IOException e){
		    	e.printStackTrace(); 
		    }
			this.status = Status.BOOK_CREATED;
			
		}
		else{
			this.status = Status.BOOK_ISBN_EXIST;
		}
		//response.add(this.status);
		//response.add(this.book);
		this.response_factory = formResponse(this.status, this.book_data);
		return this.response_factory;
	}
	
	public final ResponseFactory putNewBookInfo(String isbn, String[] newBook, ArrayList<String[]> storage){
		
		this.book_data.add(bookFormation(newBook));
		ArrayList<String[]> duplicateStorage = new ArrayList<>();
		
		// find record by id(by isbn)
		if(newBook[2].replace("-", "").equals(isbn) ||  (isUnique(newBook[2], storage) && !isUnique(isbn, storage))){
			for(int i = 0; i < storage.size(); i++){
				if(storage.get(i)[2].equals(isbn)){
					duplicateStorage.add(newBook);
				}
				else {
					duplicateStorage.add(storage.get(i));
				}
			}
			try {
		    	// create CSVWriter object filewriter object as parameter
		    	CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(this.storageReader.getPath())));
		    	csvWriter.writeAll(duplicateStorage);
		    	// closing writer connection
		    	csvWriter.close();
		    }
		    catch (IOException e){
		    	e.printStackTrace(); 
		    }
			this.status = Status.BOOK_UPDATED;
		}
		else if(isUnique(isbn, storage)){
			this.status = Status.BOOK_NOT_FOUND;
		}
		else if(!isUnique(newBook[2], storage)){
			this.status = Status.BOOK_ISBN_EXIST;
		}
		//response.add(this.status);
		this.response_factory = formResponse(this.status, this.book_data);
		return this.response_factory;
	}
	
	public final ResponseFactory deleteBook(String isbn, ArrayList<String[]> storage){
		
		ArrayList<String[]> duplicateStorage = new ArrayList<>();
		
		if(isUnique(isbn, storage)) {
			this.status = Status.BOOK_NOT_FOUND;
		}
		else {
			for(int i = 0; i < storage.size(); i++){
				if(storage.get(i)[2].equals(isbn.replace("-", ""))){
					continue;
				}
				duplicateStorage.add(storage.get(i));
			}
			try {
		    	// create CSVWriter object filewriter object as parameter
		    	CSVWriter csvWriter = new CSVWriter(new FileWriter(new File(this.storageReader.getPath())));
		    	csvWriter.writeAll(duplicateStorage);
		    	// closing writer connection
		    	csvWriter.close();
		    }
		    catch (IOException e){
		    	e.printStackTrace();
		    }
			
			this.status = Status.BOOK_DELETED;
		}
		this.response_factory = formResponse(this.status, null);
		return this.response_factory;
	}
	
	private final BookInformation bookFormation(String[] bookInStringArray){
		BookInformation BI = new BookInformation();
		BI.setBook_name(bookInStringArray[0]);
		BI.setAuthor(bookInStringArray[1]);
		BI.setISBN(bookInStringArray[2]);
		BI.setPrice(bookInStringArray[3]);
		BI.setPublish_date(bookInStringArray[4]);
		//this.queryResult.add(BI);
		
		return BI;
	}
	
	private boolean isUnique(String isbn, ArrayList<String[]> storage){
		for(int i = 0; i < storage.size(); i++){
			if(storage.get(i)[2].equals(isbn.replace("-", ""))){
				return false;
			}
		}
		return true;
	}
	
	private ResponseFactory formResponse(Status status, ArrayList<BookInformation> result){
		this.response_factory.setStatus(status);
		this.response_factory.setStatus_code();
		//this.response_factory.setStatus_message();
		this.response_factory.setBook_data(result);
		return this.response_factory;
	}
}