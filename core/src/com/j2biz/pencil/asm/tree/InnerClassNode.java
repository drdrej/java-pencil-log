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

/**
 * @author Andreas Siebert 
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class InnerClassNode {
     
    private final String name;
    private final String outerName;
    private final String innerName;
    private final int access;
    
    public int getAccess() {
        return access;
    }
    
    public String getInnerName() {
        return innerName;
    }
    
    public String getName() {
        return name;
    }
    
    public String getOuterName() {
        return outerName;
    }
    
    public InnerClassNode(final String name, final String outerName, final String innerName, final int access) {
        if(name == null) {
            throw new NullPointerException( "parameter:name" );
        }
        this.name = name;
        this.outerName = outerName;
        
//        a class can exist without an innerName? maybe it is better to log ???
//        if( innerName == null ) {
//            throw new NullPointerException();
//        }
        this.innerName = innerName;
        
        this.access = access;
    }
    
    
}
