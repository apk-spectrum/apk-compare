package com.apkcompare.data;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkscanner.util.Log;

import sun.awt.image.ToolkitImage;

public class PermissionDiffTreeUserData extends ImagePassKeyDiffTreeUserData implements MappingImp{
	
	public PermissionDiffTreeUserData(String title, ImageIcon icon) {
		super(title, "Permission");
		this.icon = icon;
		
		// TODO Auto-generated constructor stub
	}
	public PermissionDiffTreeUserData(String title, String key) {
		super(title, key);
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
