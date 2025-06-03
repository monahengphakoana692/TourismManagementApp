@echo off
set JAVA_HOME="C:\Program Files\Java\jdk-17"
set JAVAFX_PATH="C:\openjfx-17.0.15_windows-x64_bin-sdk\javafx-sdk-17.0.15\lib"
set ARCGIS_PATH="C:\arcgis-runtime-sdk-java-100.15.0\jniLibs\WIN64"

echo Starting TourGuide Application...
echo Java: %JAVA_HOME%
echo JavaFX: %JAVAFX_PATH%

%JAVA_HOME%\bin\java ^
  --module-path %JAVAFX_PATH% ^
  --add-modules javafx.controls,javafx.fxml,javafx.graphics,javafx.media,javafx.web ^
  --add-exports javafx.graphics/com.sun.javafx.tk=ALL-UNNAMED ^
  --add-exports javafx.graphics/com.sun.javafx.scene=ALL-UNNAMED ^
  -Djava.library.path=%ARCGIS_PATH% ^
  -jar target\fornow-1.0-SNAPSHOT.jar

if %errorlevel% neq 0 (
   echo.
   echo [ERROR] Application failed to start (Error Code: %errorlevel%)
   pause
)