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
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.tool.aapt.AaptNativeWrapper;
import com.apkspectrum.util.Log;

public class FilePassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	File file;
	//String apkfilePath;
	//static String apkfilePath2;
	
	public FilePassKeyDiffTreeUserData(String title) {
		super(title, "", null);
	}
	public FilePassKeyDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	
	public FilePassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}

//	public static void setApkfilepath(String mapkfilePath1, String mapkfilePath2) {
//		apkfilePath1 = mapkfilePath1;
//		apkfilePath2 = mapkfilePath2;
//	}
	    
	@Override
	public boolean compare(DiffTreeUserData data) {
		//return this.title.equals(data.toString());
		FilePassKeyDiffTreeUserData temp = (FilePassKeyDiffTreeUserData)data;
		
		//Log.d(temp.title + ":" + temp.apkinfo.filePath);
		
		return issameFile(temp);
	}
	
	protected boolean issameFile(FilePassKeyDiffTreeUserData temp) {
		try (ZipFile zipFile = new ZipFile(apkinfo.filePath);
			ZipFile zipFile2 = new ZipFile(temp.apkinfo.filePath);
				)
		{
			ZipEntry entry = zipFile.getEntry(title);
			ZipEntry entry2 = zipFile2.getEntry(temp.title);
			
			if(entry == null || entry.isDirectory() || entry2 == null || entry2.isDirectory() ) {
				Log.w("entry was no file " + temp.title);
				return true;
			}			
			//is = zipFile.getInputStream(entry);			
			//return entry.getSize() == entry2.getSize();			
			if(isEqual(zipFile.getInputStream(entry), zipFile2.getInputStream(entry2))) {
				return true;
			} else {
				if(issamestringForRes(temp)) {
					return true;
				} else {
					return false;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private String getXmlByApkinfo(ApkInfo tempapkinfo, String Res) {
		String[] convStrings = null;
		convStrings = AaptNativeWrapper.Dump.getXmltree(tempapkinfo.filePath, new String[] { Res });				
		//AxmlToXml a2x = new AxmlToXml(convStrings, tempapkinfo.resourceScanner);
		//AxmlToXml a2x = new AxmlToXml(convStrings, apkinfo.a2xConvert);
		//a2x.setMultiLinePrint(true);
		convStrings = tempapkinfo.a2xConvert.convertToText(convStrings).split(System.lineSeparator());
		
//		a2x.setMultiLinePrint(true);
//		convStrings = a2x.toString().split(System.lineSeparator());
//		
		StringBuilder sb = new StringBuilder();
		for (String s : convStrings)
			sb.append(s + "\n");
		
		return sb.toString();
	}
	
	private boolean issamestringForRes(FilePassKeyDiffTreeUserData temp) {
		//String[] convStrings = null;
		String extension = temp.title.replaceAll(".*/", "").replaceAll(".*\\.", ".").toLowerCase();
		
		if (extension.endsWith(".xml")) {
			if (temp.title.startsWith("res/") || temp.title.equals("AndroidManifest.xml")) {
				//Log.d("title : " + title);				
				if(getXmlByApkinfo(apkinfo, title).equals(getXmlByApkinfo(temp.apkinfo, temp.title))) {					
					return true;
				} else {
					//Log.d(getXmlByApkinfo(apkinfo, title));
					return false;
				}
			}
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
	public File makeFilebyNode() {		
		if(title.equals("resources.arsc")) {			
			return makeFileForString(join(this.apkinfo.resourcesWithValue, System.lineSeparator()));
		}		
		return makeFileForFile(title);
	}
	
	private String join(String[] list, String conjunction)
	{
	   StringBuilder sb = new StringBuilder();
	   boolean first = true;
	   for (String item : list)
	   {
	      if (first)
	         first = false;
	      else
	         sb.append(conjunction);
	      sb.append(item);
	   }
	   return sb.toString();
	}
}
