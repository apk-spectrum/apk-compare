package com.apkscanner.diff.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import java.util.ArrayList;
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
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkscanner.core.scanner.AaptScanner;
import com.apkscanner.core.scanner.ApkScanner;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.diff.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.apkscanner.util.Log;
import com.sun.corba.se.impl.orbutil.graph.Node;

public class DynamicTreeDemo extends JPanel implements ActionListener, TreeSelectionListener{
	
    private DiffTree left;
    private DiffTree right;
    
    private SortNode leftrootNode;
    private SortNode rightrootNode;
    
    private DefaultTreeModel lefttreeModel;
    private DefaultTreeModel righttreeModel;
    
    private JScrollPane scrollpane;
    private JScrollPane leftscrollBar;
    private JScrollPane rightscrollBar;
    
    JSplitPaneWithZeroSizeDivider splitPane;

	private ApkInfo apkinfodiff1, apkinfodiff2;
	
    
    public DynamicTreeDemo() {
        super(new BorderLayout());

        leftrootNode = new SortNode(new DiffTreeUserData("temp"));
        lefttreeModel = new DefaultTreeModel(leftrootNode);

        rightrootNode = new SortNode(new DiffTreeUserData("temp"));
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

		
		//createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp"), leftrootNode);
    	//createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp2"), rightrootNode);
        
        left = new DiffTree(lefttreeModel);
        //left.setCellRenderer(new LeftTreeCellRenderer());
        right = new DiffTree(righttreeModel);
        //right.setCellRenderer(new RightTreeCellRenderer());
                
        left.addFocusListener(fl);
		left.addTreeSelectionListener(this);
		
		right.addFocusListener(fl);
		right.addTreeSelectionListener(this);
		
        for (int row=right.getRowCount(); row>=0; row--) {
        	right.expandRow(row);
        }
        for (int row=left.getRowCount(); row>=0; row--) {
        	left.expandRow(row);
        }
		
        MyExpansionListener expansionListener = new MyExpansionListener(left, right);

        left.addTreeExpansionListener(expansionListener);
        right.addTreeExpansionListener(expansionListener);
        
        
        DiffTree.setleftrighttree(left,right);
        DiffTree.setJSplitPane(splitPane);
        //right.setSelectionModel(left.getSelectionModel());

//        for (int row=right.getRowCount(); row>=0; row--) {
//        	right.collapseRow(row);
//        }
		for (final JTree t : Arrays.asList(left, right)) {
			MouseAdapter fRowSelectionListener = new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					// Log.d("click : " + e.getClickCount());
					if (SwingUtilities.isLeftMouseButton(e)) {
						int closestRow = t.getClosestRowForLocation(e.getX(), e.getY());
						Rectangle closestRowBounds = t.getRowBounds(closestRow);

						if (e.getY() >= closestRowBounds.getY()
								&& e.getY() < closestRowBounds.getY() + closestRowBounds.getHeight()) {
							if (e.getX() > closestRowBounds.getX() && closestRow < t.getRowCount()) {
								if (e.getClickCount() == 1) {
									
									t.setSelectionRow(closestRow);
									if (left == t) {
										DiffTree.setSelectedtree(left);
									
									} else {
										DiffTree.setSelectedtree(right);
									
									}
									splitPane.repaint();
									
								} else if (e.getClickCount() == 2) {
									DefaultMutableTreeNode node = (DefaultMutableTreeNode) (t.getSelectionPath()
											.getLastPathComponent());
									if (!node.isLeaf()) {
										if (t.isCollapsed(closestRow)) {
											t.expandRow(closestRow);
										} else {
											t.collapseRow(closestRow);
										}
									}
								}
							}
						} // else
							// temp.setSelectionRow(-1);
					}
				}
			};
			t.addMouseListener(fRowSelectionListener);
		}
        
        leftscrollBar = new JScrollPane(left,
        JScrollPane.VERTICAL_SCROLLBAR_NEVER,
        JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        
        rightscrollBar = new JScrollPane(right,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    	
		splitPane.setDividerLocation(400);
		splitPane.setLeftComponent(left);
		splitPane.setRightComponent(right);
		splitPane.setResizeWeight(0.5);
		scrollpane = new JScrollPane(splitPane);
		scrollpane.getVerticalScrollBar().setUnitIncrement(10);
		expansionListener.setSrollpane(scrollpane);
		
		JPanel temppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temppanel.setPreferredSize(new Dimension(0, 50));
		
		temppanel.add(new JButton("aaaaa"));
		temppanel.add(new JButton("aaaaa"));
		temppanel.add(new JButton("aaaaa"));
		
		add(temppanel, BorderLayout.NORTH);
		add(scrollpane, BorderLayout.CENTER);
		
    }
    public void createTreeNode(ApkInfo apkinfodiff1, ApkInfo apkinfodiff2) {
    	Log.d("createTreeNode");
    	
    	DiffMappingTree.createTree(apkinfodiff1, leftrootNode);
    	lefttreeModel.reload();
    	
    	DiffMappingTree.createTree(apkinfodiff2, rightrootNode);
    	righttreeModel.reload();
    	
    	mappingtree(leftrootNode, right);
    	mappingtree(rightrootNode, left);
    	setfolderstate(leftrootNode);
    	setfolderstate(rightrootNode);
    	
    	repaint();
    	//DiffMappingTree.createTree(apkinfodiff2, rightrootNode);
    }
    
    
    private void setfolderstate(SortNode rootmynode) {
    	
    	Enumeration<DefaultMutableTreeNode> myreenode = rootmynode.depthFirstEnumeration();
    	
    	while (myreenode.hasMoreElements()) {
    		DefaultMutableTreeNode mynode = myreenode.nextElement();
    		if((mynode.getUserObject() instanceof DiffTreeUserData)) {
    			DiffTreeUserData temp = (DiffTreeUserData)mynode.getUserObject();
    			
    			//int tempstate = DiffTreeUserData.NODE_STATE_NOMAL;
    			
	    		if(temp.state != DiffTreeUserData.NODE_STATE_NOMAL && mynode.getParent()!= null) {	    			
	    			DefaultMutableTreeNode parent = (DefaultMutableTreeNode)(new TreePath(mynode.getParent()).getLastPathComponent());
	    			DiffTreeUserData parenttemp = (DiffTreeUserData)parent.getUserObject();
	    			parenttemp.setState(temp.state);
	    				    			
	    			if(parenttemp.other != null) {
		    			DiffTreeUserData parentothertemp = (DiffTreeUserData)((DefaultMutableTreeNode)(parenttemp.other.getLastPathComponent())).getUserObject();
		    			parentothertemp.setState(temp.state);
	    			}
	    		}
    		}
    	}   	
    }
    
    private void mappingtree(SortNode rootmynode, JTree othertree) {
    	
    	Enumeration<DefaultMutableTreeNode> myreenode = rootmynode.depthFirstEnumeration();
    	TreePath samebefore = null;
    	
    	while (myreenode.hasMoreElements()) {
    		DefaultMutableTreeNode mynode = myreenode.nextElement();
    		
    		if((mynode.getUserObject() instanceof DiffTreeUserData)) {
    			if((((DiffTreeUserData)mynode.getUserObject()).me != null)) {
    				continue;
    			}
    		}
    		
    		TreeNode[] path = mynode.getPath();
    		String[] patharray = new String[path.length]; 
    		for (int i=path.length-1; i>=0;i--) { 
    			patharray[i]= path[i].toString();
    		}
    		
    		TreePath leftTreepath = new TreePath(mynode.getPath()); 
            String str = leftTreepath.toString();
            
//            int spaceindex = str.indexOf("[");
//            if(spaceindex > -1) {
//            	str = str.substring(spaceindex+1, str.indexOf("]"));
//            	//Log.d(str);
//            	
//            	String[] parts = str.split(", ");
            	
            	TreePath temppath = findByName(othertree, patharray, ((DiffTreeUserData)mynode.getUserObject()).Key); 
            	//Log.d("" + temppath);
            	//exist same object
            	if(temppath!=null ) {
            		//samebefore = temppath;
            		//left
            		int tempstate = DiffTreeUserData.NODE_STATE_NOMAL;
            		if(mynode.getUserObject() instanceof DiffTreeUserData) {
            			DiffTreeUserData temp = (DiffTreeUserData)mynode.getUserObject();
            			temp.setotherTreepath(temppath);
	            		temp.setmeTreepath(new TreePath(mynode.getPath()));
	            		if(temp.Key.length() > 0 && !getUserDatabyTreePath(temppath).toString().equals(temp.toString())) {
	            			tempstate = DiffTreeUserData.NODE_STATE_DIFF;	            			
	            		}	            		
            			temp.setState(tempstate);
            		}
            		//right
        			DefaultMutableTreeNode node = (DefaultMutableTreeNode)temppath.getLastPathComponent();            			
        			if(node.getUserObject() instanceof DiffTreeUserData) {
        				DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
	            		temp.setotherTreepath(leftTreepath);
	            		temp.setmeTreepath(temppath);
            			temp.setState(tempstate);
        			}       			
        			
        		} else { //added
            		if(mynode.getUserObject() instanceof DiffTreeUserData) {
            			DiffTreeUserData temp = (DiffTreeUserData)mynode.getUserObject();
            			temp.setotherTreepath(samebefore);
	            		temp.setmeTreepath(new TreePath(mynode.getPath()));
	            		temp.setState(DiffTreeUserData.NODE_STATE_ADD);
            		}        			
        		}
//            }
    	}        	
    }
    public DiffTreeUserData getUserDatabyTreePath(TreePath path) {
    	DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();            			
		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
			return temp;
		}
		return null;
    }
    
    public TreePath findByName(JTree tree, String[] names, String key) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
        return find2(tree, new TreePath(root), names, 0, true, key);
    }
    private TreePath find2(JTree tree, TreePath parent, Object[] nodes, int depth, boolean byName, String key) {
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
                    
                    DefaultMutableTreeNode tempNode = (DefaultMutableTreeNode)path.getLastPathComponent();
                    
                    if(tempNode.getUserObject() instanceof DiffTreeUserData) {
                    	DiffTreeUserData temp = (DiffTreeUserData)tempNode.getUserObject();
                    	//Log.d(temp.Key + ":" + key);
                    	
                    	if(temp.Key.equals(key) && temp.Key.length() > 0) {
                        	return path;
                        }
					}
                    
                    TreePath result = find2(tree, path, nodes, depth+1, byName, key);
                    
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

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

    }

    /**
     * Create the GUI and show it. For thread safety, this method should be
     * invoked from the event-dispatching thread.
     */


	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub		
		if(((JTree)e.getSource()).getSelectionPath() == null) return;
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.getPath().getLastPathComponent();
		
		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp =(DiffTreeUserData)node.getUserObject();

//			Log.d("is expended : " + ((JTree)e.getSource()).isExpanded(new TreePath(node.getPath())));
			
			if(left == (JTree)e.getSource()) {
				right.setSelectionPath(temp.other);
				//Log.d("lefttree state : "+ temp.state + "   me : " + temp.me + "   other : " + temp.other + " isLeaf : " + node.isLeaf() + " key : " + temp.Key);
			} else {				
				left.setSelectionPath(temp.other);
				//Log.d("righttree state : "+ temp.state + "   me : " + temp.me + "   other : " + temp.other + " isLeaf : " + node.isLeaf()+ " key : " + temp.Key);
			}
			splitPane.repaint();
		}		
		//splitPane.setheight(left.getUI().getPathBounds(left, left.getSelectionPath()).y);
		//splitPane.updateUI();
	}
	
	public class MyExpansionListener implements TreeExpansionListener {
	    private JTree left;
	    private JTree right;
	    JScrollPane scrollpane;
	    public MyExpansionListener(JTree left, JTree right) {
	        this.left = left;
	        this.right = right;
	    }

	    public void setSrollpane(JScrollPane scrollpane) {
	    	this.scrollpane = scrollpane;
	    }
	    
	    @Override
	    public void treeExpanded(TreeExpansionEvent event) {
	        TreePath path = event.getPath();
	        //Log.d("ex");
//	        Log.d(event + "");
	        DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();    
			
			if(node.getUserObject() instanceof DiffTreeUserData) {
				DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
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
			
			if(node.getUserObject() instanceof DiffTreeUserData) {
				DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
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
}