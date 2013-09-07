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

import org.objectweb.asm.Attribute;
import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Constants;

import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;

/**
 * An instance of this class represents a field of a class.
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 * 
 * @see com.j2biz.pencil.asm.tree.VariableNode
 * 
 */
public class FieldVariableDefinition extends VariableNode {

	public final boolean isPublic;

	private int accessFlags;

	private final AbstractClassInfoNode owner;

	public int getAccessFlags() {
		return accessFlags;
	}

	public Attribute getAttributes() {
		return attributes;
	}

	public Object getValue() {
		return value;
	}

	private Attribute attributes;

	private final Object value;

	public final boolean isStatic;

	public final boolean isPrivate;

	public FieldVariableDefinition(final int accessFlags, final String id,
			final String description, final Object value,
			final Attribute attrs, final AbstractClassInfoNode owner) {
		super(id, description);

		this.accessFlags = accessFlags;
		this.attributes = attrs;
		this.value = value;
		if (owner == null) {
			throw new NullPointerException("parameter:owner");
		}
		this.owner = owner;

		this.isStatic = (this.accessFlags & Constants.ACC_STATIC) == Constants.ACC_STATIC;
		this.isPrivate = (this.accessFlags & Constants.ACC_PRIVATE) == Constants.ACC_PRIVATE;
		this.isPublic = (this.accessFlags & Constants.ACC_PUBLIC) == Constants.ACC_PUBLIC;
	}

	public AbstractClassInfoNode getOwner() {
		return owner;
	}

	public void accept(CodeWriter codeWriter) {
		throw new UnsupportedOperationException(
				"not acceptable! to call a field from the bytecode another class should be used: "
						+ FirstPartFieldRefInstruction.class.getName());
	}

}