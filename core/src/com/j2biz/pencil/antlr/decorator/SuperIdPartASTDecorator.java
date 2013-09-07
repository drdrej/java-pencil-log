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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import antlr.collections.AST;

import com.j2biz.pencil.antlr.visitor.IdPartVisitor;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class SuperIdPartASTDecorator extends IdPartASTDecorator {

	SuperIdPartASTDecorator(AST wrappedNode) {
		super(wrappedNode);
	}

	@Override
	public Node createCodeBlockToStoreInATempStringVariable(LogMessageNode logEntry, MethodInfoNode callerMethod, int localVarIndex) throws CompileTimeWarningException {
		throw new NotImplementedException();
	}

	@Override
	public final void accept( final IdPartVisitor visitor ) throws CompileTimeWarningException {
		assert visitor != null;
		
		visitor.visit(this);
	}
}
