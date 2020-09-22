package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;

public class ComponentPassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	File file;
	String size;
	String reportinfo;
	
	public ComponentPassKeyDiffTreeUserData(String title) {
		super(title, "", null);
	}
	public ComponentPassKeyDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	
	public ComponentPassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}
	
	public void setinforeport(String report) {
		this.reportinfo = report;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
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
