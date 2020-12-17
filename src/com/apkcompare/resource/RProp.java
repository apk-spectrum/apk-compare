package com.apkcompare.resource;

import java.beans.PropertyChangeListener;

import com.apkspectrum.resource.DefaultResProp;
import com.apkspectrum.resource.ResProp;
import com.apkspectrum.resource._RProp;

public enum RProp implements ResProp<Object>
{
	LANGUAGE,
	SAVE_WINDOW_SIZE,
	CURRENT_THEME,
	PREFERRED_LANGUAGE,

	APK_SCANNER_PATH,
	APK_SCANNER_DOWNLOAD_URL,
	DIFF_TOOL,
	RECENT_DIFF_TOOL,
	; // ENUM END

	public enum B implements ResProp<Boolean> {
		SAVE_WINDOW_SIZE(true),
		; // ENUM END

		private B() {}
		private B(boolean defValue) {
			getProp(name()).setDefaultValue(Boolean.valueOf(defValue));
		}

		@Override
		public Boolean get() {
			return getProp(name()).getBoolean();
		}

		@Override
		public void set(Boolean data) {
			getProp(name()).setData(data);
		}

		@Override
		public String getValue() {
			return getProp(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).addPropertyChangeListener(l);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).removePropertyChangeListener(l);
		}
	}

	public enum S implements ResProp<String> {
		LANGUAGE,
		CURRENT_THEME,
		PREFERRED_LANGUAGE,
		APK_SCANNER_PATH,
		APK_SCANNER_DOWNLOAD_URL ("https://github.com/apk-spectrum/apk-scanner/releases"),
		DIFF_TOOL,
		RECENT_DIFF_TOOL,
		; // ENUM END

		private S() {}
		private S(String defValue) {
			getProp(name()).setDefaultValue(defValue);
		}

		@Override
		public String get() {
			return getProp(name()).getString();
		}

		@Override
		public void set(String data) {
			getProp(name()).setData(data);
		}

		@Override
		public String getValue() {
			return getProp(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).addPropertyChangeListener(l);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).removePropertyChangeListener(l);
		}
	}

	public enum I implements ResProp<Integer> {
		// EMPTY
		; // ENUM END

		private I() {}
		private I(int defValue) {
			getProp(name()).setDefaultValue(Integer.valueOf(defValue));
		}

		@Override
		public Integer get() {
			return getProp(name()).getInt();
		}

		@Override
		public void set(Integer data) {
			getProp(name()).setData(data);
		}

		@Override
		public String getValue() {
			return getProp(name()).getValue();
		}

		@Override
		public void addPropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).addPropertyChangeListener(l);
		}

		@Override
		public void removePropertyChangeListener(PropertyChangeListener l) {
			getProp(name()).removePropertyChangeListener(l);
		}
	}

	private DefaultResProp res;

	private RProp() {
		Object defValue = getDefaultValue();
		res = _RProp.getProp(name());
		if(defValue != null) {
			res.setDefaultValue(defValue);
		}
	}

	private RProp(Object defValue) {
		res = _RProp.getProp(name());
		res.setDefaultValue(defValue);
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
	public void addPropertyChangeListener(PropertyChangeListener l) {
		res.addPropertyChangeListener(l);
	}

	@Override
	public void removePropertyChangeListener(PropertyChangeListener l) {
		res.removePropertyChangeListener(l);
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

	public DefaultResProp getProp() {
		return res;
	}

	public static DefaultResProp getProp(String name) {
		RProp prop = null;
		try {
			prop = valueOf(name);
		} catch (Exception e) {}
		return prop != null ? prop.res : _RProp.getProp(name);
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

    public static void addPropertyChangeListener(ResProp<?> prop,
    		PropertyChangeListener l) {
    	DefaultResProp.addPropertyChangeListener(prop, l);
    }

    public static void removePropertyChangeListener(ResProp<?> prop,
    		PropertyChangeListener l) {
    	DefaultResProp.removePropertyChangeListener(prop, l);
    }
}
