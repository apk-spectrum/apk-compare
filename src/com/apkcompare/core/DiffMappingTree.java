package com.apkcompare.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

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
import com.apkcompare.resource.RImg;
import com.apkcompare.resource.RProp;
import com.apkcompare.resource.RStr;
import com.apkspectrum.core.permissionmanager.PermissionManager;
import com.apkspectrum.data.apkinfo.ActivityAliasInfo;
import com.apkspectrum.data.apkinfo.ActivityInfo;
import com.apkspectrum.data.apkinfo.ApkInfo;
import com.apkspectrum.data.apkinfo.ApkInfoHelper;
import com.apkspectrum.data.apkinfo.ProviderInfo;
import com.apkspectrum.data.apkinfo.ReceiverInfo;
import com.apkspectrum.data.apkinfo.ResourceInfo;
import com.apkspectrum.data.apkinfo.ServiceInfo;
import com.apkspectrum.data.apkinfo.UsesPermissionInfo;
import com.apkspectrum.swing.ImageScaler;
import com.apkspectrum.util.FileUtil;
import com.apkspectrum.util.FileUtil.FSStyle;
import com.apkspectrum.util.Log;
import com.apkspectrum.util.ZipFileUtil;

public class DiffMappingTree {
	//public static String[] allowaddkey = {"Lib","Component","Resource", "Sig", "Permission"};
	PermissionManager permissionManager = null;
	public DiffMappingTree() {
		//permissionManager = new PermissionManager();
	}
	public void  createTree(ApkInfo apkInfo, SortNode node) {
		//for apk file path
		String[] tabfolders = {
				RStr.TAB_BASIC_INFO.get(),
				RStr.TAB_WIDGETS.get(),
				RStr.TAB_LIBRARIES.get(),
				RStr.TAB_RESOURCES.get(),
				RStr.TAB_COMPONENTS.get(),
				RStr.TAB_SIGNATURES.get()
			};

		for(String tabname : tabfolders) {
			SortNode TabfolderchildNode = new SortNode(new DiffTreeUserData(tabname, true));
			node.add(TabfolderchildNode);

			if(tabname.equals(RStr.TAB_BASIC_INFO.get())) {
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
								icon = ImageScaler.getScaledImageIcon(ImageIO.read(new URL(temppath)), 50, 50);
								userdata.setImageIcon(icon, respath);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}

						childNodeapkinfo.add(new SortNode(userdata));
					} else if(strapkinfo.equals("Title")){
						if(apkInfo.manifest.application.labels != null) {
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
						addPermissionString(childNodeapkinfo, apkInfo);
					}
				}
			} else if(tabname.equals(RStr.TAB_WIDGETS.get())){
				Log.d("create widget Node");


				String preferLang = RProp.S.PREFERRED_LANGUAGE.get();
				for(int i=0; i < apkInfo.widgets.length; i++) {
					ImageIcon myimageicon = null;
					try {
						String path = (String)apkInfo.widgets[i].icons[apkInfo.widgets[i].icons.length-1].name;
						if(!path.endsWith(".xml")) {
							myimageicon = ImageScaler.getScaledImageIcon(ImageIO.read(new URL(path)), 50, 50);
						}
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

			} else if(tabname.equals(RStr.TAB_LIBRARIES.get())){
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


			} else if(tabname.equals(RStr.TAB_RESOURCES.get())){
				Log.d("create image Node");

				String[] nameList = apkInfo.resources;
				for(int i=0; i< nameList.length; i++) {

					//String resPath = apkInfo.tempWorkPath + File.separator + nameList[i].replace("/", File.separator);
					//File resFile = new File(resPath);
					FilePassKeyDiffTreeUserData tempdata = new FilePassKeyDiffTreeUserData(nameList[i], "Resource", apkInfo);
					TabfolderchildNode.add(new SortNode(tempdata));
				}
			} else if(tabname.equals(RStr.TAB_COMPONENTS.get())){
				Log.d("create component Node");
				getComponents(apkInfo, TabfolderchildNode);
			} else if(tabname.equals(RStr.TAB_SIGNATURES.get())){
				Log.d("create cert Node");

				String[] mCertList = apkInfo.certificates;
//				String[] mCertFiles = apkInfo.certFiles;
//				String[] tokenmCertList = apkInfo.ss_tokens;
//				String[] tokenmCertFiles = apkInfo.ss_tokenFiles;

				if (mCertList != null) {
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
		}
		Log.d("End create Tree");
	}

	private static void getComponents(ApkInfo apkInfo, SortNode node) {
		SortNode nodetemp = new SortNode(new DiffTreeUserData("activity", true));
		node.add(nodetemp);

		if(apkInfo.manifest.application.activity != null) {
			for(ActivityInfo info: apkInfo.manifest.application.activity) {
				ComponentPassKeyDiffTreeUserData data = new ComponentPassKeyDiffTreeUserData(info.name, "Component", apkInfo);
				data.setinforeport(info.getReport());
				nodetemp.add(new SortNode(data));
			}
		}
		nodetemp = new SortNode(new DiffTreeUserData("activityAlias", true));
		node.add(nodetemp);
		if(apkInfo.manifest.application.activityAlias != null) {
			for(ActivityAliasInfo info: apkInfo.manifest.application.activityAlias) {
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

	@SuppressWarnings("unused")
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
		//boolean isInstrumentation = ApkInfoHelper.isInstrumentation(apkInfo);
		String sharedUserId = apkInfo.manifest.sharedUserId;

		boolean deviceReqData = false;
		if(apkInfo.manifest.compatibleScreens != null
				&& apkInfo.manifest.compatibleScreens.length > 0) {
			deviceReqData = true;
		}
		if(apkInfo.manifest.supportsScreens != null
				&& apkInfo.manifest.supportsScreens.length > 0) {
				deviceReqData = true;
		}
		if(apkInfo.manifest.usesFeature != null
				&& apkInfo.manifest.usesFeature.length > 0) {
				deviceReqData = true;
		}
		if(apkInfo.manifest.usesConfiguration != null
				&& apkInfo.manifest.usesConfiguration.length > 0) {
			deviceReqData = true;
		}
		if(apkInfo.manifest.usesLibrary != null
				&& apkInfo.manifest.usesLibrary.length > 0) {
			deviceReqData = true;
		}
		if(apkInfo.manifest.supportsGlTexture != null
				&& apkInfo.manifest.supportsGlTexture.length > 0) {
			deviceReqData = true;
		}

//		boolean issignaturePermissions = false;
//		if(apkInfo.manifest.permission != null && apkInfo.manifest.permission.length > 0) {
//			for(PermissionInfo info: apkInfo.manifest.permission) {
//				if(!"normal".equals(info.protectionLevel)) {
//					issignaturePermissions = true;
//				}
//			}
//		}

		if("internalOnly".equals(installLocation)) {
			str.add(RStr.FEATURE_ILOCATION_INTERNAL_LAB.get());
		} else if("auto".equals(installLocation)) {
			str.add(RStr.FEATURE_ILOCATION_AUTO_LAB.get());
		} else if("preferExternal".equals(installLocation)) {
			str.add(RStr.FEATURE_ILOCATION_EXTERNAL_LAB.get());
		}
		if(isHidden) {
			str.add(RStr.FEATURE_HIDDEN_LAB.get());
		} else {
			str.add(RStr.FEATURE_LAUNCHER_LAB.get());
		}
		if(isStartup) {
			str.add(RStr.FEATURE_STARTUP_LAB.get());
		}
		if(sharedUserId != null && !sharedUserId.startsWith("android.uid.system") ) {
			str.add(RStr.FEATURE_SHAREDUSERID_LAB.get());
		}
		if(deviceReqData) {
			str.add(RStr.FEATURE_DEVICE_REQ_LAB.get());
		}

		return str.toArray();
	}


	private String addPermissionString(SortNode childNodeapkinfo, ApkInfo apkInfo) {
//		String deprecatedPermissions = "";
//
//		boolean isSamsungSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_SAMSUNG_SIGN) != 0 ? true : false;
//		boolean isPlatformSign = (apkInfo.featureFlags & ApkInfo.APP_FEATURE_PLATFORM_SIGN) != 0 ? true : false;
//
//		boolean hasSignatureLevel = false; // apkInfo.hasSignatureLevel;
//		boolean hasSignatureOrSystemLevel = false; // apkInfo.hasSignatureOrSystemLevel;
//		boolean hasSystemLevel = false; // apkInfo.hasSystemLevel;
//		String notGrantPermmissions = "";

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

//		String signaturePermissions = "";
//		if(apkInfo.manifest.permission != null && apkInfo.manifest.permission.length > 0) {
//			if(permissionList.length() > 0) {
//				permissionList.append("\n");
//			}
			//permissionList.append("<permission> [" +  apkInfo.manifest.permission.length + "]\n");
//			for(PermissionInfo info: apkInfo.manifest.permission) {
//				if(!"normal".equals(info.protectionLevel)) {
//					signaturePermissions += info.name + " - " + info.protectionLevel + "\n";
//				}
//			}
//		}
		//XmlPath xmlPermissions = new XmlPath(PermissionGroupManager.class.getResourceAsStream("/values/permissions-info/27/AndroidManifest.xml"));


		//PermissionManager permissionManager = new PermissionManager();

//		permissionManager.clearPermissions();
//		permissionManager.setPlatformSigned(isPlatformSign);
//		permissionManager.setTreatSignAsRevoked(RProp.B.PERM_TREAT_SIGN_AS_REVOKED.get());
//		permissionManager.addUsesPermission(apkInfo.manifest.usesPermission);
//		permissionManager.addUsesPermission(apkInfo.manifest.usesPermissionSdk23);
//		permissionManager.addDeclarePemission(apkInfo.manifest.permission);
//
//		int selectSdkVer = apkInfo.manifest.usesSdk.targetSdkVersion;
//		permissionManager.setSdkVersion(selectSdkVer);

		for(int i=0; i< allPermissions.size(); i++) {

			UsesPermissionInfo temp = allPermissions.get(i);
			ImageIcon icon;
			icon = RImg.APP_ICON.getImageIcon(16, 16);

			SortNode tempnode = new SortNode(new PermissionDiffTreeUserData(temp.name, icon, apkInfo));
			childNodeapkinfo.add(tempnode);

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
//		WebPImageReader reader = new WebPImageReader(new WebPImageReaderSpi());
//		FileImageInputStream fiis;
//		BufferedImage image = null;
//
//		try {
//			URL url = new URL(path);
//			JarURLConnection connection = (JarURLConnection) url.openConnection();
//			File file = new File(connection.getJarFileURL().toURI());
//
//			fiis = new FileImageInputStream(file);
//			reader.setInput(fiis);
//			image = reader.read(0, null);
//		} catch (IOException | URISyntaxException e) {
//			e.printStackTrace();
//		}
//
//		return new ImageIcon(image);
//	}


}
