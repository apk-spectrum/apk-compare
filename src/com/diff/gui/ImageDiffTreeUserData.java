package com.diff.gui;

import javax.swing.ImageIcon;

public class ImageDiffTreeUserData extends DiffTreeUserData implements MappingImp{
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

	@Override
	public boolean compare(DiffTreeUserData data) {
		ImageDiffTreeUserData temp = (ImageDiffTreeUserData)data;		
		if(temp.equals(title) && temp.getImageIcon().equals(icon)) {
			return true;
		} else {			
			return false;
		}
	}
	
}
