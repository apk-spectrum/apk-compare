package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;

public class RootDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	public RootDiffTreeUserData(String title) {
		super(title, "Root");
		this.isfolder = true;
		// TODO Auto-generated constructor stub
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		return true;
	}
	
}
