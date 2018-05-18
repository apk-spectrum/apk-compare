package com.apkscanner.diff.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.data.apkinfo.ApkInfoHelper;
import com.apkscanner.data.apkinfo.CompatibleScreensInfo;
import com.apkscanner.data.apkinfo.PermissionInfo;
import com.apkscanner.data.apkinfo.ResourceInfo;
import com.apkscanner.data.apkinfo.SupportsGlTextureInfo;
import com.apkscanner.data.apkinfo.SupportsScreensInfo;
import com.apkscanner.data.apkinfo.UsesConfigurationInfo;
import com.apkscanner.data.apkinfo.UsesFeatureInfo;
import com.apkscanner.data.apkinfo.UsesLibraryInfo;
import com.apkscanner.data.apkinfo.UsesPermissionInfo;
import com.apkscanner.gui.util.ImageScaler;
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
	        			String temppath = apkInfo.manifest.application.icons[apkInfo.manifest.application.icons.length - 1].name;
	        			String title = temppath.substring(temppath.lastIndexOf(File.separator) + 1);
	        			
	        			ImageDiffTreeUserData userdata = new ImageDiffTreeUserData(title, strapkinfo);
	        			
	    				if(temppath != null && (temppath.startsWith("jar:") || temppath.startsWith("file:"))) {
	    					ImageIcon icon;
							try {
								icon = new ImageIcon(ImageScaler.getScaledImage(new ImageIcon(new URL(temppath)),50,50));
								userdata.setImageIcon(icon);
							} catch (MalformedURLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				}
	    				
	        			childNodeapkinfo.add(new SortNode(userdata));
	        		} else if(strapkinfo.equals("Title")){
	        			for(ResourceInfo r: apkInfo.manifest.application.labels) {
	        				if(r.configuration == null || r.configuration.isEmpty() || "default".equals(r.configuration)) {
	        					if(r.name != null) {
	        						childNodeapkinfo.add(new SortNode(new DiffTreeUserData(r.name)));
	        					} else {
	        						childNodeapkinfo.add(new SortNode(new DiffTreeUserData(apkInfo.manifest.packageName)));
	        					}
	        				} else {
	        					childNodeapkinfo.add(new SortNode(new DiffTreeUserData(r.name + "(" + r.configuration + ")")));	        					
	        				}
	        			}
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
	        			for(Object str : getfeature(apkInfo)) {
	        				childNodeapkinfo.add(new SortNode(new DiffTreeUserData(str.toString())));	
	        			}
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
	public static Object[] getfeature(ApkInfo apkInfo) {
		ArrayList<String> str = new ArrayList<String>();
		
		String installLocation = apkInfo.manifest.installLocation;
		boolean isHidden = ApkInfoHelper.isHidden(apkInfo);
		boolean isStartup = ApkInfoHelper.isStartup(apkInfo);
		boolean isInstrumentation = ApkInfoHelper.isInstrumentation(apkInfo);
		String sharedUserId = apkInfo.manifest.sharedUserId;

		boolean deviceReqData = false;
		if(apkInfo.manifest.compatibleScreens != null) {
			for(CompatibleScreensInfo info: apkInfo.manifest.compatibleScreens) {
				deviceReqData = true;
			}			
		}
		if(apkInfo.manifest.supportsScreens != null) {
			for(SupportsScreensInfo info: apkInfo.manifest.supportsScreens) {
				deviceReqData = true;
			}			
		}
		if(apkInfo.manifest.usesFeature != null) {
			for(UsesFeatureInfo info: apkInfo.manifest.usesFeature) {
				deviceReqData = true;
			}			
		}
		if(apkInfo.manifest.usesConfiguration != null) {
			for(UsesConfigurationInfo info: apkInfo.manifest.usesConfiguration) {
				deviceReqData = true;
			}			
		}
		if(apkInfo.manifest.usesLibrary != null) {			
			for(UsesLibraryInfo info: apkInfo.manifest.usesLibrary) {
				deviceReqData = true;
			}			
		}
		if(apkInfo.manifest.supportsGlTexture != null) {
			for(SupportsGlTextureInfo info: apkInfo.manifest.supportsGlTexture) {
				deviceReqData = true;
			}			
		}		
		
		boolean issignaturePermissions = false;
		if(apkInfo.manifest.permission != null && apkInfo.manifest.permission.length > 0) {
			for(PermissionInfo info: apkInfo.manifest.permission) {
				if(!"normal".equals(info.protectionLevel)) {
					issignaturePermissions = true;
				}
			}
		}
		
		if("internalOnly".equals(installLocation)) {
			str.add(Resource.STR_FEATURE_ILOCATION_INTERNAL_LAB.getString());			
		} else if("auto".equals(installLocation)) {
			str.add(Resource.STR_FEATURE_ILOCATION_AUTO_LAB.getString());			
		} else if("preferExternal".equals(installLocation)) {
			str.add(Resource.STR_FEATURE_ILOCATION_EXTERNAL_LAB.getString());
		}
		if(isHidden) {
			str.add(Resource.STR_FEATURE_HIDDEN_LAB.getString());			
		} else {
			str.add(Resource.STR_FEATURE_LAUNCHER_LAB.getString());			
		}
		if(isStartup) {
			str.add(Resource.STR_FEATURE_STARTUP_LAB.getString());			
		}
		if(issignaturePermissions) {
			str.add(Resource.STR_FEATURE_SIGNATURE_LAB.getString());		
		}
		if(sharedUserId != null && !sharedUserId.startsWith("android.uid.system") ) {
			str.add(Resource.STR_FEATURE_SHAREDUSERID_LAB.getString());			
		}
		if(deviceReqData) {
			str.add(Resource.STR_FEATURE_DEVICE_REQ_LAB.getString());			
		}
		
		return str.toArray();
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
