package com.apkcompare.gui.action;

import java.awt.event.ActionEvent;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.resource.RProp;
import com.apkspectrum.swing.ActionEventHandler;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.SystemUtil;

@SuppressWarnings("serial")
public class OpenDiffTreeFileAction extends AbstractApkScannerAction
{
	public static final String ACTION_COMMAND = "ACT_CMD_OPEN_DIFF_TREE_FILE";

	public OpenDiffTreeFileAction(ActionEventHandler h) { super(h); }

	@Override
	public void actionPerformed(ActionEvent e) {
		final JTree resTree = (JTree) e.getSource();
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) resTree.getLastSelectedPathComponent();
		if (node == null || !node.isLeaf() || !(node.getUserObject() instanceof DiffTreeUserData)) {
			return;
		}
		final DiffTreeUserData resObj = (DiffTreeUserData) node.getUserObject();
		if ((!node.isLeaf() || resObj.isfolder) && !node.isRoot() ) return;

		if(resObj.state == DiffTreeUserData.NODE_STATE_NOMAL || resObj.state == DiffTreeUserData.NODE_STATE_ADD	 || node.isRoot()) {
			Log.d("open program : " + resObj);
			SystemUtil.openFile(resObj.makeFilebyNode());
		} else if(resObj.state == DiffTreeUserData.NODE_STATE_DIFF) {
			Log.d("open diff program : " + resObj.state);
			String openner = RProp.S.DIFF_TOOL.get();
			DiffTreeUserData othertemp = null;
			DefaultMutableTreeNode otherNode = (DefaultMutableTreeNode)resObj.other.getLastPathComponent();
			if(otherNode.getUserObject() instanceof DiffTreeUserData) {
				othertemp = (DiffTreeUserData)otherNode.getUserObject();
			}
			SystemUtil.exec(new String[]{openner, resObj.makeFilebyNode().getAbsolutePath(),
					othertemp.makeFilebyNode().getAbsolutePath()});
//			if(!result) {
//				MessageBoxPane.showError(Main.frame, "please check Diff program" + "(" + openner+ ")");
//			}
		}
	}
}
