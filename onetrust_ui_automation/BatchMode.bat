@echo off

rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem ################	PHASE1		################
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

rem Reach build directory of uitestframework
cd C:/onetrust_ui_automation/build


rem Calls setenv.bat

Call setenv.bat

rem Calls grdale to build project
rem CALL gradle -f build.xml 
CALL gradle build-tests


rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem ################	PHASE2		################
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -


rem Change directory to TestNG output folder
cd C:\onetrust_ui_automation\testOutput

rem ### rem Unziping ui-tests.zip file
CALL jar xf ui-tests.zip

rem Change directory to run-tests
cd C:\onetrust_ui_automation\testOutput\run-tests

rem Run the project with gradle
rem CALL ant -f build.xml
CALL gradle run-tests

rem Stop Command Prompt to be closed automatically
pause
@echo on