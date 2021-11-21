#!/bin/bash 

javac Frogger.java
java Frogger
find . -type f -name "*.class" -delete
