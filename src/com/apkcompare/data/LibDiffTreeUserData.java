package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;

public class LibDiffTreeUserData extends FilePassKeyDiffTreeUserData {
	File file;
	String size;
	String compressed;	
	
	public LibDiffTreeUserData(String title) {
		super(title, "", null);
	}
	public LibDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	
	public LibDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}

	public void setFilesInfo(String size, String compressed) {
		this.size = size;
		this.compressed = compressed;		
	}	
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		//return this.title.equals(data.toString());
		LibDiffTreeUserData temp = (LibDiffTreeUserData)data;		
		
		if(issameFile(temp)) {
			return true;
		} else {
			//title = title + " : " + size + " : " + compressed;
			//temp.title = temp.title + " : " + temp.size + " : " + temp.compressed;
			return false;
		}
		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
}
