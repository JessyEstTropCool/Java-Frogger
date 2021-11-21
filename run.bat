@echo off

start /WAIT javac Frogger.java
start /WAIT java Frogger
del *.class