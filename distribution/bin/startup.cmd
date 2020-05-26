@echo off
if not exist "%JAVA_HOME%\bin\java.exe" (
    echo "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
    EXIT /B 1
)
set "JAVA=%JAVA_HOME%\bin\java.exe"

setlocal

set BASE_DIR=%~dp0
set BASE_DIR="%BASE_DIR:~0,-5%"

set DEFAULT_CONFIG_LOCATIONS="classpath:/,classpath:/config/,file:./,file:./config/"
set CUSTOM_CONFIG_LOCATIONS=%DEFAULT_CONFIG_LOCATIONS%,file:%BASE_DIR%/conf/

set "JAVA_OPT=%JAVA_OPT% -Xms512m -Xmx512m -Xmn256m"
set "JAVA_OPT=%JAVA_OPT% -Dvmr.home=%BASE_DIR%"
set "JAVA_OPT=%JAVA_OPT% -jar %BASE_DIR%\boot\vmr-web.jar"
set "JAVA_OPT=%JAVA_OPT% --spring.config.location=%CUSTOM_CONFIG_LOCATIONS%"

echo "%JAVA%" %JAVA_OPT%"
call "%JAVA%" %JAVA_OPT% vmr.web %*
