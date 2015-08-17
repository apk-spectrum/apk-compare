package com.ApkInfo.UI;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;

import com.ApkInfo.Core.AdbWrapper;
import com.ApkInfo.Core.AdbWrapper.DeviceStatus;
import com.ApkInfo.Core.AdbWrapper.PackageListObject;
import com.ApkInfo.Core.PackageTreeDataManager;
import com.ApkInfo.Resource.Resource;
import com.ApkInfo.UIUtil.ArrowTraversalPane;
import com.ApkInfo.UIUtil.BooleanTableModel;
import com.ApkInfo.UIUtil.ButtonType;
import com.ApkInfo.UIUtil.FilteredTreeModel;
import com.ApkInfo.UIUtil.StandardButton;
import com.ApkInfo.UIUtil.Theme;

import java.util.ArrayList;
import java.io.File;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
public class PackageTreeDlg extends JPanel
                      implements TreeSelectionListener, ActionListener{
	private static final long serialVersionUID = 813267847663868531L;
	private JTextField textFieldapkPath;
    private JTree tree;
    private DefaultMutableTreeNode top;
    private JPanel gifPanel;
    private JCheckBox checkboxUseframework;
    
    private JPanel ListPanel;
    private StandardButton refreshbtn;
    
    static private JDialog dialog;
    private String selDevice;
    private String selPackage;
    private String selApkPath;
    private String selFrameworkRes;
    private String tmpApkPath;
    private static JTextField textFilField;
    private FilteredTreeModel filteredModel;
    
	public class FrameworkTableObject {
		public Boolean buse;
		public String location;
		public String path;
	}
    
    private ArrayList<FrameworkTableObject> tableListArray = new ArrayList<FrameworkTableObject>();
    private JTable table;
    public String getSelectedDevice() {
    	return selDevice;
    }
    
    public String getSelectedPackage() {
    	return selPackage;
    }
    
    public String getSelectedApkPath() {
    	return selApkPath;
    }
    
    public String getSelectedFrameworkRes() {
    	return selFrameworkRes;
    }
    
    public File getSelectedFile() {
    	if(tmpApkPath != null)
    		return new File(tmpApkPath);
    	return null;
    }
    
    //Optionally play with line styles.  Possible values are
    //"Angled" (the default), "Horizontal", and "None".
    
    public PackageTreeDlg() {
        super(new BorderLayout());
        makeTreeForm();
        addTreeList();
    }
    
    private void addTreeList() {

    	if(!refreshbtn.isVisible()) {
    		System.out.println("Already refreshing...");
    		return;
    	}
    	
    	top.removeAllChildren();
    	tree.updateUI();
    	Thread t = new Thread(new Runnable() {
			public void run()
			{
		    	refreshbtn.setVisible(false);
				ArrayList<DeviceStatus> DeviceList;
				do {
					DeviceList = AdbWrapper.scanDevices();
	
					if(DeviceList.size() == 0) {
						final ImageIcon Appicon = Resource.IMG_WARNING.getImageIcon();
						int n = ArrowTraversalPane.showOptionDialog(null, Resource.STR_MSG_DEVICE_NOT_FOUND.getString(), Resource.STR_LABEL_WARNING.getString(), JOptionPane.WARNING_MESSAGE, JOptionPane.WARNING_MESSAGE, Appicon,
					    		new String[] {Resource.STR_BTN_REFRESH.getString(), Resource.STR_BTN_CANCEL.getString()}, Resource.STR_BTN_REFRESH.getString());
						if(n==-1 || n==1) {
							dialog.dispose();
							return;
						}
					} else {
						break;
					}
				} while(true);
				
				gifPanel.setVisible(true);

				createDeviceNodes(top, DeviceList.toArray(new DeviceStatus[0]));
				
				gifPanel.setVisible(false);
		    	refreshbtn.setVisible(true);
			}

			private void createDeviceNodes(DefaultMutableTreeNode top, DeviceStatus[] devList)
		    {
				DefaultMutableTreeNode[] devTree = new DefaultMutableTreeNode[devList.length];

				for(int i = 0; i < devList.length; i++) {
					devTree[i] = new DefaultMutableTreeNode(devList[i]);
			        top.add(devTree[i]);
					if(devList[i].status.equals("device")) {
				        devTree[i].add(new DefaultMutableTreeNode(Resource.STR_LABEL_LOADING.getString()));
					} else {
			        	devTree[i].add(new DefaultMutableTreeNode(Resource.STR_MSG_DEVICE_UNAUTHORIZED.getString()));
					}
				}
				tree.updateUI();
				expandOrCollapsePath(tree, new TreePath(top.getPath()),3,0, true);

				for(int i = 0; i < devList.length; i++) {
					if(devList[i].status.equals("device")) {
				        DefaultMutableTreeNode priv_app = new DefaultMutableTreeNode("priv-app");
				        DefaultMutableTreeNode systemapp = new DefaultMutableTreeNode("app");
				        DefaultMutableTreeNode framework_app = new DefaultMutableTreeNode("framework");
				        DefaultMutableTreeNode system = new DefaultMutableTreeNode("system");
				        DefaultMutableTreeNode dataapp = new DefaultMutableTreeNode("app");
				        DefaultMutableTreeNode data = new DefaultMutableTreeNode("data");
				        	        
				        system.add(priv_app);
				        system.add(systemapp);
				        system.add(framework_app);
				        
				        data.add(dataapp);

						PackageTreeDataManager PackageManager = new PackageTreeDataManager(devList[i].name);
						ArrayList<PackageListObject> ArrayDataObject = PackageManager.getDataArray();
				        for(PackageListObject obj: ArrayDataObject) {
				        	DefaultMutableTreeNode temp = new DefaultMutableTreeNode(obj);		        	
				        	
				        	if(obj.apkPath.startsWith("/system/priv-app/")) {
				        		priv_app.add(temp);		        		
				        	} else if(obj.apkPath.startsWith("/system/app/")) {
				        		systemapp.add(temp);
				        	} else if(obj.apkPath.startsWith("/system/framework/")) {
				        		framework_app.add(temp);
				        		
				        		FrameworkTableObject tableObject = new FrameworkTableObject();
				        		
				        		tableObject.buse = true;
				        		tableObject.location = "device";
				        		tableObject.path = ((PackageListObject)temp.getUserObject()).apkPath; 
				        		
				        		tableListArray.add(tableObject);
				        		
				        	} else if(obj.apkPath.startsWith("/data/app/")) {
				        		dataapp.add(temp);
				        	}
				        }

				        devTree[i].removeAllChildren();
				        devTree[i].add(system);		        
				        devTree[i].add(data);
				        //add table
				        
				        table.setModel(new BooleanTableModel(tableListArray));
				        table.setPreferredScrollableViewportSize(new Dimension(0,80));
				        
				        
				        
				        setJTableColumnsWidth(table,550,30,70,440);
				        
				        
				        tree.updateUI();
				        expandOrCollapsePath(tree, new TreePath(top.getPath()),3,0, true);
					}
				}
                
		        //System.out.println("end  loading package : " + dev.device);
		        
		        if(textFilField != null) {
		        	if(textFilField.getText().length() >0){
		        		makefilter(textFilField.getText());		        		
		        	}
		        }
		    }
    	});
		t.start();
    }
    
    public static void expandOrCollapsePath (JTree tree,TreePath treePath,int level,int currentLevel,boolean expand) {
//      System.err.println("Exp level "+currentLevel+", exp="+expand);
      if (expand && level<=currentLevel && level>0) return;

      TreeNode treeNode = ( TreeNode ) treePath.getLastPathComponent();
      TreeModel treeModel=tree.getModel();
      if ( treeModel.getChildCount(treeNode) >= 0 ) {
         for ( int i = 0; i < treeModel.getChildCount(treeNode); i++  ) {
            TreeNode n = ( TreeNode )treeModel.getChild(treeNode, i);
            TreePath path = treePath.pathByAddingChild( n );
            expandOrCollapsePath(tree,path,level,currentLevel+1,expand);
         }
         if (!expand && currentLevel<level) return;
      }      
      if (expand) {
         tree.expandPath( treePath );
//         System.err.println("Path expanded at level "+currentLevel+"-"+treePath);
      } else {
         tree.collapsePath(treePath);
//         System.err.println("Path collapsed at level "+currentLevel+"-"+treePath);
      }
   }
    
    private void makeTreeForm() {
        //Create the nodes.
        top =
            new DefaultMutableTreeNode("Device");
        //createNodes(top);
 
        //Create a tree that allows one selection at a time.
                        
        FilteredTreeModel model = new FilteredTreeModel(new DefaultTreeModel(top));
        
        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
            	
            	int selRow = tree.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                        TreePath path = tree.getPathForLocation ( e.getX (), e.getY () );
                        tree.setSelectionPath(selPath);
                        Rectangle pathBounds = tree.getUI ().getPathBounds ( tree, path );
                        if ( pathBounds != null && pathBounds.contains ( e.getX (), e.getY () ) )
                        {
                        	DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                    tree.getLastSelectedPathComponent();
                        	if(node.getDepth() > 0 || node.getLevel() < 3) {
                        		return;
                        	}

                            JPopupMenu menu = new JPopupMenu ();
                            menu.add ( new JMenuItem ( Resource.STR_BTN_OPEN.getString() ) ).addActionListener(new ActionListener(){ 
                            	   public void actionPerformed(ActionEvent e) {
                            		   OpenPackage();
                            	   }});
                            menu.add ( new JMenuItem ( Resource.STR_BTN_SAVE.getString() ) ).addActionListener(new ActionListener(){ 
                            	   public void actionPerformed(ActionEvent e) {
                            		   PullPackage();
                            	   }});
                            //menu.add ( new JMenuItem ( Resource.STR_BTN_EXPORT.getString() ) ).addActionListener(new ActionListener(){ 
                            //	   public void actionPerformed(ActionEvent e) {
                            //		   
                            //	   }});
                            menu.show ( tree, e.getX (), e.getY () );
                        }
                    }
                    else if(e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {                    	
                    	OpenPackage();
                    }
                }
            }
        };
        
        
        tree = new JTree(model);
        tree.addMouseListener(ml);
        
        tree.getSelectionModel().setSelectionMode
                (TreeSelectionModel.SINGLE_TREE_SELECTION);
 
        //Listen for when the selection changes.
        tree.addTreeSelectionListener(this);
 
        //Create the scroll pane and add the tree to it.
        JScrollPane treeView = new JScrollPane(tree);
 
        //Create the HTML viewing pane.
        textFieldapkPath = new JTextField();
        textFieldapkPath.setEditable(false);
        
        textFilField = new JTextField();
        

        textFilField.addKeyListener(new KeyAdapter()
        {
        	public void keyPressed(KeyEvent ke) {
        		if(ke.getKeyCode() == KeyEvent.VK_DOWN) {
            		KeyEvent key = new KeyEvent(tree, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_DOWN, KeyEvent.CHAR_UNDEFINED);
            		tree.dispatchEvent(key);	        		
        		} else if (ke.getKeyCode() == KeyEvent.VK_UP) {
            		KeyEvent key = new KeyEvent(tree, KeyEvent.KEY_PRESSED, System.currentTimeMillis(), 0, KeyEvent.VK_UP, KeyEvent.CHAR_UNDEFINED);
            		tree.dispatchEvent(key);        			
        		}
        	}
        	
            public void keyReleased(KeyEvent ke) {
            	
            	if(textFilField.getText().length() ==0) {
            		collapseAll(tree);
            		expandOrCollapsePath(tree, new TreePath(top.getPath()),3,0, true);            		
            		return;
            	}
            	
                if(!(ke.getKeyChar()==27||ke.getKeyChar()==65535))//this section will execute only when user is editing the JTextField
                {
                	//System.out.println(textFilField.getText()+ ":" + Integer.valueOf(ke.getKeyChar()));                	
                	
                	if(ke.getKeyChar()==10) {
                		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                                tree.getLastSelectedPathComponent();
                		if(node != null) {
                			OpenPackage();
                		}                		
                	} else {
                		makefilter (textFilField.getText());
                	}
                    
                }
            }
            private void expandTree(final JTree tree) {
                for (int i = 0; i < tree.getRowCount(); i++) {
                  tree.expandRow(i);
                }
              }
        });
        
        
		ListPanel = makeListTable();
		ListPanel.setVisible(false);
		
		checkboxUseframework = new JCheckBox("use framework apk???");
		checkboxUseframework.setSelected(false);
		checkboxUseframework.addActionListener(new ActionListener() {
			
            @Override
            public void actionPerformed(ActionEvent e) {
                
            	
            	if(checkboxUseframework.isSelected()) {
            		ListPanel.setVisible(true);
            	} else {
            		ListPanel.setVisible(false);
            	}    
            }
        });
        
        StandardButton openbtn = new StandardButton(Resource.STR_BTN_OPEN.getString(),Theme.GRADIENT_LIGHTBLUE_THEME,ButtonType.BUTTON_ROUNDED);		
        refreshbtn = new StandardButton(Resource.STR_BTN_REFRESH.getString()+" (F5)",Theme.GRADIENT_LIGHTBLUE_THEME,ButtonType.BUTTON_ROUNDED);
        StandardButton exitbtn = new StandardButton(Resource.STR_BTN_CANCEL.getString(),Theme.GRADIENT_LIGHTBLUE_THEME,ButtonType.BUTTON_ROUNDED);

        openbtn.addActionListener(this);
        refreshbtn.addActionListener(this);
        exitbtn.addActionListener(this);
        
        JPanel ButtonPanel = new JPanel(new BorderLayout());
                
        gifPanel = new JPanel();
        
        ImageIcon icon = Resource.IMG_WAIT_BAR.getImageIcon();
        JLabel GifLabel = new JLabel(icon);
        
        JLabel Loading = new JLabel(Resource.STR_LABEL_LOADING.getString());
        
        gifPanel.add(Loading);
        gifPanel.add(GifLabel);
        
        gifPanel.setVisible(false);
        
        JPanel ButtonPanelWest = new JPanel();
        ButtonPanelWest.add(gifPanel);
        ButtonPanelWest.add(refreshbtn);
        
        JPanel ButtonPanelEast = new JPanel();
        ButtonPanelEast.add(openbtn);
        ButtonPanelEast.add(exitbtn);     
        
        ButtonPanel.add(ButtonPanelWest, BorderLayout.WEST);
        
        ButtonPanel.add(ButtonPanelEast, BorderLayout.EAST);     
        
        
                
        Dimension minimumSize = new Dimension(100, 50);
        textFieldapkPath.setMinimumSize(minimumSize);
        treeView.setMinimumSize(minimumSize);
 
        //Add the split pane to this panel.
        //add(splitPane);
        JPanel panelnorth = new JPanel(new BorderLayout());                
        JPanel panelsourth = new JPanel(new BorderLayout());        
        JPanel panelsearch = new JPanel(new BorderLayout());
        
        panelsearch.add(new JLabel(Resource.STR_LABEL_SEARCH.getString() + " : "), BorderLayout.WEST);
        panelsearch.add(textFilField, BorderLayout.CENTER);
        
        panelnorth.add(textFieldapkPath,BorderLayout.NORTH);
        panelnorth.add(tree,BorderLayout.CENTER);
        panelnorth.add(panelsearch,BorderLayout.SOUTH);
        
        
        panelsourth.add(checkboxUseframework, BorderLayout.NORTH);
        panelsourth.add(ListPanel, BorderLayout.CENTER);
        panelsourth.add(ButtonPanel,BorderLayout.SOUTH);
        
        add(panelnorth,BorderLayout.CENTER);
        add(panelsourth,BorderLayout.SOUTH);
        
        
        
        
        
        //add(NorthPanel,BorderLayout.NORTH);
        //add(panel, BorderLayout.CENTER);

    }
    
    public void forselectionTree () {
        DefaultMutableTreeNode currentNode = top.getNextNode();

        
        
		//DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode)filteredModel.getRoot();
        do {
        		//System.out.println(currentNode.getLevel());
        		//System.out.println(currentNode.getUserObject());
        		//System.out.println(filteredModel.getChildCount(currentNode));
        		
        		//System.out.println(tree.isCollapsed(new TreePath(currentNode.getPath())));
        		
        		if(currentNode.getLevel()==3 && filteredModel.getChildCount(currentNode) >0) {
    		        for(int i=0; i<filteredModel.getChildCount(currentNode); i++) {
    		        	
    		        	TreePath temptreePath = new TreePath(((DefaultMutableTreeNode)(filteredModel.getChild(currentNode, i))).getPath());
    		        	
		        		tree.setSelectionPath(temptreePath);
		        		tree.scrollPathToVisible(temptreePath);
		        		return;
    		        }
		        }
        		
//	           if (currentNode.getLevel()==4 && tree.isVisible(new TreePath(currentNode.getPath())) == true) {
//	        	   tree.setSelectionPath(new TreePath(currentNode.getPath()));	        		        	   
//	        	   break;
//	           }
	           currentNode = currentNode.getNextNode();
           }
        while (currentNode != null);
    }
    
	/** Required by TreeSelectionListener interface. */
    public void valueChanged(TreeSelectionEvent e) {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                           tree.getLastSelectedPathComponent();
        if (node == null) return;
        //Object nodeInfo = node.getUserObject();

		if(node.getDepth() > 0 || node.getLevel() < 3) {
	        TreeNode [] treenode = node.getPath();
	        TreePath path = new TreePath(treenode);
	        textFieldapkPath.setText(path.toString());
		} else {
	        PackageListObject tempObject = ((PackageListObject)node.getUserObject()); 
	        textFieldapkPath.setText(tempObject.apkPath + " - " + tempObject.pacakge);
		}
    }
    
    private void expandTree(final JTree tree) {
        for (int i = 0; i < tree.getRowCount(); i++) {
          tree.expandRow(i);
        }
      }
    
    private void makefilter (String temp){
        filteredModel = (FilteredTreeModel) tree.getModel();
        filteredModel.setFilter(temp);
        DefaultTreeModel treeModel = (DefaultTreeModel) filteredModel.getTreeModel();
        treeModel.reload();
        
        
        expandTree(tree);
        forselectionTree ();
	}
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
 
    	final PackageTreeDlg ptg = new PackageTreeDlg();
    	
        //Create and set up the window.
    	dialog = new JDialog(new JFrame(), Resource.STR_TREE_OPEN_PACKAGE.getString(), true);
    	dialog.setIconImage(Resource.IMG_APP_ICON.getImageIcon().getImage());
    	//dialog.setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
    	
		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
		dialog.getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
			private static final long serialVersionUID = 8368291008098324014L;
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
		    }
		});
 
    	KeyStroke vk_f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, false);
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(vk_f5, "VK_F5");
		dialog.getRootPane().getActionMap().put("VK_F5", new AbstractAction() {
			private static final long serialVersionUID = -5281980076592985530L;
			public void actionPerformed(ActionEvent e) {
				ptg.addTreeList();
		    }
		});
 
        //Add content to the window.
    	dialog.add(ptg);
 
        dialog.addWindowListener( new WindowAdapter() {
      	   public void windowOpened( WindowEvent e ){
      		   textFilField.requestFocus();
      	     }
      	   } );
    	
        //Display the window.
    	dialog.pack();
    	dialog.setBounds(100, 100, 600, 400);
    	dialog.setMinimumSize(new Dimension(700, 400));
		
    	dialog.setLocationRelativeTo(null);
    	dialog.setVisible(true);
    	dialog.dispose();
    	
    	System.out.println("package dialog closed");
    }
 
    public void showTreeDlg() {
        selDevice = null;
        selPackage = null;
        selApkPath = null;
        selFrameworkRes = null;

        //Create and set up the window.
    	dialog = new JDialog(new JFrame(), Resource.STR_TREE_OPEN_PACKAGE.getString(), true);
    	dialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    	KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
		dialog.getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
			private static final long serialVersionUID = 8368291008098324014L;
			public void actionPerformed(ActionEvent e) {
				selDevice = null;
				selPackage = null;
				selApkPath = null;
				selFrameworkRes = null;
				dialog.dispose();
		    }
		});
 
    	KeyStroke vk_f5 = KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, false);
		dialog.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(vk_f5, "VK_F5");
		dialog.getRootPane().getActionMap().put("VK_F5", new AbstractAction() {
			private static final long serialVersionUID = -5281980076592985530L;
			public void actionPerformed(ActionEvent e) {
				addTreeList();
		    }
		});
		
        //Add content to the window.
    	dialog.add(this);
 
        dialog.addWindowListener( new WindowAdapter() {
     	   public void windowOpened( WindowEvent e ){
     		   textFilField.requestFocus();
     	     }
     	   } );
         
    	//dialog.setResizable( false );
    	//dialog.setLocationRelativeTo(null);
    	
        //Display the window.
    	dialog.pack();
    	//dialog.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width)/2 - getWidth()/2, (Toolkit.getDefaultToolkit().getScreenSize().height)/2 - getHeight()/2);
    	    	
    	dialog.setBounds(100, 100, 600, 400);
    	dialog.setMinimumSize(new Dimension(700, 400));
		
    	dialog.setLocationRelativeTo(null);
    	dialog.setVisible(true);
    	dialog.dispose();

    }
    
    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                System.exit(0);
            }
        });
    }

    private void OpenPackage() {

    	System.out.println("open package");
    	
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
		
		if(node.getDepth() > 0 || node.getLevel() < 3) {
			return;
		}
		
		PackageListObject tempObject = ((PackageListObject)node.getUserObject()); 
		
		System.out.println(tempObject.pacakge);
		System.out.println(tempObject.label);
		System.out.println(tempObject.apkPath);
		
		DefaultMutableTreeNode deviceNode = null;
		
		for(deviceNode = node ; deviceNode.getUserObject() instanceof DeviceStatus==false; deviceNode = ((DefaultMutableTreeNode)deviceNode.getParent())) {
			
		}
		
		System.out.println(deviceNode.getUserObject());
		
		selDevice = ((DeviceStatus)deviceNode.getUserObject()).name;
		selPackage = tempObject.pacakge;
		selApkPath = tempObject.apkPath;
		//selFrameworkRes = null;
		
		dialog.dispose();
    }

    private void PullPackage() {
    	System.out.println("PullPackage()");
		
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
                tree.getLastSelectedPathComponent();
		
		if(node.getChildCount() != 0) {
			System.out.println("not node!");
			return ;
		}

		PackageListObject tempObject = ((PackageListObject)node.getUserObject()); 
		
		System.out.println(tempObject.pacakge);
		System.out.println(tempObject.label);
		System.out.println(tempObject.apkPath);
		
		DefaultMutableTreeNode deviceNode = null;
		for(deviceNode = node ; deviceNode.getUserObject() instanceof DeviceStatus==false; deviceNode = ((DefaultMutableTreeNode)deviceNode.getParent())) { }
		
		System.out.println(deviceNode.getUserObject());
		
		String device = ((DeviceStatus)deviceNode.getUserObject()).name;

		String apkPath = tempObject.apkPath;
		if(apkPath == null) return;
		
		String saveFileName;
		if(apkPath.endsWith("base.apk")) {
			saveFileName = apkPath.replaceAll(".*/(.*)/base.apk", "$1.apk");
		} else {
			saveFileName = apkPath.replaceAll(".*/", "");
		}

		JFileChooser jfc = new JFileChooser((String)Resource.PROP_LAST_FILE_SAVE_PATH.getData(""));
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(Resource.STR_LABEL_APK_FILE_DESC.getString(),"apk"));
		jfc.setSelectedFile(new File(saveFileName));


		if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
			return;

		File dir = jfc.getSelectedFile();
		if(dir == null) return;
		Resource.PROP_LAST_FILE_SAVE_PATH.setData(dir.getParentFile().getAbsolutePath());
		
		AdbWrapper.PullApk(device, apkPath, dir.getAbsolutePath(), null);
		//dir.isDirectory()
		
		//return dir.getPath();
		
    }
    
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getActionCommand().equals(Resource.STR_BTN_OPEN.getString())) {
			OpenPackage();
		} else if(e.getActionCommand().equals(Resource.STR_BTN_CANCEL.getString())) {
			//System.out.println("exit");
			selDevice = null;
			selPackage = null;
			selApkPath = null;
			selFrameworkRes = null;
			dialog.dispose();
		} else if(e.getSource() == refreshbtn) {
			addTreeList();
		}
	}
	
	public JPanel makeListTable ( ) {
		
		JPanel panel = new JPanel(new BorderLayout());
		
		//JTable table = new JTable(new BooleanTableModel());
		table = new JTable();
		//table.setPreferredScrollableViewportSize(table.getPreferredSize());
        //table.setFillsViewportHeight(true);
		JButton addbtn= new JButton("add");
		
		addbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//            	JFileChooser jfc = new JFileChooser();			
//    			jfc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("apk","apk"));							
//    			
//    			if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
//    				return;
//    			
//    			File dir = jfc.getSelectedFile();
//    			String file = null;
//    			if(dir!=null) {
//    				file = dir.getPath();
//    			}
//    			
//    			System.out.println("Select Directory" + file);
//    			
//    			
//    			
//    			if(file == null || file.isEmpty()) return;
    			
    			FrameworkTableObject temp = new FrameworkTableObject();
    			
    			temp.buse = false;
    			temp.location = "device";
    			temp.path = "/system/framework/twframework-res.apk";
    			
    			tableListArray.add(temp);    			
    			((AbstractTableModel) table.getModel()).fireTableDataChanged();   			
    			
    			
//    			for(String f: resList) {
//    				if(file.equals(f)) return;
//    			}
//    			resList.add(file);
//    			jlist.setListData(resList.toArray(new String[0]));
            }
        });
		
		
		
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.setFillsViewportHeight(true);
		setJTableColumnsWidth(table,550,30,70,440);
		//table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
        JScrollPane pane = new JScrollPane(table);
        
        //pane.setPreferredSize(new Dimension(550, 80) );
        
        panel.add(pane, BorderLayout.CENTER);
        panel.add(addbtn, BorderLayout.EAST);
        
        
        return panel;
	}
	
	public void collapseAll(JTree tree) {
	    int row = tree.getRowCount() - 1;
	    while (row >= 0) {
	      tree.collapseRow(row);
	      row--;
	      }
	    }
	
	public static void setJTableColumnsWidth(JTable table, int tablePreferredWidth,
			double... percentages) {
		double total = 0;
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
		total += percentages[i];
		}
		
		for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
		TableColumn column = table.getColumnModel().getColumn(i);
		column.setPreferredWidth((int)(tablePreferredWidth * (percentages[i] / total)));
		}
	}	
}