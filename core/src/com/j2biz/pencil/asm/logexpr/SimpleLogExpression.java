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
package com.j2biz.pencil.asm.logexpr;

import java.util.List;

import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public class SimpleLogExpression extends AbstractLogExpression {

	public SimpleLogExpression(LogMessageNode logEntry, MethodInfoNode callerMethod) {
		super(logEntry, callerMethod);
	}

	@Override
	public Node createReplace(LogTransformer transformer) {
		return callerMethod.getOwner().addSimpleTextLogMsg(transformer, callerMethod, logEntry);
	}

	public boolean hasWarnings() {
		return false;
	}
	
	public List<CompileTimeWarningException> getWarnings() {
		throw new IllegalStateException("has no warnings");
	}
}
