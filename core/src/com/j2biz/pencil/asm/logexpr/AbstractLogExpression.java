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

import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.CompileTimeWarningException;

public abstract class AbstractLogExpression {

	protected final LogMessageNode logEntry;
	protected final MethodInfoNode callerMethod;
	
	public AbstractLogExpression( final LogMessageNode logEntry, final MethodInfoNode callerMethod) {
		Assert.isNotNull(logEntry);
		Assert.isNotNull(callerMethod);
		
		this.logEntry = logEntry;
		this.callerMethod = callerMethod;
	}
	
	public abstract Node createReplace(LogTransformer transformer);
	
	public abstract boolean hasWarnings();
	
	public abstract List<CompileTimeWarningException> getWarnings();
	
}
