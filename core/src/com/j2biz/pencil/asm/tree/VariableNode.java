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
package com.j2biz.pencil.asm.tree;

import org.objectweb.asm.Type;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.j2biz.pencil.asm.Acceptable;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.ex.ClassParseException;

/**
 * An instance of this class is an abstract representation of a variable. A
 * variable in java can be local variable, declared in a method/block, or a
 * field, declared in a class.
 * 
 * additional information, depends on the type of a variable is contained in the
 * extended classes com.j2biz.pencil.asm.tree.LocalVariableNode and
 * com.j2biz.pencil.asm.tree.FieldVariableNode.
 * 
 * @see com.j2biz.pencil.asm.tree.LoadLocalVariableNode
 * @see com.j2biz.pencil.asm.tree.FieldVariableDefinition
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public abstract class VariableNode implements Node, Acceptable {
       private final String id;

       private final String description;

       private final Type   type;

       protected VariableNode(final String id, final String description) {
              this.id = id;
              this.description = description;
              this.type = Type.getType(description);
       }

       public String getName() {
              return id;
       }

       public String getDescription() {
              return this.description;
       }

       public Type getType() {
           return this.type;
       }
	   
	   public AbstractClassInfoNode getOwner() {
		   throw new NotImplementedException();
	   }

	public String getVariableInstanceType( ) {
		final String internalName;
	
		final int typeSort = getType().getSort();
		if ( typeSort != Type.OBJECT )
			return null;
	
		if ( typeSort == Type.ARRAY ) 
			throw new ClassParseException("[PENCIL-ERROR] ARRAY is not supported in this version of pencil. variable == "
					+ getName());
		
		
		internalName = getType().getInternalName();
		
		return internalName;
	}

}