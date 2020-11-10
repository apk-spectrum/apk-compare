package com.apkcompare.resource;

import javax.swing.Icon;

import com.apkspectrum.resource.DefaultResComp;
import com.apkspectrum.resource.ResComp;
import com.apkspectrum.resource.ResImage;
import com.apkspectrum.resource.ResString;

public enum RComp implements ResComp<Object>
{
	; // ENUM END

	DefaultResComp res;

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
		res = new DefaultResComp(text, image, toolTipText);
	}

	private RComp(ResString<?> text, Icon icon, ResString<?> toolTipText) {
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
	public Icon getIcon() {
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