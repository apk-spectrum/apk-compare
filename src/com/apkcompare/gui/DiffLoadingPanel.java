package com.apkcompare.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apkscanner.gui.util.FileDrop;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.Log;

public class DiffLoadingPanel extends JPanel{
	ImageIcon imgicon;
	
	public static String LOADING = "loading";
	public static String EMPTY = "empty";
	
	private JPanel loading, empty;
	
	
	public DiffLoadingPanel() {
		super(new CardLayout());
		
		loading = new JPanel(new BorderLayout());
		empty = new JPanel(new BorderLayout());
		
		JLabel logo = new JLabel(Resource.IMG_APK_LOGO.getImageIcon(300, 200));
		logo.setOpaque(true);
		logo.setBackground(Color.white);

		JLabel gif = new JLabel(Resource.IMG_WAIT_BAR.getImageIcon());
		gif.setOpaque(true);
		gif.setBackground(Color.white);
		gif.setPreferredSize(new Dimension(Resource.IMG_WAIT_BAR.getImageIcon().getIconWidth(),Resource.IMG_WAIT_BAR.getImageIcon().getIconHeight()));
		
		loading.add(logo, BorderLayout.CENTER);
		//loading.add(gif, BorderLayout.SOUTH);
		
		JLabel emptylabel = new JLabel(Resource.IMG_DIFF_DRAG_AND_DROP.getImageIcon(150, 150));
		emptylabel.setOpaque(true);
		emptylabel.setBackground(Color.white);
		
		empty.add(emptylabel, BorderLayout.CENTER);
		
		this.setOpaque(false);
		setBackground(Color.white);
		//add(logo,BorderLayout.NORTH);
		add(loading, LOADING);
		add(empty, EMPTY);
		
		setshow(EMPTY);
	}
	
	public void setshow(String str) {
		CardLayout cl = (CardLayout)(getLayout());
	    cl.show(this, str);
	}
	
}
