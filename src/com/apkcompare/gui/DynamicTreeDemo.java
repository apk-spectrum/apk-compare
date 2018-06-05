package com.apkcompare.gui;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
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
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
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

import com.apkscanner.Launcher;
import com.apkscanner.core.scanner.AaptScanner;
import com.apkscanner.core.scanner.ApkScanner;
import com.apkscanner.core.scanner.ApkScanner.Status;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.gui.MainUI;
import com.apkscanner.gui.ToolBar.ButtonSet;
import com.apkscanner.gui.messagebox.MessageBoxPane;
import com.apkcompare.gui.dialog.SettingDlg;
import com.apkcompare.gui.dialog.AboutDlg;
import com.apkscanner.gui.util.ApkFileChooser;
import com.apkscanner.gui.util.FileDrop;
import com.apkscanner.resource.Resource;
import com.apkcompare.gui.DiffMain.ApkScannerDiffListener;
import com.apkcompare.gui.JSplitPaneWithZeroSizeDivider.SplitPaintData;
import com.apkscanner.util.Log;
import com.apkscanner.util.SystemUtil;
import com.apkcompare.ApkComparer;
import com.apkcompare.Main;
import com.apkcompare.data.FilePassKeyDiffTreeUserData;
import com.apkcompare.data.RootDiffTreeUserData;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.sun.corba.se.impl.orbutil.graph.Node;
import com.sun.corba.se.impl.protocol.BootstrapServerRequestDispatcher;
public class DynamicTreeDemo extends JPanel implements ActionListener, TreeSelectionListener{
	
	
	private ApkComparer apkComparer;
    
    private SortNode[] arraytreeNode = {null, null};
    private DiffTree[] arrayTree = {null, null};
    private FilteredTreeModel[] arrayTreemodel = {null, null};
    private DiffLoadingPanel[] loadingpanel = {null, null};
    private String CurrentmergeapkfilePath[] = {null, null};
    
    private JToggleButton btnadd, btndiff, btniden;
	private JButton btnsetting, btninfo;
	private JTextField[] pathtextfiled = {null, null};
	private JPanel[] cardpanel = {null, null};
	private JButton[] btnfileopen = {null, null};
	
    private int LEFT = 0;
    private int RIGHT = 1;
    private int textfield_height = 28;
    
    private JScrollPane scrollpane;
    private JSplitPaneWithZeroSizeDivider splitPane;
    
	private static String CMD_TOGGLE_ADD = "CMD_TOGGLE_ADD";
	private static String CMD_TOGGLE_EDITOR = "CMD_TOGGLE_EDITOR";
	private static String CMD_TOGGLE_IDEN = "CMD_TOGGLE_IDEN";
	private static String CMD_BUTTON_ABOUT = "CMD_BUTTON_ABOUT";
	
	private static String CMD_BUTTON_SETTING = "CMD_BUTTON_SETTING";
	private static String CMD_BUTTON_FILE_OPEN = "CMD_BUTTON_OPEN";	
	
	private static String CARD_LAYOUT_TREE = "card_tree";
	private static String CARD_LAYOUT_LOADING = "card_loading";
		
	private static Boolean Difflock = false;
	
	
	//https://www.picpng.com/image/scanner-png-38293
	
    public DynamicTreeDemo(ApkComparer apkComparer) {
        super(new BorderLayout());
		this.apkComparer = apkComparer;
		if(apkComparer != null) {			
			apkComparer.setStatusListener(new ApkComparerListener());
		}
		
        arrayTree[LEFT] = new DiffTree();
        arrayTree[RIGHT] = new DiffTree();
		
        initializeTree(arrayTree[LEFT]);
        initializeTree(arrayTree[RIGHT]);
               
        setCardPanel();
        setFileDrop();
        
        MyExpansionListener expansionListener = new MyExpansionListener(arrayTree[0], arrayTree[1]);
        arrayTree[LEFT].addTreeExpansionListener(expansionListener);
        arrayTree[RIGHT].addTreeExpansionListener(expansionListener);

        
        splitPane = new JSplitPaneWithZeroSizeDivider();
        
        DiffTree.setleftrighttree(arrayTree[LEFT],arrayTree[RIGHT]);
        DiffTree.setJSplitPane(splitPane);
    	
		//splitPane.setDividerLocation(400);
		splitPane.setLeftComponent(cardpanel[LEFT]);
		splitPane.setRightComponent(cardpanel[RIGHT]);
		splitPane.setResizeWeight(0.5);
		
		splitPane.setY(textfield_height+1);
		scrollpane = new JScrollPane(splitPane);
		scrollpane.getVerticalScrollBar().setUnitIncrement(10);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(new TreeAdjustmentListener());
		scrollpane.getHorizontalScrollBar().addAdjustmentListener(new TreeAdjustmentListener());
		
		expansionListener.setSrollpane(scrollpane);
		
		DiffTree.setScrollPane(scrollpane);
		
		JPanel temppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temppanel.setPreferredSize(new Dimension(0, 48));
		
		
		//https://www.shareicon.net/diff-94479
		//https://www.shareicon.net/interface-letter-i-info-circle-help-735003
		//https://www.shareicon.net/setting-598385
		
		btnadd = new JToggleButton();
		btndiff = new JToggleButton();
		btniden = new JToggleButton();
		
		btnsetting = new JButton();
		btninfo = new JButton();
		
		btnadd.setIcon(Resource.IMG_DIFF_TOOLBAR_ADD.getImageIcon());
		btndiff.setIcon(Resource.IMG_DIFF_TOOLBAR_EDITOR.getImageIcon());
		btniden.setIcon(Resource.IMG_DIFF_TOOLBAR_IDEN.getImageIcon());
		
		btnsetting.setIcon(Resource.IMG_DIFF_TOOLBAR_SETTING.getImageIcon());
		btninfo.setIcon(Resource.IMG_DIFF_TOOLBAR_INFO.getImageIcon());
		
		btnadd.setActionCommand(CMD_TOGGLE_ADD);
		btndiff.setActionCommand(CMD_TOGGLE_EDITOR);
		btniden.setActionCommand(CMD_TOGGLE_IDEN);
		
		btnsetting.setActionCommand(CMD_BUTTON_SETTING);
		btninfo.setActionCommand(CMD_BUTTON_ABOUT);
		
		for(AbstractButton btn: Arrays.asList(btniden, btnadd, btndiff, btnsetting, btninfo)) {
			//btn.setBorderPainted( false );
			//btn.setContentAreaFilled( false );
			btn.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			btn.setFocusPainted(false);
			btn.addActionListener(this);
			btn.setSelected(true);
			temppanel.add(btn);
		}
		
		setEnableToggleBtn(false);
	    
		add(temppanel, BorderLayout.NORTH);
		add(scrollpane, BorderLayout.CENTER);
		
		repaint();
		

    }
    private void setEnableToggleBtn(boolean enable) {
    	for(JToggleButton btn: Arrays.asList(btniden, btnadd, btndiff)) {
    		btn.setEnabled(enable);
    	}
    	repaint();
    }
    
    class ApkComparerListener implements ApkComparer.StatusListener {
		@Override
		public void onStart(int position) {
			
			loadingpanel[position].setshow(DiffLoadingPanel.LOADING);
		    showCardpanel(CARD_LAYOUT_LOADING, position);		    
		}

		@Override
		public void onSuccess(int position) { }

		@Override
		public void onError(int position, int error) { }

		@Override
		public void onCompleted(int position) {			
			createTreeNode(apkComparer.getApkInfo(position), position);
		}
    }
    
    class TreeAdjustmentListener implements AdjustmentListener {
    	  public void adjustmentValueChanged(AdjustmentEvent evt) {
    	    Adjustable source = evt.getAdjustable();
//    	    if (evt.getValueIsAdjusting()) {
//    	      return;
//    	    }
    	    splitPane.repaint();
    	  }
    }
    
    private void setCardPanel() {
    	JPanel []bordertreepanel = {new JPanel(new BorderLayout()), new JPanel(new BorderLayout())};
    	JPanel []pathpanel = {new JPanel(new BorderLayout()), new JPanel(new BorderLayout())};
    	
    	for(int index=0;index <2; index++) {
	    	cardpanel[index] = new JPanel(new CardLayout());	        
	        loadingpanel[index] = new DiffLoadingPanel();	        
	        pathtextfiled[index] = new JTextField();
	        
	        btnfileopen[index] = new JButton(Resource.IMG_TREE_MENU_OPEN.getImageIcon());
	        btnfileopen[index].setBorder(BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ));
	        btnfileopen[index].setPreferredSize(new Dimension(textfield_height, textfield_height));
	        btnfileopen[index].setActionCommand(CMD_BUTTON_FILE_OPEN);
	        btnfileopen[index].addActionListener(this);
	        
	        pathtextfiled[index].setPreferredSize(new Dimension(0, textfield_height));
	        		
	        pathpanel[index].add(pathtextfiled[index], BorderLayout.CENTER);
	        pathpanel[index].add(btnfileopen[index], BorderLayout.EAST);
	        
	        bordertreepanel[index].add(pathpanel[index], BorderLayout.NORTH);
	        
	        bordertreepanel[index].add(arrayTree[index], BorderLayout.CENTER);
	        
	        cardpanel[index].add(bordertreepanel[index], CARD_LAYOUT_TREE);
	        cardpanel[index].add(loadingpanel[index], CARD_LAYOUT_LOADING);
	        
	        ((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],CARD_LAYOUT_LOADING);	       
    	}
    }

    private void setFileDrop() {
        for(Component com: Arrays.asList(loadingpanel[LEFT].getEmptyPanel(), arrayTree[LEFT])) {
        	new  FileDrop( com, new FileDrop.Listener()
            {   public void  filesDropped( java.io.File[] files )
                {   
            		setApk(LEFT, files[0].getAbsolutePath());
                }
            });
        }        
        for(Component com: Arrays.asList(loadingpanel[RIGHT].getEmptyPanel(), arrayTree[RIGHT])) {
        	new  FileDrop( com, new FileDrop.Listener()
            {   public void  filesDropped( java.io.File[] files )
                {   
            		setApk(RIGHT, files[0].getAbsolutePath());	            	
                }
            });
        }
    }
    
    private void initializeTree(DiffTree tree) {
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
    	
		tree.addFocusListener(fl);
		tree.addTreeSelectionListener(this);
		DiffMouseAdapter fRowSelectionListener = new DiffMouseAdapter(tree);
		tree.addMouseListener(fRowSelectionListener);
    }
    
    class DiffMouseAdapter extends MouseAdapter {
    	JTree t;
    	public DiffMouseAdapter(JTree tree) {
    		this.t = tree;
    	}
    	
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
							if (arrayTree[LEFT] == t) {
								DiffTree.setSelectedtree(arrayTree[LEFT]);
							} else {
								DiffTree.setSelectedtree(arrayTree[RIGHT]);
							}
							splitPane.repaint();
							
						} else if (e.getClickCount() == 2) {
							DefaultMutableTreeNode node = (DefaultMutableTreeNode) (t.getSelectionPath()
									.getLastPathComponent());							
							DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();							
							if ((!node.isLeaf() || temp.isfolder) && !node.isRoot() ) {
								if (t.isCollapsed(closestRow)) {
									t.expandRow(closestRow);
								} else {
									t.collapseRow(closestRow);
								}
							} else {
								if(temp.state == DiffTreeUserData.NODE_STATE_NOMAL || temp.state == DiffTreeUserData.NODE_STATE_ADD	 || node.isRoot()) {
									Log.d("open program : " + temp);									
									SystemUtil.openFile(temp.makeFilebyNode());
								} else if(temp.state == DiffTreeUserData.NODE_STATE_DIFF) {
									Log.d("open diff program : " + temp.state);
									String openner;
									if(SystemUtil.isLinux()) {
										openner = "/opt/p4v-2017.3.1601999/bin/p4merge";
									} else {
										openner = "p4merge";
									}
									DiffTreeUserData othertemp = getUserDatabyTreePath(temp.other);										
									SystemUtil.exec(new String[]{openner, temp.makeFilebyNode().getAbsolutePath(),
											othertemp.makeFilebyNode().getAbsolutePath()});
									
								}
							}
						}
					}
				} // else
					// temp.setSelectionRow(-1);
			}
		}
    }
    
    public void createTreeNode(final ApkInfo apkinfodiff1, int num) {    	
		final int index = num;
		int otherindex = index == LEFT ? RIGHT : LEFT;
		
		synchronized (arrayTree[index].lock) {
			//Log.w("start create Tree :" + index);
			arrayTree[index].lock.valueOf(true);
			pathtextfiled[index].setText(apkinfodiff1.filePath);
			pathtextfiled[index].setCaretPosition(pathtextfiled[index].getDocument().getLength());

			arraytreeNode[index] = new SortNode(new RootDiffTreeUserData(apkinfodiff1));
			arrayTreemodel[index] = new FilteredTreeModel(arraytreeNode[index]);
			
			DiffMappingTree.createTree(apkinfodiff1, arraytreeNode[index]);
			
			arrayTree[index].setModel(arrayTreemodel[index]);
			showCardpanel(CARD_LAYOUT_TREE, index);
			
			arrayTree[index].lock.valueOf(false);
			arrayTree[index].lock.notify();
			Log.w("end create Tree :" + index);
		}
		
		
		synchronized (arrayTree[otherindex].lock) {
			try {
				while (arrayTree[otherindex].lock) {
					Log.w("wait other tree :" + index);
					arrayTree[otherindex].lock.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		Log.w("pass lock" + index);
		
		synchronized (Difflock) {
		if (arraytreeNode[LEFT] != null && arraytreeNode[RIGHT] != null && !Difflock) {
			
			if(CurrentmergeapkfilePath[LEFT] != null && CurrentmergeapkfilePath[RIGHT] !=null &&
					CurrentmergeapkfilePath[LEFT].equals(apkComparer.getApkInfo(LEFT).filePath) && 
					CurrentmergeapkfilePath[RIGHT].equals(apkComparer.getApkInfo(RIGHT).filePath)) {
				arrayTreemodel[index].reload();
		    	Log.w("Create end... not diff:" + index);
		    	return;
			}
			
			CurrentmergeapkfilePath[LEFT] = apkComparer.getApkInfo(LEFT).filePath;
			CurrentmergeapkfilePath[RIGHT] = apkComparer.getApkInfo(RIGHT).filePath;
			//Log.w("change filepath" + index);
			
			Difflock.valueOf(true);
			for (int i = 0; i < 2; i++) {
//				loadingpanel[i].setshow(DiffLoadingPanel.LOADING);
//				showCardpanel(CARD_LAYOUT_LOADING, index);
				arrayTree[i].setpaintingFlag(false);
			}
			clearnodepath(arraytreeNode[otherindex]);
			arrayTree[otherindex].setModel(arrayTreemodel[otherindex]);
						
			// new Thread(){
			// public void run(){
			
				Log.w("start diff :" + index);
			
				startDiff();
				for (int i = 0; i < 2; i++) {
					arrayTreemodel[i].reload();
					arrayTree[i].setpaintingFlag(true);
				}
				Difflock.valueOf(false);
				Log.w("end diff :" + index);
				setEnableToggleBtn(true);
				return;
			}
			// }}.start();			
		}
		
		Log.w("Create end... not diff :" + index);
    }
    
    private void showCardpanel(String str, int index) {
    	if(index==LEFT) {    		
    		 ((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],str);
    	} else  ((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],str);
    	cardpanel[index].repaint();
    }
    
    private void startDiff() {
    	//Log.d("Start Diff- mapping");
    	mappingtree(arraytreeNode[LEFT], arrayTree[RIGHT]);
    	mappingtree(arraytreeNode[RIGHT], arrayTree[LEFT]);
    	//Log.d("End Diff- mapping");
    	
    	setfolderstate(arraytreeNode[LEFT]);
    	setfolderstate(arraytreeNode[RIGHT]);
    	
    	arrayTreemodel[LEFT].reload();
    	arrayTreemodel[RIGHT].reload();
    	repaint();
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
	    			if(parenttemp.state != DiffTreeUserData.NODE_STATE_DIFF) parenttemp.setState(temp.state);
	    				    			
	    			if(parenttemp.other != null) {
		    			DiffTreeUserData parentothertemp = (DiffTreeUserData)((DefaultMutableTreeNode)(parenttemp.other.getLastPathComponent())).getUserObject();
		    			if(parentothertemp.state != DiffTreeUserData.NODE_STATE_DIFF) parentothertemp.setState(temp.state);
	    			}
	    		}
    		}
    	}   	
    }
    
    private void clearnodepath(SortNode rootmynode) {
    	Enumeration<DefaultMutableTreeNode> myreenode = rootmynode.depthFirstEnumeration();
    	
    	while (myreenode.hasMoreElements()) {
    		DefaultMutableTreeNode mynode = myreenode.nextElement();
    		
    		DiffTreeUserData temp = (DiffTreeUserData)mynode.getUserObject();
    		
    		temp.me = null;
    		temp.other = null;
    		temp.state = DiffTreeUserData.NODE_STATE_NOMAL;
    		
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
    		// for root node
    		patharray[0] = othertree.getModel().getRoot().toString();
    		
    		
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
	            		if(temp.Key.length() > 0 && !getUserDatabyTreePath(temppath).compare(temp)) {
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
                    	
                    	//if(temp.Key.equals(key) && temp.Key.length() > 0 && !Arrays.asList(DiffMappingTree.allowaddkey).contains(key)) {
                    	if(temp.Key.equals(key) && temp.Key.length() > 0 && !(temp instanceof PassKeyDiffTreeUserData)) {                    	
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

    @Override
    public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		// Log.d(e.toString());

		if (e.getSource() instanceof JToggleButton) {
			if (arraytreeNode[LEFT] != null && arraytreeNode[RIGHT] != null) {
				ArrayList<TreePath> expandedpath = new ArrayList<TreePath>();
				getPaths(arrayTree[LEFT], new TreePath(arraytreeNode[LEFT].getPath()), true, expandedpath);
				if (e.getActionCommand() == CMD_TOGGLE_IDEN) {
					arrayTreemodel[LEFT].setFilter(FilteredTreeModel.FLAG_IDEN);
					arrayTreemodel[RIGHT].setFilter(FilteredTreeModel.FLAG_IDEN);
				} else if (e.getActionCommand() == CMD_TOGGLE_ADD) {
					arrayTreemodel[LEFT].setFilter(FilteredTreeModel.FLAG_ADD);
					arrayTreemodel[RIGHT].setFilter(FilteredTreeModel.FLAG_ADD);
				} else if (e.getActionCommand() == CMD_TOGGLE_EDITOR) {
					arrayTreemodel[LEFT].setFilter(FilteredTreeModel.FLAG_DIFF);
					arrayTreemodel[RIGHT].setFilter(FilteredTreeModel.FLAG_DIFF);
				}
				for (int i = 0; i < expandedpath.size(); i++) {
					arrayTree[LEFT].expandPath(expandedpath.get(i));
				}
			}
		}
		if (e.getSource() instanceof JButton) {
			if (e.getActionCommand() == CMD_BUTTON_ABOUT) {
				AboutDlg.showAboutDialog(this);
			} else if (e.getActionCommand() == CMD_BUTTON_SETTING) {
				SettingDlg dlg = new SettingDlg(Main.frame);
				dlg.setVisible(true);
			} else if (e.getActionCommand() == CMD_BUTTON_FILE_OPEN) {
				final String apkFilePath = ApkFileChooser.openApkFilePath(Main.frame);
				if (apkFilePath == null) {
					Log.v("Not choose apk file");
					return;
				}
				setApk(e.getSource().equals(btnfileopen[LEFT]) ? LEFT : RIGHT, apkFilePath);
			}
		}
	}
    
    void setApk(int index, String filePath) {
    	
		if((CurrentmergeapkfilePath[index] == null ||
				!CurrentmergeapkfilePath[index].equals(filePath))) {
			apkComparer.setApk(index, filePath);
		} else {
			MessageBoxPane.showError(Main.frame, "same APK file");
		}
    	
    }
    
    
    private boolean ishavevisiblenode(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();				
				TreePath path = parent.pathByAddingChild(n);
				if(tree.isVisible(path)) {
					return true;
				}
			}
		}
		
    	return false;
    }
    
	private void getPaths(JTree tree, TreePath parent, boolean expanded, List<TreePath> list) {		
		if (!tree.isVisible(parent)) {
			return;
		}
		
		if(tree.isExpanded(parent)) {
			list.add(parent);
		}

		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				getPaths(tree, path, expanded, list);
			}
		}
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
			
			if(arrayTree[LEFT] == (JTree)e.getSource()) {
				arrayTree[RIGHT].setSelectionPath(temp.other);
				//Log.d("lefttree state : "+ temp.state + "   me : " + temp.me + "   other : " + temp.other + " isLeaf : " + node.isLeaf() + " key : " + temp.Key);
			} else {				
				arrayTree[LEFT].setSelectionPath(temp.other);
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
