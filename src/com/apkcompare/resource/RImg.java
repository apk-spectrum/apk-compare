package com.apkcompare.resource;

import java.awt.Image;
import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;

import com.apkspectrum.resource.DefaultResImage;
import com.apkspectrum.resource.ResImage;

public enum RImg implements ResImage<Image>
{
	APP_ICON					("AppIcon.png"),
	APK_FILE_ICON				("apk_file_icon.png"),
	APK_SCANNER_ICON			("ApkScannerIcon.png"),
	APK_SCANNER_PREVIEW			("apkscanner_preview.png"),
	LOADING						("loading.gif"),
	APK_LOGO					("Logo.png"),
	WAIT_BAR					("wait_bar.gif"),
	DEF_APP_ICON				("sym_def_app_icon.png"),
	QMG_IMAGE_ICON				("qmg_not_suporrted.png"),

	TREE_LOADING				("tree_loading.gif"),

	ADD_TO_DESKTOP				("add-to-desktop.png"),

	//https://www.shareicon.net/diff-94479
	DIFF_TOOLBAR_ADD			("diff_toolbar_add.png"),
	DIFF_TOOLBAR_EDITOR			("diff_toolbar_editor.png"),
	DIFF_TOOLBAR_IDEN			("diff_toolbar_iden.png"),
	DIFF_DRAG_AND_DROP			("diff_draganddrop.png"),	

	//https://www.shareicon.net/git-compare-94498
	DIFF_TREE_SWAP				("tree_swap.png"),

	//https://www.shareicon.net/setting-598385
	DIFF_TOOLBAR_SETTING		("diff_toolbar_setting.png"),
	//https://www.shareicon.net/interface-letter-i-info-circle-help-735003
	DIFF_TOOLBAR_INFO			("diff_toolbar_info.png"),
	//http://icons.iconarchive.com/icons/custom-icon-design/flatastic-1/24/folder-icon.png
	DIFF_TREE_FOLDER_ICON		("diff_tree_icon_folder.png"),

	DIFF_TREE_APK_ICON			("diff_tree_icon_apk.png"),
	DIFF_APK_OPEN_ICON			("diff_apk_open.png"),
	; // ENUM END

	private DefaultResImage res;

	private RImg(String value) {
		res = new DefaultResImage(value);
	}

	@Override
	public String getValue() {
		return res.getValue();
	}

	@Override
	public String getConfiguration() {
		return res.getConfiguration();
	}

	@Override
	public String toString() {
		return res.toString();
	}

	@Override
	public String getPath() {
		return res.getPath();
	}

	@Override
	public URL getURL() {
		return res.getURL();
	}

	@Override
	public Image get() {
		return res.get();
	}

	@Override
	public Image getImage() {
		return res.getImage();
	}

	@Override
	public Image getImage(int w, int h) {
		return res.getImage(w, h);
	}

	@Override
	public ImageIcon getImageIcon() {
		return res.getImageIcon();
	}

	@Override
	public ImageIcon getImageIcon(int w, int h) {
		return res.getImageIcon(w, h);
	}

	@Override
	public URL getResource() {
		return res.getResource();
	}

	@Override
	public InputStream getResourceAsStream() {
		return res.getResourceAsStream();
	}
}
