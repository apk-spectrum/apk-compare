package com.apkcompare.data;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;

public class ImagePassKeyDiffTreeUserData extends PassKeyDiffTreeUserData {
	ImageIcon icon = null;
	
	public ImagePassKeyDiffTreeUserData(String title) {
		super(title, "", null);
	}
	public ImagePassKeyDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	
	public ImagePassKeyDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}
	
	public void setImageIcon(ImageIcon icon) {
		this.icon = icon;
	}
	public ImageIcon getImageIcon() {
		return icon;
	}

	@Override
	public boolean compare(DiffTreeUserData data) {
		ImagePassKeyDiffTreeUserData temp = (ImagePassKeyDiffTreeUserData)data;
		//Log.d(temp.getImageIcon() + ":" + icon);
		
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
