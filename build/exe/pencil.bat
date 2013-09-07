@echo off
rem -- --------------------------------------------------------
rem -- check existence of java
rem -- java should be installed in the %JAVA_HOME% directory.
rem -- --------------------------------------------------------

if "%JAVA_HOME%" == "" goto noJava
if not exist "%JAVA_HOME%\bin\java.exe" goto wrongJavaHome

rem -- starts enhancer

%JAVA_HOME%\bin\java.exe -jar "%PENCIL_HOME%\lib\pencil-core.jar" %1 %2 %3 %4 %5 %6 %7 %8
goto end

rem -- --------------------------------------------------------
rem -- error messages 
rem -- --------------------------------------------------------
:noJava
echo "Pencil can't find JDK. Please install a JDK or JRE and define a JAVA_HOME as an OS-variable."
goto end

:wrongJavaHome
echo "The defined JAVA_HOME doesn't contain java.exein the bin-directory. Please specify a correct JAVA_HOME or reinstall JAVA."

:end