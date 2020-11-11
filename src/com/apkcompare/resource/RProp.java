package com.apkcompare.resource;

import java.beans.PropertyChangeListener;

import com.apkspectrum.resource.DefaultResProp;
import com.apkspectrum.resource.ResProp;
import com.apkspectrum.util.SystemUtil;

public enum RProp implements ResProp<Object>
{
	LANGUAGE					(SystemUtil.getUserLanguage()),
	SAVE_WINDOW_SIZE			(true),
	PREFERRED_LANGUAGE,			/* see getDefualtValue() */

	APK_SCANNER_PATH,
	DIFF_TOOL					(""),
	RECENT_DIFF_TOOL			(""),
	; // ENUM END

	public enum B implements ResProp<Boolean> {
		SAVE_WINDOW_SIZE,
		; // ENUM END

		@Override
		public Boolean get() {
			return RProp.valueOf(name()).getBoolean();
		}

		@Override
		public void set(Boolean data) {
			RProp.valueOf(name()).setData(data);
		}

		@Override
		public String getValue() {
			return RProp.valueOf(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).removePropertyChangeListener(listener);
		}
	}

	public enum S implements ResProp<String> {
		LANGUAGE,
		PREFERRED_LANGUAGE,
		APK_SCANNER_PATH,
		DIFF_TOOL,
		RECENT_DIFF_TOOL,
		; // ENUM END

		@Override
		public String get() {
			return RProp.valueOf(name()).getString();
		}

		@Override
		public void set(String data) {
			RProp.valueOf(name()).setData(data);
		}

		@Override
		public String getValue() {
			return RProp.valueOf(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).removePropertyChangeListener(listener);
		}
	}

	public enum I implements ResProp<Integer> {

		; // ENUM END

		@Override
		public Integer get() {
			return RProp.valueOf(name()).getInt();
		}

		@Override
		public void set(Integer data) {
			RProp.valueOf(name()).setData(data);
		}

		@Override
		public String getValue() {
			return RProp.valueOf(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).addPropertyChangeListener(listener);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener listener) {
			RProp.valueOf(name()).removePropertyChangeListener(listener);
		}
	}

	private DefaultResProp res;

	private RProp() {
		res = new DefaultResProp(name(), getDefaultValue());
	}

	private RProp(Object defValue) {
		res = new DefaultResProp(name(), defValue);
	}

	public Object getDefaultValue() {
		Object obj = res != null ? res.getDefaultValue() : null;
		return obj;
	}

	@Override
	public String getValue() {
		return res.getValue();
	}

	@Override
	public String toString() {
		return res.toString();
	}

	@Override
	public Object get() {
		return res.get();
	}

	@Override
	public void set(Object data) {
		res.set(data);
	}

	@Override
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		res.addPropertyChangeListener(listener);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		res.removePropertyChangeListener(listener);
	}

	public Object getData() {
		return res.getData();
	}

	public Object getData(Object ref) {
		return res.getData(ref);
	}

	public String getString() {
		return res.getString();
	}

	public int getInt() {
		return res.getInt();
	}

	public int getInt(int ref) {
		return res.getInt(ref);
	}

	public boolean getBoolean() {
		return res.getBoolean();
	}

	public boolean getBoolean(boolean ref) {
		return res.getBoolean();
	}

	public void setData(Object data) {
		res.setData(data);
	}

	public static Object getPropData(String key) {
		return DefaultResProp.getPropData(key);
	}

	public static Object getPropData(String key, Object ref) {
		return DefaultResProp.getPropData(key, ref);
	}

	public static void setPropData(String key, Object data) {
		DefaultResProp.setPropData(key, data);
	}

    public static void addPropertyChangeListener(ResProp<?> prop, PropertyChangeListener listener) {
    	DefaultResProp.addPropertyChangeListener(prop, listener);
    }

    public static void removePropertyChangeListener(ResProp<?> prop, PropertyChangeListener listener) {
    	DefaultResProp.removePropertyChangeListener(prop, listener);
    }
}
