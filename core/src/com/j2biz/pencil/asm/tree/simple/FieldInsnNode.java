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
package com.j2biz.pencil.asm.tree.simple;

import org.objectweb.asm.CodeWriter;

import com.j2biz.pencil.asm.Acceptable;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class FieldInsnNode extends AbstractOpcodeNode implements Acceptable {
    
    /**
     * The internal name of the class which one is the owner of this field. (see
     * {@link org.objectweb.asm.Type#getInternalName getInternalName}).
     *  
     */
    private final String owner;

    /**
     * The name of the field.
     */
    private final String name;

    /**
     * The field's descriptor (see {@link org.objectweb.asm.Type Type}).
     */
    private final String descrition;
    
    /**
     * 
     * @param opcode
     *            can be one of this: GETSTATIC, PUTSTATIC, GETFIELD or
     *            PUTFIELD.
     */
    public FieldInsnNode(final int opcode, final String name,
            final String owner, final String description) {
        super(opcode);
		
		if( name == null ) {
			throw new NullPointerException( "parameter:name" );
		}
        this.name = name;
		
		if( owner == null ) {
			throw new NullPointerException( "parameter:owner, with name = " + name );
		}
        this.owner = owner;
		
		if( description == null ) {
			throw new NullPointerException( "parameter:description" );
		}
        this.descrition = description;
    }

    /**
     * @return
     */
    public String getOwner() {
        return this.owner;
    }

    /**
     * @return
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return
     */
    public String getDescription() {
        return this.descrition;
    }

	public void accept(CodeWriter codeWriter) {
		 codeWriter.visitFieldInsn(
					this.getOpcode(), 
					this.getOwner(), this.getName(), this.getDescription());
	}


}