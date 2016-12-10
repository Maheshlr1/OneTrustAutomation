@echo off

rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem DO NOT MODIFY THE TEMPLATE FILE.  INSTEAD, COPY THIS FILE AS setenv.bat
rem AND MAKE THE NECESSARY CHANGES IN THE LOCAL (VIEW PRIVATE) FILE.
rem - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

rem - - - - - - - - - - - - - - - - - - - - - - -
rem Set JAVA_HOME to a valid JDK installation dir
rem 	e.g.: set JAVA_HOME=C:\apps\java\1.7.0_25
rem - - - - - - - - - - - - - - - - - - - - - - -
set JAVA_HOME=C:/Program Files/Java/jdk1.8.0_45

rem - - - - - - - - - - - -  - - - - - - - - - -
rem Set ANT_HOME to a valid ANT installation dir
rem 	e.g.: set ANT_HOME=C:\apps\ant\1.8.2
rem - - - - - - - - - - - -  - - - - - - - - - -
set ANT_HOME=C:/Ant

rem - - - - - - - - - - - -  - - - - - - - - - - - - - - - - -
rem TMS specific settings - others leave this empty
rem Set HOS_LIB_PATH to a valid path to HOSLibrary.dll
rem 	e.g.: set HOS_LIB_PATH=C:\build\scpp\data\so\HOS\win
rem - - - - - - - - - - - -  - - - - - - - - - - - - - - - - -
set HOS_LIB_PATH=

set LIBPATH=%HOS_LIB_PATH%;%LIBPATH%
set LD_LIBRARY_PATH=%HOS_LIB_PATH%;%LD_LIBRARY_PATH%

rem - - - - - - - - - - - -  - - - - - - - - - - - - - - - - -
rem These parameters are used to have some flexibility in specifying the log location for HOS and also for setting the log levels.
rem - - - - - - - - - - - -  - - - - - - - - - - - - - - - - -
set HOSLOGDIRPATH=$HOS_LIB_PATH/../log
set HOSCONFIGDIRPATH=$HOS_LIB_PATH/../config

rem - - - - - - - - - - - - - - - - - - - - - - - - - - -
rem Set PATH to include bin directories from JDK and ANT
rem - - - - - - - - - - - - - - - - - - - - - - - - - - -
set PATH=%JAVA_HOME%\bin;%ANT_HOME%\bin;%LIBPATH%;%PATH%

rem - - - - - - - - - - - - - - - - - - - - - - - -
rem Set ANT_OPTS to increase the ANT runtime memory
rem - - - - - - - - - - - - - - - - - - - - - - - -
set ANT_OPTS=-Xmx1444m -XX:MaxPermSize=512m

@echo on
