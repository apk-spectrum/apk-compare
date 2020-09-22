package com.apkcompare.test;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;


///home/leejinhyeong/Desktop/luciad-webp-imageio-873c5677244b/src/test/resources/lossless.webp
public class WebpTest {
	
//    static {
//        String arch = System.getProperty("sun.arch.data.model");
//        String libPath = Resource.BIN_PATH.getPath();
//        if(SystemUtil.isWindows()) {
//         //   System.load(libPath + "AaptNativeWrapper" + arch + ".dll");
//        } else {
//            //System.load(libPath + "libc++" + arch + ".so");        	
//            System.load(libPath + "libwebp-imageio"+ ".so");
//        }
//    }
	
	public static void main(String[] args) {
		
		System.setProperty("java.library.path",  System.getProperty("user.dir") + "/tool/");
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("/home/leejinhyeong/Desktop/luciad-webp-imageio-873c5677244b/src/test/resources/lossless.webp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        ImageIcon icon = new ImageIcon(image);
        JLabel label = new JLabel(icon);
        
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 300);
		frame.getContentPane().add(label);
		frame.setVisible(true);
	}
}
