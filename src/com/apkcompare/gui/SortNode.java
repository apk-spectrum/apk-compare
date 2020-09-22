package com.apkcompare.gui;

import java.util.Collections;
import java.util.Comparator;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import com.apkcompare.data.base.DiffTreeUserData;

@SuppressWarnings("serial")
public class SortNode extends DefaultMutableTreeNode {
	public SortNode(Object userObject) {
		super(userObject);
	}

	@Override
	public void add(MutableTreeNode newChild) {
		super.add(newChild);
		DiffTreeUserData temp = (DiffTreeUserData) ((DefaultMutableTreeNode)newChild).getUserObject();
		if(!temp.isfolder) {
		sort();// add to tree and sort immediately use in case the model is
				// small if large comment it and and call node.sort once
				// you've added all the children
		}
	}

	@SuppressWarnings("unchecked")
	public void sort() {
		Collections.sort(children, compare());
	}

	@SuppressWarnings("rawtypes")
	private Comparator compare() {
		return new Comparator<DefaultMutableTreeNode>() {
			@Override
			public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
				return o1.getUserObject().toString().compareTo(o2.getUserObject().toString());
			}
		};
	}
}
