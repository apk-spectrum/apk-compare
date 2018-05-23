package com.diff.data;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apkscanner.util.Log;

public class FileDiffTreeUserData extends DiffTreeUserData implements MappingImp{
	File file;
	String filepath;
	String apkfilePath;
	public FileDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public FileDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

	public void setFile(String filepath, String apkfilePath) {
		//Log.d(""+ file);
		//this.file = file;
		
		this.filepath = filepath;
		this.apkfilePath = apkfilePath;
	}	
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		//return this.title.equals(data.toString());
		FileDiffTreeUserData temp = (FileDiffTreeUserData)data;

		//Log.d(apkfilePath);
		//Log.d(filepath);
		
		try {
			ZipFile zipFile = new ZipFile(apkfilePath);
			ZipEntry entry = zipFile.getEntry(title);
			ZipEntry entry2 = zipFile.getEntry(temp.title);
			
			
			
			if(entry.getSize() == entry2.getSize()) {
				return true;
			} else {
				return false;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;		
		//return ((FileDiffTreeUserData)data).size.equals(size) &&((FileDiffTreeUserData)data).compressed.equals(compressed);
	}
}
