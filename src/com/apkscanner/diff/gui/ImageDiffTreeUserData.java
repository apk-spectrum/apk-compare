package com.apkscanner.diff.gui;

import javax.swing.ImageIcon;

public class ImageDiffTreeUserData extends DiffTreeUserData{
	ImageIcon icon = null;
	
	public ImageDiffTreeUserData(String title) {
		super(title);
		// TODO Auto-generated constructor stub
	}
	public ImageDiffTreeUserData(String title, String key) {
		super(title, key);
		// TODO Auto-generated constructor stub
	}
	
	public void setImageIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public ImageIcon getImageIcon() {
		return icon;
	}

}
