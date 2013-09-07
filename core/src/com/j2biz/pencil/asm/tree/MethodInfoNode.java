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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javolution.util.FastList;

import org.objectweb.asm.Attribute;
import org.objectweb.asm.Constants;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;

import com.j2biz.pencil.asm.Assert;
import com.j2biz.pencil.asm.LdcWrapper;
import com.j2biz.pencil.asm.LocalVarManager;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.tree.simple.FieldInsnNode;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.asm.tree.simple.TryCatchBlockNode;
import com.j2biz.utils.ListFactory;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class MethodInfoNode {

    private int access;

    private String name;

    private String description;

    private String[] exceptions;

    private Attribute attrs;

    private Attribute codeAttr;

    private final List<Node> instr = ListFactory.create();

    private final ASMClassInfoNode owner;

    private int stackSize;

    private int numberOfLocals;

    private List tryCatchBlocks = Collections.EMPTY_LIST;

    private final List lineNrs = new FastList();

    private int nrOfLogEntries;

	private final boolean isStatic;
	
	private final LocalVarManager variableManager = new LocalVarManager();

    /**
     * @param access
     * @param name
     * @param desc
     * @param exceptions können NULL sein. evtl. NULL-Objekt einführen.
     * @param attrs  können NULL sein. evtl. NULL-Objekt einführen.
     */
    public MethodInfoNode(int access, String name, String desc,
            String[] exceptions, Attribute attrs,
            final ASMClassInfoNode owner) {
		Assert.isNotNull(name);
		Assert.isNotNull(desc);
		Assert.isNotNull(owner);
		
        this.access = access;
        this.name = name;
        this.exceptions = exceptions;
        this.description = desc;
        this.attrs = attrs;
        this.owner = owner;
		
		isStatic = (this.getAccess() & Constants.ACC_STATIC) == Constants.ACC_STATIC;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#addInstruction(com.j2biz.pencil.asm.tree.Node)
     */
    public void addInstruction(Node node) {
        addNode(node);
    }

    private void addNode(Node node) {
        this.instr.add(node);
    }

    /**
     * this method adds a list of instructions to a method. if the list of the
     * instructions in this instance is empty, this methode replace the instance
     * of the list with the new list.
     * 
     * @param instructions
     */
    public void addInstructions(final List instructions) {
            this.instr.addAll(instructions);
    }

    public void addLabel(Label label) {
		LabelWrapper wrapper = new LabelWrapper(label);
        addNode(wrapper);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#setMax(int, int)
     */
    public void setMax(int stackSize, int numberOfLocals) {
        this.numberOfLocals = numberOfLocals;
        this.stackSize = stackSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#addLdc(java.lang.Object)
     */
    public void addLdc(final Object cst) {
        if (cst == null) {
            throw new NullPointerException("parameter:cst");
        }
		
        if (cst instanceof String || cst instanceof Number || cst instanceof Type) {
			LdcWrapper wrapper = new LdcWrapper(cst);
            addNode(wrapper);
        } else {
            throw new ClassCastException("cst has a wrong type: "
                    + cst.getClass() + " for cst: " + cst);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#addTryCatchBlock(org.objectweb.asm.Label,
     *          org.objectweb.asm.Label, org.objectweb.asm.Label, java.lang.String)
     */
    public void addTryCatchBlock(Label start, Label end, Label handler,
            String type) {
        final TryCatchBlockNode block = new TryCatchBlockNode(start, end,
                handler, type);

        if (this.tryCatchBlocks == Collections.EMPTY_LIST) {
            this.tryCatchBlocks = new ArrayList(2);
        }

        tryCatchBlocks.add(block);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#numberOfInstructions()
     */
    public int numberOfInstructions() {
        return this.instr.size();
    }

    public Object getInstruction(int idx) {
        return this.instr.get(idx);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getOwner()
     */
    public ASMClassInfoNode getOwner() {
        return this.owner;
    }

    public int getNumberOfLocals() {
        return this.numberOfLocals;
    }

    public List<Node> instructions() {
        return this.instr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#setCodeAttribute(org.objectweb.asm.Attribute)
     */
    public void addCodeAttribute(final Attribute attr) {
        attr.next = this.codeAttr;
        this.codeAttr = attr;
    }

    public void addLineNumber(LineNumber lineNr) {
        this.lineNrs.add(lineNr);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getAccess()
     */
    public int getAccess() {
        return this.access;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getName()
     */
    public String getName() {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getDescription()
     */
    public String getDescription() {
        return this.description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getExceptionsArray()
     */
    public String[] getExceptionsArray() {
        return this.exceptions == null ? new String[0] : this.exceptions;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getFirstAttribute()
     */
    public Attribute getFirstAttribute() {
        return this.attrs;
    }

    public Attribute getFirstCodeAttribute() {
        return this.codeAttr;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#tryCatchBlocks()
     */
    public List tryCatchBlocks() {
        return Collections.unmodifiableList(this.tryCatchBlocks);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#getMaxStack()
     */
    public int getStackSize() {
        return this.stackSize;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#lineNumbers()
     */
    public List lineNumbers() {
        return Collections.unmodifiableList(this.lineNrs);
    }

    /**
     * 
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#addInstruction(int,
     *          org.objectweb.asm.Label)
     */
    public void addInstructionAtBeginning(final Node instruction) {
        if (instruction == null) {
            throw new NullPointerException("parameter:instruction");
        }
        addNodeAtBeginning(instruction);
    }

    public void addLdcAtBeginning(Object constant) {
        if (constant == null) {
            throw new NullPointerException("parameter:constant");
        }

        if (constant instanceof String || constant instanceof Number) {
            addNodeAtBeginning(new LdcWrapper(constant));
        }
    }

    public void addLabelAtBeginning(Label label) {
        Assert.isNotNull(label);
        addNodeAtBeginning(new LabelWrapper(label) );
    }

    /**
     * @param instruction
     */
    private void addNodeAtBeginning(final Node instruction) {
            this.instr.add(0, instruction);
    }

    /**
     * @see com.j2biz.pencil.asm.tree.MethodInfoNode#hasLogEntries()
     */
    public boolean hasLogEntries() {
        return this.nrOfLogEntries > 0;
    }

    public void setNumberOfLogs(int nrOfLogs) {
        this.nrOfLogEntries = nrOfLogs;
    }

    /**
     * only in a wrapper.
     * @return
     */
    public int numberOfLogEntries() {
        return this.nrOfLogEntries;
    }

	public void addLogInitAtStaticBlock(ASMClassInfoNode classInfo) {
		addLabel(new Label());
		addLdc(classInfo.getClassName());
		final MethodInsnNode methodInsn = new MethodInsnNode(
				Constants.INVOKESTATIC,
				"org/apache/commons/logging/LogFactory", "getLog",
				"(Ljava/lang/String;)Lorg/apache/commons/logging/Log;");
		addInstruction(methodInsn);
		final FieldInsnNode fieldInsn = new FieldInsnNode(
				Constants.PUTSTATIC, LogTransformer.LOGGER_FIELD_NAME, classInfo
						.getClassName(), "Lorg/apache/commons/logging/Log;");
		addInstruction(fieldInsn);
	}

	public void insertLogInitInClearStaticBlock(ASMClassInfoNode classInfo) {
		final FieldInsnNode fieldInsn = new FieldInsnNode(
				Constants.PUTSTATIC, LogTransformer.LOGGER_FIELD_NAME, classInfo
						.getClassName(), "Lorg/apache/commons/logging/Log;");
		addInstructionAtBeginning(fieldInsn);
		final MethodInsnNode methodInsn = new MethodInsnNode(
				Constants.INVOKESTATIC,
				"org/apache/commons/logging/LogFactory", "getLog",
				"(Ljava/lang/String;)Lorg/apache/commons/logging/Log;");
		addInstructionAtBeginning(methodInsn);
		
		addLdcAtBeginning(classInfo.getClassName());
		
		addLabelAtBeginning(new Label());
	}

	public boolean isStatic() {
		return isStatic;
	}


	public LocalVarManager getVariableManager() {
		return this.variableManager;
	}
	
	@Override
	public String toString( ) {
		final StringBuilder builder = new StringBuilder();
		builder.append( this.getName() );
		builder.append(" ");
		builder.append( getDescription());
		
		
		String[] exceptions = getExceptionsArray();
		if(exceptions.length > 0) {
			builder.append(" throws ");
		
			for ( int i = 0; i < exceptions.length; i++ ) {
				builder.append( exceptions[i] );
				builder.append( ", ");
			}
		}
		return builder.toString();
		
	}

	/**
	 * 
	 * @return
	 */
	public LabelWrapper getLastRegistredLabel( ) {
		for ( int i = this.instructions().size()-1; i >= 0; i-- ) {
			final Node instruction = this.instructions().get(i);
			if( instruction instanceof LabelWrapper)
				return (LabelWrapper) instruction;
		}
		
		return LabelWrapper.NULL_INSTANCE;
	}
}