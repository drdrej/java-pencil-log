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

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Label;

import com.j2biz.pencil.asm.Acceptable;

public class LabelWrapper implements Node, Acceptable {

	public static final LabelWrapper NULL_INSTANCE = create();
	
	private final Label label;

	public LabelWrapper(final Label wrappedLabel) {
		assert wrappedLabel != null;
		
		label = wrappedLabel;
	}
	
	public LabelWrapper() {
		this.label = new Label();
	}
	
	public void accept(CodeWriter codeWriter) {
		codeWriter.visitLabel(label);		
	}

	public Label getWrappedNode() {
		return label;
	}
	
	public static LabelWrapper create() {
		return new LabelWrapper(new Label());
	}
}