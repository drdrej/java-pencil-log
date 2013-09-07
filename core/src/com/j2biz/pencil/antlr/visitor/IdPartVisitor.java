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

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.j2biz.pencil.antlr.decorator.FirstIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.IdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.NextIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.SuperIdPartASTDecorator;
import com.j2biz.pencil.antlr.decorator.ThisIdPartASTDecorator;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public abstract  class IdPartVisitor {

	public abstract void visit(final FirstIdPartASTDecorator part) throws CompileTimeWarningException;
	
	public abstract void visit(final ThisIdPartASTDecorator part) throws CompileTimeWarningException;
	
	public abstract void visit(final NextIdPartASTDecorator part) throws CompileTimeWarningException;

	public void visit( SuperIdPartASTDecorator node ) {
		 throw new NotImplementedException();
	}

	protected void next( final IdPartASTDecorator part ) throws CompileTimeWarningException {
		assert part != null;
	
		if ( part.hasNextVariablePart() ) {
			final IdPartASTDecorator nextPart = part.getNextVariablePart();
			nextPart.accept(this);
		}
	}
	
}
