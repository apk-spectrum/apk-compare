package com.apkcompare.resource;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

import com.apkspectrum.resource.LanguageChangeListener;
import com.apkspectrum.resource.ResComp;
import com.apkspectrum.resource.ResImage;
import com.apkspectrum.resource.ResString;

public enum RComp implements ResComp<Object>
{
	BTN_TOOLBAR_OPEN				(RStr.BTN_OPEN, RImg.TOOLBAR_OPEN, RStr.BTN_OPEN_LAB),
	BTN_TOOLBAR_OPEN_PACKAGE		(RStr.BTN_OPEN_PACKAGE, RImg.TOOLBAR_PACKAGETREE, RStr.BTN_OPEN_PACKAGE_LAB),
	BTN_TOOLBAR_MANIFEST			(RStr.BTN_MANIFEST, RImg.TOOLBAR_MANIFEST, RStr.BTN_MANIFEST_LAB),
	BTN_TOOLBAR_EXPLORER			(RStr.BTN_EXPLORER, RImg.TOOLBAR_EXPLORER, RStr.BTN_EXPLORER_LAB),
	BTN_TOOLBAR_OPEN_CODE			(RStr.BTN_OPENCODE, RImg.TOOLBAR_OPENCODE, RStr.BTN_OPENCODE_LAB),
	BTN_TOOLBAR_OPEN_CODE_LODING	(RStr.BTN_OPENING_CODE, RImg.TOOLBAR_LOADING_OPEN_JD, RStr.BTN_OPENING_CODE_LAB),
	BTN_TOOLBAR_SEARCH				(RStr.BTN_SEARCH, RImg.TOOLBAR_SEARCH, RStr.BTN_SEARCH_LAB),
	BTN_TOOLBAR_PLUGIN_EXTEND		(RStr.BTN_MORE, RImg.TOOLBAR_OPEN_ARROW, RStr.BTN_MORE_LAB),
	BTN_TOOLBAR_INSTALL				(RStr.BTN_INSTALL, RImg.TOOLBAR_INSTALL, RStr.BTN_INSTALL_LAB),
	BTN_TOOLBAR_INSTALL_UPDATE		(RStr.BTN_INSTALL_UPDATE, RImg.TOOLBAR_INSTALL, RStr.BTN_INSTALL_UPDATE_LAB),
	BTN_TOOLBAR_INSTALL_DOWNGRADE	(RStr.BTN_INSTALL_DOWNGRAD, RImg.TOOLBAR_INSTALL, RStr.BTN_INSTALL_DOWNGRAD_LAB),
	BTN_TOOLBAR_LAUNCH				(RStr.BTN_LAUNCH, RImg.TOOLBAR_LAUNCH, RStr.BTN_LAUNCH_LAB),
	BTN_TOOLBAR_SIGN				(RStr.BTN_SIGN, RImg.TOOLBAR_SIGNNING, RStr.BTN_SIGN_LAB),
	BTN_TOOLBAR_INSTALL_EXTEND		(RStr.BTN_MORE, RImg.TOOLBAR_OPEN_ARROW, RStr.BTN_MORE_LAB),
	BTN_TOOLBAR_SETTING				(RStr.BTN_SETTING, RImg.TOOLBAR_SETTING, RStr.BTN_SETTING_LAB),
	BTN_TOOLBAR_ABOUT				(RStr.BTN_ABOUT, RImg.TOOLBAR_ABOUT, RStr.BTN_ABOUT_LAB),

	BTN_OPEN_WITH_SYSTEM_SET		(RStr.LABEL_OPEN_WITH_SYSTEM, RImg.RESOURCE_TREE_OPEN_ICON, RStr.LABEL_OPEN_WITH_SYSTEM),
	BTN_OPEN_WITH_JD_GUI			(RStr.LABEL_OPEN_WITH_JDGUI, RImg.RESOURCE_TREE_JD_ICON, RStr.LABEL_OPEN_WITH_JDGUI),
	BTN_OPEN_WITH_JADX_GUI			(RStr.LABEL_OPEN_WITH_JADXGUI, RImg.RESOURCE_TREE_JADX_ICON, RStr.LABEL_OPEN_WITH_JADXGUI),
	BTN_OPEN_WITH_BYTECODE_VIEWER	(RStr.LABEL_OPEN_WITH_BYTECODE, RImg.RESOURCE_TREE_BCV_ICON, RStr.LABEL_OPEN_WITH_BYTECODE),
	BTN_OPEN_WITH_APK_SCANNER		(RStr.LABEL_OPEN_WITH_SCANNER, RImg.APP_ICON, RStr.LABEL_OPEN_WITH_SCANNER),
	BTN_OPEN_WITH_EXPLORER			(RStr.LABEL_OPEN_WITH_EXPLORER, RImg.TOOLBAR_EXPLORER, RStr.LABEL_OPEN_WITH_EXPLORER),
	BTN_OPEN_WITH_TEXTVIEWER		(RStr.LABEL_OPEN_TO_TEXTVIEWER, RImg.RESOURCE_TREE_OPEN_TO_TEXT, RStr.LABEL_OPEN_TO_TEXTVIEWER),
	BTN_OPEN_WITH_CHOOSER			(RStr.LABEL_OPEN_WITH_CHOOSE, RImg.RESOURCE_TREE_OPEN_OTHERAPPLICATION_ICON, RStr.LABEL_OPEN_WITH_CHOOSE),
	BTN_OPEN_WITH_LOADING			(RStr.BTN_OPENING_CODE, RImg.RESOURCE_TREE_OPEN_JD_LOADING, RStr.BTN_OPENING_CODE),

	MENU_TOOLBAR_NEW_WINDOW			(RStr.MENU_NEW),
	MENU_TOOLBAR_NEW_EMPTY			(RStr.MENU_NEW_WINDOW, RImg.TOOLBAR_MANIFEST.getImageIcon(16,16)),
	MENU_TOOLBAR_NEW_APK			(RStr.MENU_NEW_APK_FILE, RImg.TOOLBAR_OPEN.getImageIcon(16,16)),
	MENU_TOOLBAR_NEW_PACKAGE		(RStr.MENU_NEW_PACKAGE, RImg.TOOLBAR_PACKAGETREE.getImageIcon(16,16)),
	MENU_TOOLBAR_OPEN_APK			(RStr.MENU_APK_FILE, RImg.TOOLBAR_OPEN.getImageIcon(16,16)),
	MENU_TOOLBAR_OPEN_PACKAGE		(RStr.MENU_PACKAGE, RImg.TOOLBAR_PACKAGETREE.getImageIcon(16,16)),
	MENU_TOOLBAR_INSTALL_APK		(RStr.MENU_INSTALL, RImg.TOOLBAR_INSTALL.getImageIcon(16,16)),
	MENU_TOOLBAR_UNINSTALL_APK		(RStr.MENU_UNINSTALL, RImg.TOOLBAR_UNINSTALL.getImageIcon(16,16)),
	MENU_TOOLBAR_CLEAR_DATA			(RStr.MENU_CLEAR_DATA, RImg.TOOLBAR_CLEAR.getImageIcon(16,16)),
	MENU_TOOLBAR_INSTALLED_CHECK	(RStr.MENU_CHECK_INSTALLED, RImg.TOOLBAR_PACKAGETREE.getImageIcon(16,16)),
	MENU_TOOLBAR_DECODER_JD_GUI		(RStr.MENU_DECODER_JD_GUI),
	MENU_TOOLBAR_DECODER_JADX_GUI	(RStr.MENU_DECODER_JADX_GUI),
	MENU_TOOLBAR_DECODER_BYTECODE	(RStr.MENU_DECODER_BYTECODE),
	MENU_TOOLBAR_SEARCH_RESOURCE	(RStr.MENU_SEARCH_RESOURCE),
	MENU_TOOLBAR_EXPLORER_ARCHIVE	(RStr.MENU_EXPLORER_ARCHIVE),
	MENU_TOOLBAR_EXPLORER_FOLDER	(RStr.MENU_EXPLORER_FOLDER),
	MENU_TOOLBAR_LAUNCH_LAUNCHER	(RStr.MENU_LAUNCH_LAUNCHER),
	MENU_TOOLBAR_LAUNCH_SELECT		(RStr.MENU_LAUNCH_SELECT),
	MENU_TOOLBAR_SELECT_DEFAULT		(RStr.MENU_SELECT_DEFAULT),
	MENU_TOOLBAR_SEARCH_BY_PACKAGE	(RStr.LABEL_BY_PACKAGE_NAME),
	MENU_TOOLBAR_SEARCH_BY_NAME		(RStr.LABEL_BY_APP_LABEL),
	MENU_TOOLBAR_TO_BASIC_INFO		(RStr.MENU_VISIBLE_TO_BASIC_EACH),

	TABBED_BASIC_INFO				(RStr.TAB_BASIC_INFO, RStr.TAB_BASIC_INFO),
	TABBED_APEX_INFO				(RStr.TAB_APEX_INFO, RStr.TAB_APEX_INFO),
	TABBED_WIDGET					(RStr.TAB_WIDGETS, RStr.TAB_WIDGETS),
	TABBED_LIBRARIES				(RStr.TAB_LIBRARIES, RStr.TAB_LIBRARIES),
	TABBED_RESOURCES				(RStr.TAB_RESOURCES, RStr.TAB_RESOURCES),
	TABBED_COMPONENTS				(RStr.TAB_COMPONENTS, RStr.TAB_COMPONENTS),
	TABBED_SIGNATURES				(RStr.TAB_SIGNATURES, RStr.TAB_SIGNATURES),

	LABEL_XML_CONSTRUCTION			(RStr.COMPONENT_LABEL_XML),
	COMPONENT_FILTER_PROMPT_XML		(RStr.COMPONENT_FILTER_PROMPT_XML),
	COMPONENT_FILTER_PROMPT_NAME	(RStr.COMPONENT_FILTER_PROMPT_NAME),
	; // ENUM END

	private static Map<Window, Map<Component, RComp>> map = new HashMap<>();
	static {
		RStr.addLanguageChangeListener(new LanguageChangeListener() {
			@Override
			public void languageChange(String oldLang, String newLang) {
				for(Map<Component, RComp> w: map.values()) {
					for(Entry<Component, RComp> e: w.entrySet()) {
						e.getValue().applyText(e.getKey());
					}
				}
			}
		});
	}

	private ResString<?> text, toolTipText;
	private ResImage<?> image;
	private Icon icon;
	private Dimension iconSize;

	private RComp(ResString<?> text) {
		this(text, (Icon) null, null);
	}

	private RComp(ResString<?> text, ResString<?> toolTipText) {
		this(text, (Icon) null, toolTipText);
	}

	private RComp(ResString<?> text, ResImage<?> image) {
		this(text, image, null);
	}

	private RComp(ResString<?> text, Icon icon) {
		this(text, icon, null);
	}

	private RComp(ResString<?> text, ResImage<?> image, ResString<?> toolTipText) {
		this(text, (Icon) null, toolTipText);
		this.image = image;
	}

	private RComp(ResString<?> text, Icon icon, ResString<?> toolTipText) {
		this.text = text;
		this.icon = icon;
		this.toolTipText = toolTipText;
	}

	@Override
	public RComp get() {
		return this;
	}

	@Override
	public String getText() {
		return text != null ? text.getString() : null;
	}

	@Override
	public Icon getIcon() {
		if(image == null) return icon;
		if(iconSize == null) return image.getImageIcon();
		return image.getImageIcon(iconSize.width, iconSize.height);
	}

	@Override
	public String getToolTipText() {
		return toolTipText != null ? toolTipText.getString() : null;
	}

	@Override
	public void setIconSize(Dimension iconSize) {
		this.iconSize = iconSize;
	}

	private void apply(Component c) {
		applyText(c);
		applyIcon(c);
	}

	private void applyIcon(Component c) {
		if(c == null) return;
		Icon icon = getIcon();
		if(icon == null) return;
		if(c instanceof AbstractButton) {
			AbstractButton btn = (AbstractButton) c;
			if(c.isEnabled()) {
				btn.setIcon(icon);
				btn.setDisabledIcon(null);
			} else {
				btn.setDisabledIcon(icon);
			}
		} else if(c instanceof JLabel) {
			((JLabel) c).setIcon(icon);
		}
		c.firePropertyChange(RCOMP_SET_ICON_KEY, 0, 1);
	}

	private void applyText(Component c) {
		if(c == null || (text == null && toolTipText == null)) return;
		if(text != null) {
			if(c instanceof AbstractButton) {
				((AbstractButton) c).setText(text.getString());
			} else if(c instanceof JLabel) {
				((JLabel) c).setText(text.getString());
			} else {
				c.setName(text.getString());
			}
		}
		if(c instanceof JComponent && toolTipText != null) {
			((JComponent)c).setToolTipText(toolTipText.getString());
		}
		c.firePropertyChange(RCOMP_SET_TEXT_KEY, 0, 1);
	}

	@Override
	public void set(Component c) {
		apply(c);
		register(c);
	}

	private void register(Component c) {
		if(c == null) return;
		register(SwingUtilities.getWindowAncestor(c), c);
	}

	private void register(final Window window, final Component c) {
		if(c == null) return;

		Map<Component, RComp> matchMap = map.get(window);
		if(matchMap == null) {
			matchMap = new HashMap<>();
			map.put(window, matchMap);
			if(window != null) {
				window.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						window.removeWindowListener(this);
						unregister(window, null);
					}
					@Override
					public void windowClosed(WindowEvent e) {
						window.removeWindowListener(this);
						unregister(window, null);
					}
				});
			}
		} else {
			if(matchMap.containsKey(c)) matchMap.remove(c);
		}
		matchMap.put(c, this);

		if(window == null) {
			c.addHierarchyListener(new HierarchyListener() {
				@Override
				public void hierarchyChanged(HierarchyEvent e) {
					if(e.getID() != HierarchyEvent.HIERARCHY_CHANGED)
						return;
					Window win = SwingUtilities.getWindowAncestor(c);
					if(win != null) {
						unregister(null, c);
						register(win, c);
						c.removeHierarchyListener(this);
					}
				}
			});
		}
	}

	private void unregister(Window window, Component c) {
		Map<Component, RComp> matchMap = map.get(window);
		if(matchMap != null) {
			if(c != null) {
				matchMap.remove(c);
			}
			if(matchMap.isEmpty() || c == null) {
				map.remove(window);
			}
		}
	}
}