; Script generated with the Venis Install Wizard

; Define your application name
!define PROJECTNAME "APK Compare"
!define PROJECTNAMEANDVERSION "APK Compare 0.1"

; Main Install settings
Name "${PROJECTNAMEANDVERSION}"
InstallDir "$PROGRAMFILES64\APKCompare"
InstallDirRegKey HKLM "Software\${PROJECTNAME}" ""
OutFile "setup.exe"

; Use compression
SetCompressor Zlib

; Modern interface settings
!include "MUI.nsh"

!define MUI_ABORTWARNING
!define MUI_FINISHPAGE_RUN "$INSTDIR\ApkCompare.exe"

!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_COMPONENTS
!insertmacro MUI_PAGE_DIRECTORY
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

; Set languages (first is default language)
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Korean"
!insertmacro MUI_RESERVEFILE_LANGDLL

LangString APP_NAME ${LANG_ENGLISH} "APK Compare"
LangString APP_NAME ${LANG_KOREAN} "APK Compare"
LangString APP_NAME_DESC ${LANG_ENGLISH} "APK Compare"
LangString APP_NAME_DESC ${LANG_KOREAN} "APK Compare"
LangString ADD_STARTMENU ${LANG_ENGLISH} "Start Menu Shortcuts"
LangString ADD_STARTMENU ${LANG_KOREAN} "시작메뉴에 추가"
LangString ADD_STARTMENU_DESC ${LANG_ENGLISH} "Start Menu Shortcuts"
LangString ADD_STARTMENU_DESC ${LANG_KOREAN} "시작메뉴에 바로가기 아이콘을 추가 합니다."
LangString ADD_DESKTOP ${LANG_ENGLISH} "Desktop Shortcut"
LangString ADD_DESKTOP ${LANG_KOREAN} "바탕화면에 추가"
LangString ADD_DESKTOP_DESC ${LANG_ENGLISH} "Desktop Shortcut"
LangString ADD_DESKTOP_DESC ${LANG_KOREAN} "바탕화면에 바로가기 아이콘을 추가 합니다."

Section $(APP_NAME) Section1

	; Set Section properties
	SectionIn RO
	SetOverwrite on

	; Set Section Files and Shortcuts
	SetOutPath "$INSTDIR\"
	Delete "$INSTDIR\APKInfoDlg.jar"

	File "release\ApkCompare.jar"
	File "release\ApkCompare.exe"
	File "release\ApkCompareContextMenuHandler.dll"
	File "release\ScannerCore.jar"
	SetOutPath "$INSTDIR\lib\"
	File "release\lib\commons-cli-1.3.1.jar"
	File "release\lib\jna-4.4.0.jar"
	File "release\lib\jna-platform-4.4.0.jar"
	File "release\lib\json-simple-1.1.1.jar"
	File "release\lib\luciad-webp-imageio.jar"
	File "release\lib\mslinks.jar"
	File "release\lib\webp-imageio32.dll"
	File "release\lib\webp-imageio64.dll"
	SetOutPath "$INSTDIR\tool\"
	File "release\tool\aapt.exe"
	File "release\tool\AaptNativeWrapper32.dll"
	File "release\tool\AaptNativeWrapper64.dll"

    Exec '"regsvr32.exe" "$INSTDIR\ApkCompareContextMenuHandler.dll"'

SectionEnd

Section $(ADD_STARTMENU) Section2

	; Set Section properties
	SetOverwrite on

	; Set Section Files and Shortcuts
	CreateDirectory "$SMPROGRAMS\APK Compare"
	CreateShortCut "$SMPROGRAMS\APK Compare\$(APP_NAME).lnk" "$INSTDIR\ApkCompare.exe"
	CreateShortCut "$SMPROGRAMS\APK Compare\Uninstall.lnk" "$INSTDIR\uninstall.exe"

SectionEnd

Section $(ADD_DESKTOP) Section3

	; Set Section properties
	SetOverwrite on

	; Set Section Files and Shortcuts
	CreateShortCut "$DESKTOP\$(APP_NAME).lnk" "$INSTDIR\ApkCompare.exe"

SectionEnd

Section -FinishSection

	WriteRegStr HKLM "Software\${PROJECTNAME}" "" "$INSTDIR"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PROJECTNAME}" "DisplayName" "${PROJECTNAME}"
	WriteRegStr HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PROJECTNAME}" "UninstallString" "$INSTDIR\uninstall.exe"
	WriteUninstaller "$INSTDIR\uninstall.exe"

SectionEnd

; Modern install component descriptions
!insertmacro MUI_FUNCTION_DESCRIPTION_BEGIN
	!insertmacro MUI_DESCRIPTION_TEXT ${Section1} $(APP_NAME_DESC)
	!insertmacro MUI_DESCRIPTION_TEXT ${Section2} $(ADD_STARTMENU_DESC)
	!insertmacro MUI_DESCRIPTION_TEXT ${Section3} $(ADD_DESKTOP_DESC)
!insertmacro MUI_FUNCTION_DESCRIPTION_END

;Uninstall section
Section Uninstall

	;Remove from registry...
	DeleteRegKey HKLM "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PROJECTNAME}"
	DeleteRegKey HKLM "SOFTWARE\${PROJECTNAME}"

	Exec '"regsvr32.exe" /u "$INSTDIR\ApkCompareContextMenuHandler.dll"'

	; Delete self
	Delete "$INSTDIR\uninstall.exe"

	; Delete Shortcuts
	Delete "$SMPROGRAMS\APK Compare\$(APP_NAME).lnk"
	Delete "$SMPROGRAMS\APK Compare\Uninstall.lnk"
	Delete "$DESKTOP\$(APP_NAME).lnk"

	; Clean up APK Compare
	Delete "$INSTDIR\ApkCompare.jar"
	Delete "$INSTDIR\ApkCompare.exe"
	Delete "$INSTDIR\ApkCompareContextMenuHandler.dll"
	Delete "$INSTDIR\ScannerCore.jar"
	Delete "$INSTDIR\lib\commons-cli-1.3.1.jar"
	Delete "$INSTDIR\lib\jna-4.4.0.jar"
	Delete "$INSTDIR\lib\jna-platform-4.4.0.jar"
	Delete "$INSTDIR\lib\json-simple-1.1.1.jar"
	Delete "$INSTDIR\lib\luciad-webp-imageio.jar"
	Delete "$INSTDIR\lib\mslinks.jar"
	Delete "$INSTDIR\lib\webp-imageio32.dll"
	Delete "$INSTDIR\lib\webp-imageio64.dll"
	Delete "$INSTDIR\tool\aapt.exe"
	Delete "$INSTDIR\tool\AaptNativeWrapper32.dll"
	Delete "$INSTDIR\tool\AaptNativeWrapper64.dll"

	; Remove remaining directories
	RMDir "$SMPROGRAMS\APK Compare"
	RMDir "$INSTDIR\tool\"
	RMDir "$INSTDIR\lib\"
	RMDir "$INSTDIR\data\"
	RMDir "$INSTDIR\"


SectionEnd

; On initialization
Function .onInit

	!insertmacro MUI_LANGDLL_DISPLAY

FunctionEnd

; eof