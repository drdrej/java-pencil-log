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

import org.objectweb.asm.Label;

import com.j2biz.pencil.asm.tree.Node;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class TryCatchBlockNode implements Node {

    private Label start;
    private Label end;
    private Label handler;
    private String type;
    
    public Label getEnd() {
        return end;
    }
    public Label getHandler() {
        return handler;
    }
    public Label getStart() {
        return start;
    }
    public String getType() {
        return type;
    }
    public TryCatchBlockNode(final Label start, final Label end, final Label handler, final String type) {
        this.type = type;
        this.start = start;
        this.end = end;
        this.handler = handler;
    }
    /* (non-Javadoc)
     * @see com.j2biz.pencil.asm.tree.Node#getChildren()
     */
    public Iterator getChildren() {
        return Collections.EMPTY_LIST.iterator();
    }

}
