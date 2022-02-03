README
----------------------
Asignment 1c
---------------------

Instructions for execution:
----------------------------------------------
* This program is written in java. The version used is 1.8 Oracle OpenJDK version 17.0.1
* If java is installed, run the below command to compile the source code files:
	javac MyThread.java DeadlockAvoidance.java
* Then run the below command to run the program:
	java DeadlockAvoidance
* If you have facility to run makefile(for example in linux), use the following commands:
	1. make     // compiles
	2. make run // executes
* If you are using intellij idea, open the project using the IDE and run the DeadlockAvoidance class which contains 
the main method.
* The program starts by taking input from input.txt.
After that it checks if there is any safe sequence possible. If there is possible, threads are created and they run 
according to a valid safe sequence. If not possible, the program quits.