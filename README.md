PSLQ Algorithm
=========

## It is an implementation of [PSLQ Algorithm](http://mathworld.wolfram.com/PSLQAlgorithm.html).
This project using [Apache Spark](https://github.com/apache/spark) and [Breeze](https://github.com/scalanlp/breeze) as a calculating engine.

## How to run
1. Start sbt. 
2. Type run. REPL prompt will be appeared. 
3. Type your set of real numbers (for example: -1 pi . In this case the algorithm returns the good approximation of pi ). 
4. PSLQ Algorithm will be applied and [integer relations](http://mathworld.wolfram.com/IntegerRelation.html) would be printed.
5. Type "q:" without quotas for exit.

## TODO:
1. ~~Improve parsing. (Should parse "pi", "e", mathematical expression)~~
2. ~~Allow user to set accuracy. (It's 1e-6 by default now)~~
3. Improve output.
4. Use Spark for LinAlg.