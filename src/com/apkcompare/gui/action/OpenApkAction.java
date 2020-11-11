package com.apkcompare.gui.action;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

import com.apkcompare.ApkComparer;
import com.apkcompare.resource.RConst;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.ActionEventHandler;
import com.apkspectrum.swing.ApkFileChooser;
import com.apkspectrum.swing.MessageBoxPane;
import com.apkspectrum.swing.UIActionEvent;
import com.apkspectrum.util.Log;

@SuppressWarnings("serial")
public class OpenApkAction extends AbstractApkScannerAction
	implements RConst
{
	public static final String ACTION_COMMAND = "ACT_CMD_OPEN_APK";

	public OpenApkAction(ActionEventHandler h) { super(h); }

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		int position = -1;
		if(source != null && source instanceof JComponent) {
			JComponent comp = (JComponent) source;
			Integer posProp = (Integer) comp.getClientProperty(POSITION_KEY);
			if(posProp != null) position = posProp.intValue();
		}
		if(position == -1) {
			Log.e("Unknown position");
			return;
		}
		Window owner = getWindow(e);
		evtOpenApkFile(owner, getTargetPath(owner, e), position);
	}

	protected String getTargetPath(Window owner, ActionEvent e) {
		String path = null;
		Object userObj = null;

		if(e instanceof UIActionEvent) {
			userObj = ((UIActionEvent) e).getUserObject();
		}

		if(userObj instanceof File) {
			path = ((File)userObj).getAbsolutePath();
		} else if(userObj instanceof File[]) {
			if(((File[])userObj).length > 0) {
				path = ((File[])userObj)[0].getAbsolutePath();
			}
		} else if(userObj instanceof String) {
			path = (String) userObj;
		} else if(e.getSource() instanceof JTextComponent) {
			String text = ((JTextComponent) e.getSource()).getText();
			if(text == null || text.trim().isEmpty()) return null;
			text = text.trim();
			File selectedFile = new File(text);
			if(!selectedFile.exists()) {
				MessageBoxPane.showError(owner, "No such file : " + text);
				return null;
			}
			if(!text.toLowerCase().endsWith(".apk") || !selectedFile.isFile()) {
				MessageBoxPane.showError(owner, "No APK file : " + text);
				return null;
			}
			if(!selectedFile.canRead()) {
				MessageBoxPane.showError(owner, "Can not read file : " + text);
				return null;
			}
			path = selectedFile.getAbsolutePath();
			((JTextComponent) e.getSource()).transferFocus();
		} else {
			path = ApkFileChooser.openApkFilePath(owner);
		}
		return path;
	}

	protected void evtOpenApkFile(Window owner, final String apkFilePath,
			final int position) {
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
