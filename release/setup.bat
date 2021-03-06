@setlocal enableextensions
@cd /d "%~dp0"
@echo off

cd

set APP_PATH=C:\Program Files\APKCompare
set APP_FILE=ApkCompare.exe

set SRC_PATH=%~dp0.
echo SRC_PATH : %SRC_PATH%

if not exist "%SRC_PATH%\%APP_FILE%" (
    set SRC_PATH=C:
    if not exist "%SRC_PATH%\%APP_FILE%" (
        echo Fail : No such %APP_FILE% file
        echo Info : Please copy the APKCompare folder to the C:\ path.
        echo Info : And run setup.bat as administrator.
        goto exit
    )
)

rem --- Check java version ---
java -version > javaver.txt 2>&1
set /p java_ver=<javaver.txt
del javaver.txt

rem if "%java_ver%" == "%java_ver:java version =%" (
rem     goto nosuch_java
rem )
set java_ver=%java_ver:java version =%
set java_ver=%java_ver:"=%
set java_ver=%java_ver:~,3%

rem --- Need Java 1.7 ---
if not "%java_ver%" GEQ "1.7" (
    echo Need JDK7...
    echo current version : %java_ver%
rem     goto nosuch_java
)

rem --- Kill a running demon of adb ---
rem if exist "%APP_PATH%\tool\adb.exe" (
rem     echo adb kill-server
rem     "%APP_PATH%\tool\adb.exe" kill-server
rem )

rem --- Remove an existed folder ---
rmdir /s /q "%APP_PATH%"

rem --- Create folders ---
if not exist "%APP_PATH%" (
    echo Create folder : %APP_PATH%
    mkdir "%APP_PATH%"
)
rem if not exist "%APP_PATH%\data" (
rem      echo Create folder : %APP_PATH%\data
rem      mkdir "%APP_PATH%\data"
rem )
rem if not exist "%APP_PATH%\data\build-master-target-product-security" (
rem      echo Create folder : %APP_PATH%\data\build-master-target-product-security
rem      mkdir "%APP_PATH%\data\build-master-target-product-security"
rem )
if not exist "%APP_PATH%\lib" (
     echo Create folder : %APP_PATH%\lib
     mkdir "%APP_PATH%\lib"
)
if not exist "%APP_PATH%\lib\lib" (
     echo Create folder : %APP_PATH%\lib\lib
     mkdir "%APP_PATH%\lib\lib"
)
if not exist "%APP_PATH%\lib\lib64" (
     echo Create folder : %APP_PATH%\lib\lib64
     mkdir "%APP_PATH%\lib\lib64"
)
rem if not exist "%APP_PATH%\lib\proxy-vole" (
rem      echo Create folder : %APP_PATH%\lib\proxy-vole
rem      mkdir "%APP_PATH%\lib\proxy-vole"
rem )
rem if not exist "%APP_PATH%\plugin" (
rem      echo Create folder : %APP_PATH%\plugin
rem      mkdir "%APP_PATH%\plugin"
rem )
rem if not exist "%APP_PATH%\security" (
rem      echo Create folder : %APP_PATH%\security
rem      mkdir "%APP_PATH%\security"
rem )
if not exist "%APP_PATH%\tool" (
     echo Create folder : %APP_PATH%\tool
     mkdir "%APP_PATH%\tool"
)
if not exist "%APP_PATH%\tool\windows" (
     echo Create folder : %APP_PATH%\tool\windows
     mkdir "%APP_PATH%\tool\windows"
)
if not exist "%APP_PATH%" (
    echo Fail : No created a folder : %APP_PATH%
    goto exit
)

rem --- Copy files ---
copy /Y "%SRC_PATH%\ApkCompare.exe" "%APP_PATH%"
copy /Y "%SRC_PATH%\ApkCompare.jar" "%APP_PATH%"
copy /Y "%SRC_PATH%\ApkCompareContextMenuHandler.dll" "%APP_PATH%"
rem copy /Y "%SRC_PATH%\data\build-master-target-product-security\*" "%APP_PATH%\data\build-master-target-product-security"
copy /Y "%SRC_PATH%\lib\*" "%APP_PATH%\lib"
copy /Y "%SRC_PATH%\lib\lib\*.dll" "%APP_PATH%\lib\lib"
copy /Y "%SRC_PATH%\lib\lib64\*.dll" "%APP_PATH%\lib\lib64"
copy /Y "%SRC_PATH%\tool\*" "%APP_PATH%\tool"
copy /Y "%SRC_PATH%\tool\windows\*" "%APP_PATH%\tool\windows"

regsvr32.exe "%APP_PATH%\ApkCompareContextMenuHandler.dll"

rem --- Launch APK Compare  ---
"%APP_PATH%\%APP_FILE%"

echo Complete

goto exit_success

:nosuch_java
set java_ver=
echo Please retry after setup JDK7..
echo You can download the JDK7 from http://www.oracle.com/technetwork/java/javase/downloads/index.html
goto exit

:exit
pause

:exit_success