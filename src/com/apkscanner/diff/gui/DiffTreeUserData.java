package com.apkscanner.diff.gui;

import javax.swing.tree.TreePath;

public class DiffTreeUserData {
    private String title;
    TreePath me;
    TreePath other = null;
    int state = NODE_STATE_NOMAL;
    
    public static final int NODE_STATE_NOMAL = 0;
    public static final int NODE_STATE_ADD = 2;
    public static final int NODE_STATE_DIFF = 4;
    
    public DiffTreeUserData(String title) {
        this.title = title;
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
    
    public void setState(int state) {
    	this.state = state;
    }
    
    public int getState() {
    	return this.state;
    }
}
