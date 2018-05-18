package com.apkscanner.diff.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

import com.apkscanner.diff.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.Log;

class DiffTree extends JTree {
	public static final Color diffcolor = new Color(224,224,255);
	public static final Color diffcolorselect = new Color(96,107,192);
	public static final Color addcolor = new Color(255,247,217);
	public static final Color addcolorselect = new Color(190,150,30);
	public static final Color nomal = Color.white;
	public static final Color nomalselect = new Color(109,120,128);
	
	public static final Color[] colorarray = {Color.lightGray, nomalselect, addcolor, addcolorselect, diffcolor, diffcolorselect};
	//public static final Color[] focuscolorarray = {Color.lightGray, nomalselect, addcolor, addcolorselect, diffcolor, diffcolorselect};
	static JTree selectedtree;
	static DiffTree left,right;
	static JSplitPaneWithZeroSizeDivider splitPane;
	
	public DiffTree(DefaultTreeModel treeModel) {
		// TODO Auto-generated constructor stub
		super(treeModel);
		setCellRenderer(new DiffTreeCellRenderer());
		setOpaque(false);		
		setRootVisible(false);
		//left.setShowsRootHandles(true);
		setBorder(BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ));
		setToggleClickCount(0);
		final JTree temp = this;

	}
	
	public static void setSelectedtree(JTree tree) {
		selectedtree = tree;
	}
	
	public static void setleftrighttree(DiffTree lefttree, DiffTree righttree) {
		left = lefttree;
		right = righttree;
	}
	
	public static void setJSplitPane(JSplitPaneWithZeroSizeDivider pane) {
		splitPane = pane;
	}
	
	public Color getnodeColor(int row, DefaultMutableTreeNode node, boolean selected) {
		DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
		
    	if(isExpanded(row) && !node.isLeaf()){
			if(selected) {
				if(selectedtree == this) {
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL+1];
				} else{
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL];
				}
			} else {
				return Color.white;
			}
		} else {
			if(selected) {
				if(selectedtree == this) {
					return colorarray[temp.getState()+1];					
				} else {
					return colorarray[temp.getState()].darker();								
				}
			} else {
				if(temp.state == DiffTreeUserData.NODE_STATE_NOMAL) {
					return Color.white;
				} else {
					return colorarray[temp.getState()];
				}
			}
		}
    }
	
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(Color.WHITE);
		
		g.fillRect(0, 0, getWidth()+1, getHeight()+1);
		//g.drawRect(0, 0, getWidth()+1, getHeight()+1);
		for (int i = 0; i < getRowCount(); i++) {
			g.setColor(Color.WHITE);
			Object o = getPathForRow(i).getLastPathComponent();
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
		    DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
			
			Color nodecolor = this.getnodeColor(i,node, (getSelectionCount() > 0 && getSelectionRows()[0] == i));
			
			g.setColor(nodecolor);
			Rectangle r = getRowBounds(i);
			g.fillRect(0, r.y, getWidth(), r.height);
			
			//if(this == left) {
				SplitPaintData tempSplitPaintData = new SplitPaintData();					
				tempSplitPaintData.color = nodecolor;
				tempSplitPaintData.index = i;
				tempSplitPaintData.state = temp.state;
				tempSplitPaintData.isleaf = node.isLeaf();
				tempSplitPaintData.isleft = (left==this);
				tempSplitPaintData.startposition = this.getRowBounds(i).y;
				tempSplitPaintData.height = this.getRowBounds(i).height;
				
				DiffTree tempothertree = (left==this) ? right : left;
				
				if(temp.other != null) {
					tempSplitPaintData.endposition = tempothertree.getPathBounds(temp.other).y;
				} else {
					if(left==this) {						
						for(int j=i; j >=0; j--) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							DiffTreeUserData filetemp = (DiffTreeUserData)nodetemp.getUserObject();
							if(filetemp.other != null) {
								//Log.d(i + "  :  " + j);
								tempSplitPaintData.endposition = tempothertree.getPathBounds(filetemp.other).y-2;
								break;
							}
						}
					} else {
						for(int j=i; j <left.getRowCount(); j++) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							DiffTreeUserData filetemp = (DiffTreeUserData)nodetemp.getUserObject();
							Log.d(j + "  :  " + filetemp + "  other : " + filetemp.other);
							if(filetemp.other != null) {
								//Log.d("in  other : " + filetemp.other + " y : " +tempothertree.getPathBounds(filetemp.other).y);
								
								tempSplitPaintData.endposition = tempothertree.getPathBounds(filetemp.other).y - tempothertree.getPathBounds(filetemp.other).height;
								break;
							}
						}
					}
					//tempSplitPaintData.endposition = 0;
				}
				
				//Log.d(tree.getRowBounds(row).y + "");
				splitPane.setsplitPanedata(tempSplitPaintData);
				//splitPane.revalidate();
			}
		//}

		super.paintComponent(g);
	}
	
	private class DiffTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, false);
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			
			if(node.getUserObject() instanceof DiffTreeUserData) {
				DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
				Color nodecolor = ((DiffTree) tree).getnodeColor(row,node, selected);
				//l.setBackground(new Color(0,0,0,0));
				l.setBackground(nodecolor);
				if(selected && selectedtree == tree) l.setForeground(Color.WHITE);
				else l.setForeground(Color.BLACK);
				
				if(temp.Key.equals("Icon")) {
					l.setIcon(((ImageDiffTreeUserData)temp).getImageIcon());
					
				}				
				
				l.setOpaque(true);
			}
			return l;
		}
	};
}

