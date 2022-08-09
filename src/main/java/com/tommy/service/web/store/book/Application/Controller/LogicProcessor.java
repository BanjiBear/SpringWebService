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
	private ArrayList<BookInformation> queryResult = new ArrayList<>();
	private HashMap<String, BookInformation> returnBookWithStatus = new HashMap<>();
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
	
	public final ArrayList<BookInformation> getBooks(String query, ArrayList<String[]> storage){
		if(query.equals("")){
			for(int i = 1; i < storage.size(); i++){
				bookFormation(storage.get(i));
			}
			return this.queryResult;
		}
		for(int i = 0; i < this.TYPE.length; i++){
			if(this.type.equals(this.TYPE[i])){
				for(int j = 1; j < storage.size(); j++){
					if(storage.get(j)[i].contains(query)) {
						bookFormation(storage.get(j));
					}
				}
				break;
			}
		}
		return this.queryResult;
	}
	
	public final ArrayList<BookInformation> getOnlyMatchedBooks(String query, ArrayList<String[]> storage){
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
		return this.queryResult;
	}
	
	public final HashMap<String, BookInformation> postNewBook(String[] newBook, ArrayList<String[]> storage){
		//Write to CSV: https://www.geeksforgeeks.org/writing-a-csv-file-in-java-using-opencsv/
		bookFormation(newBook);
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
			Status status = new Status();
			status.setStatusCode("Status: 200 OK");
			returnBookWithStatus.put(status.getStatusCode(), this.queryResult.get(0));
		}
		else{
			Status status = new Status();
			status.setStatusCode("Status: 409 Conflict");
			returnBookWithStatus.put(status.getStatusCode(), this.queryResult.get(0));
		}
		return returnBookWithStatus;
	}
	
	public final HashMap<String, BookInformation> putNewBookInfo(String isbn, String[] newBook, ArrayList<String[]> storage){
		
		bookFormation(newBook);
		ArrayList<String[]> duplicateStorage = new ArrayList<>();
		Status status = new Status();
		
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
			status.setStatusCode("Status: 200 ok");
			returnBookWithStatus.put(status.getStatusCode(), this.queryResult.get(0));
		}
		else if(!isUnique(newBook[2], storage)){
			status.setStatusCode("Status: 409 Conflict");
			returnBookWithStatus.put(status.getStatusCode(), this.queryResult.get(0));
		}
		else{
			status.setStatusCode("Status: 406 Not Acceptable");
			returnBookWithStatus.put(status.getStatusCode(), this.queryResult.get(0));
		}
		return returnBookWithStatus;
	}
	
	private final void bookFormation(String[] bookInStringArray){
		BookInformation BI = new BookInformation();
		BI.setBookName(bookInStringArray[0]);
		BI.setAuthor(bookInStringArray[1]);
		BI.setISBN(bookInStringArray[2]);
		BI.setPrice(bookInStringArray[3]);
		BI.setPublishDate(bookInStringArray[4]);
		this.queryResult.add(BI);
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