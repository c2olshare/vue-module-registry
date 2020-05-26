@echo off
if not exist "%JAVA_HOME%\bin\jps.exe" (
    echo "Please set the JAVA_HOME variable in your environment, We need java(x64)! jdk8 or later is better!"
    EXIT /B 1
)

echo "Killing vmr.web process!"
for /f "tokens=1" %%i in ('jps -m ^| find "vmr.web"') do ( taskkill /F /PID %%i )

echo "Done!"
