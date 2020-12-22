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
	public static final int FLAG_AVAILABLE_MOVE_NEXT = 0x100;
	public static final int FLAG_AVAILABLE_MOVE_PREV = 0x200;

	public static final int DIRECTION_NEXT = 0;
	public static final int DIRECTION_PREV = 1;
}
