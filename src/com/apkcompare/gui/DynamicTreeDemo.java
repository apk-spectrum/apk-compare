package com.apkcompare.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.util.Arrays;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import com.apkcompare.ApkComparer;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.swing.FileDrop;
import com.apkspectrum.util.Log;

public class DynamicTreeDemo extends JPanel
{
	private static final long serialVersionUID = -8110312211026585408L;

	private static final int LEFT = DiffTree.LEFT;
	private static final int RIGHT = DiffTree.RIGHT;
	private static final int TEXTFIELD_HEIGHT = 28;

	private static final String CARD_LAYOUT_TREE = "CARD_LAYOUT_TREE";
	private static final String CARD_LAYOUT_LOADING = "CARD_LAYOUT_LOADING";

	private ApkComparer apkComparer;

	private DiffTreePair diffTrees;

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

		diffTrees = new DiffTreePair(uiEvtHandler);

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

		btnadd = new JToggleButton(new FilterAction(diffTrees, FilteredTreeModel.FLAG_ADD));
		btndiff = new JToggleButton(new FilterAction(diffTrees, FilteredTreeModel.FLAG_DIFF));
		btniden = new JToggleButton(new FilterAction(diffTrees, FilteredTreeModel.FLAG_IDEN));

		JButton btnsetting = new JButton(
				uiEvtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_SETTINGS));
		JButton btninfo = new JButton(
				uiEvtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_ABOUT));

		for(AbstractButton btn: Arrays.asList(btniden, btnadd, btndiff, btnsetting, btninfo)) {
			//btn.setBorderPainted( false );
			//btn.setContentAreaFilled( false );
			btn.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			btn.setFocusPainted(false);
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

			cardpanel[index].add(diffTrees.get(index), CARD_LAYOUT_TREE);
			cardpanel[index].add(loadingpanel[index], CARD_LAYOUT_LOADING);

			((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],CARD_LAYOUT_LOADING);

			bordertreepanel[index].add(pathpanel[index], BorderLayout.NORTH);
			bordertreepanel[index].add(cardpanel[index], BorderLayout.CENTER);
		}
		return bordertreepanel;
	}

	private void setFileDrop(FileDrop.Listener listener) {
		for(JComponent com: Arrays.asList(loadingpanel[LEFT].getEmptyPanel(), diffTrees.get(LEFT))) {
			com.setName("FILE_DROP_TOP");
			com.putClientProperty("POSITION", Integer.valueOf(LEFT));
			new FileDrop(com, listener);
		}
		for(JComponent com: Arrays.asList(loadingpanel[RIGHT].getEmptyPanel(), diffTrees.get(RIGHT))) {
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
					setData(apkComparer.getApkInfo(position), position);
				}
			});
		}
	}

	private void setData(final ApkInfo apkinfodiff1, final int position) {
		//Log.w("start create Tree :" + index);
		pathtextfiled[position].setText(apkinfodiff1.filePath);
		pathtextfiled[position].setCaretPosition(pathtextfiled[position].getDocument().getLength());

		diffTrees.createTreeNode(position, apkinfodiff1);
		showCardpanel(CARD_LAYOUT_TREE, position);

		Log.w("end create Tree :" + position);

		if (diffTrees.hasDataInBoth()) {
			if (CurrentmergeapkfilePath[LEFT] != null && CurrentmergeapkfilePath[RIGHT] != null
					&& CurrentmergeapkfilePath[LEFT].equals(apkComparer.getApkInfo(LEFT).filePath)
					&& CurrentmergeapkfilePath[RIGHT].equals(apkComparer.getApkInfo(RIGHT).filePath)) {
				diffTrees.reload(position);
				Log.w("in sync Create end... not diff:" + position);
				return;
			}

			CurrentmergeapkfilePath[LEFT] = apkComparer.getApkInfo(LEFT).filePath;
			CurrentmergeapkfilePath[RIGHT] = apkComparer.getApkInfo(RIGHT).filePath;
			// Log.w("change filepath" + index);

			diffTrees.setPaintingFlag(false);
			diffTrees.clearNodePath(position == LEFT ? RIGHT : LEFT);

			// new Thread(){
			// public void run(){
			this.setCursor(new Cursor(Cursor.WAIT_CURSOR));
			Log.w("start diff :" + position);

			diffTrees.mapping();
			repaint();

			this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));

			diffTrees.reload();
			diffTrees.setPaintingFlag(true);

			Log.w("end diff :" + position);
			setEnableToggleBtn(true);
			return;
		}

		Log.w("Create end... not diff :" + position);
	}

	private void showCardpanel(String str, int index) {
		((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],str);
		cardpanel[index].repaint();
	}
}
