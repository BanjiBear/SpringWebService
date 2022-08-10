package com.tommy.service.web.store.book.Application.Controller;

import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class LogicProcessor {
	private String query;
	private String type;
	private StorageReader storageReader;
	private String[] newBook;
	
	//For Bean Private Use
	private BookInformation book = new BookInformation();
	private Status status = null;
	private ArrayList<BookInformation> result = new ArrayList<>();
	private ArrayList<Object> response = new ArrayList<>();
	private final String[] TYPE = new String[] {"bookname", "author", "isbn", "price", "publishdate"};
	
	public LogicProcessor(StorageReader storageReader){ this.storageReader = storageReader; }
	
	public final void setType(String type){ this.type = type.toLowerCase(); }
	public final String getType(){ return this.type; }
	
	public final void setQuery(String query){ this.query = query.toLowerCase(); }
	public final String getQuery(){ return this.query; }
	
	//public final void setStorageReader(StorageReader storagereader){ this.storagereader = storagereader; }
	public final StorageReader getStorageReader(){ return this.storageReader; }
	
	public final void setNewBook(String[] newBook){ this.newBook = newBook; }
	public final String[] getNewBook(){ return this.newBook; }
	
	public final ArrayList<Object> getBooks(String query, ArrayList<String[]> storage){
		if(query.equals("")){
			for(int i = 1; i < storage.size(); i++){
				result.add(bookFormation(storage.get(i)));
			}
			this.status = Status.CORRESPONDING_RESULT_FOUND;
		}
		else {
			for(int i = 0; i < this.TYPE.length; i++){
				if(this.type.equals(this.TYPE[i])){
					for(int j = 1; j < storage.size(); j++){
						if(storage.get(j)[i].contains(query)) {
							result.add(bookFormation(storage.get(j)));
							this.status = Status.CORRESPONDING_RESULT_FOUND;
						}
					}
					this.status = Status.INVALID_QUERY;
					break;
				}
				this.status = Status.INVALID_TYPE;
			}
		}
		
		response.add(this.status);
		response.add(this.status.getStatus_message());
		response.add(this.result);
		return this.response;
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
	
	public final ArrayList<Object> postNewBook(String[] newBook, ArrayList<String[]> storage){
		//Write to CSV: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
		book = bookFormation(newBook);
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
		response.add(this.status.getStatus_code());
		response.add(this.status.getStatus_message());
		response.add(this.book);
		return response;
	}
	
	public final ArrayList<Object> putNewBookInfo(String isbn, String[] newBook, ArrayList<String[]> storage){
		
		ArrayList<String[]> duplicateStorage = new ArrayList<>();
		
		// find record by id(by isbn)
		if(isUnique(newBook[2], storage) && !isUnique(isbn, storage)){
			for(int i = 0; i < storage.size(); i++){
				if(storage.get(i)[2].equals(isbn.replace("-", ""))){
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
		response.add(this.status.getStatus_code());
		response.add(this.status.getStatus_message());
		return response;
	}
	
	public final ArrayList<Object> deleteBook(String isbn, ArrayList<String[]> storage){
		
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
		response.add(this.status.getStatus_code());
		response.add(this.status.getStatus_message());
		return response;
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
}