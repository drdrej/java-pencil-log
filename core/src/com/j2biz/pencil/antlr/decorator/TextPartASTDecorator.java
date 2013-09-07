/* 
 * "Pencil - Log message compiler" is (c) 2004 Andreas Siebert (j2biz community)
 *
 * Author: Andreas Siebert.
 *  
 * This file is part of "Pencil - Log message compiler".
 *
 * "Pencil - Log message compiler" is free software; 
 * you can redistribute it and/or modify it under the terms of the GNU 
 * General Public License as published by the Free Software Foundation; 
 * either version 2 of the License, or (at your option) any later version.
 *
 * "Pencil - Log message compiler" is distributed in the hope 
 * that it will be useful, but WITHOUT ANY WARRANTY; without even 
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A 
 * PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with "Pencil - Logger message compiler"; if not, 
 * write to the Free Software Foundation, Inc., 59 Temple Place, 
 * Suite 330, Boston, MA  02111-1307  USA
 * 
 */
package com.j2biz.pencil.antlr.decorator;

import antlr.collections.AST;

import com.j2biz.pencil.asm.NullCodeBlock;
import com.j2biz.pencil.asm.AsmUtils.StringBuffer;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;

public class TextPartASTDecorator extends LogMsgPartASTDecorator {

	TextPartASTDecorator(AST wrappedNode) {
		super(wrappedNode);
	}

	public Node createCode() {
		final Node appendSnippet = StringBuffer.appendString(getWrappedVariableToken()
				.getText());
	
		return appendSnippet;
	}

	public Node createSetStringCode(final MethodInfoNode callerMethod, final LogMessageNode logEntry ) {
		return createCode();
	}

	@Override
	public Node createCodeBlockBeforeDebugCall(MethodInfoNode callerMethod, LogMessageNode logEntry) {
		return NullCodeBlock.INSTANCE;
	}

}
