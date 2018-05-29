package com.apkcompare.gui;

import javax.swing.JFrame;

import com.apkscanner.core.scanner.AaptScanner;
import com.apkscanner.core.scanner.ApkScanner;
import com.apkscanner.core.scanner.ApkScanner.Status;
import com.apkscanner.util.Log;

public class DiffMain {
	private static final ApkScanner apkScannerDiff1 = new AaptScanner(null);
	private static final ApkScanner apkScannerDiff2 = new AaptScanner(null);
	private static int Count =0;
	static DynamicTreeDemo newContentPane;
	static ApkScannerDiffListener diff1listener;
	static ApkScannerDiffListener diff2listener;
	
	static String diff2path = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/NILE/Cinnamon/applications/3rd_party/jpn/dcm/DCMMagHome/zerofltedcm/DCMMagHome.apk";
	static String diff1path = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO81/Cinnamon/applications/provisional/JPN/DCM/apps/DCMMagHome/starqltedcm/DCMMagHome.apk";
	//static String diff1path = "/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/NILE/Cinnamon/applications/3rd_party/jpn/dcm/DCMImadoco/zeroltedcm/DCMImadoco.apk";
	
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("APK Scanner - Diff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        newContentPane = new DynamicTreeDemo();
        newContentPane.setOpaque(true);
        
        frame.setContentPane(newContentPane);
        frame.setSize(1000, 800);
        
        frame.setLocationRelativeTo(null);
        // Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

    // /media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO81/Cinnamon/applications/provisional/JPN/DCM/apps/DCMMail/greatqltedcm/DCMMail.apk
    // /media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO81/Cinnamon/applications/provisional/JPN/DCM/apps/DCMMail/generic/DCMMail.apk
    
    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
    	javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //createAndShowGUI();
				createAndShowGUI();
				
//				apkScannerDiff1.openApk(diff1path);
//            	apkScannerDiff1.setStatusListener(new ApkScannerDiffListener(apkScannerDiff1, 0));
//            	
//            	apkScannerDiff2.openApk(diff2path);
//            	apkScannerDiff2.setStatusListener(new ApkScannerDiffListener(apkScannerDiff2, 1));
            	
            	
            	
            }
        });
    }

    static class ApkScannerDiffListener implements ApkScanner.StatusListener {
    	ApkScanner apkScannerDiff;
    	int number;
    	public ApkScannerDiffListener(ApkScanner scanner, int num) {
    		
    		this.apkScannerDiff = scanner;
    		this.number = num;
			// TODO Auto-generated constructor stub
		}
    	
		@Override
		public void onStart(long estimatedTime) {
			// TODO Auto-generated method stub
			Log.d("onStart()" + estimatedTime);
		}

		@Override
		public void onSuccess() {
			// TODO Auto-generated method stub
			Log.d("onSuccess()");
		}

		@Override
		public void onError(int error) {
			// TODO Auto-generated method stub
			Log.d("onError()" + error);
		}

		@Override
		public void onCompleted() {
			// TODO Auto-generated method stub			
			newContentPane.createTreeNode(apkScannerDiff.getApkInfo(), number);					
			
		}

		@Override
		public void onProgress(int step, String msg) {
			// TODO Auto-generated method stub
			Log.d("onProgress()" + step +":" +  msg);
		}

		@Override
		public void onStateChanged(Status status) {
			// TODO Auto-generated method stub
			Log.d("onProgress()" + status );
		}
    	
    }
    
}
