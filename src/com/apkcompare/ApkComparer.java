package com.apkcompare;

import com.apkscanner.core.scanner.AaptScanner;
import com.apkscanner.core.scanner.ApkScanner;
import com.apkscanner.core.scanner.ApkScanner.Status;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.util.Log;

public class ApkComparer {
    static public int LEFT = 0;
    static public int RIGHT = 1;

	public interface StatusListener {
		public void onStart(int position);
		public void onSuccess(int position);
		public void onError(int position, int error);
		public void onCompleted(int position);
	}

	private ApkScanner scanner[] = { new AaptScanner(null), new AaptScanner(null) };
	private StatusListener listener;

	public ApkComparer(String path1, String path2) {
		scanner[LEFT].setStatusListener(new ApkScannerDiffListener(LEFT));
		scanner[RIGHT].setStatusListener(new ApkScannerDiffListener(RIGHT));
		if(path1 != null) {
			scanner[LEFT].openApk(path1);	
		}
		if(path2 != null) {
			scanner[RIGHT].openApk(path2);	
		}
	}

	public ApkInfo getApkInfo(int position) {
		return scanner[position].getApkInfo();
	}
	
	public void setApk(int position, String path) {
		scanner[position].openApk(path);
	}
	
	public void setStatusListener(StatusListener listener) {
		this.listener = listener;
	}

    class ApkScannerDiffListener implements ApkScanner.StatusListener {
    	private int position;
    	public ApkScannerDiffListener(int position) {
    		this.position = position;
		}
    	
		@Override
		public void onStart(long estimatedTime) {
			Log.d("onStart()" + estimatedTime);
			if(listener != null) listener.onStart(position);
		}

		@Override
		public void onSuccess() {
			Log.d("onSuccess()");
			if(listener != null) listener.onSuccess(position);
		}

		@Override
		public void onError(int error) {
			Log.d("onError()" + error);
			if(listener != null) listener.onError(position, error);
		}

		@Override
		public void onCompleted() {
			if(listener != null) listener.onCompleted(position);
		}

		@Override
		public void onProgress(int step, String msg) {
			Log.d("onProgress()" + step +":" +  msg);
		}

		@Override
		public void onStateChanged(Status status) {
			Log.d("onProgress()" + status );
		}
    }
}
