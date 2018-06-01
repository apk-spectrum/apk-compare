package com.apkcompare.gui.dialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileSystemView;

import com.android.ddmlib.AdbVersion;
import com.apkscanner.gui.util.ApkFileChooser;
import com.apkscanner.jna.FileInfo;
import com.apkscanner.jna.FileVersion;
import com.apkscanner.resource.Resource;
import com.apkscanner.tool.adb.AdbVersionManager;
import com.apkscanner.util.SystemUtil;

public class SettingDlg extends JDialog implements ActionListener{
	private JComboBox<String> jcbLanguage;
	private String propStrLanguage;
	private JComboBox<String> jcbEditors;
	private String propStrEditorPath;
	private ArrayList<String> propRecentEditors;
	private static final String ACT_CMD_EDITOR_EXPLOERE = "ACT_CMD_EDITOR_EXPLOERE";
	private static final String ACT_CMD_CREATE_SHORTCUT = "ACT_CMD_CREATE_SHORTCUT";
	private static final String ACT_CMD_ASSOCIATE_APK_FILE = "ACT_CMD_ASSOCIATE_APK_FILE";

	private static final String ACT_CMD_SAVE = "ACT_CMD_SAVE";
	private static final String ACT_CMD_EXIT = "ACT_CMD_EXIT";
	
	private static String fontOfTheme;
	
	public SettingDlg(Window owner) {
		super(owner);

		readSettings();

		Object font = UIManager.get("Label.font");
		UIManager.put("Label.font", null);
		fontOfTheme = new Font(new JLabel().getFont().getFamily(), Font.PLAIN, 12).getFamily();
		UIManager.put("Label.font", font);

		initialize(owner);
	}
	
	private void initialize(Window window)
	{
		setTitle(Resource.STR_SETTINGS_TITLE.getString());
		setIconImage(Resource.IMG_DIFF_TOOLBAR_SETTING.getImageIcon().getImage());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setSize(new Dimension(600,420));
		setResizable(true);
		setLocationRelativeTo(window);
		setModal(true);
		
		JPanel ctrPanel = new JPanel(new FlowLayout());
		JButton savebutton = new JButton(Resource.STR_BTN_SAVE.getString());
		savebutton.setActionCommand(ACT_CMD_SAVE);
		savebutton.addActionListener(this);
		savebutton.setFocusable(false);
		ctrPanel.add(savebutton);

		JButton exitbutton = new JButton(Resource.STR_BTN_CANCEL.getString());
		exitbutton.setActionCommand(ACT_CMD_EXIT);
		exitbutton.addActionListener(this);
		exitbutton.setFocusable(false);
		ctrPanel.add(exitbutton);

		JPanel contentPane = new JPanel(new BorderLayout());
		contentPane.add(makeGenericPanel(), BorderLayout.CENTER);
		contentPane.add(ctrPanel, BorderLayout.SOUTH);

		getContentPane().add(contentPane);

		KeyStroke escape = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0, false);
		getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escape, "ESCAPE");
		getRootPane().getActionMap().put("ESCAPE", new AbstractAction() {
			private static final long serialVersionUID = -8988954049940512230L;
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		
	}
	
	private class EditorItemRenderer extends JLabel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = -151339243781300421L;

		private class CellItem {
			public CellItem(String path) {
				File file = new File(path);
				text = getFileDescription(path);
				if(text == null || text.isEmpty()) {
					text = file.getName();
				}
				icon = FileSystemView.getFileSystemView().getSystemIcon(file);
			}
			String text;
			Icon icon;
		}
		private HashMap<String, CellItem> items;

		public EditorItemRenderer() {
			setOpaque(false);
			setHorizontalAlignment(LEFT);
			setVerticalAlignment(CENTER);

			items = new HashMap<String, CellItem> ();
		}

		private CellItem getCellItem(String path) {
			if(items.containsKey(path)) {
				return items.get(path);
			}
			CellItem cellItem = new CellItem(path);
			items.put(path, cellItem);
			return cellItem;
		}

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			CellItem cellItem = getCellItem((String) value);
			setText(cellItem.text);
			setIcon(cellItem.icon);
			return this;
		}

		private String getFileDescription(String filePath) {
			String desc = null;
			if(SystemUtil.isWindows()) {
				try {
					FileVersion fileVersion = new FileVersion(filePath);
					for(FileInfo info : fileVersion.getFileInfos())
					{
						desc = info.getFileDescription();
						if(desc != null && !desc.isEmpty()) {
							break;
						}
					}
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
			return desc;
		}
	}
	
	JPanel makeGenericPanel() {
		JPanel panel = new JPanel(new GridBagLayout());
		panel.setOpaque(true);

		//GridBagConstraints(int gridx, int gridy, int gridwidth, int gridheight, double weightx, double weighty, int anchor, int fill, Insets insets, int ipadx, int ipady) 
		GridBagConstraints rowHeadConst = new GridBagConstraints(0,0,1,1,0,0,GridBagConstraints.EAST,GridBagConstraints.NONE,new Insets(10,10,0,10),0,0);
		GridBagConstraints contentConst = new GridBagConstraints(1,0,1,1,1,0,GridBagConstraints.WEST,GridBagConstraints.NONE,new Insets(10,0,0,0),0,0);

		panel.add(new JLabel(Resource.STR_SETTINGS_LANGUAGE.getString()), rowHeadConst);

		jcbLanguage = new JComboBox<String>(Resource.getSupportedLanguages());
		jcbLanguage.setRenderer(new ResourceLangItemRenderer());
		jcbLanguage.setSelectedItem(propStrLanguage);
		propStrLanguage = (String)jcbLanguage.getSelectedItem();
		panel.add(jcbLanguage, contentConst);

		rowHeadConst.gridy++;
		contentConst.gridy++;

		panel.add(new JLabel(Resource.STR_SETTINGS_EDITOR.getString()), rowHeadConst);

		final JTextField editorPath = new JTextField();
		editorPath.setEditable(false);

		jcbEditors = new JComboBox<String>();
		jcbEditors.setRenderer(new EditorItemRenderer());
		jcbEditors.setEditable(false);
		jcbEditors.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				editorPath.setText(arg0.getItem().toString());
			}

		});

		if(propStrEditorPath != null) {
			jcbEditors.addItem(propStrEditorPath);
		}
		for(String editor: propRecentEditors) {
			jcbEditors.addItem(editor);
		}

		if(SystemUtil.isWindows()) {
			try {
				for(String suffix: new String[]{".txt", "txtfile", "textfile", ".xml", ".log"}) {
					String cmdLine = SystemUtil.getOpenCommand(suffix);
					if(cmdLine != null && cmdLine.indexOf("%1") >= 0) {
						String cmd = cmdLine.replaceAll("\"?(.*\\.[eE][xX][eE])\"?.*", "$1");
						if(!cmd.equals(cmdLine)) {
							String path = SystemUtil.getRealPath(cmd);
							if(path != null && !propRecentEditors.contains(path) && !path.equalsIgnoreCase(propStrEditorPath)) {
								jcbEditors.addItem(path);
							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		JButton btnExplorer = new JButton(Resource.STR_BTN_SELF_SEARCH.getString());
		btnExplorer.setToolTipText(Resource.STR_BTN_SELF_SEARCH_LAB.getString());
		btnExplorer.setMargin(new Insets(-1,10,-1,10));
		btnExplorer.setActionCommand(ACT_CMD_EDITOR_EXPLOERE);
		btnExplorer.addActionListener(this);


		JPanel txtEditPane = new JPanel(new BorderLayout(5,5));
		txtEditPane.add(jcbEditors, BorderLayout.CENTER);
		txtEditPane.add(btnExplorer, BorderLayout.EAST);
		txtEditPane.add(editorPath, BorderLayout.SOUTH);

		contentConst.fill = GridBagConstraints.HORIZONTAL;
		panel.add(txtEditPane, contentConst);
		contentConst.fill = GridBagConstraints.NONE;

		rowHeadConst.gridy++;
		contentConst.gridy++;

		if(SystemUtil.isWindows()) {
			JPanel etcBtnPanel = new JPanel();
			if(!SystemUtil.hasShortCut()) {
				JButton btnShortcut = new JButton(Resource.STR_BTN_CREATE_SHORTCUT.getString());
				btnShortcut.setToolTipText(Resource.STR_BTN_CREATE_SHORTCUT_LAB.getString());
				btnShortcut.setActionCommand(ACT_CMD_CREATE_SHORTCUT);
				btnShortcut.addActionListener(this);
				btnShortcut.setIcon(Resource.IMG_ADD_TO_DESKTOP.getImageIcon(32,32));
				btnShortcut.setVerticalTextPosition(JLabel.BOTTOM);
				btnShortcut.setHorizontalTextPosition(JLabel.CENTER);
				etcBtnPanel.add(btnShortcut);
			}

			JButton btnAssociate = new JButton();
			if(!SystemUtil.isAssociatedWithFileType(".apk")) {
				btnAssociate.setText(Resource.STR_BTN_ASSOC_FTYPE.getString());
				btnAssociate.setToolTipText(Resource.STR_BTN_ASSOC_FTYPE_LAB.getString());
				btnAssociate.setIcon(Resource.IMG_ASSOCIATE_APK.getImageIcon(32,32));
			} else {
				btnAssociate.setText(Resource.STR_BTN_UNASSOC_FTYPE.getString());
				btnAssociate.setToolTipText(Resource.STR_BTN_UNASSOC_FTYPE_LAB.getString());
				btnAssociate.setIcon(Resource.IMG_UNASSOCIATE_APK.getImageIcon(32,32));
			}
			btnAssociate.setActionCommand(ACT_CMD_ASSOCIATE_APK_FILE);
			btnAssociate.addActionListener(this);
			btnAssociate.setVerticalTextPosition(JLabel.BOTTOM);
			btnAssociate.setHorizontalTextPosition(JLabel.CENTER);

			etcBtnPanel.add(btnAssociate);

			panel.add(etcBtnPanel, contentConst);

			rowHeadConst.gridy++;
			contentConst.gridy++;
		}

		rowHeadConst.gridwidth = 2;
		rowHeadConst.weighty = 1;
		panel.add(new JPanel(), rowHeadConst);

		return panel;
	}
	
	private class ResourceLangItemRenderer extends JLabel implements ListCellRenderer<Object> {
		private static final long serialVersionUID = 3001512366576666099L;

		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			if(value.toString().isEmpty()) {
				setText("default - en");
			} else {
				setText(value.toString());
			}
			return this;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		String actCommand = e.getActionCommand();

		if(ACT_CMD_EDITOR_EXPLOERE.equals(actCommand)) {
			JFileChooser jfc = new JFileChooser();										
			if(jfc.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)
				return;

			File dir = jfc.getSelectedFile();
			if(dir!=null) {
				String path = dir.getPath();
				jcbEditors.addItem(path);
				jcbEditors.setSelectedItem(path);
			}
		} else if(ACT_CMD_CREATE_SHORTCUT.equals(actCommand)) {
			SystemUtil.createShortCut();
			if(SystemUtil.hasShortCut()) {
				((JButton)e.getSource()).setVisible(false);
			}			
		} else if(ACT_CMD_ASSOCIATE_APK_FILE.equals(actCommand)) {
			JButton btn = (JButton)e.getSource();
			if(!SystemUtil.isAssociatedWithFileType(".apk")) {
				SystemUtil.setAssociateFileType(".apk");
				btn.setText(Resource.STR_BTN_UNASSOC_FTYPE.getString());
				btn.setToolTipText(Resource.STR_BTN_UNASSOC_FTYPE_LAB.getString());
				btn.setIcon(Resource.IMG_UNASSOCIATE_APK.getImageIcon(32,32));
			} else {
				SystemUtil.unsetAssociateFileType(".apk");
				btn.setText(Resource.STR_BTN_ASSOC_FTYPE.getString());
				btn.setToolTipText(Resource.STR_BTN_ASSOC_FTYPE_LAB.getString());
				btn.setIcon(Resource.IMG_ASSOCIATE_APK.getImageIcon(32,32));
			}		
		} else if(ACT_CMD_SAVE.equals(actCommand)) {
			saveSettings();
			this.dispose();
		} else if(ACT_CMD_EXIT.equals(actCommand)) {
			this.dispose();
		}
	}
	
	private void readSettings()
	{
		propStrLanguage = (String)Resource.PROP_LANGUAGE.getData();

		propStrEditorPath = SystemUtil.getRealPath((String)Resource.PROP_EDITOR.getData());

		String recentEditors = (String)Resource.PROP_RECENT_EDITOR.getData();
		propRecentEditors = new ArrayList<String>();
		for(String s: recentEditors.split(File.pathSeparator)) {
			if(!s.isEmpty()) {
				String realPath = SystemUtil.getRealPath(s);
				if(realPath != null && !realPath.equalsIgnoreCase(propStrEditorPath)) {
					propRecentEditors.add(s);
				}
			}
		}
	}

	private void saveSettings()
	{
		if(!propStrLanguage.equals(jcbLanguage.getSelectedItem())) {
			Resource.PROP_LANGUAGE.setData(jcbLanguage.getSelectedItem());
		}

		if(!jcbEditors.getSelectedItem().equals(propStrEditorPath)){
			String editorPath = SystemUtil.getRealPath((String)jcbEditors.getSelectedItem());
			if(propRecentEditors.contains(editorPath)) {
				propRecentEditors.remove(editorPath);
			}
			if(propStrEditorPath != null) {
				propRecentEditors.add(0, propStrEditorPath);
			}
			Resource.PROP_EDITOR.setData(editorPath);

			StringBuilder recentEditors = new StringBuilder();
			for(String editor: propRecentEditors) {
				recentEditors.append(editor);
				recentEditors.append(File.pathSeparator);
			}
			Resource.PROP_RECENT_EDITOR.setData(recentEditors.toString());
		}
	}
}
