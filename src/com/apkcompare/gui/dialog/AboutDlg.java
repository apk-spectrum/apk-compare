package com.apkcompare.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import com.apkcompare.resource.RImg;
import com.apkcompare.resource.RStr;
import com.apkspectrum.swing.HtmlEditorPane;
import com.apkspectrum.swing.MessageBoxPane;

public class AboutDlg /*extends JDialog*/
{
	static public void showAboutDialog(Component component)
	{
		StringBuilder body = new StringBuilder();
		body.append("<div id=\"about\">");
		body.append("  <H1>" + RStr.APP_NAME.get() + " " + RStr.APP_VERSION.get() + "</H1>");
		body.append("  <H3>Using following tools</H3>");
		body.append("  Android Asset Packaging Tool, Android Debug Bridge, signapk<br/>");
		body.append("  - <a href=\"https://developer.android.com/tools/help/index.html\" title=\"Android Developer Site\">https://developer.android.com/tools/help/index.html</a><br/>");
		//body.append("  Apktool " + ApktoolManager.getApkToolVersion() + "<br/>");
		//body.append("  - <a href=\"http://ibotpeaches.github.io/Apktool/\" title=\"Apktool Project Site\">http://ibotpeaches.github.io/Apktool/</a><br/>");
		body.append("  <H3>Included libraries</H3>");		
		body.append("  <a href=\"https://github.com/java-native-access/jna\" title=\"jna Site\">jna-4.4.0</a>,");
		body.append("  <a href=\"https://github.com/BlackOverlord666/mslinks\" title=\"mslinks Site\">mslinks</a>,");
		body.append("  <a href=\"https://commons.apache.org/proper/commons-cli/\" title=\"commons-cli Site\">commons-cli-1.3.1</a>,");
		body.append("  <a href=\"https://code.google.com/archive/p/json-simple/\" title=\"json-simple Site\">json-simple-1.1.1</a>");
		body.append("  <br/><br/><hr/>");
		body.append("  Programmed by <a href=\"mailto:" + RStr.APP_MAKER_EMAIL.get() + "\" title=\"" + RStr.APP_MAKER_EMAIL.get() + "\">" + RStr.APP_MAKER.get() + "</a>, 2015.<br/>");
		body.append("  It is open source project on <a href=\"https://github.sec.samsung.net/jin-h-lee/apk-compare\" title=\"APK Compare Site\">SEC Github</a>");
		body.append("</div>");

		JLabel label = new JLabel();
		Font font = label.getFont();

		// create some css from the label's font
		StringBuilder style = new StringBuilder("#about {");
		style.append("width:350px;margin:0px;padding:0px;");
		style.append("background-color:#"+Integer.toHexString(label.getBackground().getRGB() & 0xFFFFFF)+";");
		style.append("font-family:" + font.getFamily() + ";");
		style.append("font-weight:" + (font.isBold() ? "bold" : "normal") + ";");
		style.append("font-size:" + font.getSize() + "pt;}");
		style.append("#about a {text-decoration:none;}");
		style.append("H1 {margin-top: 0px; margin-bottom: 0px;}");
		style.append("H3 {margin-top: 5px; margin-bottom: 0px;}");

		// html content
		HtmlEditorPane hep = new HtmlEditorPane("", style.toString(), body.toString());
		hep.setEditable(false);
		hep.setBackground(label.getBackground());
		hep.setPreferredSize(new Dimension(400,300));

		MessageBoxPane.showMessageDialog(component, hep, RStr.BTN_ABOUT.get(), MessageBoxPane.INFORMATION_MESSAGE, RImg.APP_ICON.getImageIcon(100,100));
	}
}
