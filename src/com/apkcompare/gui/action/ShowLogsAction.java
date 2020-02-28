package com.apkcompare.gui.action;

import java.awt.Window;
import java.awt.event.ActionEvent;

import com.apkcompare.gui.dialog.LogDlg;

@SuppressWarnings("serial")
public class ShowLogsAction extends AbstractUIAction
{
	public static final String ACTION_COMMAND = "ACT_CMD_SHOW_LOGS";

	public ShowLogsAction(ActionEventHandler h) { super(h); }

	@Override
	public void actionPerformed(ActionEvent e) {
		evtShowLogs(getWindow(e));
	}

	private void evtShowLogs(Window owner) {
		LogDlg.showLogDialog(owner);
	}
}