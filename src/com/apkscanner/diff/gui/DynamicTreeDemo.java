package com.apkscanner.diff.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Position;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkscanner.diff.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.apkscanner.util.Log;

public class DynamicTreeDemo extends JPanel implements ActionListener, TreeSelectionListener  {

    protected static final SimpleDateFormat SDF = new SimpleDateFormat("HH:mm:ss");
    
	
//    public static final Color diffcolor = new Color(0x8E89FF);
//	public static final Color addcolor = new Color(0xFFFFA1);
	
	public static final Color diffcolor = new Color(224,224,255);
	public static final Color diffcolorselect = new Color(96,107,192);
	public static final Color addcolor = new Color(255,247,217);
	public static final Color addcolorselect = new Color(190,150,30);
	public static final Color nomal = Color.white;
	public static final Color nomalselect = new Color(109,120,128);
	
	public static final Color[] colorarray = {Color.lightGray, nomalselect, addcolor, addcolorselect, diffcolor, diffcolorselect};
	//public static final Color[] focuscolorarray = {Color.lightGray, nomalselect, addcolor, addcolorselect, diffcolor, diffcolorselect};
	
	
    private SortNode leftrootNode;
    private SortNode rightrootNode;
    
    private final DefaultTreeModel lefttreeModel;
    private final DefaultTreeModel righttreeModel;

    private static ColorTree left;
    private static ColorTree right;
    private static JTree selectedtree;
    
    JScrollPane scrollpane;
    JScrollPane leftscrollBar;
    JScrollPane rightscrollBar;
    
    static JSplitPaneWithZeroSizeDivider splitPane;

    public DynamicTreeDemo() {
        super(new BorderLayout());

        leftrootNode = new SortNode("temp");
        lefttreeModel = new DefaultTreeModel(leftrootNode);

        rightrootNode = new SortNode("temp");
        righttreeModel = new DefaultTreeModel(rightrootNode);

        splitPane = new JSplitPaneWithZeroSizeDivider();
        
		FocusListener fl = new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {
				e.getComponent().repaint();
			}

			@Override
			public void focusLost(FocusEvent e) {
				e.getComponent().repaint();
			}
		};
		DefaultTreeCellRenderer r = new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
					boolean leaf, int row, boolean hasFocus) {
				JLabel l = (JLabel) super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, false);
				
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
				
				if(node.getUserObject() instanceof FileNode) {
					FileNode temp = (FileNode)node.getUserObject();
					Color nodecolor = ((ColorTree) tree).getnodeColor(row,node, selected);
					//l.setBackground(new Color(0,0,0,0));
					l.setBackground(nodecolor);
					if(selected && selectedtree == tree) l.setForeground(Color.WHITE);
					else l.setForeground(Color.BLACK);
					
					l.setOpaque(true);
				}
				return l;
			}
		};
		
		createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp"), leftrootNode);
    	createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp2"), rightrootNode);
        
        left = new ColorTree(lefttreeModel);
        //left.setCellRenderer(new LeftTreeCellRenderer());
        right = new ColorTree(righttreeModel);
        //right.setCellRenderer(new RightTreeCellRenderer());
        
        left.addFocusListener(fl);
		left.setCellRenderer(r);
		left.setOpaque(false);
		left.addTreeSelectionListener(this);
		left.setRootVisible(false);
		//left.setShowsRootHandles(true);
		left.setBorder(BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ));
		left.setToggleClickCount(0);
		
		//Border border = BorderFactory.createLineBorder(Color.WHITE);
		//Border border = BorderFactory.createLineBorder(Color.BLACK, 3);
		
		right.addFocusListener(fl);
		right.setCellRenderer(r);
		right.setOpaque(false);
		right.addTreeSelectionListener(this);
		right.setRootVisible(false);
		right.setBorder(BorderFactory.createEmptyBorder ( 2, 2, 2, 2 ));
		right.setToggleClickCount(0);
		
        for (int row=right.getRowCount(); row>=0; row--) {
        	right.expandRow(row);
        }
        for (int row=left.getRowCount(); row>=0; row--) {
        	left.expandRow(row);
        }
		
        MyExpansionListener expansionListener = new MyExpansionListener(left, right);

        left.addTreeExpansionListener(expansionListener);
        right.addTreeExpansionListener(expansionListener);

        //right.setSelectionModel(left.getSelectionModel());

        for (final JTree t : Arrays.asList(left, right)) {
			MouseAdapter fRowSelectionListener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					//Log.d("click : " + e.getClickCount());
					if (SwingUtilities.isLeftMouseButton(e)) {
						int closestRow = t.getClosestRowForLocation(e.getX(), e.getY());
						Rectangle closestRowBounds = t.getRowBounds(closestRow);
						
						
						if (e.getY() >= closestRowBounds.getY() && e.getY() < closestRowBounds.getY() + closestRowBounds.getHeight()) {
							if (e.getX() > closestRowBounds.getX() && closestRow < t.getRowCount()) {
								if(e.getClickCount() == 1) {									
									t.setSelectionRow(closestRow);
									if(left == t) {
										selectedtree = left;
									} else {
										selectedtree = right;
									}
								} else if(e.getClickCount() == 2){
									DefaultMutableTreeNode node = (DefaultMutableTreeNode)(t.getSelectionPath().getLastPathComponent());
									if(!node.isLeaf()) {
										if(t.isCollapsed(closestRow)) {
											t.expandRow(closestRow);
										} else {
											t.collapseRow(closestRow);
										}
									}
								}								
							}
						} else
							t.setSelectionRow(-1);
					}
				}	
			};			
			t.addMouseListener(fRowSelectionListener);
        }        
    	
    	mappingtree(leftrootNode, right);
    	mappingtree(rightrootNode, left);
    	setfolderstate(leftrootNode);
    	setfolderstate(rightrootNode);
//        for (int row=right.getRowCount(); row>=0; row--) {
//        	right.collapseRow(row);
//        }

    	//JScrollPane leftcom = new JScrollPane(left);
    	
    	//JScrollPane rightcom = new JScrollPane(right);
        
        leftscrollBar = new JScrollPane(left,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        //leftscrollBar.setViewportBorder(null);
        
        rightscrollBar = new JScrollPane(right,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	
		//splitPane.setBorder(null);
		splitPane.setDividerLocation(400);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		
		scrollpane = new JScrollPane(splitPane);
		scrollpane.getVerticalScrollBar().setUnitIncrement(10);
		//scrollpane = new JScrollPane(splitPane);
                
		//scrollpane.setBorder(BorderFactory.createEmptyBorder());
		
		add(scrollpane, BorderLayout.CENTER);
		splitPane.setResizeWeight(0.5);
    }

    private void setfolderstate(SortNode rootmynode) {
    	
    	Enumeration<DefaultMutableTreeNode> myreenode = rootmynode.depthFirstEnumeration();
    	
    	while (myreenode.hasMoreElements()) {
    		DefaultMutableTreeNode mynode = myreenode.nextElement();
    		if((mynode.getUserObject() instanceof FileNode)) {
	    		FileNode temp = (FileNode)mynode.getUserObject();
	    		if(temp.other == null) {
	    			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(new TreePath(mynode.getParent()).getLastPathComponent());
	    			FileNode parenttemp = (FileNode)parent.getUserObject();
	    			parenttemp.setState(FileNode.NODE_STATE_ADD);
	    			
	    			FileNode parentothertemp = (FileNode)((DefaultMutableTreeNode)(parenttemp.other.getLastPathComponent())).getUserObject();
	    			parentothertemp.setState(FileNode.NODE_STATE_ADD);
	    			
	    		}
    		}
    	}   	
    }
    
    private void mappingtree(SortNode rootmynode, JTree othertree) {
    	
    	Enumeration<DefaultMutableTreeNode> myreenode = rootmynode.depthFirstEnumeration();
    	TreePath samebefore = null;
    	
    	while (myreenode.hasMoreElements()) {
    		DefaultMutableTreeNode mynode = myreenode.nextElement();
    		
    		if((mynode.getUserObject() instanceof FileNode)) {
    			if((((FileNode)mynode.getUserObject()).me != null)) {
    				//samebefore = ((FileNode)leftnode.getUserObject()).other;
    				continue;
    			}
    		}
    		
    		TreePath leftTreepath = new TreePath(mynode.getPath()); 
            String str = leftTreepath.toString();
            
            int spaceindex = str.indexOf("[");
            if(spaceindex > -1) {
            	str = str.substring(spaceindex+1, str.indexOf("]"));            	
            	//Log.d(str);
            	
            	String[] parts = str.split(", ");
            	
            	TreePath temppath = findByName(othertree, parts); 
            	//Log.d("" + temppath);
            	//exist same object
            	if(temppath!=null ) {
            		//samebefore = temppath;
            		//left            		
            		if(mynode.getUserObject() instanceof FileNode) {
            			FileNode temp = (FileNode)mynode.getUserObject();
            			temp.setotherTreepath(temppath);
	            		temp.setmeTreepath(new TreePath(mynode.getPath()));
	            		temp.setState(FileNode.NODE_STATE_NOMAL);
            		}            		
            		//right
        			DefaultMutableTreeNode node = (DefaultMutableTreeNode)temppath.getLastPathComponent();            			
        			if(node.getUserObject() instanceof FileNode) {
        				FileNode temp = (FileNode)node.getUserObject();
	            		temp.setotherTreepath(leftTreepath);
	            		temp.setmeTreepath(temppath);
	            		temp.setState(FileNode.NODE_STATE_NOMAL);
        			}
        		} else { //added
            		if(mynode.getUserObject() instanceof FileNode) {
            			FileNode temp = (FileNode)mynode.getUserObject();
            			temp.setotherTreepath(samebefore);
	            		temp.setmeTreepath(new TreePath(mynode.getPath()));
	            		temp.setState(FileNode.NODE_STATE_ADD);
            		}        			
        		}
            }
    	}        	
    }
    
    public TreePath findByName(JTree tree, String[] names) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        return find2(tree, new TreePath(root), names, 0, true);
    }
    private TreePath find2(JTree tree, TreePath parent, Object[] nodes, int depth, boolean byName) {
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        Object o = node;

        // If by name, convert node to a string
        if (byName) {
            o = o.toString();
        }

        // If equal, go down the branch
        if (o.equals(nodes[depth])) {
            // If at end, return match
            if (depth == nodes.length-1) {
                return parent;
            }

            // Traverse children
            if (node.getChildCount() >= 0) {
                for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                    TreeNode n = (TreeNode)e.nextElement();
                    TreePath path = parent.pathByAddingChild(n);
                    TreePath result = find2(tree, path, nodes, depth+1, byName);
                    // Found a match
                    if (result != null) {
                        return result;
                    }
                }
            }
        }

        // No match at this branch
        return null;
    }
    
    public boolean findText(String nodes) {
        String[] parts = nodes.split(", ");
        TreePath path = null;
        //parts[0] = "temp2";//rightrootNode.getUserObject().toString();
        
        for (String part : parts) {
        	
            int row = (path==null ? 0 : right.getRowForPath(path));
            path = right.getNextMatch(part, row, Position.Bias.Forward);
        
            Log.d("find : " + part + "   row : "  + row);
            
            if (path==null) {
                return false;
            }
        }
        //left.scrollPathToVisible(path);
        //left.setSelectionPath(path);

        return path!=null;
    }
    
    private void createChildren(File fileRoot, SortNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null) return;

        for (File file : files) {
        	SortNode childNode = 
                    new SortNode(new FileNode(file));
            node.add(childNode);
            if (file.isDirectory()) {
                createChildren(file, childNode);
            }
        }
    }
    
    public class FileNode {
        private File file;
        TreePath me;
        TreePath other = null;
        int state = NODE_STATE_NOMAL;
        
        public static final int NODE_STATE_NOMAL = 0;
        public static final int NODE_STATE_ADD = 2;
        public static final int NODE_STATE_DIFF = 4;
                
        
        public FileNode(File file) {
            this.file = file;
        }
        public void setmeTreepath(TreePath path) {
        	this.me = path;
        }
        public void setotherTreepath(TreePath path) {
        	this.other = path;
        }
                
        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
        
        public void setState(int state) {
        	this.state = state;
        }
        
        public int getState() {
        	return this.state;
        }
        
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */
    private static void createAndShowGUI() {
        // Create and set up the window.
        JFrame frame = new JFrame("APK Scanner - Diff");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create and set up the content pane.
        DynamicTreeDemo newContentPane = new DynamicTreeDemo();
        newContentPane.setOpaque(true); // content panes must be opaque
        
        frame.setContentPane(newContentPane);
        frame.setSize(1000, 800);
        
        frame.setLocationRelativeTo(null);
        // Display the window.
        //frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
    
	class SortNode extends DefaultMutableTreeNode {
		public SortNode(Object userObject) {
			super(userObject);
		}

		
		@Override
		public void add(MutableTreeNode newChild) {
			super.add(newChild);
			sort();// add to tree and sort immediately use in case the model is
					// small if large comment it and and call node.sort once
					// you've added all the children
		}

		public void sort() {
			Collections.sort(children, compare());
		}

		private Comparator compare() {
			return new Comparator<DefaultMutableTreeNode>() {
				@Override
				public int compare(DefaultMutableTreeNode o1, DefaultMutableTreeNode o2) {
					return o1.getUserObject().toString().compareTo(o2.getUserObject().toString());
				}
			};
		}
	}
    
	static class ColorTree extends JTree {
		private static final Color SELC = Color.RED;
		public ColorTree(DefaultTreeModel treeModel) {
			// TODO Auto-generated constructor stub
			super(treeModel);
		}
		
		public Color getnodeColor(int row, DefaultMutableTreeNode node, boolean selected) {
	    	FileNode temp = (FileNode)node.getUserObject();
	    	
	    	if(isExpanded(row) && !node.isLeaf()){
				if(selected) {
					if(selectedtree == this) {
						return colorarray[FileNode.NODE_STATE_NOMAL+1];
					} else{
						return colorarray[FileNode.NODE_STATE_NOMAL];
					}
				} else {
					return Color.white;
				}
			} else {
				if(selected) {
					if(selectedtree == this) {
						return colorarray[temp.getState()+1];					
					} else {
						return colorarray[temp.getState()];								
					}
				} else {
					if(temp.state == FileNode.NODE_STATE_NOMAL) {
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
				FileNode temp = (FileNode)node.getUserObject();
				
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
					
					ColorTree tempothertree = (left==this) ? right : left;
					
					if(temp.other != null) {
						tempSplitPaintData.endposition = tempothertree.getPathBounds(temp.other).y;
					} else {
						for(int j=i; j >=0; j--) {							
							Object otemp = getPathForRow(j).getLastPathComponent();
							DefaultMutableTreeNode nodetemp = (DefaultMutableTreeNode) otemp;
							FileNode filetemp = (FileNode)nodetemp.getUserObject();
							if(filetemp.other != null) {
								//Log.d(i + "  :  " + j);
								tempSplitPaintData.endposition = tempothertree.getPathBounds(filetemp.other).y;
								break;
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
	}
    
    public class MyExpansionListener implements TreeExpansionListener {
        private JTree left;
        private JTree right;

        public MyExpansionListener(JTree left, JTree right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath path = event.getPath();
            //Log.d("ex");
//            Log.d(event + "");
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();    
    		
    		if(node.getUserObject() instanceof FileNode) {
    			FileNode temp = (FileNode)node.getUserObject();
    			if (event.getSource() == left) {
    				right.expandPath(temp.other);
    			} else {
    				left.expandPath(temp.other);
    			}
    			scrollpane.revalidate();
    			scrollpane.repaint();
    		}
        }

        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
            TreePath path = event.getPath();
            //Log.d("col");
            DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();    
    		
    		if(node.getUserObject() instanceof FileNode) {
    			FileNode temp = (FileNode)node.getUserObject();
    			if (event.getSource() == left) {
    				right.collapsePath(temp.other);
    			} else {
    				left.collapsePath(temp.other);
    			}
    			scrollpane.revalidate();
    			scrollpane.repaint();
    		}
        }
    }

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub		
		if(((JTree)e.getSource()).getSelectionPath() == null) return;
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
		
		if(node.getUserObject() instanceof FileNode) {
			FileNode temp =(FileNode)node.getUserObject();

//			Log.d("is expended : " + ((JTree)e.getSource()).isExpanded(new TreePath(node.getPath())));
			
			if(left == (JTree)e.getSource()) {
				right.setSelectionPath(temp.other);
//				Log.d("lefttree state : "+ temp.state + "   me : " + temp.me + "   other : " + temp.other + " isLeaf : " + node.isLeaf());
			} else {				
				left.setSelectionPath(temp.other);
//				Log.d("righttree state : "+ temp.state + "   me : " + temp.me + "   other : " + temp.other + " isLeaf : " + node.isLeaf());
			}
			splitPane.repaint();
		}		
		//splitPane.setheight(left.getUI().getPathBounds(left, left.getSelectionPath()).y);
		//splitPane.updateUI();
	}
}