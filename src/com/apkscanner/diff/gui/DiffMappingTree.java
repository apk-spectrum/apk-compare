package com.apkscanner.diff.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.tree.DefaultMutableTreeNode;

import com.apkscanner.data.apkinfo.ActivityAliasInfo;
import com.apkscanner.data.apkinfo.ActivityInfo;
import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.data.apkinfo.ApkInfoHelper;
import com.apkscanner.data.apkinfo.CompatibleScreensInfo;
import com.apkscanner.data.apkinfo.PermissionInfo;
import com.apkscanner.data.apkinfo.ProviderInfo;
import com.apkscanner.data.apkinfo.ReceiverInfo;
import com.apkscanner.data.apkinfo.ResourceInfo;
import com.apkscanner.data.apkinfo.ServiceInfo;
import com.apkscanner.data.apkinfo.SupportsGlTextureInfo;
import com.apkscanner.data.apkinfo.SupportsScreensInfo;
import com.apkscanner.data.apkinfo.UsesConfigurationInfo;
import com.apkscanner.data.apkinfo.UsesFeatureInfo;
import com.apkscanner.data.apkinfo.UsesLibraryInfo;
import com.apkscanner.data.apkinfo.UsesPermissionInfo;
import com.apkscanner.data.apkinfo.WidgetInfo;
import com.apkscanner.gui.util.ImageScaler;
import com.apkscanner.resource.Resource;
import com.apkscanner.util.FileUtil;
import com.apkscanner.util.Log;
import com.apkscanner.util.ZipFileUtil;
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
	        					childNodeapkinfo.add(new SortNode(new DiffTreeUserData("[" + r.configuration + "]"+r.name)));	        					
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
	        	WidgetInfo[] widgets = apkInfo.widgets;
	        	
	    		String preferLang = (String)Resource.PROP_PREFERRED_LANGUAGE.getData("");
	    		for(int i=0; i < apkInfo.widgets.length; i++) {
	    			ImageIcon myimageicon = null;
	    			try {
	    				myimageicon = new ImageIcon(new URL((String)apkInfo.widgets[i].icons[apkInfo.widgets[i].icons.length-1].name));
	    			} catch (MalformedURLException e) {
	    				e.printStackTrace();
	    			}
	    			if(myimageicon != null) {
	    				myimageicon.setImage(ImageScaler.getMaxScaledImage(myimageicon,100,100));
	    			}
	    			String label = ApkInfoHelper.getResourceValue(apkInfo.widgets[i].lables, preferLang);
	    			if(label == null) label = ApkInfoHelper.getResourceValue(apkInfo.manifest.application.labels, preferLang);
	    			String temp = label +" - " + apkInfo.widgets[i].size + " - " +  apkInfo.widgets[i].name + " - " + apkInfo.widgets[i].type;
	    			
        			ImageDiffTreeUserData userdata = new ImageDiffTreeUserData(temp, "Icon");
        			
        			userdata.setImageIcon(myimageicon);
    				
        			TabfolderchildNode.add(new SortNode(userdata));
	    		}
	    		
	    		//if(widgets.length ==0) node.remove(TabfolderchildNode);
	        	
	        } else if(tabname.equals(Resource.STR_TAB_LIB.getString())){
	        	String[] libList = apkInfo.libraries;
	        	for(int i=0; i< libList.length; i++) {
					long size = ZipFileUtil.getFileSize(apkInfo.filePath, libList[i]);
					long compressed = ZipFileUtil.getCompressedSize(apkInfo.filePath, libList[i]);
					String temp = libList[i] + FileUtil.getFileSize(size, FSStyle.FULL) + " - " +
							String.format("%.2f", ((float)(size - compressed) / (float)size) * 100f) + " %";							
					TabfolderchildNode.add(new SortNode(new DiffTreeUserData(temp)));
				}
	        	
	        	
	        } else if(tabname.equals(Resource.STR_TAB_IMAGE.getString())){	        	
	        	String[] nameList = apkInfo.resources;	        	
	        	for(int i=0; i< nameList.length; i++) {	        		
						TabfolderchildNode.add(new SortNode(new DiffTreeUserData(nameList[i])));				
	        	}	        	
	        } else if(tabname.equals(Resource.STR_TAB_ACTIVITY.getString())){
				getComponents(apkInfo, TabfolderchildNode);		        
	        } else if(tabname.equals(Resource.STR_TAB_CERT.getString())){
        		
				String[] mCertList = apkInfo.certificates;
				String[] mCertFiles = apkInfo.certFiles;
				String[] tokenmCertList = apkInfo.ss_tokens;
				String[] tokenmCertFiles = apkInfo.ss_tokenFiles;
	        	
				
				for(int i=0;i < mCertList.length; i++) {
					String str = mCertList[i];
					
					str = "<html>" + str.replace("\n", "<br/>") + "</html>";
					
					SortNode tempnode = new SortNode(new DiffTreeUserData(str));
					
					//"<html>Hello World!<br/>blahblahblah</html>"
					
					TabfolderchildNode.add(tempnode);
					
					//tempnode.add(new SortNode(new DiffTreeUserData(mCertList[i])));
					
				}
				
				
	        }
		}
	}
	
	public static void getComponents(ApkInfo apkInfo, SortNode node) {
		if(apkInfo.manifest.application.activity != null) {
			SortNode nodetemp = new SortNode(new DiffTreeUserData("activity"));
			node.add(nodetemp);
			
			for(ActivityInfo info: apkInfo.manifest.application.activity) {
				String type = null;
				if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0 && (info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "launcher";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "main";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0) {
					Log.w("set launcher flag, but not main");
					type = "activity";
				} else {
					type = "activity";
				}
				String startUp = (info.featureFlag & ApkInfo.APP_FEATURE_STARTUP) != 0 ? "O" : "X";
				String enabled = (info.enabled == null) || info.enabled ? "O" : "X";
				String exported = (info.exported == null) || info.exported ? "O" : "X";
				String permission = info.permission != null ? "O" : "X";
				
				nodetemp.add(new SortNode(new DiffTreeUserData(info.name)));
				
			}
		}
		if(apkInfo.manifest.application.activityAlias != null) {
			SortNode nodetemp = new SortNode(new DiffTreeUserData("activityAlias"));
			node.add(nodetemp);
			
			for(ActivityAliasInfo info: apkInfo.manifest.application.activityAlias) {
				String type = null;
				if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0 && (info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "launcher-alias";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "main-alias";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0) {
					Log.w("set launcher flag, but not main");
					type = "activity-alias";
				} else {
					type = "activity-alias";
				}
				String startUp = (info.featureFlag & ApkInfo.APP_FEATURE_STARTUP) != 0 ? "O" : "X";
				String enabled = (info.enabled == null) || info.enabled ? "O" : "X";
				String exported = (info.exported == null) || info.exported ? "O" : "X";
				String permission = info.permission != null ? "O" : "X";
				
				nodetemp.add(new SortNode(new DiffTreeUserData(info.name)));
				
			}
		}
		if(apkInfo.manifest.application.service != null) {
			SortNode nodetemp = new SortNode(new DiffTreeUserData("service"));
			node.add(nodetemp);
			
			for(ServiceInfo info: apkInfo.manifest.application.service) {
				String startUp = (info.featureFlag & ApkInfo.APP_FEATURE_STARTUP) != 0 ? "O" : "X";
				String enabled = (info.enabled == null) || info.enabled ? "O" : "X";
				String exported = (info.exported == null) || info.exported ? "O" : "X";
				String permission = info.permission != null ? "O" : "X"; 
				nodetemp.add(new SortNode(new DiffTreeUserData(info.name)));
			}
		}
		if(apkInfo.manifest.application.receiver != null) {
			SortNode nodetemp = new SortNode(new DiffTreeUserData("receiver"));
			node.add(nodetemp);
			
			for(ReceiverInfo info: apkInfo.manifest.application.receiver) {
				String startUp = (info.featureFlag & ApkInfo.APP_FEATURE_STARTUP) != 0 ? "O" : "X";
				String enabled = (info.enabled == null) || info.enabled ? "O" : "X";
				String exported = (info.exported == null) || info.exported ? "O" : "X";
				String permission = info.permission != null ? "O" : "X"; 
				nodetemp.add(new SortNode(new DiffTreeUserData(info.name)));
			}
		}
		if(apkInfo.manifest.application.provider != null) {
			
			SortNode nodetemp = new SortNode(new DiffTreeUserData("provider"));
			node.add(nodetemp);
			
			for(ProviderInfo info: apkInfo.manifest.application.provider) {
				String startUp = "X";
				String enabled = (info.enabled == null) || info.enabled ? "O" : "X";
				String exported = (info.exported == null) || info.exported ? "O" : "X";
				String permission = "X";
				if(info.permission != null || (info.readPermission != null && info.writePermission != null)) {
					permission = "R/W"; 
				} else if(info.readPermission != null) {
					permission = "Read";
				} else if(info.writePermission != null) {
					permission = "Write";
				}
				nodetemp.add(new SortNode(new DiffTreeUserData(info.name)));
				//String startUp = (info.featureFlag & ActivityInfo.ACTIVITY_FEATURE_STARTUP) != 0 ? "O" : "X";
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
