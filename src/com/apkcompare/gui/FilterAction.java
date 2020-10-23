package com.apkcompare.gui;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.tree.TreePath;

import com.apkcompare.resource.RImg;
import com.apkspectrum.swing.ActionEventHandler;
import com.apkspectrum.swing.UIAction;

@SuppressWarnings("serial")
public class FilterAction extends AbstractAction
{
	private final DiffTreePair treePair;

	// @see FilteredTreeModel.FLAG_*
	private final int flag;

	public FilterAction(DiffTreePair treePair, int flag) {
		this.treePair = treePair;
		this.flag = flag;

		Object icon = null;
		switch(flag) {
		case FilteredTreeModel.FLAG_ADD:
			icon = RImg.DIFF_TOOLBAR_ADD.getImageIcon();
			break;
		case FilteredTreeModel.FLAG_DIFF:
			icon = RImg.DIFF_TOOLBAR_EDITOR.getImageIcon();
			break;
		case FilteredTreeModel.FLAG_IDEN:
			icon = RImg.DIFF_TOOLBAR_IDEN.getImageIcon();
			break;
		}
		if(icon != null) {
			putValue(Action.LARGE_ICON_KEY, icon);
		}

		putValue(Action.ACTION_COMMAND_KEY, getActionCommand(flag));
		putValue(UIAction.ACTION_REQUIRED_CONDITIONS,
			Integer.valueOf(UiEventHandler.FLAG_SET_LEFT_TREE
					| UiEventHandler.FLAG_SET_RIGHT_TREE));
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (treePair.hasDataInBoth()) {
			List<TreePath> expandedpath = treePair.getPaths(DiffTree.LEFT);
			treePair.setFilter(flag);
			for (int i = 0; i < expandedpath.size(); i++) {
				treePair.expandPath(DiffTree.LEFT, expandedpath.get(i));
			}
		}
	}

	public static String getActionCommand(int flag) {
		return FilterAction.class.getName() + "_" + flag;
	}

	public static void addToEventHandler(ActionEventHandler h, DiffTreePair t) {
		h.addAction(new FilterAction(t, FilteredTreeModel.FLAG_ADD));
		h.addAction(new FilterAction(t, FilteredTreeModel.FLAG_DIFF));
		h.addAction(new FilterAction(t, FilteredTreeModel.FLAG_IDEN));
	}
}