package com.apkcompare.data;

import java.io.File;

public class LibDiffTreeUserData extends FileDiffTreeUserData implements MappingImp{
	File file;
	String size;
	String compressed;
	
	
	public LibDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public LibDiffTreeUserData(String title, String key) {
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
		LibDiffTreeUserData temp = (LibDiffTreeUserData)data;
		
		
		if(issameFile(title)) {
			return true;
		} else {
			title = title + " : " + size + " : " + compressed;
			temp.title = temp.title + " : " + temp.size + " : " + temp.compressed;
			return false;
		}
		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
}
