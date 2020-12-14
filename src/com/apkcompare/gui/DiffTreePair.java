package com.apkcompare.gui;

import java.awt.Container;
import java.util.Enumeration;
import java.util.List;

import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.data.base.PassKeyDiffTreeUserData;
import com.apkcompare.resource.RConst;
import com.apkspectrum.data.apkinfo.ApkInfo;

public class DiffTreePair implements RConst
{
	private DiffTree[] arrayTree = new DiffTree[2];

	DiffTreePair(UiEventHandler uiEvtHandler) {
		arrayTree[LEFT] = new DiffTree(uiEvtHandler);
		arrayTree[RIGHT] = new DiffTree(uiEvtHandler);
		DiffTree.setLinkedPosition(arrayTree[LEFT], arrayTree[RIGHT]);

		uiEvtHandler.putData(DIFF_TREE_PAIR_KEY, this);
	}

	DiffTreePair(DiffTree leftTree, DiffTree rightTree) {
		arrayTree[LEFT] = leftTree;
		arrayTree[RIGHT] = rightTree;
		DiffTree.setLinkedPosition(arrayTree[LEFT], arrayTree[RIGHT]);
	}

	public DiffTree get(int position) {
		return arrayTree[position];
	}

	public DiffTree[] get() {
		return arrayTree;
	}

	public void createTreeNode(int position, ApkInfo info) {
		get(position).createTreeNode(info);
	}

	public FilteredTreeModel getModel(int position) {
		Object model = get(position).getModel();
		return model instanceof FilteredTreeModel
				? (FilteredTreeModel) get(position).getModel() : null;
	}

	public void setFilter(int flag) {
		if(!hasDataInBoth()) return;

		List<TreePath> expandedpath = getPaths(LEFT);

		getModel(LEFT).setFilter(flag);
		getModel(RIGHT).setFilter(flag);

		for (int i = 0; i < expandedpath.size(); i++) {
			expandPath(LEFT, expandedpath.get(i));
		}
	}

	public void swap() {
		if(!hasDataInBoth()) return;

		List<TreePath> expandedpath = getPaths(LEFT);

		FilteredTreeModel model = getModel(LEFT);
		get(LEFT).setModel(getModel(RIGHT));
		get(RIGHT).setModel(model);

		for (int i = 0; i < expandedpath.size(); i++) {
			expandPath(RIGHT, expandedpath.get(i));
		}

		Container splitPanel = SwingUtilities.getAncestorOfClass(
				JSplitPane.class, get(LEFT));
		if(splitPanel != null) splitPanel.repaint();
	}

	public FilteredTreeModel[] getModels() {
		return new FilteredTreeModel[] { getModel(LEFT), getModel(RIGHT) };
	}

	public DefaultMutableTreeNode getRoot(int position) {
		FilteredTreeModel model = getModel(position);
		return model == null ? null : (DefaultMutableTreeNode) model.getRoot();
	}

	public DefaultMutableTreeNode[] getRoots() {
		return new DefaultMutableTreeNode[] { getRoot(LEFT), getRoot(RIGHT) };
	}

	public void setPaintingFlag(boolean flag) {
		get(LEFT).setpaintingFlag(flag);
		get(RIGHT).setpaintingFlag(flag);
	}

	public void reload(int position) {
		getModel(position).reload();
	}

	public void reload() {
		reload(LEFT);
		reload(RIGHT);
	}

	public List<TreePath> getPaths(int position) {
		return get(position).getPaths();
	}

	public void expandPath(int position, TreePath path) {
		get(position).expandPath(path);
	}

	public void collapsePath(int position, TreePath path) {
		get(position).collapsePath(path);
	}

	public boolean hasDataInBoth() {
		return getRoot(LEFT) != null && getRoot(RIGHT) != null;
	}

	public void clearNodePath(int position) {
		Enumeration<?> myreenode = getRoot(position).depthFirstEnumeration();

		while (myreenode.hasMoreElements()) {
			DefaultMutableTreeNode mynode = (DefaultMutableTreeNode) myreenode.nextElement();
			DiffTreeUserData temp = (DiffTreeUserData) mynode.getUserObject();

			temp.me = null;
			temp.other = null;
			temp.state = DiffTreeUserData.NODE_STATE_NOMAL;
		}
		getModel(position).reload();
	}

	public void mapping() {
		//Log.d("Start Diff- mapping");
		mappingtree(getRoot(LEFT), get(RIGHT));
		mappingtree(getRoot(RIGHT), get(LEFT));
		//Log.d("End Diff- mapping");

		setfolderstate(getRoot(LEFT));
		setfolderstate(getRoot(RIGHT));

		reload();
	}

	private void setfolderstate(DefaultMutableTreeNode rootmynode) {
		Enumeration<?> myreenode = rootmynode.depthFirstEnumeration();

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

	private void mappingtree(DefaultMutableTreeNode rootmynode, JTree othertree) {
		Enumeration<?> myreenode = rootmynode.depthFirstEnumeration();
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
						DiffTreeUserData temp = (DiffTreeUserData) mynode.getUserObject();
						temp.setotherTreepath(temppath);
						temp.setmeTreepath(new TreePath(mynode.getPath()));
						if(temp.Key.length() > 0 && !getUserDatabyTreePath(temppath).compare(temp)) {
							tempstate = DiffTreeUserData.NODE_STATE_DIFF;
						}
						temp.setState(tempstate);
					}
					//right
					DefaultMutableTreeNode node = (DefaultMutableTreeNode) temppath.getLastPathComponent();
					if(node.getUserObject() instanceof DiffTreeUserData) {
						DiffTreeUserData temp = (DiffTreeUserData) node.getUserObject();
						temp.setotherTreepath(leftTreepath);
						temp.setmeTreepath(temppath);
						temp.setState(tempstate);
					}

				} else { //added
					if(mynode.getUserObject() instanceof DiffTreeUserData) {
						DiffTreeUserData temp = (DiffTreeUserData) mynode.getUserObject();
						temp.setotherTreepath(samebefore);
						temp.setmeTreepath(new TreePath(mynode.getPath()));
						temp.setState(DiffTreeUserData.NODE_STATE_ADD);
					}
				}
//			}
		}
	}

	private DiffTreeUserData getUserDatabyTreePath(TreePath path) {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
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
}
