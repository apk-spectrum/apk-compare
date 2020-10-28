package com.apkcompare.gui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
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
	private UiEventHandler evtHandler;
	private DiffTreePair diffTrees;

	private DiffLoadingPanel[] loadingpanel = {null, null};
	private String CurrentmergeapkfilePath[] = {null, null};

	private JTextField[] pathtextfiled = {null, null};
	private JPanel[] cardpanel = {null, null};
	private JSplitPane contentSplitePane;

	//https://www.shareicon.net/diff-94479
	//https://www.shareicon.net/interface-letter-i-info-circle-help-735003
	//https://www.shareicon.net/setting-598385

	public DynamicTreeDemo(ApkComparer apkComparer, UiEventHandler uiEvtHandler) {
		super(new BorderLayout());
		setOpaque(true);

		evtHandler = uiEvtHandler;
		evtHandler.registerKeyStrokeAction(this);

		this.apkComparer = apkComparer;
		if(apkComparer != null) {
			apkComparer.setStatusListener(new ApkComparerListener());
		}

		final JSplitPane[] splitPanels = createSyncedSplitePanes(
			JSplitPaneWithZeroSizeDivider.class,
			JSplitPane.HORIZONTAL_SPLIT,
			new JPanel[][] {
				createPathPanel(evtHandler),
				createDiffPanel(evtHandler),
				createDetailPanel(evtHandler)
			}
		);

		AdjustmentListener scrollListener = new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent evt) {
//				Adjustable source = evt.getAdjustable();
//				if (evt.getValueIsAdjusting()) {
//				  return;
//				}
				splitPanels[1].repaint();
			}
		};

		JScrollPane scrollpane = new JScrollPane(splitPanels[1]);
		scrollpane.getVerticalScrollBar().setUnitIncrement(10);
		scrollpane.getVerticalScrollBar().addAdjustmentListener(scrollListener);
		scrollpane.getHorizontalScrollBar().addAdjustmentListener(scrollListener);

		contentSplitePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		contentSplitePane.setTopComponent(scrollpane);
		contentSplitePane.setBottomComponent(splitPanels[2]);
		contentSplitePane.setDividerLocation(1000);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(splitPanels[0], BorderLayout.NORTH);
		contentPane.add(contentSplitePane, BorderLayout.CENTER);

		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
		toolbar.setPreferredSize(new Dimension(0, 48));

		ArrayList<AbstractButton> buttons = new ArrayList<>();

		FilterAction.addToEventHandler(evtHandler, diffTrees);
		buttons.add(new JToggleButton(evtHandler.getAction(
				FilterAction.getActionCommand(FilteredTreeModel.FLAG_ADD))));
		buttons.add(new JToggleButton(evtHandler.getAction(
				FilterAction.getActionCommand(FilteredTreeModel.FLAG_DIFF))));
		buttons.add(new JToggleButton(evtHandler.getAction(
				FilterAction.getActionCommand(FilteredTreeModel.FLAG_IDEN))));

		buttons.add(new JButton(
				evtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_SETTINGS)));
		buttons.add(new JButton(
				evtHandler.getAction(UiEventHandler.ACT_CMD_SHOW_ABOUT)));

		for(AbstractButton btn: buttons) {
			//btn.setBorderPainted( false );
			//btn.setContentAreaFilled( false );
			btn.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
			btn.setFocusPainted(false);
			btn.setSelected(true);
			toolbar.add(btn);
		}

		add(toolbar, BorderLayout.NORTH);
		add(contentPane, BorderLayout.CENTER);

		repaint();
	}

	private JPanel[] createPathPanel(UiEventHandler handler) {
		JPanel[] pathpanel = new JPanel[2];
		JButton[] btnfileopen = new JButton[2];

		for(int index = 0; index < 2; index++) {
			Integer pos = Integer.valueOf(index);

			pathtextfiled[index] = new JTextField();
			pathtextfiled[index].setPreferredSize(
					new Dimension(0, TEXTFIELD_HEIGHT));
			pathtextfiled[index].setAction(
					handler.getAction(UiEventHandler.ACT_CMD_OPEN_APK));
			pathtextfiled[index].putClientProperty("POSITION", pos);

			btnfileopen[index] = new JButton(
					handler.getAction(UiEventHandler.ACT_CMD_OPEN_APK));
			btnfileopen[index].setBorder(
					BorderFactory.createEmptyBorder(1, 1, 1, 1));
			btnfileopen[index].setPreferredSize(
					new Dimension(TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT));
			btnfileopen[index].putClientProperty("POSITION", pos);

			pathpanel[index] = new JPanel(new BorderLayout());
			pathpanel[index].add(pathtextfiled[index], BorderLayout.CENTER);
			pathpanel[index].add(btnfileopen[index], BorderLayout.EAST);
		}
		return pathpanel;
	}

	private JPanel[] createDiffPanel(UiEventHandler handler) {
		final JPanel[] bordertreepanel = new JPanel[2];
		diffTrees = new DiffTreePair(evtHandler);

		for(int index=0;index <2; index++) {
			final Integer pos = Integer.valueOf(index);

			loadingpanel[index] = new DiffLoadingPanel();

			cardpanel[index] = new JPanel(new CardLayout());
			cardpanel[index].add(diffTrees.get(index), CARD_LAYOUT_TREE);
			cardpanel[index].add(loadingpanel[index], CARD_LAYOUT_LOADING);

			showCardpanel(CARD_LAYOUT_LOADING, index);

			bordertreepanel[index] = new JPanel(new BorderLayout());
			bordertreepanel[index].add(cardpanel[index], BorderLayout.CENTER);

			bordertreepanel[index].setName("FILE_DROP_TOP");
			bordertreepanel[index].putClientProperty("POSITION", pos);
			new FileDrop(bordertreepanel[index], handler);
		}
		return bordertreepanel;
	}

	private JPanel[] createDetailPanel(UiEventHandler handler) {
		final JPanel[] detailPanel = new JPanel[2];

		for(int index=0;index <2; index++) {
			final Integer pos = Integer.valueOf(index);
			detailPanel[index] = new JPanel(new BorderLayout());
			detailPanel[index].setName("FILE_DROP_TOP");
			detailPanel[index].putClientProperty("POSITION", pos);
			new FileDrop(detailPanel[index], handler);
		}
		return detailPanel;
	}

	private JSplitPane[] createSyncedSplitePanes(
			Class<? extends JSplitPane> clazz,
			final int orientation, final JPanel[][] panels) {

		final JSplitPane[] splitPanels = new JSplitPane[panels.length];
		for(int i = 0; i < panels.length; i++) {
			try {
				splitPanels[i] = clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
				return null;
			}
			splitPanels[i].setOrientation(orientation);
			splitPanels[i].setContinuousLayout(true);
			splitPanels[i].setLeftComponent(panels[i][0]);
			splitPanels[i].setRightComponent(panels[i][1]);
			splitPanels[i].setResizeWeight(0.5);
			if(panels.length == 1) break;
			splitPanels[i].addPropertyChangeListener(
					JSplitPane.DIVIDER_LOCATION_PROPERTY,
					new PropertyChangeListener() {
				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if(evt.getOldValue().equals(-1)) return;
					int location = ((Integer) evt.getNewValue()).intValue();
					for(int j = 0; j < panels.length; j++) {
						if(evt.getSource() == splitPanels[j]) continue;
						if(splitPanels[j].getDividerLocation() != location) {
							splitPanels[j].setDividerLocation(location);
						}
					}
				}
			});
		}
		return splitPanels;
	}

	private class ApkComparerListener implements ApkComparer.StatusListener {
		@Override
		public void onStart(final int position) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					evtHandler.unsetFlag(position+1);
					loadingpanel[position].setshow(DiffLoadingPanel.LOADING);
					showCardpanel(CARD_LAYOUT_LOADING, position);
					contentSplitePane.setDividerLocation(1.);
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
					evtHandler.setFlag(position+1);
					int checkFlag = UiEventHandler.FLAG_SET_LEFT_TREE
							| UiEventHandler.FLAG_SET_RIGHT_TREE;
					if((evtHandler.getFlag() & checkFlag) == checkFlag) {
						contentSplitePane.setDividerLocation(.7);
					}
				}
			});
		}
	}

	private void setData(final ApkInfo apkinfodiff1, final int position) {
		//Log.w("start create Tree :" + index);
		pathtextfiled[position].setText(apkinfodiff1.filePath);
		pathtextfiled[position].setCaretPosition(
				pathtextfiled[position].getDocument().getLength());

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
			return;
		}

		Log.w("Create end... not diff :" + position);
	}

	private void showCardpanel(String str, int index) {
		((CardLayout)cardpanel[index].getLayout()).show(cardpanel[index],str);
		cardpanel[index].repaint();
	}
}
