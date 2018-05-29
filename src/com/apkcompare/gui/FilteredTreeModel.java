package com.apkcompare.gui;

import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkscanner.util.Log;

class FilteredTreeModel extends DefaultTreeModel {
	 
    private static final long serialVersionUID = 5632874215454365848L;
    
    public static int FLAG_IDEN = 0x01;
    public static int FLAG_ADD = 0x02;
    public static int FLAG_DIFF = 0x04;
    
    public static int FLAG_ALL = FLAG_IDEN | FLAG_ADD | FLAG_DIFF;
    
    private int flag;
    
    private String filter = "";

    /**
     * 
     * @param root the root node of our model
     */
    public FilteredTreeModel(DefaultMutableTreeNode root) {
        super(root);
        flag = FLAG_ALL;
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeModel#getChild(java.lang.Object, int)
     */
    private boolean checkstate(int state) {
        switch(state) {
        case DiffTreeUserData.NODE_STATE_NOMAL:
        	return ((this.flag & FLAG_IDEN) != 0x00);        	
        case DiffTreeUserData.NODE_STATE_DIFF:
        	return ((this.flag & FLAG_DIFF) != 0x00);        	
        case DiffTreeUserData.NODE_STATE_ADD:
        	return ((this.flag & FLAG_ADD) != 0x00);        	
        }
        return false;
    }
    
    private boolean checknode(DefaultMutableTreeNode tempnode) {
    	    	
    	if(!tempnode.isLeaf() || ((DiffTreeUserData)tempnode.getUserObject()).isfolder) {
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public Object getChild(Object parent, int index) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
        if (this.flag == (FLAG_ALL)) {
            return node.getChildAt(index);
        } else {
            int pos = 0;
            for (int i = 0, cnt = 0; i < node.getChildCount(); i++) {
                int state = ((DiffTreeUserData)((DefaultMutableTreeNode) node.getChildAt(i)).getUserObject()).state;
                if (checkstate(state) || checknode((DefaultMutableTreeNode) node.getChildAt(i))) {
                    if (cnt++ == index) {
                        pos = i;
                        break;
                    }
                }
            }
            if(pos==0) node.getChildAt(index);
            
            return node.getChildAt(pos);
        }
    }

    /*
     * (non-Javadoc)
     * @see javax.swing.tree.DefaultTreeModel#getChildCount(java.lang.Object)
     */
    public int getChildCount(Object parent) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) parent;
        if (this.flag == (FLAG_ALL) ) {
            return node.getChildCount();
        } else {
            int childCount = 0;
            Enumeration<DefaultMutableTreeNode> children = node.children();
            while (children.hasMoreElements()) {
                DefaultMutableTreeNode treenode = children.nextElement();
                
                int state = ((DiffTreeUserData)treenode.getUserObject()).state;
                
                if (checkstate(state) || checknode(treenode)) {
                    childCount++;
                }
            }
            return childCount;
        }

    }

    /**
     * 
     * @param filter a string to filter nodes in our model with
     */
    public void setFilter(int flag) {
        if (flag == 0) {
        	this.flag = FLAG_ALL;
        } else {
        	this.flag ^= flag;
        }
        
        Object[] path = { root };
        int[] childIndices = new int[root.getChildCount()];
        Object[] children = new Object[root.getChildCount()];
        for (int i = 0; i < root.getChildCount(); i++) {
            childIndices[i] = i;
            children[i] = root.getChildAt(i);
        }
        fireTreeStructureChanged(this, path, childIndices, children);
    }
    
    public void setFilter(String filter) {
        if (filter == null || filter.trim().toLowerCase().equals(this.filter.toLowerCase().trim()))
            return;
        this.filter = filter == null ? "" : filter.trim();
        Object[] path = { root };
        int[] childIndices = new int[root.getChildCount()];
        Object[] children = new Object[root.getChildCount()];
        for (int i = 0; i < root.getChildCount(); i++) {
            childIndices[i] = i;
            children[i] = root.getChildAt(i);
        }
        fireTreeStructureChanged(this, path, childIndices, children);
    }
}