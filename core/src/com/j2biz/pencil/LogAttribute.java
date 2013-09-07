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
package com.j2biz.pencil;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.ByteVector;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;

/**
 * @author andrej
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LogAttribute extends Attribute {

    private boolean $compiled = false;
    
    /**
     * @param type
     */
    public LogAttribute(int compiled) {
        super( "j2biz:loco" );
        $compiled = compiled > 0;
        this.next = null;
    }
    
    public LogAttribute(final int compiled, final Attribute next) {
        super( "j2biz:loco" );
        $compiled = compiled > 0;
        this.next = next;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.objectweb.asm.Attribute#read(org.objectweb.asm.ClassReader, int,
     *      int, char[], int, org.objectweb.asm.Label[])
     */
    protected Attribute read(ClassReader cr, int off, int len, char[] buf,
            int codeOff, Label[] labels) {
        return new LogAttribute( cr.readByte(off) );
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.objectweb.asm.Attribute#write(org.objectweb.asm.ClassWriter,
     *      byte[], int, int, int)
     */
    protected ByteVector write(ClassWriter cw, byte[] code, int len,
            int maxStack, int maxLocals) {
        ByteVector bv = new ByteVector().putByte( $compiled ? 1 : 0 );
        return bv;
    }
    
    private String getLogFlag() {
        return ($compiled) ? "T" : "F";
    }
    
    public boolean isCompiled() {
        return $compiled;
    }
}

