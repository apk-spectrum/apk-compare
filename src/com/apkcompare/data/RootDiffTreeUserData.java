package com.apkcompare.data;

import java.io.File;

public class RootDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	
	public RootDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public RootDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		return true;
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}	
	
}
