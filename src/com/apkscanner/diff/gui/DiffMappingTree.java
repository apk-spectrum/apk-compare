package com.apkscanner.diff.gui;

import java.io.File;
import java.util.ArrayList;

import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.data.apkinfo.PermissionInfo;
import com.apkscanner.data.apkinfo.UsesPermissionInfo;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.FileUtil;
import com.apkscanner.util.Log;
import com.apkscanner.util.FileUtil.FSStyle;

public class DiffMappingTree {
	public static void  createTree(ApkInfo apkInfo, SortNode node) {
	
		String[] tabfolders = {Resource.STR_TAB_BASIC_INFO.getString(),
								Resource.STR_TAB_WIDGET.getString(),
								Resource.STR_TAB_LIB.getString(),
								Resource.STR_TAB_IMAGE.getString(),
								Resource.STR_TAB_ACTIVITY.getString(),
								Resource.STR_TAB_CERT.getString()};
		
		for(String tabname : tabfolders) {
			SortNode TabfolderchildNode = new SortNode(new DiffTreeUserData(tabname));
	        node.add(TabfolderchildNode);
	        
	        if(tabname.equals(Resource.STR_TAB_BASIC_INFO.getString())) {
	        	String[] apkinfofolders = {"Icon",
						"Title",
						"Package",
						"Version",
						"SDK Version",
						"Size",
						"Feature",
						"Permission"};
	        	
	        	for(String strapkinfo : apkinfofolders) {
	    			SortNode childNodeapkinfo = new SortNode(new DiffTreeUserData(strapkinfo));
	    			TabfolderchildNode.add(childNodeapkinfo);

	        		if(strapkinfo.equals("Icon")) {
	        			String temp = apkInfo.manifest.application.icons[0].name;
	        			temp = temp.substring(temp.lastIndexOf(File.separator) + 1);
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("Title")){
	        			String temp = apkInfo.manifest.packageName;
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("Package")){
	        			String temp = apkInfo.manifest.packageName;
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("Version")){
	        			String temp = apkInfo.manifest.versionName + "/" + apkInfo.manifest.versionCode;
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("SDK Version")){
	        			String temp = apkInfo.manifest.usesSdk.minSdkVersion+"(Min)," +apkInfo.manifest.usesSdk.targetSdkVersion + " (Target)";
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("Size")){
	        			String temp = FileUtil.getFileSize(apkInfo.fileSize, FSStyle.FULL);
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo)));
	        		} else if(strapkinfo.equals("Feature")){
	        			String temp = FileUtil.getFileSize(apkInfo.fileSize, FSStyle.FULL);
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp)));
	        		} else if(strapkinfo.equals("Permission")){
		    			String[] temp = getPermissionString(apkInfo).split("\n");
	        			for(String str: temp) {
	        				childNodeapkinfo.add(new SortNode(new DiffTreeUserData(str)));
	        			}
	        		}	        	
	        	}
	        } else if(tabname.equals(Resource.STR_TAB_WIDGET.getString())){
	        		
	        } else if(tabname.equals(Resource.STR_TAB_LIB.getString())){
        		
	        } else if(tabname.equals(Resource.STR_TAB_IMAGE.getString())){
        		
	        } else if(tabname.equals(Resource.STR_TAB_ACTIVITY.getString())){
        		
	        } else if(tabname.equals(Resource.STR_TAB_CERT.getString())){
        		
	        }
		}		
	}
	
	public static String getPermissionString(ApkInfo apkInfo) {
		String deprecatedPermissions = "";

		boolean isSamsungSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_SAMSUNG_SIGN) != 0 ? true : false;
		boolean isPlatformSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_PLATFORM_SIGN) != 0 ? true : false;

		boolean hasSignatureLevel = false; // apkInfo.hasSignatureLevel;
		boolean hasSignatureOrSystemLevel = false; // apkInfo.hasSignatureOrSystemLevel;
		boolean hasSystemLevel = false; // apkInfo.hasSystemLevel;
		String notGrantPermmissions = "";

		ArrayList<UsesPermissionInfo> allPermissions = new ArrayList<UsesPermissionInfo>(); 
		StringBuilder permissionList = new StringBuilder();
		if(apkInfo.manifest.usesPermission != null && apkInfo.manifest.usesPermission.length > 0) {
			//permissionList.append("<uses-permission> [" +  apkInfo.manifest.usesPermission.length + "]\n");
			for(UsesPermissionInfo info: apkInfo.manifest.usesPermission) {
				allPermissions.add(info);
				permissionList.append(info.name + " - " + info.protectionLevel);
				if(info.isSignatureLevel()) hasSignatureLevel = true;
				if(info.isSignatureOrSystemLevel()) hasSignatureOrSystemLevel = true;
				if(info.isSystemLevel()) hasSystemLevel = true;
				if(((info.isSignatureLevel() || info.isSignatureOrSystemLevel()) && !(isSamsungSign || isPlatformSign)) || info.isSystemLevel()) {
					notGrantPermmissions += info.name + " - " + info.protectionLevel + "\n";
				}
				if(info.maxSdkVersion != null) {
					permissionList.append(", maxSdkVersion : " + info.maxSdkVersion);
				}
				if(info.isDeprecated()) {
					deprecatedPermissions += info.getDeprecatedMessage() + "\n\n";
				}
				permissionList.append("\n");
			}
		}
		if(apkInfo.manifest.usesPermissionSdk23 != null && apkInfo.manifest.usesPermissionSdk23.length > 0) {
			if(permissionList.length() > 0) {
				permissionList.append("\n");
			}
			//permissionList.append("<uses-permission-sdk-23> [" +  apkInfo.manifest.usesPermissionSdk23.length + "]\n");
			for(UsesPermissionInfo info: apkInfo.manifest.usesPermissionSdk23) {
				allPermissions.add(info);
				permissionList.append(info.name + " - " + info.protectionLevel);
				if(info.isSignatureLevel()) hasSignatureLevel = true;
				if(info.isSignatureOrSystemLevel()) hasSignatureOrSystemLevel = true;
				if(info.isSystemLevel()) hasSystemLevel = true;
				if(((info.isSignatureLevel() || info.isSignatureOrSystemLevel()) && !(isSamsungSign || isPlatformSign)) || info.isSystemLevel()) {
					notGrantPermmissions += info.name + " - " + info.protectionLevel + "\n";
				}
				if(info.maxSdkVersion != null) {
					permissionList.append(", maxSdkVersion : " + info.maxSdkVersion);
				}
				if(info.isDeprecated()) {
					deprecatedPermissions += info.getDeprecatedMessage() + "\n\n";
				}
				permissionList.append("\n");
			}
		}

		String signaturePermissions = "";
		if(apkInfo.manifest.permission != null && apkInfo.manifest.permission.length > 0) {
//			if(permissionList.length() > 0) {
//				permissionList.append("\n");
//			}
			//permissionList.append("<permission> [" +  apkInfo.manifest.permission.length + "]\n");
			for(PermissionInfo info: apkInfo.manifest.permission) {
				permissionList.append(info.name + " - " + info.protectionLevel + "\n");
				if(!"normal".equals(info.protectionLevel)) {
					signaturePermissions += info.name + " - " + info.protectionLevel + "\n";
				}
			}
		}
		return permissionList.toString();
	}
}
