package com.apkcompare.gui;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import com.apkcompare.ApkComparer;
import com.apkcompare.gui.action.AbstractApkScannerAction;
import com.apkcompare.gui.action.AbstractUIAction;
import com.apkcompare.gui.action.ActionEventHandler;
import com.apkcompare.gui.action.ShowAboutAction;
import com.apkcompare.gui.action.ShowLogsAction;
import com.apkspectrum.plugin.IPlugIn;
import com.apkspectrum.plugin.PlugInManager;
import com.apkspectrum.swing.KeyStrokeAction;
import com.apkspectrum.util.ClassFinder;
import com.apkspectrum.util.Log;

public class UiEventHandler extends ActionEventHandler implements WindowListener
{
	public static final String APK_COMPARER_KEY	= AbstractApkScannerAction.APK_COMPARER_KEY;
	public static final String OWNER_WINDOW_KEY	= AbstractApkScannerAction.OWNER_WINDOW_KEY;

	public static final String ACT_CMD_SHOW_ABOUT				= ShowAboutAction.ACTION_COMMAND;
	public static final String ACT_CMD_SHOW_LOGS				= ShowLogsAction.ACTION_COMMAND;

	public UiEventHandler(ApkComparer apkComparer) {
		setApkComparer(apkComparer);
		loadAllActions();
	}

	private void loadAllActions() {
		try {
			for(Class<?> cls : ClassFinder.getClasses(AbstractUIAction.class.getPackage().getName())) {
				if(cls.isMemberClass() || cls.isInterface()
					|| !AbstractUIAction.class.isAssignableFrom(cls)) continue;
				AbstractUIAction action = null;
				try {
					action = (AbstractUIAction) cls.getDeclaredConstructor(ActionEventHandler.class).newInstance(this);
				} catch (Exception e) { }
				if(action == null) {
					try {
						action = (AbstractUIAction) cls.getDeclaredConstructor().newInstance();
					} catch (Exception e1) { }
				}
				if(action != null) {
					addAction(action);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
	}

	public void registerKeyStrokeAction(JComponent c) {
		// Shortcut key event processing
		KeyStrokeAction.registerKeyStrokeActions(c, JComponent.WHEN_IN_FOCUSED_WINDOW,
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
		putData(APK_COMPARER_KEY, apkComparer);
	}

	public ApkComparer getApkComparer() {
		return (ApkComparer) getData(APK_COMPARER_KEY);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String actCmd = e.getActionCommand();
		if(actCmd != null) {
			Action act = actionMap.get(actCmd);
			if(act != null) {
				act.actionPerformed(e);
				return;
			}

			IPlugIn plugin = PlugInManager.getPlugInByActionCommand(actCmd);
			if(plugin != null) {
				plugin.launch();
				return;
			}
		}
		Log.e("Unknown action command : " + actCmd);
	}

	private void finished(AWTEvent e) {
		Log.v("finished()");

		Object source = e.getSource();
		if(source instanceof Component) {
			Window window = SwingUtilities.getWindowAncestor((Component) source);
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
