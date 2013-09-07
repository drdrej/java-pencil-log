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
package com.j2biz.info;


/**
 * @author andrej
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class SimpleErrorStatus implements ErrorStatus {

    private final String msgKey;
    private final int    type;
    private final String[] msgValues;
    
    public SimpleErrorStatus(final String msgKey, final int type, final String[] msgValues) {
        if( msgKey == null ){
            throw new NullPointerException( "parameter:msgKey" );
        }
        this.msgKey = msgKey;
        
        if( msgValues == null ){
            throw new NullPointerException( "parameter:msgValues" );
        }
        this.msgValues = msgValues;
        
        this.type   = type;
    }
    
    public SimpleErrorStatus(final String msgKey, final int type) {
        this(msgKey, type, new String[0]);
    }
    
    /* (non-Javadoc)
     * @see com.j2biz.log.compile.ex.ErrorStatus#getLocalizedMessage()
     */
    public String getLocalizedMessage() {
        return msgKey;
    }

    /* (non-Javadoc)
     * @see com.j2biz.log.compile.ex.ErrorStatus#getType()
     */
    public int getType() {
        return type;
    }

}
