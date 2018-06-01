package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.util.Log;

public class ComponentPassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	File file;
	String size;
	String reportinfo;
	
	public ComponentPassKeyDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public ComponentPassKeyDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

	public void setinforeport(String reportinfo) {
		this.reportinfo = reportinfo;		
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
	public File makeFilebyNode(ApkInfo apkinfo) {
		return makeFileForString(reportinfo, apkinfo);
	}
}
