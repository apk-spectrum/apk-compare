package com.apkcompare.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.apkcompare.resource.Resource;

public class DiffLoadingPanel extends JPanel{
	private static final long serialVersionUID = 7818687226070887018L;

	ImageIcon imgicon;
	
	public static String LOADING = "loading";
	public static String EMPTY = "empty";
	
	private JPanel loading, empty;
	
	
	public DiffLoadingPanel() {
		super(new CardLayout());
		
		loading = new JPanel(new BorderLayout());
		empty = new JPanel(new BorderLayout());
		
		loading.setLayout(new BoxLayout(loading, BoxLayout.Y_AXIS));
		
		JLabel logo = new JLabel(Resource.IMG_APK_LOGO.getImageIcon(300, 200));
		//logo.setOpaque(true);
		//logo.setBackground(Color.white);

		JLabel gif = new JLabel(Resource.IMG_WAIT_BAR.getImageIcon());
		//gif.setOpaque(true);
		//gif.setBackground(Color.white);
		gif.setPreferredSize(new Dimension(Resource.IMG_WAIT_BAR.getImageIcon().getIconWidth(),Resource.IMG_WAIT_BAR.getImageIcon().getIconHeight()));
		
		logo.setAlignmentX(Component.CENTER_ALIGNMENT);
		logo.setAlignmentY(Component.CENTER_ALIGNMENT);
		gif.setAlignmentX(Component.CENTER_ALIGNMENT);
		gif.setAlignmentY(Component.CENTER_ALIGNMENT);
		
		loading.add(Box.createVerticalGlue());
		loading.add(logo);
		loading.add(gif);
		loading.add(Box.createVerticalGlue());
		
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
	
	public JComponent getEmptyPanel() {
		return empty;
	}
}
