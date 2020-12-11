package com.apkcompare.resource;

import com.apkcompare.gui.DiffTree;
import com.apkspectrum.resource._RConst;

public interface RConst extends _RConst
{
	public static final String POSITION_KEY = "POSITION";
	public static final String FILE_DROP_TOP_KEY = "FILE_DROP_TOP";

	public static final int UNASSIGNED = DiffTree.UNASSIGNED;
	public static final int LEFT = DiffTree.LEFT;
	public static final int RIGHT = DiffTree.RIGHT;

	// @see UiEventHandler
	public static final String APK_COMPARER_KEY = "APK_COMPARER_KEY";
	public static final String DIFF_TREE_PAIR_KEY = "DIFF_TREE_PAIR_KEY";

	public static final int FLAG_SET_LEFT_TREE	= 0x01;
	public static final int FLAG_SET_RIGHT_TREE	= 0x02;
	public static final int FLAG_SET_BOTH_TREE	= FLAG_SET_LEFT_TREE
												| FLAG_SET_RIGHT_TREE;
}
