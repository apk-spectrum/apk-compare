package com.apkcompare.core;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import com.apkcompare.data.ComponentPassKeyDiffTreeUserData;
import com.apkcompare.data.FilePassKeyDiffTreeUserData;
import com.apkcompare.data.ImageDiffTreeUserData;
import com.apkcompare.data.ImagePassKeyDiffTreeUserData;
import com.apkcompare.data.LibDiffTreeUserData;
import com.apkcompare.data.PermissionDiffTreeUserData;
import com.apkcompare.data.SigPassKeyDiffTreeUserData;
import com.apkcompare.data.base.DiffTreeUserData;
import com.apkcompare.gui.SortNode;
import com.apkcompare.resource.Resource;
import com.apkcompare.util.ApkCompareUtil;
import com.apkscanner.core.permissionmanager.PermissionGroupInfoExt;
import com.apkscanner.core.permissionmanager.PermissionManager;
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
import com.apkscanner.resource.RProp;
import com.apkscanner.util.FileUtil;
import com.apkscanner.util.FileUtil.FSStyle;
import com.apkscanner.util.Log;
import com.apkscanner.util.XmlPath;
import com.apkscanner.util.ZipFileUtil;

public class DiffMappingTree {
	//public static String[] allowaddkey = {"Lib","Component","Resource", "Sig", "Permission"};
	
	public static void  createTree(ApkInfo apkInfo, SortNode node) {		
		//for apk file path
		String[] tabfolders = {Resource.STR_TAB_BASIC_INFO.getString(),
								Resource.STR_TAB_WIDGET.getString(),
								Resource.STR_TAB_LIB.getString(),
								Resource.STR_TAB_IMAGE.getString(),
								Resource.STR_TAB_ACTIVITY.getString(),
								Resource.STR_TAB_CERT.getString()};
		
		for(String tabname : tabfolders) {
			SortNode TabfolderchildNode = new SortNode(new DiffTreeUserData(tabname, true));
	        node.add(TabfolderchildNode);
	        
	        if(tabname.equals(Resource.STR_TAB_BASIC_INFO.getString())) {
	        	Log.d("create basic Node");
	        	String[] apkinfofolders = {"Icon",
						"Title",
						"Package",
						"Version",
						"SDK Version",
						"Size",
						"Feature",
						"Permission"};
	        	
	        	for(String strapkinfo : apkinfofolders) {
	    			SortNode childNodeapkinfo = new SortNode(new DiffTreeUserData(strapkinfo, true));
	    			TabfolderchildNode.add(childNodeapkinfo);

	        		if(strapkinfo.equals("Icon")) {
	        			String temppath = apkInfo.manifest.application.icons[apkInfo.manifest.application.icons.length - 1].name;
	        			String title = temppath.substring(temppath.lastIndexOf(File.separator) + 1);
	        			String respath = temppath.substring(temppath.lastIndexOf("!") + 2);
	        			
	        			ImageDiffTreeUserData userdata = new ImageDiffTreeUserData(title, "Icon", apkInfo);
	        			
	    				if(temppath != null && (temppath.startsWith("jar:") || temppath.startsWith("file:"))) {
	    					ImageIcon icon;
							try {
								icon = new ImageIcon(ApkCompareUtil.getScaledImage(new ImageIcon(ImageIO.read(new URL(temppath))),50,50));
								userdata.setImageIcon(icon, respath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				}
	    				
	        			childNodeapkinfo.add(new SortNode(userdata));
	        		} else if(strapkinfo.equals("Title")){
	        			for(ResourceInfo r: apkInfo.manifest.application.labels) {
	        				if(r.configuration == null || r.configuration.isEmpty() || "default".equals(r.configuration)) {
	        					if(r.name != null) {
	        						childNodeapkinfo.add(new SortNode(new DiffTreeUserData(r.name, apkInfo)));
	        					} else {
	        						childNodeapkinfo.add(new SortNode(new DiffTreeUserData(apkInfo.manifest.packageName, apkInfo)));
	        					}
	        				} else {
	        					childNodeapkinfo.add(new SortNode(new DiffTreeUserData("[" + r.configuration + "]"+r.name, apkInfo)));
	        				}
	        			}
	        		} else if(strapkinfo.equals("Package")){
	        			String temp = apkInfo.manifest.packageName;
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo, apkInfo)));
	        		} else if(strapkinfo.equals("Version")){
	        			String temp = apkInfo.manifest.versionName + "/" + apkInfo.manifest.versionCode;
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo, apkInfo)));
	        		} else if(strapkinfo.equals("SDK Version")){
	        			String temp = apkInfo.manifest.usesSdk.minSdkVersion+"(Min)," +apkInfo.manifest.usesSdk.targetSdkVersion + " (Target)";
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo, apkInfo)));
	        		} else if(strapkinfo.equals("Size")){
	        			String temp = FileUtil.getFileSize(apkInfo.fileSize, FSStyle.FULL);
	        			childNodeapkinfo.add(new SortNode(new DiffTreeUserData(temp, strapkinfo, apkInfo)));
	        		} else if(strapkinfo.equals("Feature")){	        			
	        			for(Object str : getfeature(apkInfo)) {
	        				childNodeapkinfo.add(new SortNode(new DiffTreeUserData(str.toString(), apkInfo)));	
	        			}
	        		} else if(strapkinfo.equals("Permission")){
		    			addPermissionString(childNodeapkinfo, apkInfo).split("\n");		    			
	        		}	        	
	        	}
	        } else if(tabname.equals(Resource.STR_TAB_WIDGET.getString())){
	        	Log.d("create widget Node");
	        	
	        	
	    		String preferLang = (String)Resource.PROP_PREFERRED_LANGUAGE.getData("");
	    		for(int i=0; i < apkInfo.widgets.length; i++) {
	    			ImageIcon myimageicon = null;
	    			try {
	    				String path = (String)apkInfo.widgets[i].icons[apkInfo.widgets[i].icons.length-1].name;
						myimageicon = new ImageIcon(ApkCompareUtil.getScaledImage(new ImageIcon(ImageIO.read(new URL(path))),50,50));
	    			} catch (IOException e) {
	    				e.printStackTrace();
	    			}

	    			String label = ApkInfoHelper.getResourceValue(apkInfo.widgets[i].labels, preferLang);
	    			if(label == null) label = ApkInfoHelper.getResourceValue(apkInfo.manifest.application.labels, preferLang);
	    			String temp = label +" - " + apkInfo.widgets[i].size + " - " +  apkInfo.widgets[i].name + " - " + apkInfo.widgets[i].type;
	    			
        			ImagePassKeyDiffTreeUserData userdata = new ImagePassKeyDiffTreeUserData(temp, "Icon", apkInfo);
        			
        			userdata.setImageIcon(myimageicon);
    				
        			TabfolderchildNode.add(new SortNode(userdata));
	    		}
	    		
	    		//if(widgets.length ==0) node.remove(TabfolderchildNode);
	        	
	        } else if(tabname.equals(Resource.STR_TAB_LIB.getString())){
	        	Log.d("create Lib Node");
	        	
	        	String[] libList = apkInfo.libraries;
	        	for(int i=0; i< libList.length; i++) {
					long size = ZipFileUtil.getFileSize(apkInfo.filePath, libList[i]);
					long compressed = ZipFileUtil.getCompressedSize(apkInfo.filePath, libList[i]);
					String temp = libList[i];
					LibDiffTreeUserData libuserdata = new LibDiffTreeUserData(temp, "Lib", apkInfo);
					libuserdata.setFilesInfo(FileUtil.getFileSize(size, FSStyle.FULL), String.format("%.2f", ((float)(size - compressed) / (float)size) * 100f) + " %");
					TabfolderchildNode.add(new SortNode(libuserdata));
				}
	        	
	        	
	        } else if(tabname.equals(Resource.STR_TAB_IMAGE.getString())){
	        	Log.d("create image Node");
	        	
	        	String[] nameList = apkInfo.resources;
	        	for(int i=0; i< nameList.length; i++) {
	        		
	        		String resPath = apkInfo.tempWorkPath + File.separator + nameList[i].replace("/", File.separator);
	        		File resFile = new File(resPath);	        		
	        		FilePassKeyDiffTreeUserData tempdata = new FilePassKeyDiffTreeUserData(nameList[i], "Resource", apkInfo);	        		
	        		TabfolderchildNode.add(new SortNode(tempdata));
	        	}	        	
	        } else if(tabname.equals(Resource.STR_TAB_ACTIVITY.getString())){
	        	Log.d("create component Node");
				getComponents(apkInfo, TabfolderchildNode);
	        } else if(tabname.equals(Resource.STR_TAB_CERT.getString())){
	        	Log.d("create cert Node");
	        	
				String[] mCertList = apkInfo.certificates;
				String[] mCertFiles = apkInfo.certFiles;
//				String[] tokenmCertList = apkInfo.ss_tokens;
//				String[] tokenmCertFiles = apkInfo.ss_tokenFiles;
	        					
				
				for(String str : mCertList) {
					if(str == null) continue;
						
						//str = "<html>" + str.replace("\n", "<br/>") + "</html>";
						String[] tempstr = str.split("\\r?\\n");
						 
						//str = "<html>" + "Owner : " + findString(tempstr[0], "CN=", ", ") + "<br/>"
						//+ "Issuer : " + findString(tempstr[1], "CN=", ", ") + "<br/>"
													
						//Log.d(tempstr[0]);
						if(tempstr.length > 8) {							
							str =  "<html>"
							+ tempstr[0] + "<br/>"
							+ tempstr[1] + "<br/>"
							+ tempstr[5] + "<br/>"
							+ tempstr[6] + "<br/>"
							+ tempstr[7] + "<br/>"
							+ "</html>";
						}
						
						SigPassKeyDiffTreeUserData tempdata = new SigPassKeyDiffTreeUserData(str, "Sig", apkInfo, false);
						tempdata.setOrignalSig(str);
						TabfolderchildNode.add(new SortNode(tempdata));
						
				}
				
	        }
		}		
		Log.d("End create Tree");
	}

	private static void getComponents(ApkInfo apkInfo, SortNode node) {
		SortNode nodetemp = new SortNode(new DiffTreeUserData("activity", true));
		node.add(nodetemp);
		
		if(apkInfo.manifest.application.activity != null) {			
			for(ActivityInfo info: apkInfo.manifest.application.activity) {
				String type = null;
				if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0 && (info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "[LAUNCHER]";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "[MAIN]";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0) {
					Log.w("set launcher flag, but not main");
					type = "";//"ACTIVITY";
				} else {
					type = "";//"ACTIVITY";
				}
				
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
			}
		}
		nodetemp = new SortNode(new DiffTreeUserData("activityAlias", true));
		node.add(nodetemp);
		if(apkInfo.manifest.application.activityAlias != null) {
			for(ActivityAliasInfo info: apkInfo.manifest.application.activityAlias) {
				String type = null;
				if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0 && (info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "[LAUNCHER-ALIAS]";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_MAIN) != 0) {
					type = "[MAIN-ALIAS]";
				} else if((info.featureFlag & ApkInfo.APP_FEATURE_LAUNCHER) != 0) {
					Log.w("set launcher flag, but not main");
					type = "";
				} else {
					type = "";
				}
				
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
				
			}
		}
		nodetemp = new SortNode(new DiffTreeUserData("service", true));
		node.add(nodetemp);
		if(apkInfo.manifest.application.service != null) {
			for(ServiceInfo info: apkInfo.manifest.application.service) {				
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
			}
		}
		nodetemp = new SortNode(new DiffTreeUserData("receiver", true));
		node.add(nodetemp);
		if(apkInfo.manifest.application.receiver != null) {
			for(ReceiverInfo info: apkInfo.manifest.application.receiver) {
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
			}
		}
		nodetemp = new SortNode(new DiffTreeUserData("provider", true));
		node.add(nodetemp);
		if(apkInfo.manifest.application.provider != null) {			
			for(ProviderInfo info: apkInfo.manifest.application.provider) {				
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
				//String startUp = (info.featureFlag & ActivityInfo.ACTIVITY_FEATURE_STARTUP) != 0 ? "O" : "X";
			}
		}
	}
	private static String findString(String ori, String begin, String end) {
		
		if(ori.indexOf(begin) < 0 || ori.indexOf(end) < 0) {
			return ori;
		}
		
		String temp = ori.substring(ori.indexOf(begin));
		return temp.substring(0, temp.indexOf(end)); 
	}
	
	private static Object[] getfeature(ApkInfo apkInfo) {
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
	
	
	private static String addPermissionString(SortNode childNodeapkinfo, ApkInfo apkInfo) {
		String deprecatedPermissions = "";

		boolean isSamsungSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_SAMSUNG_SIGN) != 0 ? true : false;
		boolean isPlatformSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_PLATFORM_SIGN) != 0 ? true : false;

		boolean hasSignatureLevel = false; // apkInfo.hasSignatureLevel;
		boolean hasSignatureOrSystemLevel = false; // apkInfo.hasSignatureOrSystemLevel;
		boolean hasSystemLevel = false; // apkInfo.hasSystemLevel;
		String notGrantPermmissions = "";

		ArrayList<UsesPermissionInfo> allPermissions = new ArrayList<UsesPermissionInfo>(); 
		
		if(apkInfo.manifest.usesPermission != null && apkInfo.manifest.usesPermission.length > 0) {
			//permissionList.append("<uses-permission> [" +  apkInfo.manifest.usesPermission.length + "]\n");
			for(UsesPermissionInfo info: apkInfo.manifest.usesPermission) {
				allPermissions.add(info);		
			}
		}
		if(apkInfo.manifest.usesPermissionSdk23 != null && apkInfo.manifest.usesPermissionSdk23.length > 0) {
			
			//permissionList.append("<uses-permission-sdk-23> [" +  apkInfo.manifest.usesPermissionSdk23.length + "]\n");
			for(UsesPermissionInfo info: apkInfo.manifest.usesPermissionSdk23) {
				allPermissions.add(info);			
			}
		}

		String signaturePermissions = "";
		if(apkInfo.manifest.permission != null && apkInfo.manifest.permission.length > 0) {
//			if(permissionList.length() > 0) {
//				permissionList.append("\n");
//			}
			//permissionList.append("<permission> [" +  apkInfo.manifest.permission.length + "]\n");
			for(PermissionInfo info: apkInfo.manifest.permission) {
				if(!"normal".equals(info.protectionLevel)) {
					signaturePermissions += info.name + " - " + info.protectionLevel + "\n";
				}
			}
		}
		//XmlPath xmlPermissions = new XmlPath(PermissionGroupManager.class.getResourceAsStream("/values/permissions-info/27/AndroidManifest.xml"));
		
		
		PermissionManager permissionManager = new PermissionManager();
		permissionManager.clearPermissions();
		permissionManager.setPlatformSigned(isPlatformSign);
		permissionManager.setTreatSignAsRevoked(RProp.B.PERM_TREAT_SIGN_AS_REVOKED.get());
		permissionManager.addUsesPermission(apkInfo.manifest.usesPermission);
		permissionManager.addUsesPermission(apkInfo.manifest.usesPermissionSdk23);
		permissionManager.addDeclarePemission(apkInfo.manifest.permission);
		
		int selectSdkVer = apkInfo.manifest.usesSdk.targetSdkVersion;
		permissionManager.setSdkVersion(selectSdkVer);
		for(int i=0; i< allPermissions.size(); i++) {
			UsesPermissionInfo temp = allPermissions.get(i);
			for(PermissionGroupInfoExt g: permissionManager.getPermissionGroups()) {				
				String path = g.getIconPath();
				
				try {
					ImageIcon icon;
					icon = new ImageIcon(ApkCompareUtil.getScaledImage(new ImageIcon(new URL(path)),16,16));
					
					SortNode tempnode = new SortNode(new PermissionDiffTreeUserData(temp.name, icon, apkInfo));					
					childNodeapkinfo.add(tempnode);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
//	private static String getIconPath(String value)
//	{
//		if(value == null || !value.startsWith("@drawable")) {
//			value = "@drawable/perm_group_unknown";
//		}
//		String path = value.replace("@drawable/", "");
//		
//		if(PermissionGroupManager.class.getResource("/icons/" + path + ".png") != null) {
//			path = PermissionGroupManager.class.getResource("/icons/" + path + ".png").toString();
//		} else {
//			//path = getClass().getResource("/icons/perm_group_default.png").toString();
//		}
//		
//		return path;
//	}
	
	
//	public static ImageIcon getwebpImage(String path) {
//        WebPImageReader reader = new WebPImageReader(new WebPImageReaderSpi());
//        FileImageInputStream fiis;
//        BufferedImage image = null;
//        
//		try {
//			URL url = new URL(path);
//			JarURLConnection connection = (JarURLConnection) url.openConnection();
//			File file = new File(connection.getJarFileURL().toURI());
//			
//			fiis = new FileImageInputStream(file);
//	        reader.setInput(fiis);
//	        image = reader.read(0, null);
//		} catch (IOException | URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        return new ImageIcon(image);
//	}
	
	
}
