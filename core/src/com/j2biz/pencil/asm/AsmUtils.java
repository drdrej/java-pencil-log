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
package com.j2biz.pencil.asm;

import org.objectweb.asm.Constants;
import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.tree.InstructionGroup;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.asm.tree.VariableNode;
import com.j2biz.pencil.asm.tree.simple.InsnNode;
import com.j2biz.pencil.asm.tree.simple.IntInsnNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.TypeInsnNode;

/**
 * This class contains some methods to work with bytecode.
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class AsmUtils {

	private static final String STRING_DESC = Type.getDescriptor(String.class);

	public static final class StringBuffer implements Constants {

		/**
		 * @param text
		 * @return
		 */
		public static Node appendString(String text) {
			final String type = Type.getDescriptor(String.class);

			InstructionGroup rval = new InstructionGroup();
			rval.addInstruction(new LdcWrapper(String.valueOf(text)));
			rval.addInstruction(
					new MethodInsnNode(Constants.INVOKEVIRTUAL,
							"java/lang/StringBuffer", "append", "(" + type
									+ ")Ljava/lang/StringBuffer;"));

			return rval;
		}

		/**
		 * @param lastVar
		 * @return
		 */
		public static String getTypeForAppend(VariableNode lastVar) {
			assert (lastVar != null) : "parameter:lastVar must be not NULL.";
			
			final String description = lastVar.getDescription();
			return getTypeForAppend(description);
		}

		public static String getTypeForAppend(String description) {
			if (description == null) {
				throw new IllegalStateException(
						"the description of this method is not setted. so it can't be converted to the description which is accepted by the append() method.");
			}

			if (STRING_DESC.equals(description)
					|| Type.BOOLEAN_TYPE.getDescriptor().equals(description)
					|| Type.CHAR_TYPE.getDescriptor().equals(description)
					|| Type.DOUBLE_TYPE.getDescriptor().equals(description)
					|| Type.FLOAT_TYPE.getDescriptor().equals(description)
					|| Type.INT_TYPE.getDescriptor().equals(description)
					|| Type.LONG_TYPE.getDescriptor().equals(description)) {
				return description;
			} else if (Type.BYTE_TYPE.getDescriptor().equals(description)
					|| Type.SHORT_TYPE.getDescriptor().equals(description)) {
				return Type.INT_TYPE.getDescriptor();
			} else {
				return Type.getDescriptor(Object.class);
			}
		}
		
		public static String getTypeForValueOf(VariableNode lastVar) {
			assert (lastVar != null) : "parameter:lastVar must be not NULL.";
			
			final String description = lastVar.getDescription();
			return getTypeForValueOf(description);
		}
		
		public static String getTypeForValueOf(String description) {
			if (description == null) {
				throw new IllegalStateException(
						"the description of this method is not setted. so it can't be converted to the description which is accepted by the append() method.");
			}

			if (/* STRING_DESC.equals(description)
					|| */ Type.BOOLEAN_TYPE.getDescriptor().equals(description)
					|| Type.CHAR_TYPE.getDescriptor().equals(description)
					|| Type.DOUBLE_TYPE.getDescriptor().equals(description)
					|| Type.FLOAT_TYPE.getDescriptor().equals(description)
					|| Type.INT_TYPE.getDescriptor().equals(description)
					|| Type.LONG_TYPE.getDescriptor().equals(description)) {
				return description;
			} else if (Type.BYTE_TYPE.getDescriptor().equals(description)
					|| Type.SHORT_TYPE.getDescriptor().equals(description)) {
				return Type.INT_TYPE.getDescriptor();
			} else {
				return Type.getDescriptor(Object.class);
			}
		}
		
		public static Node createToStringCall() {
			return new MethodInsnNode(Constants.INVOKEVIRTUAL,
					"java/lang/StringBuffer", "toString",
			"()Ljava/lang/String;");
		}

		public static Node createConstructorCode(final int templateLength) {
			final InstructionGroup rval = new InstructionGroup();
			rval.addInstruction(new TypeInsnNode(Constants.NEW,
					"java/lang/StringBuffer"));
			rval.addInstruction(new InsnNode(Constants.DUP));
			rval.addInstruction(new IntInsnNode(Constants.SIPUSH, templateLength));
			// call Instance of the object
			rval.addInstruction(new MethodInsnNode(Constants.INVOKESPECIAL,
					"java/lang/StringBuffer", "<init>", "(I)V"));
			return rval;
		}
		
		public static Node createConstructorCode(final LogMessageNode logEntry) {
			final int templateLength = logEntry.getTemplate().length();
			return AsmUtils.StringBuffer.createConstructorCode(templateLength);
		}

	}

}