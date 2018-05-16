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
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkscanner.diff.gui.DynamicTreeDemo.FileNode;
import com.apkscanner.diff.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.apkscanner.util.Log;

public class DynamicTreeDemo extends JPanel implements ActionListener, TreeSelectionListener{
	
    private DiffTree left;
    private DiffTree right;
    
    static private SortNode leftrootNode;
    static private SortNode rightrootNode;
    
    private final DefaultTreeModel lefttreeModel;
    private final DefaultTreeModel righttreeModel;
    
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

		
		createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp"), leftrootNode);
    	createChildren(new File("/media/leejinhyeong/Perforce/DCM_APP_DEV_LJH_DEV/OHIO/Cinnamon/applications/provisional/temp2"), rightrootNode);
        
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
		
		add(scrollpane, BorderLayout.CENTER);
		
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
    
    public class FileNode extends Object{
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
}