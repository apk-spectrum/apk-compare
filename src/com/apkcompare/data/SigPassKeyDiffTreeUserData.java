package com.apkcompare.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkscanner.util.Log;

public class SigPassKeyDiffTreeUserData extends FilePassKeyDiffTreeUserData implements MappingImp{
	String original;
	boolean isFile;
	public SigPassKeyDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public SigPassKeyDiffTreeUserData(String title, String key) {
		super(title, key);
		
		 isFile = true;
		// TODO Auto-generated constructor stub
	}

	public void setOrignalSig(String original) {
		this.original = original;
		this.isFile = false;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		SigPassKeyDiffTreeUserData temp = (SigPassKeyDiffTreeUserData)data;

		if(isFile) {
			return issameFile(temp.title, temp.apkfilePath);
		} else {
			return original.equals(temp.original);
		}
	}
}
