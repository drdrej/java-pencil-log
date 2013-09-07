package com.j2biz.pencil.antlr.decorator;

import antlr.collections.AST;

import com.j2biz.pencil.asm.AsmUtils;
import com.j2biz.pencil.asm.NullCodeBlock;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;

public class ClassFunctionPartASTDecorator extends LogMsgPartASTDecorator {

	private final Function	function;

	ClassFunctionPartASTDecorator( final AST wrappedNode ) {
		super(wrappedNode);

		final AST child = this.getWrappedVariableToken().getFirstChild();
		if ( child == null || "name".equals(child.getText()) ) { // case
																	// sensitiv
																	// ???
			function = new ClassNameFunction();
		} else if ( child != null && "lineNumber".equals(child.getText()) ) {
			this.function = new ClassLineNumberFunction();
		} else {
			throw new RuntimeException(">>> function not found: " + child);
		}
	}

	@Override
	public Node createCodeBlockBeforeDebugCall(
		final MethodInfoNode callerMethod,
		final LogMessageNode logEntry ) {
		return NullCodeBlock.INSTANCE;
	}

	@Override
	public Node createSetStringCode(
		final MethodInfoNode callerMethod,
		final LogMessageNode logEntry ) {
		return function.createOutputCode(callerMethod, logEntry);
	}

}

interface Function {

	Node createOutputCode( MethodInfoNode callerMethod, LogMessageNode logEntry );

}

class ClassNameFunction implements Function {

	public Node createOutputCode(
		MethodInfoNode callerMethod,
		LogMessageNode logEntry ) {
		final String className = callerMethod.getOwner().getJavaClassName();
		return AsmUtils.StringBuffer.appendString(className);
	}

}

class ClassLineNumberFunction implements Function {

	public Node createOutputCode(
		MethodInfoNode callerMethod,
		LogMessageNode logEntry ) {
		// TODO: kann hier optimiert werden.
		final String lineNumber = String.valueOf(logEntry.getLineNr());
		return AsmUtils.StringBuffer.appendString(lineNumber);
	}

}
