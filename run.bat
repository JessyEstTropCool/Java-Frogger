@echo off

echo Compiling...
start /WAIT /B javac -Xdiags:verbose Frogger.java
if %errorlevel% equ 0 ( 
    echo Starting...
    start /WAIT /B java Frogger
) else (
    echo Error code %errorlevel%
    pause
)

del *.class