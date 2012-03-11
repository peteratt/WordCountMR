#!/bin/bash

cp javaexec/*.java .
javac WordCountJ.java WordCountMonitor.java WordCountThread.java
java -classpath . WordCountJ -t 4 -q

rm WordCountJ.java WordCountMonitor.java WordCountThread.java *.class