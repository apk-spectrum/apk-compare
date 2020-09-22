/*
 * Copyright (c) 2011 Karl Tauber <karl at jformdesigner dot com>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.apkcompare.gui;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Path2D;
import java.util.ArrayList;

import javax.swing.JSplitPane;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import com.apkcompare.data.base.DiffTreeUserData;

/**
 * A JSplitPane that uses a 1 pixel thin visible divider,
 * but a 9 pixel wide transparent drag area.
 */
@SuppressWarnings("serial")
public class JSplitPaneWithZeroSizeDivider extends JSplitPane {
	/**
	 * The size of the transparent drag area.
	 */
	private int dividerDragSize = 49;
	private int y = 0;
	Color defaultColor = new Color(234, 234, 234);
	Color selectdefaultColor = defaultColor.darker();
	
	ArrayList<SplitPaintData> leftpaintdata = new ArrayList<SplitPaintData>();
	ArrayList<SplitPaintData> rightpaintdata = new ArrayList<SplitPaintData>();
	
	/**
	 * The offset of the transparent drag area relative to the visible divider line.
	 * Positive offset moves the drag area left/top to the divider line.
	 * If zero then the drag area is right/bottom of the divider line.
	 * Useful values are in the range 0 to {@link #dividerDragSize}.
	 * Default is centered.
	 */
	private int dividerDragOffset = 0;
		
	static public class SplitPaintData {		
		int index;
		int startposition;
		int endposition;
		int height;
		int ohterheight;
		int state;
		boolean isleaf;
		boolean isleft;
		Color color;
		public SplitPaintData() {
			
		}
	}
	
	public void setsplitPanedata(SplitPaintData data) {
		if(data.isleft) {
			if(leftpaintdata.size() <= data.index) {
				leftpaintdata.add(data);
			} else {
				leftpaintdata.set(data.index, data);
			}
		} else {
			if(rightpaintdata.size() <= data.index) {
				rightpaintdata.add(data);
			} else {
				rightpaintdata.set(data.index, data);
			}
		}
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public JSplitPaneWithZeroSizeDivider() {
		this( HORIZONTAL_SPLIT );
	}

	public JSplitPaneWithZeroSizeDivider( int orientation ) {
		super( orientation );
		setContinuousLayout( true );
		setDividerSize( 49 );
	}

	public int getDividerDragSize() {
		return dividerDragSize;
	}

	public void setDividerDragSize( int dividerDragSize ) {
		this.dividerDragSize = dividerDragSize;
		revalidate();
	}

	public int getDividerDragOffset() {
		return dividerDragOffset;
	}

	public void setDividerDragOffset( int dividerDragOffset ) {
		this.dividerDragOffset = dividerDragOffset;
		revalidate();
	}

	@Override
	@SuppressWarnings("deprecation")
	public void layout() {
		super.layout();

		// increase divider width or height
		BasicSplitPaneDivider divider = ((BasicSplitPaneUI)getUI()).getDivider();
		Rectangle bounds = divider.getBounds();
		if( orientation == HORIZONTAL_SPLIT ) {
			bounds.x -= dividerDragOffset;
			bounds.width = dividerDragSize;
			bounds.y = y;
		} else {
			bounds.y -= dividerDragOffset;
			bounds.height = dividerDragSize;
		}
		
		divider.setBounds( bounds );
	}

	@Override
	public void updateUI() {
		setUI( new SplitPaneWithZeroSizeDividerUI() );
		revalidate();
	}

	//---- class SplitPaneWithZeroSizeDividerUI -------------------------------

	private class SplitPaneWithZeroSizeDividerUI
		extends BasicSplitPaneUI
	{
		@Override
		public BasicSplitPaneDivider createDefaultDivider() {
			return new ZeroSizeDivider( this );
		}
	}

	//---- class ZeroSizeDivider ----------------------------------------------

	
	private class ZeroSizeDivider extends BasicSplitPaneDivider {
        		
		public ZeroSizeDivider( BasicSplitPaneUI ui ) {
			super( ui );
			//super.setBorder( BorderFactory.createEmptyBorder(1,1,1,1) );
			//setBackground( UIManager.getColor( "controlShadow" ) );
		}
		
		@Override
		public void setBorder( Border border ) {
			// ignore
			//super.setBorder(border);
		}

		@Override
		public void paint( Graphics g ) {
			Graphics2D g2d = (Graphics2D)g;
			
		    RenderingHints rh = new RenderingHints(
		             RenderingHints.KEY_ANTIALIASING,
		             RenderingHints.VALUE_ANTIALIAS_ON);
		    g2d.setRenderingHints(rh);
			
			g.setColor(defaultColor);
			g.fillRect(0, 0, getWidth(), getHeight());
			
			for(int i=0; i<leftpaintdata.size(); i++) {
				if(leftpaintdata.get(i).color == Color.white) {
					g.setColor(defaultColor);
				} else {
					g.setColor(leftpaintdata.get(i).color.darker());
				}
				//endposition
					//g.fillRect(0, leftpaintdata.get(i).startposition +1 , getWidth(), 19);
				//if(leftpaintdata.get(i).endposition != 0) {
				
					Path2D.Double parallelogram = new Path2D.Double();
			        parallelogram.moveTo(0,leftpaintdata.get(i).startposition);
			        if(leftpaintdata.get(i).state != DiffTreeUserData.NODE_STATE_ADD
			        		|| !leftpaintdata.get(i).isleaf) parallelogram.lineTo(getWidth(), leftpaintdata.get(i).endposition);			        
			        parallelogram.lineTo(getWidth(), leftpaintdata.get(i).endposition + leftpaintdata.get(i).ohterheight);
			        parallelogram.lineTo(0,leftpaintdata.get(i).startposition + leftpaintdata.get(i).height);
			        parallelogram.closePath();
			        g2d.fill(parallelogram);
				//}
			}
			
			for(int i=0; i<rightpaintdata.size(); i++) {
				if(rightpaintdata.get(i).color == Color.white) {					
					g.setColor(defaultColor);
				} else {
					g.setColor(rightpaintdata.get(i).color.darker());
				}				
				//endposition
					//g.fillRect(0, leftpaintdata.get(i).startposition +1 , getWidth(), 19);
				//if(rightpaintdata.get(i).endposition != 0) {
					Path2D.Double parallelogram = new Path2D.Double();
			        parallelogram.moveTo(getWidth(),rightpaintdata.get(i).startposition);
			        if(rightpaintdata.get(i).state != DiffTreeUserData.NODE_STATE_ADD
			        		|| !rightpaintdata.get(i).isleaf) parallelogram.lineTo(0, rightpaintdata.get(i).endposition);			        
			        parallelogram.lineTo(0, rightpaintdata.get(i).endposition + rightpaintdata.get(i).ohterheight);
			        parallelogram.lineTo(getWidth(),rightpaintdata.get(i).startposition + rightpaintdata.get(i).height);
			        parallelogram.closePath();
			        g2d.fill(parallelogram);
				//}
			}
			
			//if(DiffTree.selectedtree.getSelectionRows()[0])
			
			
			leftpaintdata.clear();
			rightpaintdata.clear();
		}
		@Override
		protected void dragDividerTo( int location ) {
			super.dragDividerTo( location + dividerDragOffset );
		}

		@Override
		protected void finishDraggingTo( int location ) {
			super.finishDraggingTo( location + dividerDragOffset );
		}
	}
}
