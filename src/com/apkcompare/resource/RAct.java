package com.apkcompare.resource;

import com.apkspectrum.resource.DefaultResAction;
import com.apkspectrum.resource.ResAction;
import com.apkspectrum.resource.ResImage;
import com.apkspectrum.resource.ResString;

public enum RAct implements ResAction<ResAction<?>>
{
	ACT_CMD_OPEN_APK				(null, RImg.DIFF_APK_OPEN_ICON),
	ACT_CMD_OPEN_SETTINGS			(null, RImg.DIFF_TOOLBAR_SETTING),
	ACT_CMD_SHOW_ABOUT				(null, RImg.DIFF_TOOLBAR_INFO)
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
}
