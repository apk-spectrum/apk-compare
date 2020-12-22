package com.apkcompare.resource;

import com.apkspectrum.resource.DefaultResAction;
import com.apkspectrum.resource.ResAction;
import com.apkspectrum.resource.ResImage;
import com.apkspectrum.resource.ResString;
import com.apkspectrum.resource._RAct;

public enum RAct implements ResAction<ResAction<?>>
{
	ACT_CMD_FILTER_ADD				(RStr.BTN_FILTER_UNIQUE, RImg.DIFF_TOOLBAR_ADD, RStr.BTN_FILTER_UNIQUE_LAB),
	ACT_CMD_FILTER_DIFF				(RStr.BTN_FILTER_DIFFERENT, RImg.DIFF_TOOLBAR_EDITOR, RStr.BTN_FILTER_DIFFERENT_LAB),
	ACT_CMD_FILTER_IDEN				(RStr.BTN_FILTER_IDENTICAL, RImg.DIFF_TOOLBAR_IDEN, RStr.BTN_FILTER_IDENTICAL_LAB),
	ACT_CMD_TREE_NEVI_NEXT			(RStr.BTN_TREE_NEXT, RImg.DIFF_TOOLBAR_NEXT, RStr.BTN_TREE_NEXT_LAB),
	ACT_CMD_TREE_NEVI_PREV			(RStr.BTN_TREE_PREV, RImg.DIFF_TOOLBAR_PREV, RStr.BTN_TREE_PREV_LAB),
	ACT_CMD_TREE_SWAP				(RStr.BTN_TREE_SWAP, RImg.DIFF_TREE_SWAP, RStr.BTN_TREE_SWAP_LAB),
	ACT_CMD_OPEN_APK				(RStr.BTN_OPEN, RImg.DIFF_APK_OPEN_ICON, RStr.BTN_OPEN_LAB),
	ACT_CMD_RUN_APKSCANNER			(RStr.BTN_OPEN_APK_SCANNER, RImg.APK_SCANNER_ICON.getImageIcon(22,22), RStr.BTN_OPEN_APK_SCANNER_LAB),
	ACT_CMD_OPEN_SETTINGS			(RStr.BTN_SETTING, RImg.DIFF_TOOLBAR_SETTING, RStr.BTN_SETTING),
	ACT_CMD_SHOW_ABOUT				(RStr.BTN_ABOUT, RImg.DIFF_TOOLBAR_INFO, RStr.BTN_ABOUT)
	; // ENUM END

	private DefaultResAction res;

	private RAct(ResString<?> text) {
		res = new DefaultResAction(text);
	}

	private RAct(ResString<?> text, ResString<?> toolTipText) {
		res = new DefaultResAction(text, toolTipText);
	}

	private RAct(ResString<?> text, ResImage<?> image) {
		res = new DefaultResAction(text, image);
	}

	private RAct(ResString<?> text, javax.swing.Icon icon) {
		res = new DefaultResAction(text, icon);
	}

	private RAct(ResString<?> text, ResImage<?> image, ResString<?> toolTipText) {
		res = new DefaultResAction(text, image, toolTipText);
	}

	private RAct(ResString<?> text, javax.swing.Icon icon, ResString<?> toolTipText) {
		res = new DefaultResAction(text, icon, toolTipText);
	}

	@Override
	public ResAction<?> get() {
		return res.get();
	}

	@Override
	public String getText() {
		return res.getText();
	}

	@Override
	public javax.swing.Icon getIcon() {
		return res.getIcon();
	}

	@Override
	public String getToolTipText() {
		return res.getToolTipText();
	}

	@Override
	public void setIconSize(java.awt.Dimension iconSize) {
		res.setIconSize(iconSize);
	}

	@Override
	public void set(javax.swing.Action a) {
		res.set(a);
	}

	public static ResAction<?> getResAction(String name) {
		ResAction<?> res = null;
		try {
			res = valueOf(name);
			if(res == null) {
				res = _RAct.valueOf(name);
			}
		} catch (Exception e) {}
		return res;
	}
}
