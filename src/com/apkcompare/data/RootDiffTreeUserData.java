package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.util.Log;

public class RootDiffTreeUserData extends DiffTreeUserData {
	public RootDiffTreeUserData(ApkInfo apkinfo) {
		super(apkinfo.manifest.packageName, "Root", apkinfo);
		this.isfolder = true;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		//return this.title.equals(data.toString());
		return true;
	}
	
	@Override
	public File makeFilebyNode() {		
		Log.d(apkinfo.filePath);
		return new File(apkinfo.filePath);
	}
}
