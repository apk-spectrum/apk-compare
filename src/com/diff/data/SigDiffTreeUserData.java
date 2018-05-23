package com.diff.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apkscanner.util.Log;

public class SigDiffTreeUserData extends FileDiffTreeUserData implements MappingImp{
	String original;
	boolean isFile = true;
	public SigDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public SigDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

	public void setOrignalSig(String original) {
		this.original = original;
		this.isFile = false;
	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		SigDiffTreeUserData temp = (SigDiffTreeUserData)data;

		if(isFile) {
			return issameFile(temp.title);
		} else {
			return original.equals(temp.original);
		}
	}
}
