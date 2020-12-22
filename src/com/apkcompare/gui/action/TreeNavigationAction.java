package com.apkcompare.gui.action;

import java.awt.event.ActionEvent;

import com.apkcompare.gui.DiffTreePair;
import com.apkcompare.resource.RConst;
import com.apkspectrum.swing.AbstractUIAction;
import com.apkspectrum.swing.ActionEventHandler;

@SuppressWarnings("serial")
public class TreeNavigationAction extends AbstractUIAction implements RConst
{
	// Prevents adding to ActionEventHandler by set an empty.
	public static final String ACTION_COMMAND		= "";

	public static final String ACTION_COMMAND_NEXT	= "ACT_CMD_TREE_NEVI_NEXT";
	public static final String ACTION_COMMAND_PREV	= "ACT_CMD_TREE_NEVI_PREV";

	private int direction = -1;

	public TreeNavigationAction(ActionEventHandler h) {
		if(h == null) return;
		h.addAction(new TreeNavigationAction(h, ACTION_COMMAND_NEXT));
		h.addAction(new TreeNavigationAction(h, ACTION_COMMAND_PREV));
	}

	public TreeNavigationAction(ActionEventHandler h, String actCmd) {
		setHandler(h);

		int reqCond = FLAG_SET_BOTH_TREE;
		switch(actCmd) {
		case ACTION_COMMAND_NEXT:
			reqCond |= FLAG_AVAILABLE_MOVE_NEXT;
			direction = DIRECTION_NEXT;
			break;
		case ACTION_COMMAND_PREV:
			reqCond |= FLAG_AVAILABLE_MOVE_PREV;
			direction = DIRECTION_PREV;
			break;
		}
		setRequiredConditions(reqCond);

		setActionCommand(actCmd);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object data = getHandlerData(DIFF_TREE_PAIR_KEY);
		if(!(data instanceof DiffTreePair)) return;
		DiffTreePair diffTree = (DiffTreePair) data;
		diffTree.navigate(direction);
	}
}
