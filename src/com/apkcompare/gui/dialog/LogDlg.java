package com.apkcompare.gui.dialog;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.apkcompare.gui.util.MessageBoxPane;
import com.apkcompare.resource.Resource;
import com.apkscanner.util.Log;

public class LogDlg
{
	public LogDlg()
	{

	}
	
	static public void showLogDialog(Component component)
	{
		JTextArea taskOutput = new JTextArea();
		taskOutput.setText(Log.getLog());
		taskOutput.setEditable(false);
		taskOutput.setCaretPosition(0);
		
		JScrollPane scrollPane = new JScrollPane(taskOutput);
		scrollPane.setPreferredSize(new Dimension(600, 400));

		MessageBoxPane.showMessageDialog(component, scrollPane, Resource.STR_LABEL_LOG.getString(), MessageBoxPane.INFORMATION_MESSAGE, null);
	}
}
