package com.tommy.service.web.store.book.Application.Controller;

import java.util.*;
//For CSV reading
import java.io.FileReader;
import com.opencsv.CSVReader;

public class StorageReader {
	private ArrayList<String[]> storage = new ArrayList<>();
	private final String path = "C:\\Users\\User\\Desktop\\SpringWebService\\WebService"
			+ "\\src\\main\\java\\com\\tommy\\service\\web\\store\\book\\Application"
			+ "\\Controller\\TommyBookStore.csv";
	private CSVReader csvReader;
	private String[] readLine = new String[] {};
	
	public StorageReader(){}
	
	public void setStorage(){
		try {
			this.csvReader = new CSVReader(new FileReader(this.path));
			while ((readLine = this.csvReader.readNext()) != null) {
				//convert into lower case before storage
				for(int i = 0; i < readLine.length; i++){
					this.readLine[i] = this.readLine[i].toLowerCase();
					if(i == 2){
						this.readLine[i] = this.readLine[i].replace("-", "");
					}
				}
				
				this.storage.add(readLine);
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public ArrayList<String[]> getStorage(){
		//System.out.println(this.storage.size());
		
		//Beware: index 0 is the title header row
		/*
		for(int i = 1; i < this.storage.size(); i++){
			String[] getRecord = this.storage.get(i);
			for (String token: getRecord) {
				System.out.println(token);
			}
		    System.out.print("\n");
			//System.out.println(this.storage.get(i));
		}
		*/
		return this.storage;
	}
	
	public String getPath(){
		return this.path;
	}
	
}
