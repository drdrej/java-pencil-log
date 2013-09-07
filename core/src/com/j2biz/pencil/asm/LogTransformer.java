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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.grlea.log.SimpleLogger;
import org.objectweb.asm.CodeWriter;

import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.logexpr.FirstPartFieldRefInstruction;
import com.j2biz.pencil.asm.tree.FieldVariableDefinition;
import com.j2biz.pencil.asm.tree.LoadLocalVariableNode;
import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.pencil.asm.tree.MethodInfoNode;
import com.j2biz.pencil.asm.tree.ModifiableClassInfoNode;
import com.j2biz.pencil.asm.tree.Node;
import com.j2biz.pencil.ex.CompileTimeWarningException;
import com.j2biz.utils.SysoutUtils;

/**
 * @author Andreas Siebert (c) 2004 by Andreas Siebert / j2biz.com Copyright
 *         Claimed: Copyright (c) 2004 Andreas Siebert. All Rights Reserved.
 */
public class LogTransformer extends Transformer {

	private static final SimpleLogger LOG = new SimpleLogger(LogTransformer.class);
	
	public static final String	LOGGER_FIELD_NAME	= "$COM_J2BIZ_LOG";

	public ASMClassInfoNode		modifiedClass;
	
	private List<ASMClassInfoNode> classesWithLoggingClinit = new ArrayList();

	/**
	 * @param computeMaxs
	 */
	public LogTransformer( boolean computeMaxs, final ErrorStatusLogger logger ) {
		super(computeMaxs, logger);
	}

	/**
	 * @see com.j2biz.pencil.asm.Transformer#init(com.j2biz.pencil.asm.tree.ModifiableClassInfoNode)
	 */
	public void init( final ASMClassInfoNode classToTransform ) {
		LOG.entry("init( ASMClassInfoNode classToTransform )");
		super.init(classToTransform);
		
		this.modifiedClass = classToTransform;
		
		performPostTransformations();
		
		LOG.exit("init( ASMClassInfoNode classToTransform )");
	}
	
	private void performPostTransformations( ) {
		LOG.entry("performPostTransformations( )");
		for ( final  CodeTransformation transformation : this.modifiedClass.transformations() ) {
			performPostTransformation(transformation);
		}
		LOG.exit("performPostTransformations( )");
	}

	private void performPostTransformation( final CodeTransformation transformation ) {
		LOG.ludicrousObject( "transformer", transformation);
		transformation.transform();
	}

	/**
	 * @see com.j2biz.pencil.asm.Transformer#end(com.j2biz.pencil.asm.tree.ModifiableClassInfoNode)
	 */
	public void end( ) {
		super.end();
		this.modifiedClass = null;
	}

	/**
	 * @see com.j2biz.pencil.asm.Transformer#beforeWriteMethod(com.j2biz.pencil.asm.tree.ModifiableClassInfoNode,
	 *      com.j2biz.pencil.asm.tree.MethodInfoNode)
	 */
	public void beforeWriteMethod(
		ModifiableClassInfoNode classInfo,
		MethodInfoNode callerMethod ) {
		super.beforeWriteMethod(classInfo, callerMethod);

		if ( callerMethod.hasLogEntries() ) {
			replaceLogEntriesInAMethod(callerMethod);
		}
	}

	private void replaceLogEntriesInAMethod( MethodInfoNode callerMethod ) {
		final List instructions = callerMethod.instructions();

		for ( int i = 0; i < instructions.size(); i++ ) {
			Object instr = instructions.get(i);
			if ( instr instanceof LogMessageNode ) {
				replaceLogEntry(callerMethod, instructions, i,
						(LogMessageNode) instr);
			}
		}
	}

	private void replaceLogEntry(
		final MethodInfoNode callerMethod,
		final List instructions,
		final int logEntryPosition,
		final LogMessageNode logEntry ) {
		final Node codePart = logEntry
				.createReplaceCodePart(callerMethod, this);

		instructions.set(logEntryPosition, codePart);

		// behandele die fehler :::
		if ( logEntry.hasWarnings() ) {
			for ( final Iterator<CompileTimeWarningException> i = logEntry
					.getWarnings(); i.hasNext(); ) {
				final CompileTimeWarningException warning = i.next();

				System.err.println("[PENCIL-WARNING] "
						+ warning.getLocalizedMessage());
				System.err.println("\t at " + warning.getCallerClass() + "."
						+ warning.getCallerMethod() + "("
						+ warning.getCallerSource() + ":"
						+ warning.getLineNumber() + ")");

				System.err.println("\t Information:"
						+ SysoutUtils.LINE_SEPARATOR + "\t -------------");

				for ( final String name : warning.additionalInfoNames() ) {
					System.err.println("\t " + name + ": "
							+ warning.getAdditionalInfo(name));
				}

				System.err.println();
			}
		}
	}

	/**
	 * @see com.j2biz.pencil.asm.Transformer#visitInstruction(com.j2biz.pencil.asm.tree.Node,
	 *      org.objectweb.asm.CodeWriter)
	 */
	public void visitInstruction( Node node, CodeWriter codeWriter ) {
		if ( node instanceof LoadLocalVariableNode ) {
			((LoadLocalVariableNode) node).accept(codeWriter);
		} else if ( node instanceof FieldVariableDefinition ) {
			((FieldVariableDefinition) node).accept(codeWriter);
		} else if ( node instanceof FirstPartFieldRefInstruction ) {
			final FirstPartFieldRefInstruction fieldRef = (FirstPartFieldRefInstruction) node;
			fieldRef.accept(codeWriter, this);
		} else {
			super.visitInstruction(node, codeWriter);
		}
	}
}