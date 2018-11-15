//I have used Java as the programming language	
//Version required for java : 1.8
_______________________________________________________________________________________________________________
The following files have been submitted:
1) Interpreter.java
2) SExp.java
3) Evaluator.java
4) Makefile
5) READMEP2.txt
6) DesignP2.txt
__________________________________________________________________________________________________________________
Instructions to run the program:--
Here are the instructions to compile and run the program:

1) Run make
  This should build all the .java files and generate necessary .class files.

2) java Interpreter 
  Please run this command. 
  
To clean all class files:--

1) Run make clean
  The .class files created will be deleted. 

_______________________________________________________________________________________________________________

NEEDED FORMAT FOR INPUT:--

Every s-expression except the last must be followed by a line conatining a "$" sign. After reading the line, program will output the dotted 
representation of the input. Then the interpreter evaluates the s-expression and the result is printed in the next line. 

 After entering the last s-expression a line has to be entered with "$$". Now the program terminates after producing output
for the last entered s-expression. This format has to be followed for the program to work smoothly.