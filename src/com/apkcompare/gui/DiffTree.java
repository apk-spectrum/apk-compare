package com.apkcompare.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.apkscanner.gui.util.ImageScaler;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.Log;
import com.apkcompare.data.ImageDiffTreeUserData;
import com.apkcompare.data.ImagePassKeyDiffTreeUserData;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.sun.scenario.animation.SplineInterpolator;

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
	static JScrollPane hostingScrollPane;
	static Image foldericon = ImageScaler.getScaledImage(Resource.IMG_TREE_FOLDER.getImageIcon(), 16, 16);
	static Image rooticon = ImageScaler.getScaledImage(Resource.IMG_APK_FILE_ICON.getImageIcon(), 16, 16);
	
	boolean painting = true;
	
	public DiffTree() {
		super();
		initTree();	
	}
	
	public void setpaintingFlag(boolean flag) {
		this.painting = flag;
	}
	
	public DiffTree(DefaultTreeModel treeModel) {
		// TODO Auto-generated constructor stub
		super(treeModel);
		initTree();
	}
	
	private void initTree() {
		//getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
	    //getSelectionModel().setSelectionMode(TreeSelectionModel.CONTIGUOUS_TREE_SELECTION);
	    //getSelectionModel().setSelectionMode(TreeSelectionModel.DISCONTIGUOUS_TREE_SELECTION);
		setCellRenderer(new DiffTreeCellRenderer());
		setOpaque(false);	
		//setRootVisible(false);
		//left.setShowsRootHandles(true);
		setBorder(BorderFactory.createEmptyBorder ( 5, 5, 5, 5 ));
		setToggleClickCount(0);
	}
	
	public static void setScrollPane(JScrollPane scrollpane) {
		hostingScrollPane = scrollpane;
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
	
	public Color getnodeColor(int row, DefaultMutableTreeNode node, boolean selected, boolean forsplitpane) {
		DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
		
    	if(isExpanded(row) && !node.isLeaf()){
			if(selected) {
				if(selectedtree == this || forsplitpane) {
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL+1];
				} else{
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL];
				}
			} else {
				return Color.white;
			}
		} else {
			if(selected) {
				if(selectedtree == this || forsplitpane) {
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
		
		if(!painting) {
			super.paintComponent(g);
			return;
		}
		
		if(hostingScrollPane != null && getRowCount() > 0) {
			List<TreePath> visiblenode = getVisibleNodes(this);
			
			for (int k = 0; k < visiblenode.size(); k++) {
				g.setColor(Color.WHITE);				
				TreePath o = visiblenode.get(k);
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) o.getLastPathComponent();
				DiffTreeUserData temp = (DiffTreeUserData) node.getUserObject();
				int i = this.getRowForPath(o);
				Color nodecolor = this.getnodeColor(i, node, (getSelectionCount() > 0 && getSelectionRows()[0] == i), false);

				g.setColor(nodecolor);
				//Log.d(getRowCount() + "");
				Rectangle r = getRowBounds(i);
				
				
				g.fillRect(0, r.y, getWidth(), r.height);

				// if(this == left) {
				SplitPaintData tempSplitPaintData = new SplitPaintData();
				tempSplitPaintData.color = this.getnodeColor(i, node, (getSelectionCount() > 0 && getSelectionRows()[0] == i), true);
				tempSplitPaintData.index = i;
				tempSplitPaintData.state = temp.state;
				tempSplitPaintData.isleaf = node.isLeaf() && !temp.isfolder;
				tempSplitPaintData.isleft = (left == this);
				tempSplitPaintData.startposition = this.getRowBounds(i).y;
				tempSplitPaintData.ohterheight = tempSplitPaintData.height = this.getRowBounds(i).height;

				DiffTree tempothertree = (left == this) ? right : left;

				if (temp.other != null) {
									
					if(tempothertree.getPathBounds(temp.other)== null) {
						Log.d(tempothertree.getPathBounds(temp.other) + ": " + temp.other);
						
						
					}
					
					tempSplitPaintData.endposition = tempothertree.getPathBounds(temp.other).y;
				} else {
					if (left == this) {
						for (int j = i; j >= 0; j--) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							DiffTreeUserData filetemp = (DiffTreeUserData) nodetemp.getUserObject();
							if (filetemp.other != null) {
								// Log.d(i + " : " + j);
								tempSplitPaintData.endposition = tempothertree.getPathBounds(filetemp.other).y;
								tempSplitPaintData.ohterheight = tempothertree.getPathBounds(filetemp.other).height;
								break;
							}
						}
					} else {
						for (int j = i; j < right.getRowCount(); j++) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							DiffTreeUserData filetemp = (DiffTreeUserData) nodetemp.getUserObject();

							// Log.d("i = " + i + filetemp);
							if (filetemp.other != null) {
								tempSplitPaintData.endposition = tempothertree.getPathBounds(filetemp.other).y
										- tempothertree.getPathBounds(filetemp.other).height;
								tempSplitPaintData.ohterheight = tempothertree.getPathBounds(filetemp.other).height;

								// Log.d(filetemp.toString());
								// tempSplitPaintData
								break;
							}
						}
					}
					// tempSplitPaintData.endposition = 0;
				}
				if (tempSplitPaintData.endposition != 0) {
					splitPane.setsplitPanedata(tempSplitPaintData);
					//splitPane.repaint();
				}				
			}
			
		}
		
		super.paintComponent(g);
	}
	
    private static List<TreePath> getVisibleNodes(JTree hostingJTree){
        //Find the first and last visible row within the scroll pane.
        final Rectangle visibleRectangle = hostingScrollPane.getViewport().getViewRect();
        final int firstRow = hostingJTree.getClosestRowForLocation(visibleRectangle.x, visibleRectangle.y);
        final int lastRow  = hostingJTree.getClosestRowForLocation(visibleRectangle.x, visibleRectangle.y + visibleRectangle.height);   
        //Iterate through each visible row, identify the object at this row, and add it to a result list.
        List<TreePath> resultList = new ArrayList<TreePath>();          
        for (int currentRow = firstRow; currentRow<=lastRow; currentRow++){
            TreePath currentPath = hostingJTree.getPathForRow(currentRow);            
            if(currentPath==null) return null;
            resultList.add(currentPath);                       
        }
        return(resultList);
    }  
	
	private class DiffTreeCellRenderer extends DefaultTreeCellRenderer {
		@Override
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
				boolean leaf, int row, boolean hasFocus) {
			JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, false);
			
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			
			if(node.getUserObject() instanceof DiffTreeUserData) {
				DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
				
				Color nodecolor = ((DiffTree) tree).getnodeColor(row,node, selected, false);
				//l.setBackground(new Color(0,0,0,0));
				l.setBackground(nodecolor);
				if(selected && selectedtree == tree) l.setForeground(Color.WHITE);
				else l.setForeground(Color.BLACK);
				
				if(temp instanceof ImagePassKeyDiffTreeUserData) {
					l.setIcon(((ImagePassKeyDiffTreeUserData)temp).getImageIcon());
				} else if(temp instanceof ImageDiffTreeUserData) {
					l.setIcon(((ImageDiffTreeUserData)temp).getImageIcon());
				} else if(temp.Key.equals("Root")) {
					l.setIcon(new ImageIcon(rooticon));
				} else if(temp.isfolder) {
					l.setIcon(new ImageIcon(foldericon));
				}
				
				l.setOpaque(true);
			}
			return l;
		}
	};
}

