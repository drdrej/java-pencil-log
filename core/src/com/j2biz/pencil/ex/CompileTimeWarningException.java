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
package com.j2biz.pencil.ex;

import java.util.Map;
import java.util.Set;

import com.j2biz.utils.MapFactory;




/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class CompileTimeWarningException extends Exception {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String callerClassName;

	private String	callerMethodName;

	private String	callerSourceFile;

	private int	lineNumber;

	private Map<String, String>	addInfo;

	/**
     * 
     */
    public CompileTimeWarningException() {
        super();
    }
	
    /**
     * @param message
     */
    public CompileTimeWarningException(String message) {
        super(message);
    }
	
    /**
     * @param message
     * @param cause
     */
    public CompileTimeWarningException(String message, Throwable cause) {
        super(message, cause);
    }
	
    /**
     * @param cause
     */
    public CompileTimeWarningException(Throwable cause) {
        super(cause);
    }
    
   
	/* (non-Javadoc)
	 * @see java.lang.Throwable#fillInStackTrace()
	 */
	public synchronized Throwable fillInStackTrace() {
	    return this;
	}

	public String getAdditionalInformation( ) {
		// TODO Auto-generated method stub
		return "...";
	}
	
	public void setCallerClassName(final String callerClass) {
		assert callerClass != null;
		assert this.callerClassName == null;
		
		this.callerClassName = callerClass;
	}
	
	public void setCallerMethodName(final String callerMethod) {
		assert callerMethod != null;
		assert this.callerMethodName == null;
		
		this.callerMethodName = callerMethod;
		
	}

	public void setCallerSource( final String callerSource ) {
		assert callerSource != null;
		assert this.callerSourceFile == null;
		
		this.callerSourceFile = callerSource;
	}
	
	public void setLineNumber(final int lineNumber) {
		assert lineNumber >= 0;
		
		this.lineNumber = lineNumber;
	}
	
	
	public void addAdditionalInfo(final String name, final String value) {
		assert name != null;
		assert value != null;
		
		if( this.addInfo == null )
			this.addInfo = MapFactory.create();
		
		this.addInfo.put(name, value);
	}
	
	public String getAdditionalInfo(final String name){
		assert name != null;
		
		if ( this.addInfo == null )
			return "";
		
		final String rval = this.addInfo.get(name);

		return rval == null ? "" : rval;
	}

	public String getCallerClass( ) {
		return this.callerClassName == null ? "" : this.callerClassName;
	}
	
	public String getCallerMethod( ) {
		return this.callerMethodName == null ? "" : this.callerMethodName;
	}

	public String getCallerSource( ) {
		return this.callerSourceFile == null ? "" : this.callerSourceFile;
	}

	public int getLineNumber( ) {
		return this.lineNumber;
	}
	
	public Set<String> additionalInfoNames() {
		return this.addInfo.keySet();
	}
}
