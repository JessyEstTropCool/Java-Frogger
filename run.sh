#!/bin/bash

javac *.java
java Frogger
find . -type f -name "*.class" -delete
