package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkscanner.data.apkinfo.ApkInfo;

public class ComponentPassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	File file;
	String size;
	String reportinfo;
	
	public ComponentPassKeyDiffTreeUserData(String title) {
		super(title, "", null);
		// TODO Auto-generated constructor stub
	}
	public ComponentPassKeyDiffTreeUserData(String title, String key) {
		super(title, key, null);
		// TODO Auto-generated constructor stub
	}
	
	public ComponentPassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
		// TODO Auto-generated constructor stub
	}
	
	public void setinforeport(String report) {
		// TODO Auto-generated method stub
		this.reportinfo = report;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		ComponentPassKeyDiffTreeUserData temp = (ComponentPassKeyDiffTreeUserData)data;		

		if(temp.reportinfo.equals(reportinfo)) {
			return true;
		} else {			
			return false;
		}
		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
	
	@Override
	public File makeFilebyNode() {
		return makeFileForString(reportinfo);
	}
}
