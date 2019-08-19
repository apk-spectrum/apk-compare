package com.apkscanner.core.scanner;

import java.io.File;

import com.apkscanner.data.apkinfo.ApkInfo;
import com.apkscanner.data.apkinfo.ResourceInfo;
import com.apkscanner.resource.Resource;
import com.apkscanner.tool.aapt.AaptNativeWrapper;
import com.apkscanner.tool.aapt.AaptXmlTreeNode;
import com.apkscanner.tool.aapt.AaptXmlTreePath;
import com.apkscanner.util.FileUtil;
import com.apkscanner.util.Log;
import com.apkscanner.util.ZipFileUtil;

public class AaptScanner extends ApkScanner
{
	private AaptNativeScanner resourceScanner;

	public AaptScanner(StatusListener statusListener)
	{
		super(statusListener);
	}

	@Override
	public void openApk(final String apkFilePath, final String frameworkRes)
	{
		Log.i("openApk() " + apkFilePath + ", res " + frameworkRes);
		if(apkFilePath == null) {
			Log.e("APK file path is null");
			errorOccurred(ERR_UNAVAIlABLE_PARAM);
			return;
		}

		File apkFile = new File(apkFilePath);
		if(!apkFile.exists()) {
			Log.e("No Such APK file");
			errorOccurred(ERR_NO_SUCH_FILE);
			return;
		}

		scanningStarted();

		Log.i("I: new resourceScanner...");
		if(resourceScanner != null) {
			resourceScanner.clear(true);
		}
		resourceScanner = new AaptNativeScanner(null);

		Log.i("I: add asset apk");
		resourceScanner.openApk(apkFile.getAbsolutePath(), frameworkRes);
		if(!resourceScanner.hasAssetManager()){
			resourceScanner = null;
			Log.e("Failure : Can't open the AssetManager");
			errorOccurred(ERR_CAN_NOT_ACCESS_ASSET);
			return;
		}

		Log.i("I: getDump AndroidManifest...");
		String[] androidManifest = AaptNativeWrapper.Dump.getXmltree(apkFile.getAbsolutePath(), new String[] { "AndroidManifest.xml" });
		if(androidManifest == null || androidManifest.length == 0) {
			Log.e("Failure : Can't read the AndroidManifest.xml");
			errorOccurred(ERR_NO_SUCH_MANIFEST);
			return;
		}

		Log.i("I: createAaptXmlTree...");
		AaptXmlTreePath manifestPath = new AaptXmlTreePath();
		manifestPath.createAaptXmlTree(androidManifest);

		if(manifestPath.getNode("/manifest") == null || manifestPath.getNode("/manifest/application") == null) {
			Log.e("Failure : Wrong format. Don't have '<manifest>' or '<application>' tag");
			errorOccurred(ERR_WRONG_MANIFEST);
			return;
		}

		stateChanged(Status.STANBY);

		apkInfo = new ApkInfo();
		apkInfo.filePath = apkFile.getAbsolutePath();
		apkInfo.fileSize = apkFile.length();
		apkInfo.tempWorkPath = FileUtil.makeTempPath(apkInfo.filePath.substring(apkInfo.filePath.lastIndexOf(File.separator)));
		apkInfo.resourceScanner = resourceScanner;

		Log.v("Temp path : " + apkInfo.tempWorkPath);

		final AaptManifestReader manifestReader = new AaptManifestReader(null, apkInfo.manifest);
		manifestReader.setResourceScanner(resourceScanner);
		manifestReader.setManifestPath(manifestPath);
		Log.i("xmlTreeSync completed");

		Log.i("I: read basic info...");
		manifestReader.readBasicInfo();
		Log.i("readBasicInfo() completed");

		Log.i("read permissions start");
		manifestReader.readPermissions();
		Log.i("read permissions completed");

		ResourceInfo[] icons = apkInfo.manifest.application.icons;
		if(icons != null && icons.length > 0) {
			String urlFilePath = null;
			urlFilePath = apkInfo.filePath.replaceAll("#", "%23");

			String jarPath = "jar:file:" + urlFilePath + "!/";
			for(ResourceInfo r: icons) {
				if(r.name == null) {
					r.name = Resource.IMG_DEF_APP_ICON.getPath();
				} else if(r.name.endsWith(".qmg")) {
					r.name = Resource.IMG_QMG_IMAGE_ICON.getPath();
				} else if(r.name.endsWith(".xml")) {
					Log.w("image resource is xml : " + r.name);
					String[] iconXml = AaptNativeWrapper.Dump.getXmltree(apkInfo.filePath, new String[] { r.name });
					AaptXmlTreePath iconXmlPath = new AaptXmlTreePath();
					iconXmlPath.createAaptXmlTree(iconXml);
					AaptXmlTreeNode iconNode = iconXmlPath.getNode("//item[@"+iconXmlPath.getAndroidNamespaceTag()+":drawable]");
					if(iconNode != null) {
						icons = manifestReader.getAttrResourceValues(iconNode, ":drawable", iconXmlPath.getAndroidNamespaceTag());
					}
					if(icons == null || icons.length == 0) {
						icons = new ResourceInfo[] { new ResourceInfo(Resource.IMG_DEF_APP_ICON.getPath()) };
					} else {
						for(ResourceInfo r2: icons) {
							r2.name = jarPath + r2.name;
						}
					}
				} else {
					r.name = jarPath + r.name;
				}
			}
		} else {
			icons = new ResourceInfo[] { new ResourceInfo(Resource.IMG_DEF_APP_ICON.getPath()) };
		}
		apkInfo.manifest.application.icons = icons;

		Log.i("read basic info completed");
		stateChanged(Status.BASIC_INFO_COMPLETED);

		new Thread(new Runnable() {
			public void run() {
				Log.i("read signatures...");
				apkInfo.certificates = solveCert();
				stateChanged(Status.CERT_COMPLETED);
				Log.i("read signatures completed...");
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				Log.i("I: read libraries list...");
				apkInfo.libraries = ZipFileUtil.findFiles(apkInfo.filePath, ".so", null);
				stateChanged(Status.LIB_COMPLETED);

				Log.i("I: read Resource list...");
				apkInfo.resources = ZipFileUtil.findFiles(apkInfo.filePath, null, null);
				stateChanged(Status.RESOURCE_COMPLETED);
			}
		}).start();

		new Thread(new Runnable() {
			public void run() {
				Log.i("I: read aapt dump resources...");
				apkInfo.resourcesWithValue = AaptNativeWrapper.Dump.getResources(apkInfo.filePath, true);
				stateChanged(Status.RES_DUMP_COMPLETED);
				Log.i("resources completed");
			}
		}).start();

		// Activity/Service/Receiver/provider intent-filter
		Log.i("I: read components...");
		manifestReader.readComponents();
		stateChanged(Status.ACTIVITY_COMPLETED);

		// widget
		Log.i("I: read widgets...");
		apkInfo.widgets = manifestReader.getWidgetList(apkInfo.filePath);
		stateChanged(Status.WIDGET_COMPLETED);
	}

	private void deleteTempPath(String tmpPath, String apkPath)
	{
		if(tmpPath != null && !tmpPath.isEmpty()) {
			Log.i("delete Folder : "  + tmpPath);
			FileUtil.deleteDirectory(new File(tmpPath));
		}
		if(apkPath != null && !apkPath.isEmpty() && apkPath.startsWith(FileUtil.getTempPath())) {
			File parent = new File(apkPath).getParentFile();
			Log.i("delete temp APK folder : "  + parent.getPath());
			while(parent != null && parent.exists() && parent.getParentFile() != null 
					&& parent.getParentFile().listFiles().length == 1 
					&& parent.getParentFile().getAbsolutePath().length() > FileUtil.getTempPath().length()) {
				parent = parent.getParentFile();
			}
			FileUtil.deleteDirectory(parent);
			if(new File(apkPath).exists()) {
				Log.i("failure: not delete apk file");
				//Launcher.deleteTempPath(apkPath);
			}
		}
	}

	@Override
	public void clear(boolean sync)
	{
		Log.d("clear");
		if(apkInfo == null)
			return;

		if(resourceScanner != null) {
			resourceScanner.clear(true);
			resourceScanner = null;
		}
		AaptNativeScanner.lock();
		AaptNativeWrapper.lock();

		final String tmpPath = apkInfo.tempWorkPath;
		final String apkPath = apkInfo.filePath;
		if(sync) {
			deleteTempPath(tmpPath, apkPath);
			AaptNativeScanner.unlock();
			AaptNativeWrapper.unlock();
		} else {
			new Thread(new Runnable() {
				public void run()
				{
					deleteTempPath(tmpPath, apkPath);
					AaptNativeScanner.unlock();
					AaptNativeWrapper.unlock();
				}
			}).start();
		}
		apkInfo = null;
	}
}
