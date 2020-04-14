# COSC2123 - Algorithms & Analysis - Assignment I

### Author: [Anh Nguyen](alex.nguyen.3141@gmail.com) , [Daniel Ho](s3718201@rmit.edu.au)
  

#### To run the code
- Compile the program:
```
javac *.java
``` 
- Run the program afterwards:
```
java RunqueueTester <option> <input_file> <output_file>
```
- There are 3 **options**: tree, array, linkedlist


#### To run the data generator functionalities
- Compile the program:
```
javac *.java
``` 
- To generate data:
```
java RunqueueTimeTester generate <size> <output file>
```
- To test the data:
```
java RunqueueTimeTester time <implementation> <function> <input_file>
```
where

**\<functions\>**: enqueue, dequeue, preceding

**\<implementation\>**: array, linkedlist, tree

**\<input_file\>**: a file containing EN operations
