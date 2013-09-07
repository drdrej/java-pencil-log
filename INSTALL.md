# Install Pencil-Log

## Requirements

### Java/JVM

In the actual version pencil supports only the **Javac
1.5 compatible compiler**. I've tested this release
with javac 1.5.0 b64 and eclipse-jdt compiler 3.1 with
the javac 1.5 settings. Both to run pencil
and to execute enhanced code you need the
*JDK1.5*. 

I've decided to drop JDK1.4 support for
the next releases. Maybe, if i have enougth time, i
will add JDK1.4 support again.


### Libraries

All necessary jars are added to the full distribution. This
jars are described in the following list.

1. [ASM](http://asm.objectweb.org) - a Java bytecode manipulation framework. (Version 1.52)
2. [ANTLR](http://antlr.org) - a framework for constructing recognizers, compilers, and translators from grammatical descriptions. (Version 2.7.5)
3. [Commons IO](http://jakarta.apache.org/commons/io/) - collection of I/O utilities. (Version 1.0)
4. [Commons logging](http://jakarta.apache.org/commons/logging/) - a wrapper around a variety of logging API implementations. (Version 1.0)
5. [Commons cli](http://jakarta.apache.org/commons/cli/) - simple API for working with comamnd line arguments. (Version 1.0)
6. [Javolution](javolution.org) - class library. I use this library to improve string handling and to read xml config files. (Version 3.3.0)
7. [SimpleLog](https://simple-log.dev.java.net/) - I use this framework to log. (Version 1.7.0)

## How to install
To install pencil on your computer you should
**download the full distribution**. **Unzip** it
in a directory of your choice. Set up an **environment
variable** PENCIL_HOME. (for Windows %PENCIL_HOME%,
for Linux $PENCIL_HOME) That's all. You can see, pencil
is very easy to install and also very easy to use. To
understand how pencil can be used you should read the
documentation in the doc directory.



*code with fun!* ~~ Andreas Siebert aka drdrej/nestor_kodila

