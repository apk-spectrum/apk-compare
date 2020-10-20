package com.apkcompare.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkcompare.ApkComparer;
import com.apkcompare.core.DiffMappingTree;
import com.apkcompare.data.RootDiffTreeUserData;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkcompare.resource.RImg;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.FileDrop;
import com.apkspectrum.util.Log;

public class DynamicTreeDemo extends JPanel implements ActionListener
{
	private static final long serialVersionUID = -8110312211026585408L;

	private static final int LEFT = DiffTree.LEFT;
	private static final int RIGHT = DiffTree.RIGHT;
	private static final int TEXTFIELD_HEIGHT = 28;

	private static final String CMD_TOGGLE_ADD = "CMD_TOGGLE_ADD";
	private static final String CMD_TOGGLE_EDITOR = "CMD_TOGGLE_EDITOR";
	private static final String CMD_TOGGLE_IDEN = "CMD_TOGGLE_IDEN";

	private static final String CARD_LAYOUT_TREE = "CARD_LAYOUT_TREE";
	private static final String CARD_LAYOUT_LOADING = "CARD_LAYOUT_LOADING";

	private ApkComparer apkComparer;

	private SortNode[] arraytreeNode = {null, null};
	private DiffTree[] arrayTree = {null, null};
	private FilteredTreeModel[] arrayTreemodel = {null, null};
	private DiffLoadingPanel[] loadingpanel = {null, null};
	private String CurrentmergeapkfilePath[] = {null, null};

	private JToggleButton btnadd, btndiff, btniden;
	private JTextField[] pathtextfiled = {null, null};
	private JPanel[] cardpanel = {null, null};

	//https://www.shareicon.net/diff-94479
	//https://www.shareicon.net/interface-letter-i-info-circle-help-735003
	//https://www.shareicon.net/setting-598385

	public DynamicTreeDemo(ApkComparer apkComparer, UiEventHandler uiEvtHandler) {
		super(new BorderLayout());
		setOpaque(true);
		uiEvtHandler.registerKeyStrokeAction(this);

		this.apkComparer = apkComparer;
		if(apkComparer != null) {
			apkComparer.setStatusListener(new ApkComparerListener());
		}

		arrayTree[LEFT] = new DiffTree(uiEvtHandler);
		arrayTree[RIGHT] = new DiffTree(uiEvtHandler);
		DiffTree.setLinkedPosition(arrayTree[LEFT], arrayTree[RIGHT]);

		JPanel[] contentPanel = setCardPanel(uiEvtHandler);
		setFileDrop(uiEvtHandler);

		final JSplitPaneWithZeroSizeDivider splitPane = new JSplitPaneWithZeroSizeDivider();
		//splitPane.setDividerLocation(400);
		splitPane.setLeftComponent(contentPanel[LEFT]);
		splitPane.setRightComponent(contentPanel[RIGHT]);
		splitPane.setResizeWeight(0.5);
		splitPane.setY(TEXTFIELD_HEIGHT+1);

		AdjustmentListener scrollListener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent evt) {
//				Adjustable source = evt.getAdjustable();
//				if (evt.getValueIsAdjusting()) {
//				  return;
//				}
				splitPane.repaint();
			}
		};

		JScrollPane scrollpane = new JScrollPane(splitPane);
		scrollpane.getVerticalScrollBar().setUnitIncrement(10);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(scrollListener);
		scrollpane.getHorizontalScrollBar().addAdjustmentListener(scrollListener);

		JPanel temppanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		temppanel.setPreferredSize(new Dimension(0, 48));

		btnadd = new JToggleButton(RImg.DIFF_TOOLBAR_ADD.getImageIcon());
		btndiff = new JToggleButton(RImg.DIFF_TOOLBAR_EDITOR.getImageIcon());
		btniden = new JToggleButton(RImg.DIFF_TOOLBAR_IDEN.getImageIcon());

		btnadd.setActionCommand(CMD_TOGGLE_ADD);
		btndiff.setActionCommand(CMD_TOGGLE_EDITOR);
		btniden.setActionCommand(CMD_TOGGLE_IDEN);

		JButton btnsetting = new JButton(
				uiEvtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_SETTINGS));
		JButton btninfo = new JButton(
				uiEvtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_ABOUT));

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

	private JPanel[] setCardPanel(UiEventHandler handler) {
		JPanel []bordertreepanel = {new JPanel(new BorderLayout()), new JPanel(new BorderLayout())};
		JPanel []pathpanel = {new JPanel(new BorderLayout()), new JPanel(new BorderLayout())};
		JButton[] btnfileopen = {null, null};

		for(int index=0;index <2; index++) {
			cardpanel[index] = new JPanel(new CardLayout());
			loadingpanel[index] = new DiffLoadingPanel();
			pathtextfiled[index] = new JTextField();

			btnfileopen[index] = new JButton(handler.getAction(UiEventHandler.ACT_CMD_OPEN_APK));
			btnfileopen[index].setBorder(BorderFactory.createEmptyBorder ( 1, 1, 1, 1 ));
			btnfileopen[index].setPreferredSize(new Dimension(TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT));
			btnfileopen[index].putClientProperty("POSITION", Integer.valueOf(index));

			pathtextfiled[index].setPreferredSize(new Dimension(0, TEXTFIELD_HEIGHT));
			pathtextfiled[index].setAction(handler.getAction(UiEventHandler.ACT_CMD_OPEN_APK));
			pathtextfiled[index].putClientProperty("POSITION", Integer.valueOf(index));

			pathpanel[index].add(pathtextfiled[index], BorderLayout.CENTER);
			pathpanel[index].add(btnfileopen[index], BorderLayout.EAST);

			cardpanel[index].add(arrayTree[index], CARD_LAYOUT_TREE);
			cardpanel[index].add(loadingpanel[index], CARD_LAYOUT_LOADING);

			((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],CARD_LAYOUT_LOADING);

			bordertreepanel[index].add(pathpanel[index], BorderLayout.NORTH);
			bordertreepanel[index].add(cardpanel[index], BorderLayout.CENTER);
		}
		return bordertreepanel;
	}

	private void setFileDrop(FileDrop.Listener listener) {
		for(JComponent com: Arrays.asList(loadingpanel[LEFT].getEmptyPanel(), arrayTree[LEFT])) {
			com.setName("FILE_DROP_TOP");
			com.putClientProperty("POSITION", Integer.valueOf(LEFT));
			new FileDrop(com, listener);
		}
		for(JComponent com: Arrays.asList(loadingpanel[RIGHT].getEmptyPanel(), arrayTree[RIGHT])) {
			com.setName("FILE_DROP_TOP");
			com.putClientProperty("POSITION", Integer.valueOf(RIGHT));
			new FileDrop(com, listener);
		}
	}

	private class ApkComparerListener implements ApkComparer.StatusListener {
		@Override
		public void onStart(final int position) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					loadingpanel[position].setshow(DiffLoadingPanel.LOADING);
					showCardpanel(CARD_LAYOUT_LOADING, position);
					Log.w("change loading");
				}
			});
		}

		@Override
		public void onSuccess(int position) { }

		@Override
		public void onError(int position, int error) { }

		@Override
		public void onCompleted(final int position) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					createTreeNode(apkComparer.getApkInfo(position), position);
				}
			});
		}
	}

	private void createTreeNode(final ApkInfo apkinfodiff1, final int index) {
		int otherindex = index == LEFT ? RIGHT : LEFT;

		//Log.w("start create Tree :" + index);
		pathtextfiled[index].setText(apkinfodiff1.filePath);
		pathtextfiled[index].setCaretPosition(pathtextfiled[index].getDocument().getLength());

		arraytreeNode[index] = new SortNode(new RootDiffTreeUserData(apkinfodiff1));
		arrayTreemodel[index] = new FilteredTreeModel(arraytreeNode[index]);

		DiffMappingTree mappingtree = new DiffMappingTree();
		mappingtree.createTree(apkinfodiff1, arraytreeNode[index]);

		arrayTree[index].setModel(arrayTreemodel[index]);
		showCardpanel(CARD_LAYOUT_TREE, index);

		Log.w("end create Tree :" + index);

		if (arraytreeNode[LEFT] != null && arraytreeNode[RIGHT] != null) {
			if (CurrentmergeapkfilePath[LEFT] != null && CurrentmergeapkfilePath[RIGHT] != null
					&& CurrentmergeapkfilePath[LEFT].equals(apkComparer.getApkInfo(LEFT).filePath)
					&& CurrentmergeapkfilePath[RIGHT].equals(apkComparer.getApkInfo(RIGHT).filePath)) {
				arrayTreemodel[index].reload();
				Log.w("in sync Create end... not diff:" + index);
				return;
			}

			CurrentmergeapkfilePath[LEFT] = apkComparer.getApkInfo(LEFT).filePath;
			CurrentmergeapkfilePath[RIGHT] = apkComparer.getApkInfo(RIGHT).filePath;
			// Log.w("change filepath" + index);

			for (int i = 0; i < 2; i++) {
				// loadingpanel[i].setshow(DiffLoadingPanel.LOADING);
				// showCardpanel(CARD_LAYOUT_LOADING, index);
				arrayTree[i].setpaintingFlag(false);
			}
			clearnodepath(arraytreeNode[otherindex]);
			arrayTree[otherindex].setModel(arrayTreemodel[otherindex]);

			// new Thread(){
			// public void run(){
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Log.w("start diff :" + index);

			startDiff();

			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			for (int i = 0; i < 2; i++) {
				arrayTreemodel[i].reload();
				arrayTree[i].setpaintingFlag(true);
			}
			Log.w("end diff :" + index);
			setEnableToggleBtn(true);
			return;
		}

		Log.w("Create end... not diff :" + index);
	}

	private void showCardpanel(String str, int index) {
		((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],str);
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

		@SuppressWarnings("unchecked")
		Enumeration<TreeNode> myreenode = (Enumeration<TreeNode>)(Enumeration<?>) rootmynode.depthFirstEnumeration();

		while (myreenode.hasMoreElements()) {
			DefaultMutableTreeNode mynode = (DefaultMutableTreeNode) myreenode.nextElement();
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
		@SuppressWarnings("unchecked")
		Enumeration<TreeNode> myreenode = (Enumeration<TreeNode>)(Enumeration<?>) rootmynode.depthFirstEnumeration();

		while (myreenode.hasMoreElements()) {
			DefaultMutableTreeNode mynode = (DefaultMutableTreeNode) myreenode.nextElement();

			DiffTreeUserData temp = (DiffTreeUserData)mynode.getUserObject();

			temp.me = null;
			temp.other = null;
			temp.state = DiffTreeUserData.NODE_STATE_NOMAL;

		}
	}

	private void mappingtree(SortNode rootmynode, JTree othertree) {

		@SuppressWarnings("unchecked")
		Enumeration<TreeNode> myreenode = (Enumeration<TreeNode>)(Enumeration<?>) rootmynode.depthFirstEnumeration();
		TreePath samebefore = null;

		while (myreenode.hasMoreElements()) {
			DefaultMutableTreeNode mynode = (DefaultMutableTreeNode) myreenode.nextElement();

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
//			String str = leftTreepath.toString();

//			int spaceindex = str.indexOf("[");
//			if(spaceindex > -1) {
//				str = str.substring(spaceindex+1, str.indexOf("]"));
//				//Log.d(str);
//
//				String[] parts = str.split(", ");

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
//			}
		}
	}

	private DiffTreeUserData getUserDatabyTreePath(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)path.getLastPathComponent();
		if(node.getUserObject() instanceof DiffTreeUserData) {
			DiffTreeUserData temp = (DiffTreeUserData)node.getUserObject();
			return temp;
		}
		return null;
	}

	private TreePath findByName(JTree tree, String[] names, String key) {
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
				for (Enumeration<?> e=node.children(); e.hasMoreElements(); ) {
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
		//String command = e.getActionCommand();
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
	}

	@SuppressWarnings("unused")
	private boolean ishavevisiblenode(JTree tree, TreePath parent) {
		TreeNode node = (TreeNode) parent.getLastPathComponent();
		if (node.getChildCount() >= 0) {
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
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
			for (Enumeration<?> e = node.children(); e.hasMoreElements();) {
				TreeNode n = (TreeNode) e.nextElement();
				TreePath path = parent.pathByAddingChild(n);
				getPaths(tree, path, expanded, list);
			}
		}
	}
}
