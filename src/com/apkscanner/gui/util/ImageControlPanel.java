package com.apkscanner.gui.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import com.apkscanner.resource.Resource;
import com.apkscanner.util.Log;

public class ImageControlPanel extends JPanel{
	private static final long serialVersionUID = -391185152837196160L;
	
	JScrollPane scroll;
	ImageViewPanel imagepanel;
	Dimension Imagearea;
	JLabel ImageInfo;
	Dimension ViewPortsize;
	double positionx, positiony;
	
	public ImageControlPanel() {
		imagepanel = new ImageViewPanel();
		ImageInfo = new JLabel("");
		//imagepanel.setLayout(new GridLayout());
		setLayout(new BorderLayout());
		imagepanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		imagepanel.setBackground(Color.BLACK);
		
		scroll = new JScrollPane(imagepanel);
		scroll.setPreferredSize(new Dimension(300, 400));
		scroll.repaint();
		
		add(scroll, BorderLayout.CENTER);
		add(ImageInfo, BorderLayout.SOUTH);
	}

	public void setImage(ImageIcon img) {
		
		imagepanel.setImage(img);
		repaint();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		imagepanel.repaint();
		imagepanel.revalidate();
	}
	
	private class ImageViewPanel extends JPanel implements MouseListener{

		int x, y;
		int beforx,befory;
		private float scale = 1;
		private float DefalutMinscale =1;
		BufferedImage bi;
		BufferedImage bgbi;
		
		public ImageViewPanel() {
			setBackground(Color.white);		
			addMouseMotionListener(new MouseMotionHandler());
			addMouseListener(this);
	        addMouseWheelListener(new MouseAdapter() {
	            @Override
	            public void mouseWheelMoved(MouseWheelEvent e) {
	                double delta = (-e.getPreciseWheelRotation() * 0.05 + 1);
	                scale *= delta;
	                
	                if(scale > 20) scale = 20f;
	                else if(scale < 0.02f) scale = 0.02f;
	                
	                revalidate();
	                repaint();
	                
	                Log.d("scale : " + scale);
	            }
	        });
		}
		public void setImage(ImageIcon img) {
			Image image = img.getImage();
			bi = new BufferedImage(image.getWidth(this), image.getHeight(this), BufferedImage.TYPE_INT_ARGB);
			Graphics2D big = bi.createGraphics();
			big.drawImage(image, 0, 0, this);

			x = 0;
			y = 0;

			beforx = befory = 0;
			scale = 1;	
			
			ViewPortsize = scroll.getViewport().getSize();
			
			Log.d("rect " + ViewPortsize.toString());
			
			scale = DefalutMinscale = (float) ((ViewPortsize.getWidth()/(image.getWidth(this)) <= ViewPortsize.getHeight()/(image.getHeight(this)))?
					(ViewPortsize.getWidth()/image.getWidth(this)) :
				(ViewPortsize.getHeight()/image.getHeight(this)));
			
			Log.d("DefalutMinscale : " + DefalutMinscale);
			
			Imagearea = new Dimension(x,y);
		}
		
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2D = (Graphics2D) g;
			ViewPortsize = scroll.getViewport().getSize();
			AffineTransform at = new AffineTransform();
			if(bi!=null) {
				Rectangle Rect = g2D.getClipBounds();
				
				
				positionx = (int)((Rect.getWidth()-bi.getWidth() * scale)/2) +x;
				positiony = (int)((Rect.getHeight()-bi.getHeight() * scale)/2) + y;
				
				
				at.translate(positionx, positiony);
				
				TexturePaint paint;
			    Image imageBackground = Resource.IMG_RESOURCE_IMG_BACKGROUND.getImageIcon().getImage();
			    bgbi = new BufferedImage(imageBackground.getWidth(this), imageBackground.getHeight(this), BufferedImage.TYPE_INT_ARGB);
			    bgbi.createGraphics().drawImage(imageBackground, 0, 0, this);
			    
				paint = new TexturePaint(bgbi, new Rectangle(0, 0, bgbi.getWidth(), bgbi.getHeight()));
				
				g2D.setPaint(paint);
				//g2D.fillRect((int)positionx, (int)positiony, (int)(bi.getWidth()* scale), (int)(bi.getHeight() * scale));
				
				Rectangle2D rect = new Rectangle2D.Double(positionx, positiony, (bi.getWidth()* scale), (bi.getHeight() * scale));
				
				//og.d("positionx : " + positionx + " positiony : " + positiony +  "scale : " + scale);
				//Log.d("bi.getWidth()* scale : " + bi.getWidth()* scale + "    bi.getHeight() * scale : " +(bi.getHeight() * scale) );
				
				g2D.fill(rect);;
				
		        at.scale(scale, scale);	        
				String text = "W : " + bi.getWidth() + "      H : " + bi.getHeight() + "  " + Math.round(scale * 100) + "%";		
				g2D.drawImage(bi, at, this);
		        //g2D.setColor(Color.WHITE);
		        
				//g2D.drawChars(text.toCharArray(), 0, text.length(), 10,10);		        
		        
				ImageInfo.setText(text);
				
		        Imagearea.setSize(bi.getWidth()*scale,bi.getHeight()*scale);
				setPreferredSize(Imagearea);
				scrollRectToVisible(new Rectangle(-x,-y,(int)(bi.getWidth()*scale),(int)(bi.getHeight()*scale)));				
				Log.d("positionx : "+positionx +"positiony = "+ positiony);
			}
		}
		
		@Override
		public void mouseClicked(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			beforx = arg0.getX();
			befory = arg0.getY();
			
			this.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}

		@Override
		public void mouseReleased(java.awt.event.MouseEvent arg0) {
			// TODO Auto-generated method stub
			this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			
		}
		
		class MouseMotionHandler extends MouseMotionAdapter {
			public void mouseDragged(MouseEvent e) {
				if(scroll.getHorizontalScrollBar().isVisible()) {
					x += ( e.getX()- beforx);
					beforx = e.getX();
					if(positionx+x >0) {
						positionx = 0;						
					}
				}
				if(scroll.getVerticalScrollBar().isVisible()) {
					y += ( e.getY()- befory);
					befory = e.getY();
					if(positiony+y >0) {
						positiony = 0;
					}					
				}	
				revalidate();
				repaint();
			}
		}
	}
}