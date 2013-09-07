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

import org.objectweb.asm.Label;

import com.j2biz.pencil.asm.tree.LabelWrapper;

public class ClassParseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private int	lineNumber = -1;
	
	private Label lastLabel;

	private String	className;

	private String	methodName;

	private String	affectedSource;

	public ClassParseException(String arg0, Throwable arg1) {
		super(arg0, arg1);
		
		this.lastLabel = null;
	}

	public ClassParseException(final String arg0) {
		super(arg0);
		
		this.lastLabel = null;
	}

	public ClassParseException(Throwable arg0) {
		super(arg0);
		
		this.lastLabel = null;
	}

	public ClassParseException(final String msg, final int lineNumber) {
		super(msg);
		
		assert lineNumber >= 0;
		
		this.lineNumber = lineNumber;
		this.lastLabel = null;
	}

	public ClassParseException(final String msg, final LabelWrapper previousLabel) {
		super(msg);
		
		assert previousLabel != null;
		this.lastLabel = previousLabel.getWrappedNode();
	}
	
	public int getLineNumber() {
		return this.lineNumber;
	}
	
	/**
	 * label can be used to determine the line-number of this exception.
	 * this information is very usefull to analyze bugs or unacceptable situations.
	 * 
	 * @return previous label in the bytecode. never NULL.
	 */
	public Label getAssotiatedLabel() {
		return this.lastLabel;
	}
	
	
	public void setLineNumber(final int lineNumber) {
		assert lineNumber >= 0;
		
		this.lineNumber = lineNumber;
	}

	public void setLabel( final LabelWrapper wrapper ) {
		assert wrapper != null;
		
		this.lastLabel = wrapper.getWrappedNode();
	}

	public Label getLabel( ) {
		return this.lastLabel;
	}

	public String getAffectedClass( ) {
		return this.className == null ? "" : this.className;
	}
	
	public void setAffectedClass(final String className) {
		assert className != null;
		
		this.className = className;
	}
	
	public void setAffectedMethod(final String methodName) {
		assert methodName != null;
		
		this.methodName = methodName;
	}
	
	public String getAffectedMethod() {
		return this.methodName == null ? "" : this.methodName;
	}

	public String getAffectedSource( ) {
		return this.affectedSource == null ? "" : this.affectedSource;
	}
	
	public void setAffectedSource(final String source) {
		assert source != null;
		
		this.affectedSource = source;
	}
}
