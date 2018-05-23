package com.diff.gui;

import java.io.File;

public class FileDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	File file;
	String size;
	String compressed;
	
	
	public FileDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public FileDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

	public void setFilesInfo(String size, String compressed) {
		this.size = size;
		this.compressed = compressed;
	}	
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		FileDiffTreeUserData temp = (FileDiffTreeUserData)data;
		
		
		if(temp.size.equals(size) && temp.compressed.equals(compressed)) {
			return true;
		} else {
			title = title + " : " + size + " : " + compressed;
			temp.title = temp.title + " : " + temp.size + " : " + temp.compressed;
			return false;
		}
		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
}
