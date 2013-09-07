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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.CodeWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.Acceptable;
import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.Transformer;
import com.j2biz.utils.ListFactory;

/**
 * A placeholder for more than one instruction. this construct is usefull to
 * replace entries in a bytecode with more complex instruction entries.
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class InstructionGroup implements Node, Iterable<Node>  {

	private List<Node> instr = ListFactory.create(3);

	public List<Node> instructions() {
		return Collections.unmodifiableList(instr);
	}
	
	public void clear() {
		this.instr.clear();
	}

	public void addInstruction(Node instruction) {
		Assert.isNotNull(instruction);
		Assert.isTheSame(instruction, this);
		
		this.instr.add(instruction);
	}

	public void accept(final CodeWriter codeWriter, Transformer transformer) {
		final List list = instructions();
		
		for (int i = 0; i < list.size(); i++) {
			Object instr = list.get(i);
			if (instr == null) {
				continue;
			}

//			((Acceptable) instr).accept(codeWriter);
			
			if (instr instanceof Node) {
				transformer.visitInstruction((Node) instr, codeWriter);
			} else if (instr instanceof Label) {
				final LabelWrapper wrapper = new LabelWrapper((Label) instr);
				wrapper.accept(codeWriter);
			} else if (instr instanceof String || instr instanceof Number
					|| instr instanceof Type) {
//				LogTransformer.visitLdc(instr, codeWriter);
		   Assert.shouldNeverReachHere("remove it");
			} else {
				throw new UnsupportedOperationException("value : " + instr);
			}
		}
	}

	public void accept(CodeWriter codeWriter) {
		final List list = instructions();
		
		for (int i = 0; i < list.size(); i++) {
			Object instr = list.get(i);
			if (instr == null) {
				continue;
			}

			((Acceptable) instr).accept(codeWriter);
		}
	}

	public Iterator<Node> iterator( ) {
		return instructions().iterator();
	}

}
