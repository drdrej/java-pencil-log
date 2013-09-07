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

import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.LoadLocalVariableNode;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public interface ScopeCaller {

    /**
     * The caller is the implementation of the scope from the other point of view.
     * every scope can call another scope. the visibility is strong defined by the language.
     * for example a static method in java can never call an instance field. but the jvm allow it?
     * to proove the visibility, every caller should implement this method.
     * 
     * 
     * @param node of the field, which should be prooved ?
     * 
     * @return true, if the caller can see this field.
     */
    public boolean canSee(final FieldVariableDefinition node);
    
    public boolean canSee(final LoadLocalVariableNode node);
    
}
