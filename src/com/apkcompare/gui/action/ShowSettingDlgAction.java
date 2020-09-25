package com.apkcompare.gui.action;

import java.awt.event.ActionEvent;

import com.apkcompare.gui.dialog.SettingDlg;
import com.apkspectrum.swing.AbstractUIAction;
import com.apkspectrum.swing.ActionEventHandler;

@SuppressWarnings("serial")
public class ShowSettingDlgAction extends AbstractUIAction
{
	public static final String ACTION_COMMAND = "ACT_CMD_OPEN_SETTINGS";

	public ShowSettingDlgAction(ActionEventHandler h) { super(h); }

	@Override
	public void actionPerformed(ActionEvent e) {
		SettingDlg dlg = new SettingDlg(getWindow(e));
		dlg.setVisible(true);
	}
}
