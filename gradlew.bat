@ECHO OFF
SET DIR=%~dp0
IF NOT EXIST "%DIR%gradle\wrapper\gradle-wrapper.jar" (
  ECHO Wrapper JAR missing; invoking system gradle to generate wrapper...
  gradle wrapper --gradle-version 8.7
)
CALL "%DIR%gradlew.bat" %*
