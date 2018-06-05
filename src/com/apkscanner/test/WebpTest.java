package com.apkscanner.test;


import javax.swing.JLabel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;


///home/leejinhyeong/Desktop/luciad-webp-imageio-873c5677244b/src/test/resources/lossless.webp
public class WebpTest {
	public static void main(String[] args) {
		
		System.setProperty("java.library.path",  System.getProperty("user.dir") + "/tool/");
		
		BufferedImage image = null;
		try {
			image = ImageIO.read(new File("/home/leejinhyeong/Desktop/luciad-webp-imageio-873c5677244b/src/test/resources/lossless.webp"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
