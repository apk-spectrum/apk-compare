package com.apkcompare.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.apkcompare.resource.Resource;
import com.apkscanner.util.ConsolCmd;
import com.apkscanner.util.Log;
import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.WinReg;

import mslinks.ShellLink;
import mslinks.ShellLinkException;

public class SystemUtil extends com.apkscanner.util.SystemUtil {

	public static String getDefaultCompareApp() throws Exception {
		String[] apps = getCompareApps();
		return (apps != null && apps.length > 0) ? apps[0] : null;
	}

	public static String[] getCompareApps() throws Exception {
		ArrayList<String> compareList = new ArrayList<String>();
		if(isWindows()) {
			// Beyond Compare
			boolean hasBeyond = false;
			if(Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Scooter Software")) {
				String[] apps = Advapi32Util.registryGetKeys(WinReg.HKEY_CURRENT_USER, "SOFTWARE\\Scooter Software");
				if(apps != null) {
					for(String ver: apps) {
						if(!ver.startsWith("Beyond Compare")) continue;
						if(Advapi32Util.registryValueExists(WinReg.HKEY_CURRENT_USER, "Software\\Scooter Software\\" + ver, "ExePath")) {
							String beyond = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\Scooter Software\\" + ver, "ExePath");
							if(!compareList.contains(beyond) && new File(beyond).canExecute()) {
								compareList.add(beyond);
								hasBeyond = true;
							}
						}
					}
				}
			}
			if(!hasBeyond) {
				String[] expectPaths = new String[] {
					"C:\\Program Files (x86)\\Beyond Compare\\BCompare.exe",
					"C:\\Program Files (x86)\\Beyond Compare 5\\BCompare.exe",
					"C:\\Program Files (x86)\\Beyond Compare 4\\BCompare.exe",
					"C:\\Program Files (x86)\\Beyond Compare 3\\BCompare.exe",
					"C:\\Program Files\\Beyond Compare\\BCompare.exe",
					"C:\\Program Files\\Beyond Compare 5\\BCompare.exe",
					"C:\\Program Files\\Beyond Compare 4\\BCompare.exe",
					"C:\\Program Files\\Beyond Compare 3\\BCompare.exe"
				};
				for(String path: SystemUtil.getRealPath(expectPaths)) {
					if(!compareList.contains(path)) {
						compareList.add(path);
						hasBeyond = true;
					}
				}
			}

			// P4 Merge
			boolean hasP4merge = false;
			if(Advapi32Util.registryKeyExists(WinReg.HKEY_CURRENT_USER, "Software\\Perforce")
					&& Advapi32Util.registryKeyExists(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Wow6432Node\\Perforce\\Environment")
					&& Advapi32Util.registryValueExists(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Wow6432Node\\Perforce\\Environment", "P4INSTROOT")) {
				String perforceRoot = Advapi32Util.registryGetStringValue(WinReg.HKEY_LOCAL_MACHINE, "SOFTWARE\\Wow6432Node\\Perforce\\Environment", "P4INSTROOT");
				String p4merge = SystemUtil.getRealPath(perforceRoot + "p4merge.exe");
				if(p4merge != null) {
					compareList.add(p4merge);
					hasP4merge = true;
				}
			}
			if(!hasP4merge) {
				String[] expectPaths = new String[] {
					"C:\\Program Files (x86)\\Perforce\\p4merge.exe",
					"C:\\Program Files\\Perforce\\p4merge.exe"
				};
				for(String path: SystemUtil.getRealPath(expectPaths)) {
					if(!compareList.contains(path)) {
						compareList.add(path);
						hasP4merge = true;
					}
				}
			}
		} else if(isLinux()) {
			// Beyond Compare
			String beyond = getRealPath("bcompare");
			if(beyond != null && new File(beyond).canExecute()) {
				compareList.add(beyond);
			}

			// P4 Merge
			String p4Path = getRealPath("p4v");
			if(p4Path != null && !p4Path.trim().isEmpty()) {
				String[] result = ConsolCmd.exc(new String[] { "readlink", "-f",  p4Path }, true, null);
				if(result != null && result.length > 0
						&& result[0].matches("^/.*/p4v") && new File(result[0]).exists()) {
					String p4merge = result[0].replaceAll("/p4v$", "/p4merge");
					if(new File(p4merge).canExecute()) {
						compareList.add(p4merge);
					}
				}
			}
		} else {
			throw new Exception("Unknown OS : " + OS);
		}
		return compareList.toArray(new String[compareList.size()]);
	}

	public static String getRealPath(String path) {
		if(path == null || path.trim().isEmpty()) return null;
		String[] realPaths = getRealPath(new String[]{ path });
		if(realPaths == null || realPaths.length == 0) return null;
		return realPaths[0];
	}

	public static String[] getRealPath(String[] paths) {
		if(paths == null || paths.length == 0) return null;

		ArrayList<String> realPathList = new ArrayList<String>(paths.length);
		ArrayList<String> shortPathList = new ArrayList<String>(paths.length);

		for(String path: paths) {
			if(path.indexOf(File.separator) > -1) {
				if(path.startsWith("%")) {
					String env = path.replaceAll("^%(.*)%.*", "$1");
					if(!env.equals(path)) {
						path = System.getenv(env) + path.replaceAll("^%.*%(.*)", "$1");
					}
				}
				File file = new File(path);
				if(!file.exists()) continue;
				if(!realPathList.contains(file.getAbsolutePath())) {
					realPathList.add(file.getAbsolutePath());
				}
			} else {
				if(path == null || path.trim().isEmpty()) continue; 
				shortPathList.add(path.trim());
			}
		}
		
		if(!shortPathList.isEmpty()) {
			String cmd = null;
			String regular = null;
			if(isWindows()) {
				cmd = "where";
				regular = "^[A-Z]:\\\\.*";
			} else if(isLinux()) {
				cmd = "which";
				regular = "^/.*";
			}
			
			shortPathList.add(0, cmd);

			String[] result = ConsolCmd.exc(shortPathList.toArray(new String[shortPathList.size()]), true, null);
			if(result != null) {
				for(String r: result) {
					if(r.matches(regular) && new File(r).exists()
							&& !realPathList.contains(r)) {
						realPathList.add(r);
					}
				}
			}
		}

		return !realPathList.isEmpty() ? realPathList.toArray(new String[realPathList.size()]) : null;
	}

	public static void createShortCut() {
		if(isWindows()) {
			String filePath = Resource.getUTF8Path() + File.separator + "ApkCompare.exe";
			String lnkPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + Resource.STR_APP_NAME.getString() + ".lnk";
			try {
				ShellLink.createLink(filePath, lnkPath);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if(isLinux()) {

		}
	}

	public static boolean hasShortCut() {
		if(isWindows()) {
			String filePath = Resource.getUTF8Path() + File.separator + "ApkCompare.exe";
			String lnkPath = System.getProperty("user.home") + File.separator + "Desktop" + File.separator + Resource.STR_APP_NAME.getString() + ".lnk";

			if(!new File(lnkPath).exists()) {
				return false;
			}
			try {
				String pathToExistingFile = new ShellLink(lnkPath).resolveTarget();
				Log.v("pathToExistingFile " + pathToExistingFile);
				if(pathToExistingFile == null || !new File(pathToExistingFile).exists()
						|| !pathToExistingFile.equals(filePath)) {
					return false;
				}
			} catch (IOException | ShellLinkException e) {
				e.printStackTrace();
			}
		}
		return true;
	}

}
