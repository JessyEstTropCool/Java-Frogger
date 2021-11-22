@echo off

echo Compiling...
start /WAIT /B javac -Xdiags:verbose Snake.java
if %errorlevel% equ 0 ( 
    echo Starting...
    start /WAIT /B java Snake
) else (
    echo Error code %errorlevel%
    pause
)

del *.class