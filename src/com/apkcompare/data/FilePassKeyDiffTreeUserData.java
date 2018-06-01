package com.apkcompare.data;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.util.Log;

import sun.misc.IOUtils;

public class FilePassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	File file;
	String filepath;
	String apkfilePath;
	//static String apkfilePath2;
	
	public FilePassKeyDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public FilePassKeyDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}

//	public static void setApkfilepath(String mapkfilePath1, String mapkfilePath2) {
//		apkfilePath1 = mapkfilePath1;
//		apkfilePath2 = mapkfilePath2;
//	}
	
	public void setApkfilepath(String mapkfilePath1) {
		apkfilePath = mapkfilePath1;		
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
		FilePassKeyDiffTreeUserData temp = (FilePassKeyDiffTreeUserData)data;
		
		//Log.d(apkfilePath + ":" + temp.apkfilePath);
		
		return issameFile(temp.title, temp.apkfilePath);
	}
	
	protected boolean issameFile(String path, String apkfilePath2) {
		ZipFile zipFile, zipFile2;
		try {
			zipFile = new ZipFile(apkfilePath);
			ZipEntry entry = zipFile.getEntry(title);
			
			zipFile2 = new ZipFile(apkfilePath2);
			ZipEntry entry2 = zipFile2.getEntry(path);
			
			
			if(entry == null || entry.isDirectory() || entry2 == null || entry2.isDirectory() ) {
				Log.w("entry was no file " + path);
				return true;
			}
			
			//is = zipFile.getInputStream(entry);
			
			
			//return entry.getSize() == entry2.getSize();
			
			return isEqual(zipFile.getInputStream(entry), zipFile2.getInputStream(entry2));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private boolean isEqual(InputStream i1, InputStream i2)
	        throws IOException {

	    ReadableByteChannel ch1 = Channels.newChannel(i1);
	    ReadableByteChannel ch2 = Channels.newChannel(i2);

	    ByteBuffer buf1 = ByteBuffer.allocateDirect(1024);
	    ByteBuffer buf2 = ByteBuffer.allocateDirect(1024);

	    try {
	        while (true) {

	            int n1 = ch1.read(buf1);
	            int n2 = ch2.read(buf2);

	            if (n1 == -1 || n2 == -1) return n1 == n2;

	            buf1.flip();
	            buf2.flip();

	            for (int i = 0; i < Math.min(n1, n2); i++)
	                if (buf1.get() != buf2.get())
	                    return false;

	            buf1.compact();
	            buf2.compact();
	        }

	    } finally {
	        if (i1 != null) i1.close();
	        if (i2 != null) i2.close();
	    }
	}
		
	@Override
	public File makeFilebyNode(ApkInfo apkinfo) {
		return makeFileForFile(title, apkinfo);
	}
}
