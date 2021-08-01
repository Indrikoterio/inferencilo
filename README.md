# Inferencilo - A Prolog-like Inference Engine

Inferencilo is an inference engine written in Java. Facts and rules can be defined in Java, or loaded from a text file.

In the code sample below, Java objects are used to define the fact 'mother(Gina, Frank).'

```
 Constant mother = Constant.inst("mother");
 Constant Gina   = Constant.inst("Gina");
 Constant Frank  = Constant.inst("Frank");
 Complex  fact   = new Complex(mother, Gina, Frank);
```

(About terminology: 'complex' is synonymous with 'compound'.)

The above fact can be defined more simply by passing a string to the Complex constructor:

```
 Complex  fact = new Complex("mother(Gina, Frank).");
```

Inferencilo can also fetch facts and rules from a text file. The above fact is written:

```
 mother(Gina, Frank).
```

Notice that in Inferencilo, unlike Prolog, constants (or atoms) can start with an upper case letter. (They can even have spaces.)

Variables are defined by putting a dollar sign in front of the variable name:

```
 Variable ch = Variable.inst("$Child");
 Complex  goal  = new Complex(mother, Gina, ch);
```

In a text file, the goal above would be written:

```
 mother(Gina, $Child)
```


## Requirements

JDK version 8 or higher is sufficient.

[https://www.oracle.com/java/technologies/javase-downloads.html](https://www.oracle.com/java/technologies/javase-downloads.html)

## Cloning

To clone the repository, run the following command in a terminal window:

```
 git clone https://github.com/Indrikoterio/inferencilo
```

The repository has three folders:

```
 inference/inferencilo
 inference/test
 inference/demo
```

## Test, Demo

To run the all the tests, move to the test folder and enter './run'.

```
 cd test
 ./run
```

Similarly, to run the demo program...

```
 cd demo
 ./run
```

## Use

To use Inferencilo in your own project, copy the subfolder 'inferencilo' to your project folder. In your Java classes, you must import the package:

```
 import inferencilo.*;
```

You may have to set Java's CLASSPATH environment variable...

```
 export CLASSPATH=.:..
```

...or add the path as a command line option (cp).

```
 javac -cp ".:.." ParseDemo.java
 java -cp ".:.." ParseDemo
```

The program ParseDemo.java demonstrates how to set up a knowledge base and make queries. If you intend to incorporate Inferencilo into your own project, this is a good reference. There are detailed comments in the header. 

Inferencilo doesn't have a lot of built-in functions, but it does have:

```
append, functor, print, nl, include, exclude, greater_than (etc.)
```

...and some arithmetic functions:

```
add, subtract, multply, divide
```

Please refer to the test programs for examples of how to use these.

If you need write your own predicates and functions, the classes BuiltInPredicate and PFunction can be extended. Refer to the BuiltIn5.java, Capitalize.java, TestBuiltInPredicate.java, and TestFunction.java to see how this is done.

## Developer

Inferencilo was developed by Cleve (Klivo) Lendon.

## Contact

To contact the developer, send email to indriko@yahoo.com . Comments, suggestions and criticism are welcomed.

## History

First release, August 2021.

## Reference

The structure of this inference engine is based on the Predicate Calculus Problem Solver presented in chapters 23 and 24 of 'AI Algorithms...'. I highly recommend this book.

```
AI Algorithms, Data Structures, and Idioms in Prolog, Lisp, and Java
George F. Luger, William A. Stubblefield, ©2009 | Pearson Education, Inc. 
ISBN-13: 978-0-13-607047-4
ISBN-10: 0-13-607047-7
```

## License

The source code for Inferencilo is licensed under the MIT license, which you can find in [LICENSE](LICENSE).
