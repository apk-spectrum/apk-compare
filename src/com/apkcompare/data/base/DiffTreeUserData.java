package com.apkcompare.data.base;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.tree.TreePath;

import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.gui.tabpanels.Resources.ResourceObject;
import com.apkscanner.tool.aapt.AaptNativeWrapper;
import com.apkscanner.tool.aapt.AxmlToXml;
import com.apkscanner.util.FileUtil;
import com.apkscanner.util.Log;
import com.apkscanner.util.SystemUtil;
import com.apkscanner.util.ZipFileUtil;

public class DiffTreeUserData implements MappingImp{
    public String title;
    public TreePath me;
    public TreePath other = null;
    public boolean isfolder;
    public boolean isdisplaysplit;
    public String Key;
    
    public int state = NODE_STATE_NOMAL;
    public static final int NODE_STATE_NOMAL = 0;
    public static final int NODE_STATE_ADD = 2;
    public static final int NODE_STATE_DIFF = 4;
        
    
    public DiffTreeUserData(String title) {
        this.title = title;
        this.Key = "";
    }
    
    public DiffTreeUserData(String title, String key) {
        this.title = title;
        this.Key = key;
    }
    
    public DiffTreeUserData(String title, boolean isfolder) {
    	this(title);
    	this.isfolder = isfolder;    	
    }
    
    public DiffTreeUserData(String title, String key, boolean isfolder) {
    	this(title, key);    	
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

    protected void openFile(String filePath, ApkInfo apkinfo) {
    	Log.d("open file : " + filePath);
		String resPath = apkinfo.tempWorkPath + File.separator + filePath.replace("/", File.separator);
		File resFile = new File(resPath);
		if (!resFile.exists()) {
			if (!resFile.getParentFile().exists()) {
				if (FileUtil.makeFolder(resFile.getParentFile().getAbsolutePath())) {
					Log.i("sucess make folder : " + resFile.getParentFile().getAbsolutePath());
				}
			}
		}

		String[] convStrings = null;
		String extension = filePath.replaceAll(".*/", "").replaceAll(".*\\.", ".").toLowerCase();
		if (extension.endsWith(".xml")) {
			if (filePath.startsWith("res/") || filePath.equals("AndroidManifest.xml")) {				
				convStrings = AaptNativeWrapper.Dump.getXmltree(apkinfo.filePath, new String[] { filePath });
				
				AxmlToXml a2x = new AxmlToXml(convStrings, apkinfo.resourceScanner);
				a2x.setMultiLinePrint(true);
				convStrings = a2x.toString().split(System.lineSeparator());
			}
		} 
//		else if ("resources.arsc".equals(filePath)) {
//			convStrings = resourcesWithValue;
//			resPath += ".txt";
//		} 
		else {
			ZipFileUtil.unZip(apkinfo.filePath, filePath, resPath);
		}
		
		if (convStrings != null) {
			StringBuilder sb = new StringBuilder();
			for (String s : convStrings)
				sb.append(s + "\n");
			try {
				FileWriter fw = new FileWriter(new File(resPath));
				fw.write(sb.toString());
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		SystemUtil.openFile(resPath);
    }

    protected void openString(String str, ApkInfo apkinfo) {
    	Log.d("open String : ");
    	
		File resFile = new File(apkinfo.tempWorkPath + File.separator + "openstring");
		if (!resFile.exists()) {
			Log.d("not exist");
			//resFile.mkdirs();
			if (!resFile.getParentFile().exists()) {
				if (FileUtil.makeFolder(resFile.getAbsolutePath())) {
					Log.i("sucess make folder : " + resFile.getParentFile().getAbsolutePath());
				}
			}
		}
		
		try {
						
			resFile = File.createTempFile("openstring", ".txt", resFile);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
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
		SystemUtil.openFile(resFile.getAbsolutePath());    	
    }
    
	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		return this.title.equals(data.toString());
	}	
	
	@Override
	public void openFileNode(ApkInfo apkinfo) {
		// TODO Auto-generated method stub
		openString(title, apkinfo);		
	}
}
