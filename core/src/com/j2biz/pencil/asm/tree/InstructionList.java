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

import java.awt.Label;
import java.util.Iterator;

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public interface InstructionList {

    
    	public void set(int index, Node instruction);
    	
    	public void set(int index, Object value);
    	
    	public void set(int index, Label label);
    	
    	public void add(int index, Node instruction);
    	
    	public void add(int index, Object value);
    	
    	public void add(int index, Label label);
    	
    	public void add(Node instruction);
    	
    	public void add(Object value);
    	
    	public void add(Label label);
    	
    	public void remove(int index);
    	
    	public Iterator iterator();
    	
}
