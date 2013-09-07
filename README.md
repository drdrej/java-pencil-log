## Philosphy
-- less logging code
-- logging shouldn't disturb business-logic
-- code-generation
-- simple logging-statements

## Current State
-- experimental
-- Version: 0.2.3 / 2005


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

´´´java
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
´´´

You can see, that the code with pencil-logging-statements is more readable than in the old-school way of logging. 
Pencil also provides a NullPointerException-precaution mechanism. It also does not allow to call business-logic-methods 
from within your logging-expressions. 
The result is, your code is safer than before. No more NullPointerExceptions and no more unintended business logic in 
the logging expressions. Pencil simply forbids a faulty logging-functionality in your code.


Feel free to use, to fork and to code!
      Andreas Siebert 
	  aka drdrej/nestor_kodila