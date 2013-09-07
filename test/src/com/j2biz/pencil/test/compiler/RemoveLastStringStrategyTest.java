package com.j2biz.pencil.test.compiler;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.method.LogMsgBytecodePatternHandler;
import com.j2biz.pencil.asm.tree.LabelWrapper;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.ClassParseException;

public class RemoveLastStringStrategyTest extends TestCase {

	private LogMsgBytecodePatternHandler	strategy;
	private List<Node> instructions;
	
	@Override
	protected void setUp( ) throws Exception {
		this.instructions = new ArrayList<Node>();
		strategy = new LogMsgBytecodePatternHandler("enclosingMethod", instructions);
	}

	public void testMultiLiner( ) {
		final LdcWrapper codeBlockBefore = new LdcWrapper("code block before");
		instructions.add(codeBlockBefore);

		final LabelWrapper beforeTemplate = LabelWrapper.create();
		instructions.add(beforeTemplate);

		final LdcWrapper template = new LdcWrapper("template");
		instructions.add(template);
		strategy.markPossibleTemplateString();

		final LabelWrapper afterTemplate = LabelWrapper.create();
		instructions.add(afterTemplate);

		Object label = strategy.removeLastByteCodesToLastLabel();

		assertEquals(instructions.size(), 2);
		assertEquals(beforeTemplate, label);
	}

	public void testOneLiner( ) {
		final LdcWrapper codeBlockBefore = new LdcWrapper("code block before");
		instructions.add(codeBlockBefore);

		final LabelWrapper beforeTemplate = LabelWrapper.create();
		instructions.add(beforeTemplate);

		final LdcWrapper template = new LdcWrapper("template");
		instructions.add(template);
		strategy.markPossibleTemplateString();

		Object label = strategy.removeLastByteCodesToLastLabel();

		assertEquals(instructions.size(), 2);
		assertEquals(beforeTemplate, label);
	}

	public void testWrongNodesOnTheStack( ) {
		try {
			final LdcWrapper codeBlockBefore = new LdcWrapper(
					"code block before");
			instructions.add(codeBlockBefore);

			final LabelWrapper beforeTemplate = LabelWrapper.create();
			instructions.add(beforeTemplate);

			final LdcWrapper template = new LdcWrapper("template");
			instructions.add(template);
			strategy.markPossibleTemplateString();

			final LdcWrapper template2 = new LdcWrapper("template2");
			instructions.add(template2);

			Object label = strategy.removeLastByteCodesToLastLabel();
			
			fail("should not allow java-syntax in the call. only template-syntax." );
		} catch ( final ClassParseException x ) {
			assertTrue(true);
		}
	}

}
