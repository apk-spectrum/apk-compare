package com.diff.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apkscanner.util.Log;

public class FileDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	File file;
	String filepath;
	static String apkfilePath1;
	static String apkfilePath2;
	
	public FileDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public FileDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

	public static void setApkfilepath(String mapkfilePath1, String mapkfilePath2) {
		apkfilePath1 = mapkfilePath1;
		apkfilePath2 = mapkfilePath2;
	}
	
	public void setFile(String filepath) {
		//Log.d(""+ file);
		//this.file = file;
		
		this.filepath = filepath;		
	}	
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		FileDiffTreeUserData temp = (FileDiffTreeUserData)data;
		
		//Log.d(apkfilePath + ":" + temp.apkfilePath);
		
		return issameFile(temp.title);
	}
	
	protected boolean issameFile(String path) {
		ZipFile zipFile;
		try {
			zipFile = new ZipFile(apkfilePath1);
			ZipEntry entry = zipFile.getEntry(title);
			
			zipFile = new ZipFile(apkfilePath2);
			ZipEntry entry2 = zipFile.getEntry(path);
			
			return entry.getSize() == entry2.getSize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
}
