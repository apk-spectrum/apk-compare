package com.apkcompare.gui.action;

import java.awt.Window;

import com.apkcompare.ApkComparer;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.AbstractUIAction;
import com.apkspectrum.swing.ActionEventHandler;

@SuppressWarnings("serial")
public abstract class AbstractApkScannerAction extends AbstractUIAction
{
	public static final String APK_COMPARER_KEY = "APK_COMPARER_KEY";
	public static final String OWNER_WINDOW_KEY = "WINDOW_KEY";

	public AbstractApkScannerAction() { }

	public AbstractApkScannerAction(ActionEventHandler h) { super(h); }

	protected ApkComparer getApkComparer() {
		if(handler == null) return null;
		return (ApkComparer) handler.getData(APK_COMPARER_KEY);
	}

	protected ApkInfo getApkInfo(int position) {
		ApkComparer comparer = getApkComparer();
		return comparer != null ? comparer.getApkInfo(position) : null;
	}

	protected Window getWindow() {
		if(handler == null) return null;
		return (Window) handler.getData(OWNER_WINDOW_KEY);
	}
}
