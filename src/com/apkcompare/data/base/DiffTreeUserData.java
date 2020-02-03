package com.apkcompare.data.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.tree.TreePath;

import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.tool.aapt.AaptNativeWrapper;
import com.apkspectrum.util.FileUtil;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.ZipFileUtil;

public class DiffTreeUserData implements MappingImp{
    
    public TreePath me;
    public TreePath other = null;
    public boolean isfolder;
    public boolean isdisplaysplit;
    public String Key;
    
    
    public int state = NODE_STATE_NOMAL;
    public static final int NODE_STATE_NOMAL = 0;
    public static final int NODE_STATE_ADD = 2;
    public static final int NODE_STATE_DIFF = 4;        
    
    protected ApkInfo apkinfo;
    public String title;
    
    public DiffTreeUserData(String title) {
        this.title = title;
        this.Key = "";
        this.isfolder = false;
        apkinfo = null;
    }
    public DiffTreeUserData(String title, String key) {
    	this(title, key, null);
    }

    public DiffTreeUserData(String title, ApkInfo apkinfo) {
    	this(title, "", null);
    }
    
    public DiffTreeUserData(String title, String key, ApkInfo apkinfo) {
    	this.title = title;
        this.apkinfo = apkinfo;
        this.Key = key;
        this.isfolder = false;
    }
    
    public DiffTreeUserData(String title, boolean isfolder) {
    	this(title, "", null);
    	this.isfolder = isfolder;
    }
        
    public void setmeTreepath(TreePath path) {
    	this.me = path;
    }
    public void setotherTreepath(TreePath path) {
    	this.other = path;
    }

    @Override
    public String toString() {
    	return title;
    }
    
    public boolean equals(DiffTreeUserData temp) {
    	return this.title.equals(temp.toString());
    }
    
    public void setState(int state) {
    	this.state = state;
    }
    
    public int getState() {
    	return this.state;
    }

    protected File makeFileForFile(String filePath) {
    	Log.d("open file : " + filePath);
    	if(apkinfo == null) {
    		Log.d("apkinfo is null");
    		return null;
    	}
    	
		String resPath = apkinfo.tempWorkPath + File.separator + filePath.replace("/", File.separator);
		File resFile = new File(resPath);
		
		
		
		if (!resFile.getParentFile().exists()) {
			if (FileUtil.makeFolder(resFile.getParentFile().getAbsolutePath())) {
				Log.i("sucess make folder : " + resFile.getParentFile().getAbsolutePath());
			}
			
		}
		
		Log.d("resFile : " + resFile);
		
		try {			
			resFile = File.createTempFile("fileopen", filePath.substring(filePath.lastIndexOf(".")), resFile.getParentFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String[] convStrings = null;
		String extension = filePath.replaceAll(".*/", "").replaceAll(".*\\.", ".").toLowerCase();
		if (extension.endsWith(".xml")) {
			if (filePath.startsWith("res/") || filePath.equals("AndroidManifest.xml")) {
				convStrings = AaptNativeWrapper.Dump.getXmltree(apkinfo.filePath, new String[] { filePath });
				
				//AxmlToXml a2x = new AxmlToXml(convStrings, apkinfo.a2xConvert);
				//a2x.setMultiLinePrint(true);
				convStrings = apkinfo.a2xConvert.convertToText(convStrings).split(System.lineSeparator());
			} else {
				ZipFileUtil.unZip(apkinfo.filePath, filePath, resFile.getAbsolutePath());
			}
		} else {
			ZipFileUtil.unZip(apkinfo.filePath, filePath, resFile.getAbsolutePath());
		} 
//		else if ("resources.arsc".equals(filePath)) {
//			convStrings = resourcesWithValue;
//			resPath += ".txt";
//		} 
		
		if (convStrings != null) {
			StringBuilder sb = new StringBuilder();
			for (String s : convStrings)
				sb.append(s + "\n");
			try {
				FileWriter fw = new FileWriter(resFile);
				fw.write(sb.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		//SystemUtil.openFile(resPath);
		return resFile;
    }

    protected File makeFileForString(String str) {
    	Log.d("open String");
    	
    	if(apkinfo == null) {
    		Log.d("apkinfo is null");
    		return null;
    	}
    	
		File resFile = new File(apkinfo.tempWorkPath + File.separator + "openstring");

			if (!resFile.getParentFile().exists()) {
				if (FileUtil.makeFolder(resFile.getParentFile().getAbsolutePath())) {
					Log.i("sucess make folder : " + resFile.getParentFile().getAbsolutePath());
				}
		}
		
		try {
						
			resFile = File.createTempFile("openstring", ".txt", resFile.getParentFile());
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		String[] convStrings = str.split(System.lineSeparator());
		if (convStrings != null) {
			StringBuilder sb = new StringBuilder();
			for (String s : convStrings)
				sb.append(s + "\n");
			try {
				FileWriter fw = new FileWriter(resFile);
				fw.write(sb.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		//SystemUtil.openFile(resFile.getAbsolutePath());
		return resFile;
    }
    
	@Override
	public boolean compare(DiffTreeUserData data) {
		return this.title.equals(data.toString());
	}
		
	@Override
	public File makeFilebyNode() {
		return makeFileForString(title);
	}	
}
