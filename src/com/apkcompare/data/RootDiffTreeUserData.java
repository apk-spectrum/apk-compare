package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.util.SystemUtil;

public class RootDiffTreeUserData extends DiffTreeUserData {
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
	
	@Override
	public void openFileNode(ApkInfo apkinfo) {
		// TODO Auto-generated method stub
		
		SystemUtil.openFile(apkinfo.filePath);
	}
}
