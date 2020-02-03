package com.apkcompare.data;

import javax.swing.ImageIcon;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;

public class PermissionDiffTreeUserData extends ImagePassKeyDiffTreeUserData {
	
	public PermissionDiffTreeUserData(String title, ImageIcon icon) {
		super(title);
		this.icon = icon;
		
		// TODO Auto-generated constructor stub
	}
	public PermissionDiffTreeUserData(String title, String key, ImageIcon icon, ApkInfo apkinfo) {
		super(title, key, apkinfo);
		this.icon = icon;
		// TODO Auto-generated constructor stub
	}
	
	public PermissionDiffTreeUserData(String title, ImageIcon icon, ApkInfo apkinfo) {
		super(title, "", apkinfo);
		this.icon = icon;
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean compare(DiffTreeUserData data) {
		if(title.equals(data.title)) {
			return true;
		} else {
			return false;
		}
	}
}
