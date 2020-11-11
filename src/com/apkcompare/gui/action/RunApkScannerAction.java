package com.apkcompare.gui.action;

import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JComponent;

import com.apkcompare.gui.UiEventHandler;
import com.apkcompare.resource.RAct;
import com.apkcompare.resource.RConst;
import com.apkcompare.resource.RProp;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.ActionEventHandler;
import com.apkspectrum.util.ConsolCmd;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.SystemUtil;
import com.google.common.base.Objects;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

@SuppressWarnings("serial")
public class RunApkScannerAction extends AbstractApkScannerAction
	implements RConst
{
	public static final String ACTION_COMMAND = "ACT_CMD_RUN_APKSCANNER";
	public static final String ACTION_COMMAND_LEFT = "ACT_CMD_RUN_APKSCANNER_0";
	public static final String ACTION_COMMAND_RIGHT = "ACT_CMD_RUN_APKSCANNER_1";

	public static String getActionCommand(int postition) {
		switch(postition) {
		case  LEFT: return ACTION_COMMAND_LEFT;
		case RIGHT: return ACTION_COMMAND_RIGHT;
		}
		return ACTION_COMMAND;
	}

	public RunApkScannerAction(ActionEventHandler h) {
		super(h);
		if(h != null) {
			h.addAction(new RunApkScannerAction(h, LEFT));
			h.addAction(new RunApkScannerAction(h, RIGHT));
		}
	}

	protected RunApkScannerAction(ActionEventHandler h, int position) {
		super(h);
		switch(position) {
		case LEFT:
			putValue(ACTION_COMMAND_KEY, ACTION_COMMAND_LEFT);
			setRequiredConditions(UiEventHandler.FLAG_SET_LEFT_TREE);
			break;
		case RIGHT:
			putValue(ACTION_COMMAND_KEY, ACTION_COMMAND_RIGHT);
			setRequiredConditions(UiEventHandler.FLAG_SET_RIGHT_TREE);
			break;
		}
		RAct.ACT_CMD_RUN_APKSCANNER.set(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		int position = UNASSIGNED;

		switch(String.valueOf(e.getActionCommand())) {
		case ACTION_COMMAND_LEFT:
			position = LEFT;
			break;
		case ACTION_COMMAND_RIGHT:
			position = RIGHT;
			break;
		default:
			Object source = e.getSource();
			if(source != null && source instanceof JComponent) {
				JComponent comp = (JComponent) source;
				Integer posProp = (Integer)comp.getClientProperty(POSITION_KEY);
				if(posProp != null) position = posProp.intValue();
			}
			break;
		}
		if(position == UNASSIGNED) {
			Log.e("Unknown position");
			return;
		}
		Window owner = getWindow(e);
		evtRunApkScanner(owner, position);
	}

	protected void evtRunApkScanner(Window owner, final int position) {
		final String scannerPath = getApkScannerPath();
		if(scannerPath == null) {
			Log.v("No such ApkScanner");
			showPopup();
			return;
		}
		savePath();

		ApkInfo info = getApkInfo(position);
		if(info == null || info.filePath == null) return;

		final String apkPath = info.filePath;
		Thread thread = new Thread(new Runnable() {
			public void run() {
				ConsolCmd.exec(new String[] {scannerPath, apkPath}, true);
			}
		});
		thread.setPriority(Thread.NORM_PRIORITY);
		thread.start();
	}

	private static String scannerPath;
	protected String getApkScannerPath() {
		if(scannerPath != null && new File(scannerPath).canExecute()) {
			return scannerPath;
		}

		scannerPath = RProp.S.APK_SCANNER_PATH.get();
		if(scannerPath != null && new File(scannerPath).canExecute()) {
			return scannerPath;
		}

		if(SystemUtil.isWindows()) {
			String[][] HKLM_keys = {
				{"SOFTWARE\\Wow6432Node\\APK Scanner", ""},
				{"SOFTWARE\\APK Scanner", ""},
				{"SOFTWARE\\Wow6432Node\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\APK Scanner", "UninstallString"},
				{"SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Uninstall\\APK Scanner", "UninstallString"},
				//{"SOFTWARE\\Classes\\CLSID\\{FCF5634A-6021-4E70-A895-4D21422BCF93}\\InprocServer32", ""}
			};
			for(String[] key: HKLM_keys) {
				if(Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, key[0])
						&& Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, key[0], key[1])) {
					scannerPath = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, key[0], key[1]);
					if(scannerPath == null || scannerPath.isEmpty()) continue;
					if(scannerPath.endsWith(".exe") || scannerPath.endsWith(".dll") || scannerPath.endsWith("\\")) {
						scannerPath = scannerPath.substring(0, scannerPath.lastIndexOf("\\"));
					}
					scannerPath += "\\ApkScanner.exe";
					if(new File(scannerPath).canExecute()) {
						return scannerPath;
					}
				}
			}

			for(String path: new String[] {
					"C:\\Program Files\\APKScanner\\ApkScanner.exe",
					"C:\\Program Files (x86)\\APKScanner\\ApkScanner.exe"}) {
				if(new File(path).canExecute()) {
					return scannerPath = path;
				}
			}
		} else if(SystemUtil.isLinux()) {
			scannerPath = "/opt/APKScanner/APKScanner.sh";
			if(new File(scannerPath).canExecute()) {
				return scannerPath;
			}
		} else if(SystemUtil.isMac()) {
			
		}
		return scannerPath = null;
	}

	protected void savePath() {
		String path = RProp.S.APK_SCANNER_PATH.get();
		if(!Objects.equal(path, scannerPath)) {
			RProp.S.APK_SCANNER_PATH.set(scannerPath);
		}
	}

	protected void showPopup() {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				// TODO Make ApkScannerSelecter
				//new ApkScannerSelecter().setVisible(true);
			}
		});
	}
}
