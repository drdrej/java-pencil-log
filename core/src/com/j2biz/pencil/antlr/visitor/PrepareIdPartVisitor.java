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
package com.j2biz.pencil.antlr.visitor;

import java.util.List;

import com.j2biz.pencil.antlr.CallerContext;
import com.j2biz.pencil.antlr.decorator.FirstIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.NextIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.ThisIdPartASTDecorator;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;
import com.j2biz.pencil.ex.CompileTimeWarningException;
import com.j2biz.utils.ListFactory;

public class PrepareIdPartVisitor extends IdPartVisitor {

	private List<VariableDefinitionReferer>	callVariableStack	= ListFactory
																		.create();

	private final CallerContext				callerContext;

	private VariableDefinitionReferer	lastVariableOnStack	= null;

	public PrepareIdPartVisitor( CallerContext callerCtx ) {
		assert callerCtx != null;

		this.callerContext = callerCtx;
	}

	@Override
	public void visit( final FirstIdPartASTDecorator part )
			throws CompileTimeWarningException {
		assert part != null;
		assert this.callVariableStack != null;
		assert this.callerContext != null;

		part.initVariableReferer(this.callerContext);
		this.lastVariableOnStack = part.getAssignedVariableReferer();
		callVariableStack.add( this.lastVariableOnStack );
		next(part);
	}

	@Override
	public void visit( final ThisIdPartASTDecorator part )
			throws CompileTimeWarningException {
		assert part != null;
		assert this.callVariableStack != null;
		assert this.callerContext != null;

		part.initVariableReferer(this.callerContext);
		this.lastVariableOnStack = part.getAssignedVariableReferer();
		callVariableStack.add( this.lastVariableOnStack );
		next(part);
	}

	@Override
	public void visit( final NextIdPartASTDecorator part )
			throws CompileTimeWarningException {
		
		assert part != null;
		assert this.callVariableStack != null;
		assert this.callerContext != null;
		assert this.lastVariableOnStack != null;

		part.initVariableReferer(this.callerContext, this.lastVariableOnStack
				.getDefinition());
		
		this.lastVariableOnStack = part.getAssignedVariableReferer();
		callVariableStack.add( this.lastVariableOnStack );
		next(part);
	}

	public List<VariableDefinitionReferer> getVariableRefererStack( ) {
		assert callVariableStack != null;

		return this.callVariableStack;
	}

}
