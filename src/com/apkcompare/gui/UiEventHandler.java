package com.apkcompare.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.apkcompare.ApkComparer;
import com.apkcompare.gui.action.AbstractApkScannerAction;
import com.apkcompare.gui.action.OpenApkAction;
import com.apkcompare.gui.action.OpenDiffTreeFileAction;
import com.apkcompare.gui.action.ShowAboutAction;
import com.apkcompare.gui.action.ShowLogsAction;
import com.apkcompare.gui.action.ShowSettingDlgAction;
import com.apkcompare.resource.RAct;
import com.apkcompare.resource.RConst;
import com.apkspectrum.core.scanner.ApkScanner;
import com.apkspectrum.swing.ApkActionEventHandler;
import com.apkspectrum.swing.FileDrop;
import com.apkspectrum.swing.KeyStrokeAction;
import com.apkspectrum.swing.UIActionEvent;
import com.apkspectrum.util.Log;

public class UiEventHandler	extends ApkActionEventHandler
	implements WindowListener, FileDrop.Listener, RConst
{
	public static final String ACT_CMD_OPEN_APK
								= OpenApkAction.ACTION_COMMAND;
	public static final String ACT_CMD_OEPN_DIFFTREE_FILE
								= OpenDiffTreeFileAction.ACTION_COMMAND;
	public static final String ACT_CMD_SHOW_ABOUT
								= ShowAboutAction.ACTION_COMMAND;
	public static final String ACT_CMD_SHOW_LOGS
								= ShowLogsAction.ACTION_COMMAND;
	public static final String ACT_CMD_SHOW_SETTINGS
								= ShowSettingDlgAction.ACTION_COMMAND;

	public static final int FLAG_SET_LEFT_TREE		= 0x01;
	public static final int FLAG_SET_RIGHT_TREE		= 0x02;

	public UiEventHandler(ApkComparer apkComparer) {
		super(AbstractApkScannerAction.class.getPackage(), RAct.class);
		setApkComparer(apkComparer);
	}

	public void registerKeyStrokeAction(JComponent c) {
		// Shortcut key event processing
		KeyStrokeAction.registerKeyStrokeActions(c,
			JComponent.WHEN_IN_FOCUSED_WINDOW,
			new KeyStroke[] {
				KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0),
				KeyStroke.getKeyStroke(KeyEvent.VK_F12, 0)
			},
			new String[] {
				ACT_CMD_SHOW_ABOUT,
				ACT_CMD_SHOW_LOGS
			}, this);
	}

	public void setOwner(Window owner) {
		putData(OWNER_WINDOW_KEY, owner);
	}

	public Window getOwner() {
		return (Window) getData(OWNER_WINDOW_KEY);
	}

	public void setApkComparer(ApkComparer apkComparer) {
		ApkScanner[] scanners = new ApkScanner[2];
		scanners[LEFT] = apkComparer.getApkScanner(LEFT);
		scanners[RIGHT] = apkComparer.getApkScanner(RIGHT);
		putData(MULTI_APK_SCANNER_KEY, scanners);
		putData(APK_COMPARER_KEY, apkComparer);
	}

	public ApkComparer getApkComparer() {
		return (ApkComparer) getData(APK_COMPARER_KEY);
	}

	@Override
	public void filesDropped(File[] files) {
		AWTEvent evt = EventQueue.getCurrentEvent();
		if(evt == null || evt.getSource() == null) return;

		JComponent c = null;
		if(FILE_DROP_TOP_KEY.equals(((Component)evt.getSource()).getName())) {
			c = (JComponent) evt.getSource();
		} else {
			c = (JComponent) SwingUtilities.getAncestorNamed(FILE_DROP_TOP_KEY,
					(Component) evt.getSource());
		}
		if(c == null) return;

		actionPerformed(new UIActionEvent(c,
				ActionEvent.ACTION_PERFORMED, ACT_CMD_OPEN_APK, files));
	}

	@Override public void dragEnter() { }
	@Override public void dragExit() { }

	private void finished(AWTEvent e) {
		Log.v("finished()");

		Object source = e.getSource();
		if(source instanceof Component) {
			Window window = SwingUtilities.getWindowAncestor((Component)source);
			if(window != null) {
				window.setVisible(false);
			}
		}

		ApkComparer comparer = getApkComparer();
		if(comparer != null) {
			comparer.clear(true);
		}

		System.exit(0);
	}

	// Closing event of window be delete tempFile
	@Override public void windowClosing(WindowEvent e) { finished(e); }
	@Override public void windowClosed(WindowEvent e) { finished(e); }
	@Override public void windowOpened(WindowEvent e) { }
	@Override public void windowIconified(WindowEvent e) { }
	@Override public void windowDeiconified(WindowEvent e) { }
	@Override public void windowActivated(WindowEvent e) { }
	@Override public void windowDeactivated(WindowEvent e) { }
}
