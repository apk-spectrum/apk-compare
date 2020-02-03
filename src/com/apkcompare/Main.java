package com.apkcompare;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.nio.charset.Charset;

import javax.swing.JFrame;

import com.apkcompare.gui.DynamicTreeDemo;
import com.apkcompare.gui.dialog.AboutDlg;
import com.apkcompare.gui.dialog.LogDlg;
import com.apkcompare.resource.Resource;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.SystemUtil;

public class Main {
	private static final ApkComparer apkComparer = new ApkComparer(null, null);
	public static JFrame frame;
	public static void main(final String[] args) {
		
    	//args[1] = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/NILE/Cinnamon/applications/3rd_party/jpn/dcm/DCMFacebook/hero2qltedcm/DCMFacebook.apk";
    	//args[0] = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/NILE/Cinnamon/applications/3rd_party/jpn/dcm/DCMFacebook/zeroltedcm/DCMFacebook.apk";
    	//args[0] = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/NILE/Cinnamon/applications/3rd_party/jpn/dcm/DCMAreaMail/dream2qltedcm/DCMAreaMail.apk";
    	//args[0] = "";
		
		Resource.setLanguage((String)Resource.PROP_LANGUAGE.getData(SystemUtil.getUserLanguage()));
		if("user".equalsIgnoreCase(Resource.STR_APP_BUILD_MODE.getString())) {
			Log.enableConsoleLog(false);
		}

		Log.i(Resource.STR_APP_NAME.getString() + " " + Resource.STR_APP_VERSION.getString() + " " + Resource.STR_APP_BUILD_MODE.getString());
		Log.i("OS : " + SystemUtil.OS);
		Log.i("java.version : " + System.getProperty("java.version"));
		Log.i("java.specification.version : " + System.getProperty("java.specification.version"));
		Log.i("file.encoding : " + System.getProperty("file.encoding"));
		Log.i("java.library.path : " + System.getProperty("java.library.path"));
		Log.i("Default Charset : " + Charset.defaultCharset());
		Log.i("sun.arch.data.model : " + System.getProperty("sun.arch.data.model"));

    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
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
	}

    private static void createAndShowGUI() {
        // Create and set up the window.
    	Compareevent event = new Compareevent();
    	
        frame = new JFrame(Resource.STR_APP_NAME.getString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create and set up the content pane.
        DynamicTreeDemo newContentPane = new DynamicTreeDemo(apkComparer);
        newContentPane.setOpaque(true);
        frame.addWindowListener(event);
		KeyboardFocusManager ky=KeyboardFocusManager.getCurrentKeyboardFocusManager();
		ky.addKeyEventDispatcher(event);
		
        frame.setContentPane(newContentPane);
        frame.setSize(1000, 800);
        frame.setIconImage(Resource.IMG_APP_ICON.getImageIcon().getImage());
        frame.setLocationRelativeTo(null);        
        // Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

	private static void finished()
	{
		Log.v("finished()");

		if((boolean)Resource.PROP_SAVE_WINDOW_SIZE.getData(false)) {
			int width = (int)frame.getWidth();
			int height = (int)frame.getHeight();
			if(Resource.PROP_WINDOW_WIDTH.getInt() != width
					|| Resource.PROP_WINDOW_HEIGHT.getInt() != (int)frame.getSize().getHeight()) {
				Resource.PROP_WINDOW_WIDTH.setData(width);
				Resource.PROP_WINDOW_HEIGHT.setData(height);
			}
		}

		frame.setVisible(false);
		
		apkComparer.getApkScanner(ApkComparer.LEFT).clear(true);
		apkComparer.getApkScanner(ApkComparer.RIGHT).clear(true);
		System.exit(0);
	}
    static class Compareevent implements WindowListener, KeyEventDispatcher {
    	@Override public void windowClosing(WindowEvent e) { finished(); }
    	@Override public void windowClosed(WindowEvent e) { finished(); }
    	@Override public void windowOpened(WindowEvent e) { }
    	@Override public void windowIconified(WindowEvent e) { }
    	@Override public void windowDeiconified(WindowEvent e) { }
    	@Override public void windowActivated(WindowEvent e) { }
    	@Override public void windowDeactivated(WindowEvent e) { }
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if(!frame.isFocused()) return false;
			if (e.getID() == KeyEvent.KEY_RELEASED) {
					if(e.getModifiers() == 0) {
						switch(e.getKeyCode()) {
						case KeyEvent.VK_F1 : AboutDlg.showAboutDialog(frame);break;
						case KeyEvent.VK_F12: LogDlg.showLogDialog(frame);	break;
						default: return false;
					}
					return true;
				}
			}
			return false;
		}    	
    }
}
