package com.apkcompare.data;

import javax.swing.tree.TreePath;

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

	@Override
	public boolean compare(DiffTreeUserData data) {
		// TODO Auto-generated method stub
		return this.title.equals(data.toString());
	}
}
