package com.apkcompare;

import com.apkspectrum.core.scanner.AaptScanner;
import com.apkspectrum.core.scanner.ApkScanner;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.util.Log;

public class ApkComparer {
    static public final int LEFT = 0;
    static public final int RIGHT = 1;

	public interface StatusListener {
		public void onStart(int position);
		public void onSuccess(int position);
		public void onError(int position, int error);
		public void onCompleted(int position);
	}

	private ApkScanner scanner[] = {
		new AaptScanner(null),
		new AaptScanner(null)
	};
	private StatusListener listener;

	public ApkComparer() {
		scanner[LEFT].setStatusListener(new ScannerListener(LEFT));
		scanner[RIGHT].setStatusListener(new ScannerListener(RIGHT));
	}

	public ApkComparer(String path1, String path2) {
		this();
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

	public ApkScanner getApkScanner(int position) {
		return scanner[position];
	}

	public void setApk(int position, String path) {
		scanner[position].openApk(path);
	}

	public void swap() {
		ApkScanner temp;
		temp = scanner[LEFT];
		scanner[LEFT] = scanner[RIGHT];
		scanner[RIGHT] = temp;
		((ScannerListener) scanner[LEFT].getStatusListener()).position = LEFT;
		((ScannerListener) scanner[RIGHT].getStatusListener()).position = RIGHT;
	}

	public void clear(boolean sync) {
		scanner[LEFT].clear(sync);
		scanner[RIGHT].clear(sync);
	}

	public void setStatusListener(StatusListener listener) {
		this.listener = listener;
	}

    class ScannerListener implements ApkScanner.StatusListener {
    	private int position;
    	private int error;

    	public ScannerListener(int position) {
    		this.position = position;
    		this.error = 0;
		}

		@Override
		public void onStart(long estimatedTime) {
			error = 0;
			if(listener != null) listener.onStart(position);
		}

		@Override
		public void onSuccess() {
			Log.d("onSuccess()");
			error = 0;
			if(listener != null) listener.onSuccess(position);
		}

		@Override
		public void onError(int error) {
			Log.d("onError()" + error);
			this.error = error;
			if(listener != null) listener.onError(position, error);
		}

		@Override
		public void onCompleted() {
			Log.d("onCompleted() : " + error);
			if(listener != null && this.error == 0) {
				listener.onCompleted(position);
			}
		}

		@Override
		public void onProgress(int step, String msg) {
			Log.d("onProgress()" + step +":" +  msg);
		}

		@Override
		public void onStateChanged(int status) {
			Log.d("onProgress()" + status );
		}
    }
}