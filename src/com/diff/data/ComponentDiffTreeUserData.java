package com.diff.data;

import java.io.File;

public class ComponentDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	File file;
	String size;
	String compressed;
	String reportinfo;
	
	public ComponentDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public ComponentDiffTreeUserData(String title, String key) {
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
		ComponentDiffTreeUserData temp = (ComponentDiffTreeUserData)data;		

		if(temp.reportinfo.equals(reportinfo)) {
			return true;
		} else {
			return false;
		}
		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
}
