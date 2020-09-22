package com.apkcompare.data;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.util.FileUtil;
import com.apkspectrum.util.Log;

public class ImageDiffTreeUserData extends DiffTreeUserData{
	ImageIcon icon = null;
	String respath = null;
	public ImageDiffTreeUserData(String title, String key) {
		super(title, key, null);
	}
	public ImageDiffTreeUserData(String title, String key, ApkInfo apkinfo) {
		super(title, key, apkinfo);
	}
	
	public void setImageIcon(ImageIcon icon, String respath) {
		this.icon = icon;
		this.respath = respath;
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
    
    @SuppressWarnings("unused")
	private File ImageiconToFile() {
        BufferedImage bi = (BufferedImage) icon.getImage();
        //File outputfile = new File("saved.png");
        
        if(FileUtil.makeFolder(apkinfo.tempWorkPath + File.separator)) {
        	Log.i("sucess make folder : " + apkinfo.tempWorkPath);
        }
        
        File outputfile = null;
		try {
        	outputfile = File.createTempFile("image", ".png", new File(apkinfo.tempWorkPath + File.separator));
			ImageIO.write(bi, "png", outputfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
        return outputfile;
    }
    
	@Override
	public File makeFilebyNode() {
		//String imagefilepath = apkinfo.manifest.application.icons[apkinfo.manifest.application.icons.length - 1].name;
		//imagefilepath = imagefilepath.substring(imagefilepath.indexOf("!")+2, imagefilepath.length());
		//Log.d(imagefilepath.substring(imagefilepath.indexOf("!")+2, imagefilepath.length()));
		
		return makeFileForFile(respath);
	}
}
