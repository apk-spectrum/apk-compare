package com.apkcompare.gui.action;

import java.awt.event.ActionEvent;

import com.apkcompare.gui.DiffTreePair;
import com.apkcompare.gui.FilteredTreeModel;
import com.apkcompare.resource.RConst;
import com.apkspectrum.swing.AbstractUIAction;
import com.apkspectrum.swing.ActionEventHandler;

@SuppressWarnings("serial")
public class FilterAction extends AbstractUIAction implements RConst
{
	public static final String ACTION_COMMAND		= "ACT_CMD_FILTER_RESET";
	public static final String ACTION_COMMAND_ADD	= "ACT_CMD_FILTER_ADD";
	public static final String ACTION_COMMAND_DIFF	= "ACT_CMD_FILTER_DIFF";
	public static final String ACTION_COMMAND_IDEN	= "ACT_CMD_FILTER_IDEN";

	private int flag = 0;

	public FilterAction(ActionEventHandler h) {
		super(h);
		setRequiredConditions(FLAG_SET_LEFT_TREE | FLAG_SET_RIGHT_TREE);
		if(h != null) {
			h.addAction(new FilterAction(h, FilteredTreeModel.FLAG_ADD));
			h.addAction(new FilterAction(h, FilteredTreeModel.FLAG_DIFF));
			h.addAction(new FilterAction(h, FilteredTreeModel.FLAG_IDEN));
		}
	}

	protected FilterAction(ActionEventHandler h, int flag) {
		setHandler(h);
		this.flag = flag;
		switch(flag) {
		case FilteredTreeModel.FLAG_ADD:
			setActionCommand(ACTION_COMMAND_ADD);
			break;
		case FilteredTreeModel.FLAG_DIFF:
			setActionCommand(ACTION_COMMAND_DIFF);
			break;
		case FilteredTreeModel.FLAG_IDEN:
			setActionCommand(ACTION_COMMAND_IDEN);
			break;
		}
		setSelected(true);
		setRequiredConditions(FLAG_SET_LEFT_TREE | FLAG_SET_RIGHT_TREE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object data = getHandlerData(DIFF_TREE_PAIR_KEY);
		if(data instanceof DiffTreePair) {
			if(isSelected()) {
				((DiffTreePair) data).setFilter(flag);
			} else {
				((DiffTreePair) data).unsetFilter(flag);
			}
		}
	}
}