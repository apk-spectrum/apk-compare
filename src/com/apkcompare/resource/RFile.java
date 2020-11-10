package com.apkcompare.resource;

import java.io.File;

import com.apkspectrum.resource.DefaultResFile;
import com.apkspectrum.resource.ResFile;
import com.apkspectrum.resource._RFile;

public enum RFile implements ResFile<File>
{
	BIN_PATH					(Type.BIN, ""),

	DATA_PATH					(Type.DATA, ""),
	DATA_STRINGS_EN				(Type.DATA, "strings.xml"),

	RAW_ROOT_PATH				(Type.RES_ROOT, ""),

	RAW_VALUES_PATH				(Type.RES_VALUE, ""),
	RAW_STRINGS_EN				(Type.RES_VALUE, "strings.xml"),
	RAW_STRINGS_KO				(Type.RES_VALUE, "strings-ko.xml"),

	ETC_APKCOMPARE_EXE			(Type.ETC, "ApkCompare.exe"),
	ETC_SETTINGS_FILE			(Type.ETC, "settings.txt"),
	; // ENUM END

	private DefaultResFile res;

	private RFile(Type type, String value) {
		res = new DefaultResFile(type, value);
	}

	private RFile(Type type, String value, String os) {
		res = new DefaultResFile(type, value, os);
	}

	private RFile(Type type, _RFile[] cfgResources) {
		res = new DefaultResFile(type, cfgResources);
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
	public java.net.URL getURL() {
		return res.getURL();
	}

	@Override
	public java.io.File get() {
		return res.get();
	}

	@Override
	public java.net.URL getResource() {
		return res.getResource();
	}

	@Override
	public java.io.InputStream getResourceAsStream() {
		return res.getResourceAsStream();
	}

	public String getString() {
		return res.getString();
	}
}
