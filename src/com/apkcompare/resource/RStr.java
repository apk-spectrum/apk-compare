package com.apkcompare.resource;

import com.apkspectrum.resource.DefaultResString;
import com.apkspectrum.resource.LanguageChangeListener;
import com.apkspectrum.resource.ResString;

public enum RStr implements ResString<String>
{
	APP_NAME					("@app_name"),
	APP_VERSION					("1.3"),
	APP_BUILD_MODE				("eng"),
	APP_MAKER					("Jinhyeong Lee / Sunggyu Kam"),
	APP_MAKER_EMAIL				("jacsaldevil@gmail.com;sunggyu.kam@gmail.com"),

	BTN_OPEN					("@btn_open"),
	BTN_OPEN_PACKAGE			("@btn_open_pacakge"),
	BTN_MANIFEST				("@btn_manifest"),
	BTN_MANIFEST_SAVE_AS		("@btn_manifest_save"),
	BTN_EXPLORER				("@btn_explorer"),
	BTN_INSTALL					("@btn_install"),
	BTN_INSTALL_DOWNGRAD		("@btn_install_downgrad"),
	BTN_INSTALL_UPDATE			("@btn_install_update"),
	BTN_LAUNCH					("@btn_launch"),
	BTN_LAUNCH_SELECT			("@btn_launch_select_launcher"),
	BTN_SIGN					("@btn_sign"),
	BTN_SETTING					("@btn_setting"),
	BTN_ABOUT					("@btn_about"),
	BTN_SAVE					("@btn_save"),
	BTN_CLOSE					("@btn_close"),
	BTN_OK						("@btn_ok"),
	BTN_YES						("@btn_yes"),
	BTN_NO						("@btn_no"),
	BTN_CANCEL					("@btn_cancel"),
	BTN_REFRESH					("@btn_refresh"),
	BTN_ADD						("@btn_add"),
	BTN_DEL						("@btn_del"),
	BTN_OPENCODE				("@btn_opencode"),
	BTN_OPENING_CODE			("@btn_opening_code"),
	BTN_SEARCH					("@btn_search"),
	BTN_MORE					("@btn_more"),
	BTN_ASSOC_FTYPE				("@btn_assoc_ftype"),
	BTN_UNASSOC_FTYPE			("@btn_unassoc_ftype"),
	BTN_CREATE_SHORTCUT			("@btn_create_shortcut"),
	BTN_SHOW_PERMISSIONS		("@btn_show_permissions"),
	BTN_SHOW_SDK_INFO			("@btn_show_sdk_info"),
	BTN_SELF_SEARCH				("@btn_self_search"),
	BTN_OPEN_LAB				("@btn_open_lab"),
	BTN_OPEN_PACKAGE_LAB		("@btn_open_pacakge_lab"),
	BTN_MANIFEST_LAB			("@btn_manifest_lab"),
	BTN_EXPLORER_LAB			("@btn_explorer_lab"),
	BTN_INSTALL_LAB				("@btn_install_lab"),
	BTN_INSTALL_DOWNGRAD_LAB	("@btn_install_downgrad_lab"),
	BTN_INSTALL_UPDATE_LAB		("@btn_install_update_lab"),
	BTN_LAUNCH_LAB				("@btn_launch_lab"),
	BTN_SIGN_LAB				("@btn_sign_lab"),
	BTN_SETTING_LAB				("@btn_setting_lab"),
	BTN_ABOUT_LAB				("@btn_about_lab"),
	BTN_OPENCODE_LAB			("@btn_opencode_lab"),
	BTN_OPENING_CODE_LAB		("@btn_opening_code_lab"),
	BTN_SEARCH_LAB				("@btn_search_lab"),
	BTN_MORE_LAB				("@btn_more_lab"),
	BTN_ASSOC_FTYPE_LAB			("@btn_assoc_ftype_lab"),
	BTN_UNASSOC_FTYPE_LAB		("@btn_unassoc_ftype_lab"),
	BTN_CREATE_SHORTCUT_LAB		("@btn_create_shortcut_lab"),
	BTN_SELF_SEARCH_LAB			("@btn_self_search_lab"),
	BTN_DETAILS_INFO			("@btn_details_info"),
	BTN_TO_INSTALL				("@btn_to_install"),
	BTN_TO_CANNOT_INSTALL		("@btn_to_cannot_install"),
	BTN_TO_PUSH					("@btn_to_push"),
	BTN_TO_CANNOT_PUSH			("@btn_to_cannot_push"),
	BTN_NO_INSTALL				("@btn_no_install"),
	BTN_IMPOSSIBLE_INSTALL		("@btn_impossible_install"),
	BTN_APPLY_ALL_MODELS		("@btn_apply_all_models"),
	BTN_LAUNCH_AF_INSTALLED		("@btn_launch_after_installed"),
	BTN_REPLACE_EXISTING_APP	("@btn_replace_existing_app"),
	BTN_ALLOW_DOWNGRADE			("@btn_allow_downgrade"),
	BTN_INSTALL_ON_SDCARD		("@btn_install_on_sdcard"),
	BTN_GRANT_RUNTIME_PERM		("@btn_grant_runtime_perm"),
	BTN_FORWARD_LOCK			("@btn_forward_lock"),
	BTN_ALLOW_TEST_PACKAGE		("@btn_allow_test_package"),
	BTN_REBOOT_AF_PUSHED		("@btn_reboot_after_pushed"),
	BTN_PREVIOUS				("@btn_previous"),
	BTN_BACK					("@btn_back"),
	BTN_ADVANCED_OPTIONS_LAB	("@btn_advanced_options_lab"),

	BTN_INSTALLED				("@btn_installed"),
	BTN_NOT_INSTALLED			("@btn_not_installed"),
	BTN_WAITING					("@btn_waiting"),
	BTN_SUCCESS					("@btn_success"),
	BTN_FAIL					("@btn_fail"),
	BTN_REMOVE					("@btn_remove"),
	BTN_CHECK_UPDATE			("@btn_check_update"),
	BTN_GOTO_DOWNLOAD_URL		("@btn_goto_download_url"),

	BTN_FILTER_UNIQUE			("@btn_filter_unique"),
	BTN_FILTER_UNIQUE_LAB		("@btn_filter_unique_lab"),
	BTN_FILTER_DIFFERENT		("@btn_filter_different"),
	BTN_FILTER_DIFFERENT_LAB	("@btn_filter_different_lab"),
	BTN_FILTER_IDENTICAL		("@btn_filter_identical"),
	BTN_FILTER_IDENTICAL_LAB	("@btn_filter_identical_lab"),

	BTN_TREE_NEXT				("@btn_tree_next"),
	BTN_TREE_NEXT_LAB			("@btn_tree_next_lab"),
	BTN_TREE_PREV				("@btn_tree_prev"),
	BTN_TREE_PREV_LAB			("@btn_tree_prev_lab"),

	BTN_TREE_SWAP				("@btn_tree_swap"),
	BTN_TREE_SWAP_LAB			("@btn_tree_swap_lab"),

	BTN_OPEN_APK_SCANNER		("@btn_open_apkscanner"),
	BTN_OPEN_APK_SCANNER_LAB	("@btn_open_apkscanner_lab"),

	TAB_BASIC_INFO				("@tab_basic_info"),
	TAB_APEX_INFO				("@tab_apex_info"),
	TAB_WIDGETS					("@tab_widgets"),
	TAB_LIBRARIES				("@tab_libraries"),
	TAB_RESOURCES				("@tab_resources"),
	TAB_COMPONENTS				("@tab_components"),
	TAB_SIGNATURES				("@tab_signatures"),
	TAB_ABOUT					("@tab_about"),
	TAB_UPDATE					("@tab_update"),

	TAB_SETTING_GENERIC			("@tab_setting_generic"),
	TAB_SETTING_GENERIC_LAB		("@tab_setting_generic_lab"),
	TAB_SETTING_PLUGINS			("@tab_setting_plugins"),
	TAB_SETTING_PLUGINS_LAB		("@tab_setting_plugins_lab"),

	FEATURE_LAB					("@feature_lab"),
	FEATURE_ILOCATION_INTERNAL_LAB	("@feature_install_location_internal_only_lab"),
	FEATURE_ILOCATION_INTERNAL_DESC	("@feature_install_location_internal_only_desc"),
	FEATURE_ILOCATION_AUTO_LAB		("@feature_install_location_auto_lab"),
	FEATURE_ILOCATION_AUTO_DESC		("@feature_install_location_auto_desc"),
	FEATURE_ILOCATION_EXTERNAL_LAB	("@feature_install_location_prefer_external_lab"),
	FEATURE_ILOCATION_EXTERNAL_DESC	("@feature_install_location_prefer_external_desc"),
	FEATURE_LAUNCHER_LAB		("@feature_launcher_lab"),
	FEATURE_LAUNCHER_DESC		("@feature_launcher_desc"),
	FEATURE_HIDDEN_LAB			("@feature_hidden_lab"),
	FEATURE_HIDDEN_DESC			("@feature_hidden_desc"),
	FEATURE_STARTUP_LAB			("@feature_startup_lab"),
	FEATURE_STARTUP_DESC		("@feature_startup_desc"),
	FEATURE_SIGNATURE_UNSIGNED	("@feature_signature_unsigned"),
	FEATURE_SIGNATURE_SIGNED	("@feature_signature_signed"),
	FEATURE_SYSTEM_UID_LAB		("@feature_system_user_id_lab"),
	FEATURE_SYSTEM_UID_DESC		("@feature_system_user_id_desc"),
	FEATURE_SHAREDUSERID_LAB	("@feature_shared_user_id_lab"),
	FEATURE_SHAREDUSERID_DESC	("@feature_shared_user_id_desc"),
	FEATURE_PLATFORM_SIGN_LAB	("@feature_platform_sign_lab"),
	FEATURE_PLATFORM_SIGN_DESC	("@feature_platform_sign_desc"),
	FEATURE_SAMSUNG_SIGN_LAB	("@feature_samsung_sign_lab"),
	FEATURE_SAMSUNG_SIGN_DESC	("@feature_samsung_sign_desc"),
	FEATURE_DEBUGGABLE_LAB		("@feature_debuggable_lab"),
	FEATURE_DEBUGGABLE_DESC		("@feature_debuggable_desc"),
	FEATURE_INSTRUMENTATION_LAB	("@feature_instrumentation_lab"),
	FEATURE_INSTRUMENTATION_DESC("@feature_instrumentation_desc"),
	FEATURE_DEVICE_REQ_LAB		("@feature_device_requirements_lab"),
	FEATURE_DEVICE_REQ_DESC		("@feature_device_requirements_desc"),
	FEATURE_FLAG_LAB			("@feature_flag_lab"),
	FEATURE_FLAG_DESC			("@feature_flag_desc"),
	FEATURE_INSTALLER_LAB		("@feature_installer_lab"),
	FEATURE_HIDDEN_SYS_PACK_LAB	("@feature_hidden_system_package_lab"),
	FEATURE_HIDDEN_SYS_PACK_DESC("@feature_hidden_system_package_desc"),
	FEATURE_DISABLED_PACK_LAB	("@feature_disabled_package_lab"),
	FEATURE_DISABLED_PACK_DESC	("@feature_disabled_package_desc"),

	WIDGET_COLUMN_IMAGE			("@widget_column_image"),
	WIDGET_COLUMN_LABEL			("@widget_column_label"),
	WIDGET_COLUMN_SIZE			("@widget_column_size"),
	WIDGET_COLUMN_ACTIVITY		("@widget_column_activity"),
	WIDGET_COLUMN_ENABLED		("@widget_column_enabled"),
	WIDGET_COLUMN_TYPE			("@widget_column_type"),
	WIDGET_RESIZE_MODE			("@widget_resize_mode"),
	WIDGET_HORIZONTAL			("@widget_horizontal"),
	WIDGET_VERTICAL				("@widget_vertical"),
	WIDGET_TYPE_NORMAL			("@widget_type_nomal"),
	WIDGET_TYPE_SHORTCUT		("@widget_type_shortcut"),

	LIB_COLUMN_INDEX			("@lib_column_index"),
	LIB_COLUMN_PATH				("@lib_column_path"),
	LIB_COLUMN_SIZE				("@lib_column_size"),
	LIB_COLUMN_COMPRESS			("@lib_column_compressibility"),

	COMPONENT_COLUME_CLASS		("@component_column_class"),
	COMPONENT_COLUME_TYPE		("@component_column_type"),
	COMPONENT_COLUME_STARTUP	("@component_column_startup"),
	COMPONENT_COLUME_ENABLED	("@component_column_enabled"),
	COMPONENT_COLUME_EXPORT		("@component_column_export"),
	COMPONENT_COLUME_PERMISSION	("@component_column_permission"),
	COMPONENT_LABEL_XML			("@component_label_xml"),
	COMPONENT_FILTER_PROMPT_XML	("@component_filter_prompt_xml"),
	COMPONENT_FILTER_PROMPT_NAME("@component_filter_prompt_name"),

	CERT_SUMMURY				("@cert_summury"),
	CERT_CERTIFICATE			("@cert_certificate"),

	TREE_OPEN_PACKAGE			("@tree_open_package"),

	SETTINGS_TITLE				("@settings_title"),
	SETTINGS_EDITOR				("@settings_editor"),
	SETTINGS_DIFF_TOOL			("@settings_diff_tool"),
	SETTINGS_TOOLBAR			("@settings_toolbar"),
	SETTINGS_RES				("@settings_res"),
	SETTINGS_LANGUAGE			("@settings_language"),
	SETTINGS_REMEMBER_WINDOW_SIZE("@settings_remember_window_size"),
	SETTINGS_THEME				("@settings_theme"),
	SETTINGS_TABBED_UI			("@settings_tabbed_ui"),
	SETTINGS_FONT				("@settings_font"),
	SETTINGS_THEME_FONT			("@settings_theme_font"),
	SETTINGS_THEME_PREVIEW		("@settings_theme_preview"),
	SETTINGS_PREFERRED_LANG		("@settings_preferred_language"),
	SETTINGS_PREFERRED_EX		("@settings_preferred_example"),
	SETTINGS_ADB_POLICY			("@settings_adb_policy"),
	SETTINGS_ADB_SHARED			("@settings_adb_shared"),
	SETTINGS_ADB_RESTART		("@settings_adb_restart"),
	SETTINGS_ADB_PATH			("@settings_adb_path"),
	SETTINGS_ADB_AUTO_SEL		("@settings_adb_auto_selected"),
	SETTINGS_ADB_SHARED_LAB		("@settings_adb_shared_lab"),
	SETTINGS_ADB_RESTART_LAB	("@settings_adb_restart_lab"),
	SETTINGS_ADB_MONITOR		("@settings_adb_monitor"),
	SETTINGS_LAUNCH_OPTION		("@settings_launch_option"),
	SETTINGS_LAUNCHER_ONLY		("@settings_launcher_only"),
	SETTINGS_LAUNCHER_OR_MAIN	("@settings_launcher_or_main"),
	SETTINGS_LAUNCHER_CONFIRM	("@settings_launcher_confirm"),
	SETTINGS_TRY_UNLOCK			("@settings_try_unlock"),
	SETTINGS_LAUNCH_INSTALLED	("@settings_launch_installed"),
	SETTINGS_TOOLBAR_WITH_SHIFT	("@settings_ext_toolbar_with_shift"),
	SETTINGS_TOOLBAR_WITHOUT_SHIFT("@settings_ext_toolbar_without_shift"),
	SETTINGS_EASYMODE_OPTION_STANDARD("@settings_easymode_option_standard"),
	SETTINGS_EASYMODE_OPTION_EASY("@settings_easymode_option_easy"),
	SETTINGS_USE_UI_BOOSTER		("@settings_use_ui_booster"),
	SETTINGS_ESC_ACT_NONE		("@settings_esc_act_none"),
	SETTINGS_ESC_ACT_CHANG_UI_MODE("@settings_esc_act_change_ui_mode"),
	SETTINGS_ESC_ACT_EXIT		("@settings_esc_act_exit"),

	LABEL_ERROR					("@label_error"),
	LABEL_WARNING				("@label_warning"),
	LABEL_INFO					("@label_info"),
	LABEL_QUESTION				("@label_question"),
	LABEL_LOG					("@label_log"),
	LABEL_INSTALLING			("@label_installing"),
	LABEL_APP_NAME_LIST			("@label_app_name_list"),
	LABEL_NO_PERMISSION			("@label_no_permission"),
	LABEL_SEARCH				("@label_search"),
	LABEL_LOADING				("@label_loading"),
	LABEL_APK_FILE_DESC			("@label_apk_file_description"),
	LABEL_DEVICE				("@label_device"),
	LABEL_PATH					("@label_path"),
	LABEL_USES_RESOURCE			("@label_uses_resource"),
	LABEL_OPEN_WITH				("@label_open_with"),
	LABEL_OPEN_WITH_SYSTEM		("@label_open_with_system"),
	LABEL_OPEN_WITH_JDGUI		("@label_open_with_jdgui"),
	LABEL_OPEN_WITH_JADXGUI		("@label_open_with_jadxgui"),
	LABEL_OPEN_WITH_BYTECODE	("@label_open_with_bytecode_viewer"),
	LABEL_OPEN_WITH_EXPLORER	("@label_open_with_explorer"),
	LABEL_OPEN_WITH_SCANNER		("@label_open_with_apkscanner"),
	LABEL_OPEN_WITH_CHOOSE		("@label_open_with_choose"),
	LABEL_OPEN_TO_TEXTVIEWER	("@label_open_to_textviewer"),
	LABEL_HOW_TO_INSTALL		("@label_how_to_install"),
	LABEL_INSTALL_OPTIONS		("@label_install_options"),
	LABEL_PUSH_OPTIONS			("@label_push_options"),
	LABEL_LAUNCHER_AF_INSTALLED	("@label_launcher_af_installed"),
	LABEL_WITH_LIBRARIES		("@label_with_libraies"),
	LABEL_NUM					("@label_num"),
	LABEL_ABI					("@label_abi"),
	LABEL_DESTINATION			("@label_destination"),
	LABEL_APK_VERIFY			("@label_apk_verify"),
	LABEL_WAIT_FOR_DEVICE		("@label_wait_for_device"),
	LABEL_APK_INSTALL_OPT		("@label_apk_install_option"),
	LABEL_APK_INSTALLING		("@label_apk_installing"),
	LABEL_APK_INSTALL_FINISH	("@label_apk_install_finish"),
	LABEL_SAVE_AS				("@label_save_as"),
	LABEL_BY_PACKAGE_NAME		("@label_by_package_name"),
	LABEL_BY_APP_LABEL			("@label_by_app_label"),
	LABEL_DO_NOT_LOOK_AGAIN		("@label_do_not_look_again"),
	LABEL_SHOW_UPDATE_STARTUP	("@label_show_update_at_startup"),
	LABEL_MARK_A_RUNTIME		("@label_mark_a_runtime"),
	LABEL_MARK_A_COUNT			("@label_mark_a_count"),
	LABEL_TREAT_SIGN_AS_REVOKED	("@label_treat_sign_as_revoked"),
	LABEL_WITH_BELOW_OPTIONS	("@label_with_below_options"),
	LABEL_WITH_OPTIONS			("@label_with_options"),
	LABEL_BY_GROUP				("@label_by_group"),
	LABEL_WITH_LABEL			("@label_with_label"),
	LABEL_REFERENCE_N_LEVELS	("@label_reference_n_levels"),
	LABEL_FILTER				("@label_filter"),
	LABEL_USED_IN_PACKAGE		("@label_used_in_package"),
	LABEL_ALL_ON_ANDROID		("@label_all_on_android"),
	LABEL_USED_IN_PACKAGE_SHORT	("@label_used_in_package_short"),
	LABEL_ALL_ON_ANDROID_SHORT	("@label_all_on_android_short"),
	LABEL_GROUP_COUNT_FORMAT	("@label_group_count_format"),
	LABEL_PERM_COUNT_FORMAT		("@label_perm_count_format"),
	LABEL_FILTER_NONE			("@label_filter_none"),
	LABEL_FILTER_ALL			("@label_filter_all"),
	LABEL_PERMISSION_INFO		("@label_permission_info"),
	LABEL_UI_MODE				("@label_ui_mode"),
	LABEL_CHANGE_UI_SHORTKEY	("@label_change_ui_shortkey"),
	LABEL_UI_BOOSTER			("@label_ui_booster"),
	LABEL_ESC_KEY				("@label_esc_key"),
	LABEL_NO_SUCH_APKSCANNER	("@label_no_such_apkscanner"),
	LABEL_SET_APKSCANNER_PATH	("@label_set_apksacanner_path"),
	LABEL_INTRO_APKSCANNER		("@label_intro_apkscanner"),

	TREE_NODE_DEVICE			("@tree_node_device"),
	TREE_NODE_DISPLAYED			("@tree_node_displayed"),
	TREE_NODE_RECENTLY			("@tree_node_recently"),
	TREE_NODE_RUNNING_PROC		("@tree_node_running"),

	MSG_FAILURE_OPEN_APK		("@msg_failure_open_apk"),
	MSG_NO_SUCH_APK_FILE		("@msg_no_such_apk"),
	MSG_DEVICE_NOT_FOUND		("@msg_device_not_found"),
	MSG_NO_SUCH_LAUNCHER		("@msg_no_such_launcher"),
	MSG_NO_SUCH_PACKAGE_DEVICE	("@msg_no_such_package_device"),
	MSG_NO_SUCH_CLASSES_DEX		("@msg_no_such_classes_dex"),
	MSG_FAILURE_LAUNCH_APP		("@msg_failure_launch_app"),
	MSG_FAILURE_UNINSTALLED		("@msg_failure_uninstalled"),
	MSG_FAILURE_DEX2JAR			("@msg_failure_dex2jar"),
	MSG_SUCCESS_REMOVED			("@msg_success_removed"),
	MSG_SUCCESS_PULL_APK		("@msg_success_pull_apk"),
	MSG_FAILURE_PULLED			("@msg_failure_pulled"),
	MSG_DEVICE_UNAUTHORIZED		("@msg_device_unauthorized"),
	MSG_UNSUPPORTED_PREVIEW		("@msg_unsupported_preview"),
	MSG_CANNOT_WRITE_FILE		("@msg_cannot_write_file"),
	MSG_DISABLED_PACKAGE		("@msg_disabled_package"),
	MSG_SUCCESS_CLEAR_DATA		("@msg_success_clear_data"),
	MSG_FAILURE_CLEAR_DATA		("@msg_failure_clear_data"),
	MSG_INTRO_APKSCANNER_DESC	("@msg_intro_apkscanner_desc"),

	QUESTION_SAVE_OVERWRITE		("@question_save_overwrite"),
	QUESTION_PACK_INFO_CLOSE	("@question_pack_info_close"),
	QUESTION_PACK_INFO_REFRESH	("@question_pack_info_refresh"),
	QUESTION_REMOVE_SYSTEM_APK	("@question_remove_system_apk"),
	QUESTION_REMOVED_REBOOT		("@question_removed_reboot"),
	; // ENUM END
	private DefaultResString res;

	private RStr(String value) {
		res = new DefaultResString(value);
	}

	@Override
	public String getValue() {
		return res.getValue();
	}

	@Override
	public String get() {
		return res.get();
	}

	@Override
	public String toString() {
		return res.toString();
	}

	@Override
	public String getString() {
		return res.getString();
	}

	public static void setLanguage(String l) {
		DefaultResString.setLanguage(l);
	}

	public static String getLanguage() {
		return DefaultResString.getLanguage();
	}

	public static String[] getSupportedLanguages() {
		return DefaultResString.getSupportedLanguages();
	}

	public static void addLanguageChangeListener(
			LanguageChangeListener listener) {
		DefaultResString.addLanguageChangeListener(listener);
	}

	public static void removeLanguageChangeListener(
			LanguageChangeListener listener) {
		DefaultResString.removeLanguageChangeListener(listener);
	}

	public static LanguageChangeListener[] getLanguageChangeListener() {
		return DefaultResString.getLanguageChangeListener();
	}
}
