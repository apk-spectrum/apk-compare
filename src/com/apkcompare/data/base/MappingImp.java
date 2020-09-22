package com.apkcompare.data.base;

import java.io.File;

public interface MappingImp {
	boolean compare(DiffTreeUserData data);
	//void openFileNode(ApkInfo apkinfo);
	File makeFilebyNode();
}
