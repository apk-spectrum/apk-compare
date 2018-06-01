package com.apkcompare.data.base;

import java.io.File;

import com.apkscanner.data.apkinfo.ApkInfo;

public interface MappingImp {
	boolean compare(DiffTreeUserData data);
	//void openFileNode(ApkInfo apkinfo);
	File makeFilebyNode(ApkInfo apkinfo);
}
