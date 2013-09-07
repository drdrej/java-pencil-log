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

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.Acceptable;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableDefinitionReferer;

/**
 * Ersetze an den Stellen,wo der zweite Part eines Aufrufs statt findet,durch dieses Objekt
 * Nach einerkompletten Trennung von FirstPartFieldRefInstruction ergibt das mehr Sinn
 * und kann einfacher getrenntwerden.
 * 
 * @author andrej
 *
 */
public class NextPartFieldRefInstruction implements Node, Acceptable, VariableDefinitionReferer {

	public NextPartFieldRefInstruction(final FieldVariableDefinition field) {
		Assert.isNotNull(field);
		this.field = field;
	}
	
	private final FieldVariableDefinition field;
	
	public FieldVariableDefinition getDefinition() {
		return this.field;
	}
	
	public void accept(CodeWriter codeWriter) {
		final FieldVariableDefinition field = getDefinition();
		if ((field.getAccessFlags() & Constants.ACC_STATIC) == Constants.ACC_STATIC) {
			codeWriter.visitFieldInsn(Constants.GETSTATIC, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		} else {
			codeWriter.visitFieldInsn(Constants.GETFIELD, field.getOwner()
					.getClassName(), field.getName(), field
					.getDescription());
		}
	}


	
}
