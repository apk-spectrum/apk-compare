package com.apkcompare.gui.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JComponent;

import com.apkcompare.ApkComparer;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.ActionEventHandler;
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

	protected String getTargetPath(Window owner) {
		String path = null;
		Object userObj = getUserObject();
		if(userObj instanceof File) {
			path = ((File)userObj).getAbsolutePath();
		} else if(userObj instanceof File[]) {
			if(((File[])userObj).length > 0) {
				path = ((File[])userObj)[0].getAbsolutePath();
			}
		} else if(userObj instanceof String) {
			path = (String) userObj;
		} else {
			path = ApkFileChooser.openApkFilePath(owner);
		}
		return path;
	}

	protected void evtOpenApkFile(Window owner, final int position) {
		final String apkFilePath = getTargetPath(owner);
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
		if(info != null && apkFilePath.equals(info.filePath)) {
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
