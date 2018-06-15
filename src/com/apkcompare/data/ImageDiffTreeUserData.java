package com.apkcompare.data;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.MappingImp;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.util.Log;

import sun.awt.image.ToolkitImage;

public class ImageDiffTreeUserData extends DiffTreeUserData{
	ImageIcon icon = null;
	
	public ImageDiffTreeUserData(String title, String key) {
		super(title, key, null);
		// TODO Auto-generated constructor stub
	}
	public ImageDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
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
		
		if(icon == null || temp.getImageIcon() == null) {
			return title.equals(temp.title);
		}
		
		if(compareImages(temp.getImageIcon(), icon)) {
			return true;
		} else {
			return false;
		}
	}
	
	private boolean compareImages(ImageIcon imgiconA, ImageIcon imgiconB) {		
		BufferedImage imgA = bufferImage(imgiconA.getImage());
		BufferedImage imgB = bufferImage(imgiconB.getImage());		
		// The images must be the same size.
		if (imgA.getWidth() != imgB.getWidth() || imgA.getHeight() != imgB.getHeight()) {
			return false;
		}

		int width = imgA.getWidth();
		int height = imgA.getHeight();

		// Loop over every pixel.
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				// Compare the pixels for equality.
				if (imgA.getRGB(x, y) != imgB.getRGB(x, y)) {
					return false;
				}
			}
		}

		return true;
	}
    private BufferedImage bufferImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null),
				BufferedImage.TYPE_INT_RGB);

		Graphics g = bufferedImage.createGraphics();
		g.drawImage(image, 0, 0, null);
		g.dispose();
        
        return bufferedImage;
    }
}
