# Inferencilo - A Prolog-like Inference Engine

Inferencilo is an inference engine written in Java. The rule syntax looks very much like Prolog, but there are some differences.

## Briefly

In the code sample below, the fact 'mother(Gina, Frank).' is defined in Java.

```
 Complex  fact = new Complex("mother(Gina, Frank).");
```

('Complex' is synonymous with 'compound'.)

Inferencilo can also read facts and rules from a text file. The above fact is written:

```
 mother(Gina, Frank).
```

Notice that in Inferencilo, unlike Prolog, constants (or atoms) can start with an upper case letter. (They can even have spaces.)

Variables are defined by putting a dollar sign in front of the variable name:

```
 Constant mother = Constant.inst("mother");
 Constant Gina   = Constant.inst("Gina");
 Variable child  = Variable.inst("$Child");
 Complex  goal   = new Complex(mother, Gina, child);
```

In a text file, the goal above would be written:

```
 mother(Gina, $Child)
```

Note: The <i>anonymous variable</i> must also begin with a dollar sign: $\_ . A simple underscore '\_' is treated as a Constant.

## Application / Limitations

This inference engine implements a back-chaining algorithm. It was designed primarily to solve problems of logic.

Numbers are implemented as Constant objects, which enclose String objects. Because of this, Inferencilo should <b>not</b> be used for number crunching. It is OK to include arithmetical functions, such as '<i>greater than or equal</i>', in the knowledge base:

```
voter($P) :- $P = person($_, $Age), $Age >= 18.
```

But a Quick Sort algorithm, for example, written with inference engine rules, would be horrendously inefficient. Do not use the inference engine to solve such problems. Write sorting algorithms in Java code; you can pass the sorted data to the inference engine.

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
 inferencilo/inferencilo
 inferencilo/test
 inferencilo/demo
```

## Usage

The Java class Query loads facts and rules from a file, and allows the user to query the knowledge base. Query can be built, run and tested in a terminal window as follows:

```
javac Query.java
java Query test/kings.txt
?- father($F, $C).
$F = Godwin, $C = Harold II
$F = Godwin, $C = Tostig
$F = Godwin, $C = Edith
$F = Tostig, $C = Skule
$F = Harold II, $C = Harold
No
?-
```

To use Inferencilo in your own project, copy the subfolder 'inferencilo' to your project folder. In your Java classes, you must import the package:

```
 import inferencilo.*;
```

You may have to set Java's CLASSPATH environment variable...

```
 export CLASSPATH=.:..
```

...or add the class path as a command line option (cp).

```
 javac -cp ".:.." ParseDemo.java
 java -cp ".:.." ParseDemo
```

The program ParseDemo.java demonstrates how to set up a knowledge base and make queries. If you intend to incorporate Inferencilo into your own project, this is a good reference. There are detailed comments in the header.

To run ParseDemo, move to the demo folder and execute the batch file 'run'.

```
 cd demo
 ./run
```

Inferencilo doesn't have a lot of built-in predicates, but it does have:

```
append, functor, print, nl, include, exclude, greater_than (etc.)
```

...and some arithmetic functions:

```
add, subtract, multply, divide
```

Please refer to the test programs for examples of how to use these.

To run the tests, open a terminal window, go to the test folder, and execute 'run'.

```
 cd test
 ./run
```

If you need write your own predicates and functions (in Java), the classes BuiltInPredicate and PFunction can be extended. Refer to the BuiltIn5.java, Capitalize.java, TestBuiltInPredicate.java, and TestFunction.java to see how this is done.

## Developer

Inferencilo was developed by Cleve (Klivo) Lendon.

## Contact

To contact the developer, send email to indriko@yahoo.com . Comments, suggestions and criticism are welcomed.

## History

First release, August 2021.

## Reference

The code structure of this inference engine is based on the Predicate Calculus Problem Solver presented in chapters 23 and 24 of 'AI Algorithms...' by Luger and Stubblefield. I highly recommend this book.

```
AI Algorithms, Data Structures, and Idioms in Prolog, Lisp, and Java
George F. Luger, William A. Stubblefield, ©2009 | Pearson Education, Inc. 
ISBN-13: 978-0-13-607047-4
ISBN-10: 0-13-607047-7
```

## License

The source code for Inferencilo is licensed under the MIT license, which you can find in [LICENSE](LICENSE).
