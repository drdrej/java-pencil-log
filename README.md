# Pencil Log Compiler for Java
![pencil-logo](https://github.com/drdrej/java-pencil-log/raw/master/pencil.www/images/logo_splash_little.gif?raw=true)

## Philosphy
1. less logging code
2. logging shouldn't disturb business-logic
3. code-generation
4. simple logging-statements

## Current State
		experimental
		Version: 0.2.3 / 2005
		should be compatible with classes for jvm <= 1.5
		(c)2005 by Andreas Siebert
		**Download**: --


## History
I've staretd this project in 2005. After SUN has shut down his hosts (java.net) for open-source projects i've decided not to
develop this stuff. In 2013 I've made a new decision to publish this project as open-source under LGPL again. I like the idea, so
maybe I will work on it in the near future. 


## Logging with Pencil and commons-logging. Fast and safe way to log.


### What is "Pencil" and how i can eat it?

"Pencil" is a log-message-compiler for java (keyword: generative programming). It takes bytecode produced by javac (or another java-compiler), transforms it and enriches it with real logging expressions. 

The main goal of the "Pencil" project is to provide a mechanism to make logging easy, safe and fast. To achieve this result, "Pencil" uses its own expression language developed with the ANTLR parser-generator. But have no fear! "Pencil" is not a Language-extension. You don't need a special IDE with a new source-code viewer. There's no need to extend the syntax of java to use pencil and your code will look cleaner than it did before. 
To log some stuff you simply use a class from "Pencil"-framework within your java-code. Pencil behaves kind of like a logging plugin to your code. 

**Example:**
```java
class MyClass {
	private String var1;
	private MyOtherClass var2;	

	public MyClass() {
			LOGGER.info("begin with initialization" );
			doSomething();
			LOGGER.debug( "some additional vars initialized; var1 =${var1}/
		        var2.name = ${var2.name}");
		}
	}
```

To compile your program without logging just compile your java-code with javac. If you want to distribute your app with logging, simply use pencil to enrich the bytecode with logging-code of your favorite logging framework. (at this moment only commons-logging is supported)

The above example is then transformed to the following code:

```java
class MyClass {

	private static final Log LOGGER = LogFactory.getLog( MyClass.class );

	private String var1;
	private MyOtherClass var2;	

	public MyClass() {
	      if( LOGGER.isInfoEnabled() ) 
		      LOGGER.info("begin with initialization" );
		
	          doSomething();
	                         
	      if( LOGGER.isDebugEnabled() ) {
	          String tempVar;
	          if(var2 != null ) 
	             tempVar = String.valueOf(tempVar);
	          else  // to give the user information, which part of reference is ill.
	          	 tempVar = "NULL.name"; 
	          	 
		      LOGGER.debug( "some additional vars initialized; var1 =" 
		           + var1 + "/ var2.name = " + tempVar);
		  }
	}
}
```

You can see, that the code with pencil-logging-statements is more readable than in the old-school way of logging. 
Pencil also provides a NullPointerException-precaution mechanism. It also does not allow to call business-logic-methods 
from within your logging-expressions. 
The result is, your code is safer than before. No more NullPointerExceptions and no more unintended business logic in 
the logging expressions. Pencil simply forbids a faulty logging-functionality in your code.

## License
**LGPL v.3 (updated 2013)**

                   GNU LESSER GENERAL PUBLIC LICENSE
                       Version 3, 29 June 2007

 Copyright (C) 2007 Free Software Foundation, Inc. <http://fsf.org/>
 Everyone is permitted to copy and distribute verbatim copies
 of this license document, but changing it is not allowed.


  This version of the GNU Lesser General Public License incorporates
the terms and conditions of version 3 of the GNU General Public
License, supplemented by the additional permissions listed below.

  0. Additional Definitions.

  As used herein, "this License" refers to version 3 of the GNU Lesser
General Public License, and the "GNU GPL" refers to version 3 of the GNU
General Public License.

  "The Library" refers to a covered work governed by this License,
other than an Application or a Combined Work as defined below.

  An "Application" is any work that makes use of an interface provided
by the Library, but which is not otherwise based on the Library.
Defining a subclass of a class defined by the Library is deemed a mode
of using an interface provided by the Library.

  A "Combined Work" is a work produced by combining or linking an
Application with the Library.  The particular version of the Library
with which the Combined Work was made is also called the "Linked
Version".

  The "Minimal Corresponding Source" for a Combined Work means the
Corresponding Source for the Combined Work, excluding any source code
for portions of the Combined Work that, considered in isolation, are
based on the Application, and not on the Linked Version.

  The "Corresponding Application Code" for a Combined Work means the
object code and/or source code for the Application, including any data
and utility programs needed for reproducing the Combined Work from the
Application, but excluding the System Libraries of the Combined Work.

  1. Exception to Section 3 of the GNU GPL.

  You may convey a covered work under sections 3 and 4 of this License
without being bound by section 3 of the GNU GPL.

  2. Conveying Modified Versions.

  If you modify a copy of the Library, and, in your modifications, a
facility refers to a function or data to be supplied by an Application
that uses the facility (other than as an argument passed when the
facility is invoked), then you may convey a copy of the modified
version:

   a) under this License, provided that you make a good faith effort to
   ensure that, in the event an Application does not supply the
   function or data, the facility still operates, and performs
   whatever part of its purpose remains meaningful, or

   b) under the GNU GPL, with none of the additional permissions of
   this License applicable to that copy.

  3. Object Code Incorporating Material from Library Header Files.

  The object code form of an Application may incorporate material from
a header file that is part of the Library.  You may convey such object
code under terms of your choice, provided that, if the incorporated
material is not limited to numerical parameters, data structure
layouts and accessors, or small macros, inline functions and templates
(ten or fewer lines in length), you do both of the following:

   a) Give prominent notice with each copy of the object code that the
   Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the object code with a copy of the GNU GPL and this license
   document.

  4. Combined Works.

  You may convey a Combined Work under terms of your choice that,
taken together, effectively do not restrict modification of the
portions of the Library contained in the Combined Work and reverse
engineering for debugging such modifications, if you also do each of
the following:

   a) Give prominent notice with each copy of the Combined Work that
   the Library is used in it and that the Library and its use are
   covered by this License.

   b) Accompany the Combined Work with a copy of the GNU GPL and this license
   document.

   c) For a Combined Work that displays copyright notices during
   execution, include the copyright notice for the Library among
   these notices, as well as a reference directing the user to the
   copies of the GNU GPL and this license document.

   d) Do one of the following:

       0) Convey the Minimal Corresponding Source under the terms of this
       License, and the Corresponding Application Code in a form
       suitable for, and under terms that permit, the user to
       recombine or relink the Application with a modified version of
       the Linked Version to produce a modified Combined Work, in the
       manner specified by section 6 of the GNU GPL for conveying
       Corresponding Source.

       1) Use a suitable shared library mechanism for linking with the
       Library.  A suitable mechanism is one that (a) uses at run time
       a copy of the Library already present on the user's computer
       system, and (b) will operate properly with a modified version
       of the Library that is interface-compatible with the Linked
       Version.

   e) Provide Installation Information, but only if you would otherwise
   be required to provide such information under section 6 of the
   GNU GPL, and only to the extent that such information is
   necessary to install and execute a modified version of the
   Combined Work produced by recombining or relinking the
   Application with a modified version of the Linked Version. (If
   you use option 4d0, the Installation Information must accompany
   the Minimal Corresponding Source and Corresponding Application
   Code. If you use option 4d1, you must provide the Installation
   Information in the manner specified by section 6 of the GNU GPL
   for conveying Corresponding Source.)

  5. Combined Libraries.

  You may place library facilities that are a work based on the
Library side by side in a single library together with other library
facilities that are not Applications and are not covered by this
License, and convey such a combined library under terms of your
choice, if you do both of the following:

   a) Accompany the combined library with a copy of the same work based
   on the Library, uncombined with any other library facilities,
   conveyed under the terms of this License.

   b) Give prominent notice with the combined library that part of it
   is a work based on the Library, and explaining where to find the
   accompanying uncombined form of the same work.

  6. Revised Versions of the GNU Lesser General Public License.

  The Free Software Foundation may publish revised and/or new versions
of the GNU Lesser General Public License from time to time. Such new
versions will be similar in spirit to the present version, but may
differ in detail to address new problems or concerns.

  Each version is given a distinguishing version number. If the
Library as you received it specifies that a certain numbered version
of the GNU Lesser General Public License "or any later version"
applies to it, you have the option of following the terms and
conditions either of that published version or of any later version
published by the Free Software Foundation. If the Library as you
received it does not specify a version number of the GNU Lesser
General Public License, you may choose any version of the GNU Lesser
General Public License ever published by the Free Software Foundation.

  If the Library as you received it specifies that a proxy can decide
whether future versions of the GNU Lesser General Public License shall
apply, that proxy's public statement of acceptance of any version is
permanent authorization for you to choose that version for the
Library.


## References





*Feel free to use, to fork and to code!*
      Andreas Siebert 
	  aka drdrej/nestor_kodila