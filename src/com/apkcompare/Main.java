package com.apkcompare;

import java.nio.charset.Charset;

import javax.swing.JFrame;

import com.apkcompare.gui.DynamicTreeDemo;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.Log;
import com.apkscanner.util.SystemUtil;

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
        frame = new JFrame(Resource.STR_APP_NAME.getString());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create and set up the content pane.
        DynamicTreeDemo newContentPane = new DynamicTreeDemo(apkComparer);
        newContentPane.setOpaque(true);
        frame.addWindowListener(newContentPane);
        frame.setContentPane(newContentPane);
        frame.setSize(1000, 800);
        frame.setIconImage(Resource.IMG_DIFF_APP_ICON.getImageIcon().getImage());
        frame.setLocationRelativeTo(null);
        
        // Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

}
