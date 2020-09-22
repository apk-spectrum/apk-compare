package com.apkcompare.data.base;

import com.apkspectrum.data.apkinfo.ApkInfo;

public class PassKeyDiffTreeUserData extends DiffTreeUserData {
	
	public PassKeyDiffTreeUserData(String title) {
		this(title, "", null);
	}
	public PassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}
}
