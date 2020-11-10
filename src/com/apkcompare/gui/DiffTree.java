package com.apkcompare.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkcompare.core.DiffMappingTree;
import com.apkcompare.data.ImageDiffTreeUserData;
import com.apkcompare.data.ImagePassKeyDiffTreeUserData;
import com.apkcompare.data.RootDiffTreeUserData;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.resource.RImg;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.util.Log;


@SuppressWarnings("serial")
public class DiffTree extends JTree
	implements TreeExpansionListener, TreeSelectionListener, MouseListener
{

	public static final int UNASSIGNED = -1;
	public static final int LEFT = 0;
	public static final int RIGHT = 1;

	public static final Color diffcolor = new Color(224,224,255);
	public static final Color diffcolorselect = new Color(96,107,192);
	public static final Color addcolor = new Color(255,247,217);
	public static final Color addcolorselect = new Color(190,150,30);
	public static final Color nomal = Color.white;
	public static final Color nomalselect = new Color(109,120,128);

	public static final Color[] colorarray = {
			Color.lightGray, nomalselect, addcolor,
			addcolorselect, diffcolor, diffcolorselect
		};
	//public static final Color[] focuscolorarray = {Color.lightGray, nomalselect, addcolor, addcolorselect, diffcolor, diffcolorselect};
	private static final Image foldericon = RImg.DIFF_TREE_FOLDER_ICON.getImage(16, 16);
	private static final Image rooticon = RImg.DIFF_TREE_APK_ICON.getImage(16, 16);

	private int position = UNASSIGNED;
	private DiffTree linkedTree;
	private ActionListener actListener;

	private boolean painting = true;

	public DiffTree(ActionListener listener) {
		super();
		actListener = listener;
		initTree();
	}

	public DiffTree(DefaultTreeModel treeModel, ActionListener listener) {
		super(treeModel);
		initTree();
	}

	public static void setLinkedPosition(DiffTree left, DiffTree right) {
		left.position = LEFT;
		right.position = RIGHT;
		left.linkedTree = right;
		right.linkedTree = left;
	}

	public int getPosition() {
		return position;
	}

	public void setpaintingFlag(boolean flag) {
		this.painting = flag;
	}

	public boolean getpaintingFlag() {
		return this.painting;
	}

	public void setActionListener(ActionListener listener) {
		actListener = listener;
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

		addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				e.getComponent().repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				e.getComponent().repaint();
			}
		});

		addTreeExpansionListener(this);
		addTreeSelectionListener(this);
		addMouseListener(this);
	}

	public void setLinkedTargetTree(DiffTree linkedTree) {
		this.linkedTree = linkedTree;
	}

	public void createTreeNode(ApkInfo info) {
		SortNode arraytreeNode = new SortNode(new RootDiffTreeUserData(info));
		FilteredTreeModel arrayTreemodel = new FilteredTreeModel(arraytreeNode);

		DiffMappingTree mappingtree = new DiffMappingTree();
		mappingtree.createTree(info, arraytreeNode);

		setModel(arrayTreemodel);
	}

	public List<TreePath> getPaths() {
		ArrayList<TreePath> expandedpath = new ArrayList<TreePath>();
		DefaultMutableTreeNode rootNode =
				(DefaultMutableTreeNode) getModel().getRoot();
		getPaths(new TreePath(rootNode.getPath()), expandedpath);
		return expandedpath;
	}

	private void getPaths(TreePath parent, List<TreePath> list) {
		if (!isVisible(parent)) {
			return;
		}

		if(isExpanded(parent)) {
			list.add(parent);
		}

		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				getPaths(path, list);
			}
		}
	}

	public Color getnodeColor(int row, DefaultMutableTreeNode node,
			boolean selected, boolean forsplitpane) {
		DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();

		if(isExpanded(row) && !node.isLeaf()){
			if(selected) {
				if(isFocusOwner() || forsplitpane) {
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL+1];
				} else{
					return colorarray[DiffTreeUserData.NODE_STATE_NOMAL];
				}
			} else {
				return Color.white;
			}
		} else {
			if(selected) {
				if(isFocusOwner() || forsplitpane) {
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
	public void valueChanged(TreeSelectionEvent e) {
		if(((JTree)e.getSource()).getSelectionPath() == null) return;

		DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();

		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp =(DiffTreeUserData)node.getUserObject();

//			Log.d("is expended : " + ((JTree)e.getSource()).isExpanded(new TreePath(node.getPath())));

			linkedTree.setSelectionPath(temp.other);

			Container splitPanel = SwingUtilities.getAncestorOfClass(JSplitPane.class, this);
			if(splitPanel != null) splitPanel.repaint();
		}
		//splitPane.setheight(left.getUI().getPathBounds(left, left.getSelectionPath()).y);
		//splitPane.updateUI();
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

		JScrollPane hostingScrollPane = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, this);

		if(hostingScrollPane != null && getRowCount() > 0) {
			List<TreePath> visiblenode = getVisibleNodes(hostingScrollPane);
			List<SplitPaintData> splitPaintData = null;
			if(position != UNASSIGNED) splitPaintData = new ArrayList<>();

			if(visiblenode==null) {
				super.paintComponent(g);
				return;
			}

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
				tempSplitPaintData.isleft = (position == LEFT);
				tempSplitPaintData.startposition = this.getRowBounds(i).y;
				tempSplitPaintData.ohterheight = tempSplitPaintData.height = this.getRowBounds(i).height;

				//exception working diff
				if (temp.other != null) {
					if(linkedTree.getPathBounds(temp.other)== null) {
						Log.d(linkedTree.getPathBounds(temp.other) + ": " + temp.other);
						tempSplitPaintData.endposition = tempSplitPaintData.startposition;
						super.paintComponent(g);
						return;
					} else {
						tempSplitPaintData.endposition = linkedTree.getPathBounds(temp.other).y;
					}
				} else {
					if (position == LEFT) {
						for (int j = i; j >= 0; j--) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							DiffTreeUserData filetemp = (DiffTreeUserData) nodetemp.getUserObject();
							if (filetemp.other != null) {
								// Log.d(i + " : " + j);
								tempSplitPaintData.endposition = linkedTree.getPathBounds(filetemp.other).y;
								tempSplitPaintData.ohterheight = linkedTree.getPathBounds(filetemp.other).height;
								break;
							}
						}
					} else {
						boolean found = false;
						DiffTreeUserData filetemp = null;
						for (int j = i; j < getRowCount(); j++) {
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							filetemp = (DiffTreeUserData) nodetemp.getUserObject();

							// Log.d("i = " + i + filetemp);
							if (filetemp.other != null) {
								tempSplitPaintData.endposition = linkedTree.getPathBounds(filetemp.other).y
										- linkedTree.getPathBounds(filetemp.other).height;
								tempSplitPaintData.ohterheight = linkedTree.getPathBounds(filetemp.other).height;

								 //Log.d(filetemp.toString());
								// tempSplitPaintData
								 found = true;
								break;
							}
						}
						if(!found) {
							tempSplitPaintData.endposition = linkedTree.getRowBounds(linkedTree.getRowCount()-1).y;
							tempSplitPaintData.ohterheight = linkedTree.getRowBounds(linkedTree.getRowCount()-1).height;
						}

					}
					// tempSplitPaintData.endposition = 0;
				}
				if (tempSplitPaintData.endposition != 0 && splitPaintData != null) {
					if(splitPaintData.size() <= tempSplitPaintData.index) {
						splitPaintData.add(tempSplitPaintData);
					} else {
						splitPaintData.set(tempSplitPaintData.index, tempSplitPaintData);
					}
				}
			}

			JSplitPane splitPane = (JSplitPane) SwingUtilities.getAncestorOfClass(JSplitPane.class, this);
			if(splitPane != null) {
				splitPane.putClientProperty(position == LEFT ?
						SplitPaintData.LEFT_DATA : SplitPaintData.RIGHT_DATA, splitPaintData);
			}
		}

		super.paintComponent(g);
	}

	private List<TreePath> getVisibleNodes(JScrollPane hostingScrollPane){
		//Find the first and last visible row within the scroll pane.
		final Rectangle visibleRectangle = hostingScrollPane.getViewport().getViewRect();
		final int firstRow = getClosestRowForLocation(visibleRectangle.x, visibleRectangle.y);
		final int lastRow  = getClosestRowForLocation(visibleRectangle.x, visibleRectangle.y + visibleRectangle.height);
		//Iterate through each visible row, identify the object at this row, and add it to a result list.
		List<TreePath> resultList = new ArrayList<TreePath>();
		for (int currentRow = firstRow; currentRow<=lastRow; currentRow++){
			TreePath currentPath = getPathForRow(currentRow);
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

			if (node.getUserObject() instanceof DiffTreeUserData) {
				DiffTreeUserData temp = (DiffTreeUserData) node.getUserObject();

				if (temp instanceof ImagePassKeyDiffTreeUserData) {
					l.setIcon(((ImagePassKeyDiffTreeUserData) temp).getImageIcon());
				} else if (temp instanceof ImageDiffTreeUserData) {
					l.setIcon(((ImageDiffTreeUserData) temp).getImageIcon());
				} else if (temp instanceof RootDiffTreeUserData) {
					l.setIcon(new ImageIcon(rooticon));
				} else if (temp.isfolder) {
					l.setIcon(new ImageIcon(foldericon));
				}
				if (painting) {
					Color nodecolor = ((DiffTree) tree).getnodeColor(row, node, selected, false);
					// l.setBackground(new Color(0,0,0,0));
					l.setBackground(nodecolor);
					if (selected && tree.isFocusOwner())
						l.setForeground(Color.WHITE);
					else
						l.setForeground(Color.BLACK);

					l.setOpaque(true);
					return l;
				}
			}
			return l;
		}
	}

	private Dimension getSuperPreferredSize() {
		return super.getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension dim = super.getPreferredSize();
		int marginHeight = linkedTree.getSuperPreferredSize().height
				- getSuperPreferredSize().height;
		if(marginHeight > 0) dim.height += marginHeight;
		return dim; 
	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		//Log.d("ex");
		//		Log.d(event + "");
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp = (DiffTreeUserData) node.getUserObject();
			linkedTree.expandPath(temp.other);
			//scrollpane.revalidate();
			//scrollpane.repaint();
		}
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		TreePath path = event.getPath();
		//Log.d("col");
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();

		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
			linkedTree.collapsePath(temp.other);
			//scrollpane.revalidate();
			//scrollpane.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// Log.d("click : " + e.getClickCount());
		if (SwingUtilities.isLeftMouseButton(e)) {
			int closestRow = getClosestRowForLocation(e.getX(), e.getY());
			Rectangle closestRowBounds = getRowBounds(closestRow);

			if (e.getY() >= closestRowBounds.getY()
					&& e.getY() < closestRowBounds.getY() + closestRowBounds.getHeight()) {
				if (e.getX() > closestRowBounds.getX() && closestRow < getRowCount()) {

					if (e.getClickCount() == 1) {
						setSelectionRow(closestRow);
					} else if (e.getClickCount() == 2 && getpaintingFlag()) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) (getSelectionPath()
								.getLastPathComponent());
						DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
						if ((!node.isLeaf() || temp.isfolder) && !node.isRoot() ) {
							if (isCollapsed(closestRow)) {
								expandRow(closestRow);
							} else {
								collapseRow(closestRow);
							}
						}
					}
				}
			} // else
				// temp.setSelectionRow(-1);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(actListener == null) return;
		if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2 && getpaintingFlag()) {
			if (getPathForLocation(e.getX(), e.getY()) == null) return;
			actListener.actionPerformed(new ActionEvent(e.getSource(), ActionEvent.ACTION_PERFORMED,
					UiEventHandler.ACT_CMD_OEPN_DIFFTREE_FILE, e.getWhen(), e.getModifiersEx()));
		}
	}

	@Override public void mouseReleased(MouseEvent e) { }
	@Override public void mouseEntered(MouseEvent e) { }
	@Override public void mouseExited(MouseEvent e) {	}
}

