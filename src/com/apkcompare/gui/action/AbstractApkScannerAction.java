package com.apkcompare.gui.action;

import java.awt.Window;

import com.apkcompare.ApkComparer;
import com.apkcompare.resource.RConst;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.AbstractUIAction;
import com.apkspectrum.swing.ActionEventHandler;
import com.apkspectrum.swing.ApkActionEventHandler;
import com.apkspectrum.util.Log;

@SuppressWarnings("serial")
public abstract class AbstractApkScannerAction extends AbstractUIAction
{
	public AbstractApkScannerAction() { }

	public AbstractApkScannerAction(ApkActionEventHandler h) { super(h); }

	@Override
	public ApkActionEventHandler getHandler() {
		return (ApkActionEventHandler) super.getHandler();
	}

	@Override
	public void setHandler(ActionEventHandler h) {
		if(!(h instanceof ApkActionEventHandler)) {
			Log.w("The event handler must be type ApkActionEventHandler.");
			return;
		}
		super.setHandler(h);
	}

	protected ApkComparer getApkComparer() {
		if(handler == null) return null;
		return (ApkComparer) handler.getData(RConst.APK_COMPARER_KEY);
	}

	protected ApkInfo getApkInfo(int position) {
		if(!(handler instanceof ApkActionEventHandler)) return null;
		return getHandler().getApkInfo(position);
	}

	protected Window getWindow() {
		if(handler == null) return null;
		return (Window) handler.getData(RConst.OWNER_WINDOW_KEY);
	}
}
