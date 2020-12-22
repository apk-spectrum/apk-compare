package com.apkcompare.resource;

import com.apkspectrum.resource.DefaultResComp;
import com.apkspectrum.resource.ResComp;
import com.apkspectrum.resource.ResImage;
import com.apkspectrum.resource.ResString;

public enum RComp implements ResComp<Object>
{
	; // ENUM END

	DefaultResComp res;

	private RComp(ResString<?> text) {
		res = new DefaultResComp(text);
	}

	private RComp(ResString<?> text, ResString<?> toolTipText) {
		res = new DefaultResComp(text, toolTipText);
	}

	private RComp(ResString<?> text, ResImage<?> image) {
		res = new DefaultResComp(text, image);
	}

	private RComp(ResString<?> text, javax.swing.Icon icon) {
		res = new DefaultResComp(text, icon);
	}

	private RComp(ResString<?> text, ResImage<?> image, ResString<?> toolTipText) {
		res = new DefaultResComp(text, image, toolTipText);
	}

	private RComp(ResString<?> text, javax.swing.Icon icon, ResString<?> toolTipText) {
		res = new DefaultResComp(text, icon, toolTipText);
	}

	@Override
	public ResComp<?> get() {
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
	public void set(java.awt.Component c) {
		res.set(c);
	}
}