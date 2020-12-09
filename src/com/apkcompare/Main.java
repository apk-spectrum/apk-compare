package com.apkcompare;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.nio.charset.Charset;

import javax.swing.JFrame;

import com.apkcompare.gui.DynamicTreeDemo;
import com.apkcompare.gui.UiEventHandler;
import com.apkcompare.resource.RImg;
import com.apkcompare.resource.RProp;
import com.apkcompare.resource.RStr;
import com.apkspectrum.plugin.PlugInManager;
import com.apkspectrum.swing.WindowSizeMemorizer;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.SystemUtil;

public class Main
{
	private static final ApkComparer apkComparer
							= new ApkComparer(null, null);
	private static final UiEventHandler evtHandler
							= new UiEventHandler(apkComparer);

	public static void main(final String[] args) {
		RStr.setLanguage(RProp.S.LANGUAGE.get());
		if("user".equalsIgnoreCase(RStr.APP_BUILD_MODE.get())) {
			Log.enableConsoleLog(false);
		}

		Log.i(RStr.APP_NAME.get() + " " + RStr.APP_VERSION.get() + " " + RStr.APP_BUILD_MODE.get());
		Log.i("OS : " + SystemUtil.OS);
		Log.i("java.version : " + System.getProperty("java.version"));
		Log.i("java.specification.version : " + System.getProperty("java.specification.version"));
		Log.i("file.encoding : " + System.getProperty("file.encoding"));
		Log.i("java.library.path : " + System.getProperty("java.library.path"));
		Log.i("Default Charset : " + Charset.defaultCharset());
		Log.i("sun.arch.data.model : " + System.getProperty("sun.arch.data.model"));

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();

				if(args.length > 0) {
					apkComparer.setApk(ApkComparer.LEFT, args[0]);
				}
				if(args.length > 1) {
					apkComparer.setApk(ApkComparer.RIGHT, args[1]);
				}
			}
		});

		loadPlugIn();
	}

	private static void createAndShowGUI() {
		WindowSizeMemorizer.setEnabled(RProp.B.SAVE_WINDOW_SIZE);

		// Create and set up the window.
		JFrame frame = new JFrame(RStr.APP_NAME.get());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(RImg.APP_ICON.getImage());

		// Create and set up the content pane.
		frame.setContentPane(new DynamicTreeDemo(apkComparer, evtHandler));

		WindowSizeMemorizer.apply(frame, new Dimension(1000, 800));
		//frame.setSize(1000, 800);

		frame.setLocationRelativeTo(null);
		// Display the window.
		//frame.pack();
		frame.setVisible(true);

		frame.addWindowListener(evtHandler);
	}

	private static void loadPlugIn() {
		PlugInManager.setAppPackage(Main.class.getPackage().getName(),
				RStr.APP_VERSION.get(), RStr.APP_NAME, RImg.APP_ICON);
		PlugInManager.setLang(RStr.getLanguage());
		PlugInManager.loadPlugIn();
		PlugInManager.setActionEventHandler(evtHandler);
	}
}
