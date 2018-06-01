package com.apkcompare.data.base;

import com.apkscanner.data.apkinfo.ApkInfo;

public interface MappingImp {
	boolean compare(DiffTreeUserData data);
	void openFileNode(ApkInfo apkinfo);
}
