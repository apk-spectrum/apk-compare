package com.apkcompare.gui.action;

import java.awt.Window;
import java.awt.event.ActionEvent;

import javax.swing.JComponent;

import com.apkcompare.ApkComparer;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.ApkFileChooser;
import com.apkspectrum.swing.MessageBoxPane;
import com.apkspectrum.util.Log;

@SuppressWarnings("serial")
public class OpenApkAction extends AbstractApkScannerAction
{
	public static final String ACTION_COMMAND = "ACT_CMD_OPEN_APK";

	public OpenApkAction(ActionEventHandler h) { super(h); }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int position = -1;
		if(source != null && source instanceof JComponent) {
			Integer posProp = (Integer) ((JComponent) source).getClientProperty("POSITION");
			if(posProp != null) position = posProp.intValue();
		}
		if(position == -1) {
			Log.e("Unknown position");
			return;
		}
		evtOpenApkFile(getWindow(e), position);
	}

	protected void evtOpenApkFile(Window owner, final int position) {
		final String apkFilePath = ApkFileChooser.openApkFilePath(owner);
		if(apkFilePath == null) {
			Log.v("Not choose apk file");
			return;
		}

		final ApkComparer compare = getApkComparer();
		if(compare == null) {
			Log.v("evtOpenApkFile() compare is null");
			return;
		}

		ApkInfo info = getApkInfo(position);
		if(apkFilePath.equals(info.filePath)) {
			MessageBoxPane.showError(owner, "same APK file");
			return;
		}

		Thread thread = new Thread(new Runnable() {
			public void run() {
				compare.getApkScanner(position).clear(false);
				compare.setApk(position, apkFilePath);
			}
		});
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}
}
