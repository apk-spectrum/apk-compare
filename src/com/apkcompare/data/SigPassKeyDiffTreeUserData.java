package com.apkcompare.data;

import java.io.File;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.util.Log;

public class SigPassKeyDiffTreeUserData extends FilePassKeyDiffTreeUserData {
	String original;
	boolean isFile;
	public SigPassKeyDiffTreeUserData(String title) {
		super(title, "", null);
	}
	public SigPassKeyDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	
	public SigPassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo, boolean isFile) {
		super(title, key, apkinfo);
		this.isFile = isFile; 
	}

	public void setOrignalSig(String original) {
		this.original = original;
		this.isFile = false;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		SigPassKeyDiffTreeUserData temp = (SigPassKeyDiffTreeUserData)data;

		if(isFile) {
			return issameFile(temp);
		} else {
			return original.equals(temp.original);
		}
	}

	@Override
	public File makeFilebyNode() {		
		Log.d(title);
		if(isFile) return makeFileForFile(title);
		else return makeFileForString(original);
	}
}
