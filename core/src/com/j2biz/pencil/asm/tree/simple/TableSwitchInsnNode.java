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

import java.util.Collections;
import java.util.Iterator;

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Label;

import com.j2biz.pencil.asm.Acceptable;
import com.j2biz.pencil.asm.tree.Node;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class TableSwitchInsnNode implements Node, Acceptable {

    private int min;
    private int max;
    private Label defaultLabel;
    private Label[] labels;
    
    /**
     * 
     */
    public TableSwitchInsnNode(final int min, final int max, final Label defaultLabel, Label[] labels) {
        this.min = min;
        this.max = max;
        this.defaultLabel = defaultLabel;
        this.labels = labels;
    }

    /* (non-Javadoc)
     * @see com.j2biz.pencil.asm.tree.Node#getChildren()
     */
    public Iterator getChildren() {
        return Collections.EMPTY_LIST.iterator();
    }

    /**
     * @return
     */
    public Label[] getLabels() {
        return this.labels;
    }

    /**
     * @return
     */
    public Label getDefault() {
        return this.defaultLabel;
    }

    /**
     * @return
     */
    public int getMax() {
        return this.max;
    }

    /**
     * @return
     */
    public int getMin() {
        return this.min;
    }

	public void accept(CodeWriter codeWriter) {
		codeWriter.visitTableSwitchInsn( this.getMin(), this.getMax(), this.getDefault(), this.getLabels() );		
	}
    
}
