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
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

import com.apkcompare.ApkComparer;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.gui.action.RunApkScannerAction;
import com.apkcompare.resource.RConst;
import com.apkcompare.resource.RProp;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.plugin.PackageSearcher;
import com.apkspectrum.plugin.PlugIn;
import com.apkspectrum.plugin.PlugInEventAdapter;
import com.apkspectrum.plugin.PlugInManager;
import com.apkspectrum.plugin.UpdateChecker;
import com.apkspectrum.swing.FileDrop;
import com.apkspectrum.swing.UIAction;
import com.apkspectrum.util.Log;

public class DynamicTreeDemo extends JPanel
	implements TreeSelectionListener, RConst
{
	private static final long serialVersionUID = -8110312211026585408L;

	private static final int TEXTFIELD_HEIGHT = 28;

	private static final String CARD_LAYOUT_TREE = "CARD_LAYOUT_TREE";
	private static final String CARD_LAYOUT_LOADING = "CARD_LAYOUT_LOADING";

	private ApkComparer apkComparer;
	private UiEventHandler evtHandler;
	private DiffTreePair diffTrees;

	private DiffLoadingPanel[] loadingpanel = {null, null};
	private String filePath[] = {null, null};

	private JTextField[] pathtextfiled = {null, null};
	private JPanel[] cardpanel = {null, null};
	private JSplitPane contentSplitePane;

	private JLabel[] content = new JLabel[2];

	public DynamicTreeDemo(ApkComparer apkComparer, UiEventHandler uiEvtHandler) {
		super(new BorderLayout());
		setOpaque(true);

		try {
			UIManager.setLookAndFeel(RProp.S.CURRENT_THEME.get());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}

		evtHandler = uiEvtHandler;
		evtHandler.registerKeyStrokeAction(this);

		this.apkComparer = apkComparer;
		if(apkComparer != null) {
			apkComparer.setStatusListener(new ApkComparerListener());
		}

		JScrollBar veticalScrollBar = new JScrollBar();
		final JSplitPane[] splitPanels = createSyncedSplitePanes(
			JSplitPaneWithZeroSizeDivider.class,
			JSplitPane.HORIZONTAL_SPLIT,
			new JPanel[][] {
				createPathPanel(evtHandler),
				createDiffPanel(evtHandler, veticalScrollBar),
				createDetailPanel(evtHandler)
			}
		);
		veticalScrollBar.setUnitIncrement(10);
		veticalScrollBar.addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent evt) {
				splitPanels[1].repaint();
			}
		});

		JPanel diffTreePane = new JPanel(new BorderLayout());
		diffTreePane.add(splitPanels[1], BorderLayout.CENTER);
		diffTreePane.add(veticalScrollBar, BorderLayout.EAST);

		int width = ((Integer) UIManager.get("ScrollBar.width")).intValue();
		splitPanels[0].setBorder(BorderFactory.createEmptyBorder(0,0,0,width));
		splitPanels[1].setBorder(BorderFactory.createEmptyBorder());
		splitPanels[2].setBorder(BorderFactory.createEmptyBorder(0,0,0,width));

		contentSplitePane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
		contentSplitePane.setTopComponent(diffTreePane);
		contentSplitePane.setBottomComponent(splitPanels[2]);
		contentSplitePane.setDividerLocation(1000);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(splitPanels[0], BorderLayout.NORTH);
		contentPane.add(contentSplitePane, BorderLayout.CENTER);

		FilterAction.addToEventHandler(evtHandler, diffTrees);

		ArrayList<AbstractButton> buttons = new ArrayList<>();
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

		JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
		for(AbstractButton btn: buttons) {
			btn.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
			btn.setFocusable(false);
			toolbar.add(btn);
		}

		add(toolbar, BorderLayout.NORTH);
		add(contentPane, BorderLayout.CENTER);

		PlugInManager.addPlugInEventListener(new PlugInEventAdapter() {
			@Override
			public void onPluginLoaded() {
		        PlugInManager.checkForUpdatesWithUI(DynamicTreeDemo.this, 1000);
			}

			@Override
			public void onPluginUpdated(UpdateChecker[] list) {
				if(list != null && list.length > 0) {
					// TODO Updated Badge Count
					// setUpdatedBadgeCount(list.length);	
				}
			}
		});
	}

	private JButton makeButtonForPathPanel(Action action, int position) {
		JButton btn = new JButton(action);
		btn.setHideActionText(true);
		btn.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		btn.setPreferredSize(new Dimension(TEXTFIELD_HEIGHT, TEXTFIELD_HEIGHT));
		btn.putClientProperty(POSITION_KEY, position);
		return btn;
	}

	private JPanel[] createPathPanel(UiEventHandler handler) {
		JPanel[] pathpanel = new JPanel[2];

		for(int index = 0; index < 2; index++) {
			Integer pos = Integer.valueOf(index);

			pathtextfiled[index] = new JTextField();
			pathtextfiled[index].setPreferredSize(
					new Dimension(0, TEXTFIELD_HEIGHT));
			pathtextfiled[index].setAction(
					handler.getAction(UiEventHandler.ACT_CMD_OPEN_APK));
			pathtextfiled[index].putClientProperty(POSITION_KEY, pos);

			JPanel tools = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
			tools.add(makeButtonForPathPanel(handler.getAction(
					UiEventHandler.ACT_CMD_OPEN_APK), pos));
			tools.add(makeButtonForPathPanel(handler.getAction(
					RunApkScannerAction.getActionCommand(pos)), pos));

			for(PlugIn plugin: PlugInManager.getPlugInAll()) {
				if(plugin instanceof UpdateChecker
						|| (plugin instanceof PackageSearcher
						&& !((PackageSearcher) plugin).isVisibleToBasic())) {
					continue;
				}
				Action action = plugin.makeAction();
				String actCmd = plugin.getActionCommand() + "_" + pos;
				action.putValue(Action.ACTION_COMMAND_KEY, actCmd);
				action.putValue(UIAction.ACTION_REQUIRED_CONDITIONS, 1 << pos);
				action.putValue(UIAction.ICON_SIZE_KEY, new Dimension(16,16));
				handler.addAction(action);
				tools.add(makeButtonForPathPanel(action, pos));
			}

			pathpanel[index] = new JPanel(new BorderLayout());
			pathpanel[index].add(pathtextfiled[index], BorderLayout.CENTER);
			pathpanel[index].add(tools, BorderLayout.EAST);
		}
		return pathpanel;
	}

	private JPanel[] createDiffPanel(UiEventHandler handler,
			final JScrollBar vertical) {
		final JPanel[] bordertreepanel = new JPanel[2];
		final JScrollPane[] scrollPane = new JScrollPane[2];
		diffTrees = new DiffTreePair(evtHandler);

		for(int idx=0;idx <2; idx++) {
			final Integer pos = Integer.valueOf(idx);
			diffTrees.get(idx).addTreeSelectionListener(this);

			loadingpanel[idx] = new DiffLoadingPanel();
			scrollPane[idx] = new JScrollPane(diffTrees.get(idx),
					JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			final JScrollBar vsb = scrollPane[idx].getVerticalScrollBar();
			vsb.setPreferredSize(new Dimension(0, 0));
			vsb.addAdjustmentListener(
					new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollPane[pos == LEFT ? RIGHT : LEFT]
						.getVerticalScrollBar().setValue(e.getValue());
				}
			});
			vsb.getModel().addChangeListener(
				new ChangeListener() {
					int preMax = -1;
					@Override
					public void stateChanged(ChangeEvent e) {
						BoundedRangeModel model, otherModel;
						model = (BoundedRangeModel) e.getSource();
						if(model.getValueIsAdjusting()
								|| preMax == model.getMaximum()) {
							return;
						}
						preMax = model.getMaximum();
						otherModel = scrollPane[pos == LEFT ? RIGHT : LEFT]
								.getVerticalScrollBar().getModel();
						if(model.getMaximum() >= otherModel.getMaximum()) {
							vertical.setModel(model);
						} else {
							vertical.setModel(otherModel);
						}
					}
				}
			);
			scrollPane[idx].getHorizontalScrollBar().addAdjustmentListener(
					new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					scrollPane[pos == LEFT ? RIGHT : LEFT]
						.getHorizontalScrollBar().setValue(e.getValue());
				}
			});
			vertical.addAdjustmentListener(new AdjustmentListener() {
				@Override
				public void adjustmentValueChanged(AdjustmentEvent e) {
					vsb.setValue(e.getValue());
				}
			});

			cardpanel[idx] = new JPanel(new CardLayout());
			cardpanel[idx].add(scrollPane[idx], CARD_LAYOUT_TREE);
			cardpanel[idx].add(loadingpanel[idx], CARD_LAYOUT_LOADING);

			showCardpanel(CARD_LAYOUT_LOADING, idx);

			bordertreepanel[idx] = new JPanel(new BorderLayout());
			bordertreepanel[idx].add(cardpanel[idx], BorderLayout.CENTER);

			bordertreepanel[idx].setName(FILE_DROP_TOP_KEY);
			bordertreepanel[idx].putClientProperty(POSITION_KEY, pos);
			new FileDrop(bordertreepanel[idx], handler);
		}

		return bordertreepanel;
	}

	private JPanel[] createDetailPanel(UiEventHandler handler) {
		final JPanel[] detailPanel = new JPanel[2];

		for(int index=0;index <2; index++) {
			final Integer pos = Integer.valueOf(index);

			// TODO Make content panels
			content[index] = new JLabel();

			detailPanel[index] = new JPanel(new BorderLayout());
			detailPanel[index].add(content[index]);
			detailPanel[index].setName(FILE_DROP_TOP_KEY);
			detailPanel[index].putClientProperty(POSITION_KEY, pos);
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
			if(panels[i] != null) {
				if(panels[i].length > 0 && panels[i][0] != null) {
					splitPanels[i].setLeftComponent(panels[i][0]);
				}
				if(panels[i].length > 1 && panels[i][1] != null) {
					splitPanels[i].setRightComponent(panels[i][1]);
				}
			}
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
			if (filePath[LEFT] != null && filePath[RIGHT] != null
					&& filePath[LEFT].equals(apkComparer.getApkInfo(LEFT).filePath)
					&& filePath[RIGHT].equals(apkComparer.getApkInfo(RIGHT).filePath)) {
				diffTrees.reload(position);
				Log.w("in sync Create end... not diff:" + position);
				return;
			}

			filePath[LEFT] = apkComparer.getApkInfo(LEFT).filePath;
			filePath[RIGHT] = apkComparer.getApkInfo(RIGHT).filePath;
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

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(!(e.getSource() instanceof DiffTree)) return;

		DiffTree tree = (DiffTree) e.getSource();
		int position = tree.getPosition();

		if(tree.getSelectionPath() == null) {
			content[position].setText("No have resource");
			return;
		}

		DefaultMutableTreeNode node;
		node = (DefaultMutableTreeNode) e.getPath().getLastPathComponent();

		Object usrObj = node.getUserObject();
		if(usrObj instanceof DiffTreeUserData) {
			DiffTreeUserData data = (DiffTreeUserData) usrObj;
			Log.i(position + ", " + data.toString());

			// TODO set data to content panel
			content[position].setText(data.toString());
		}
	}
}
