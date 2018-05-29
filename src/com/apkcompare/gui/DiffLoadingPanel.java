package com.apkcompare.gui;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
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
	public DiffLoadingPanel() {
		super(new BorderLayout());
		
		imgicon = Resource.IMG_APK_LOGO.getImageIcon(300, 200);
		BufferedImage buffer = (BufferedImage)imgicon.getImage();
		
		Graphics g = buffer.getGraphics();
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.1f));
		
		ImageIcon tempimg = new ImageIcon(buffer);
		
		JLabel logo = new JLabel(tempimg);
		logo.setOpaque(true);
		logo.setBackground(Color.white);

		JLabel gif = new JLabel(Resource.IMG_DIFF_DRAG_AND_DROP.getImageIcon());
		gif.setOpaque(true);
		gif.setBackground(Color.white);
		gif.setPreferredSize(new Dimension(Resource.IMG_WAIT_BAR.getImageIcon().getIconWidth(),Resource.IMG_WAIT_BAR.getImageIcon().getIconHeight()));
		
		this.setLayout(new BorderLayout());
		this.setOpaque(false);
		setBackground(Color.white);
		//add(logo,BorderLayout.NORTH);
		add(gif,BorderLayout.CENTER);
		
		
		  new  FileDrop( this, new FileDrop.Listener()
		  {   public void  filesDropped( java.io.File[] files )
		      {   
		          // handle file drop
		          Log.d(files[0].toString());
		      }   // end filesDropped
		  }); // end FileDrop.Listener
		
		setVisible(true);
	}
}
