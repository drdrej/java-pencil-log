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
import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.Constants;
import org.objectweb.asm.Label;

import antlr.collections.AST;

import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.logexpr.AbstractLogExpression;
import com.j2biz.pencil.asm.logexpr.ComplexLogExpression;
import com.j2biz.pencil.asm.logexpr.LogMsgScope;
import com.j2biz.pencil.asm.logexpr.Scope;
import com.j2biz.pencil.asm.logexpr.ScopeCaller;
import com.j2biz.pencil.asm.logexpr.SimpleLogExpression;
import com.j2biz.pencil.asm.tree.simple.MethodInsnNode;
import com.j2biz.pencil.el.Template;
import com.j2biz.pencil.el.TemplateTreeParser;
import com.j2biz.pencil.ex.CompileTimeWarningException;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class LogMessageNode implements Node, ScopeCaller {

    private final String name;

    private final String template;

    private final String isName;

    private Scope scope;

    private final TemplateTreeParser templateParser;

    private final boolean complex;

	private final Label scopeStart;
	
	private final Label scopeEnd;

	private int lineNr;
	
	
	private List<List<CompileTimeWarningException>> exceptions = new ArrayList();
	
    /**
     * @param name
     * @param template
     */
    public LogMessageNode(final String name, final String template) {
        this.name = name;
        this.template = template;

        // TODO: isXXXEnabled() Methods are used by commons logging.
        // if you want to adapt this code for log4j, rewrite this code or
        // reimplement an own node.
        // ---------------------------------------------------------------------------
        isName = "is" + Character.toUpperCase(name.charAt(0))
                + name.substring(1) + "Enabled";
        // ---------------------------------------------------------------------------

        this.templateParser = Template.parse(this.template);
        this.complex = checkParser(this.templateParser);
		this.scopeStart = new Label();
		this.scopeEnd = new Label();
    }

    /**
     * @param parser
     */
    private boolean checkParser(TemplateTreeParser parser) {
        try {
            parser.parseStart();
            return parser.isComplex();
        } catch (final Exception x) {
            x.printStackTrace();
            return false;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.j2biz.pencil.asm.tree.Node#getChildren()
     */
    public Iterator getChildren() {
        return Collections.EMPTY_LIST.iterator();
    }

    public void setScope(final Scope scope) {
		assert (scope != null) : "parameter:scope must be not NULL";
		assert (this.scope == null) : "scope can only one time setted.";
		
        this.scope = scope;
    }

    public Scope getScope() {
        if(this.scope == null) 
			setScope(LogMsgScope.createEmpty());
		
		return this.scope;
    }

    public boolean isComplexTemplate() {
        return this.complex;
    }

    public String getIsEnabledName() {
        return this.isName;
    }

    public String getName() {
        return this.name;
    }

    public TemplateTreeParser getParser() {
        return this.templateParser;
    }

    /**
     * @return
     */
    public String getTemplate() {
        return this.template;
    }

    /**
     * @return
     */
    public AST getRootAST() {
        return this.templateParser.getAST();
    }

    /**
     * @see com.j2biz.pencil.asm.logexpr.ScopeCaller#canSee(com.j2biz.pencil.asm.tree.FieldVariableDefinition)
     */
    public boolean canSee(FieldVariableDefinition node) {
        return false;
    }

    /**
     * @see com.j2biz.pencil.asm.logexpr.ScopeCaller#canSee(com.j2biz.pencil.asm.tree.LoadLocalVariableNode)
     */
    public boolean canSee(LoadLocalVariableNode node) {
        return false;
    }

	public MethodInsnNode createDebugCall() {
		return new MethodInsnNode(Constants.INVOKEINTERFACE,
				"org/apache/commons/logging/Log", getName(),
				"(Ljava/lang/Object;)V");
	}

	public MethodInsnNode createIsDebugEnabledCall() {
		return new MethodInsnNode(Constants.INVOKEINTERFACE,
				"org/apache/commons/logging/Log", getName(),
				"(Ljava/lang/Object;)V");
	}

	public Node createReplaceCodePart(final MethodInfoNode callerMethod, final LogTransformer transformer) {
		final AbstractLogExpression logExpressionReplacer;
		
		if (isComplexTemplate()) {
			logExpressionReplacer = new ComplexLogExpression(this, callerMethod);
		} else {
			logExpressionReplacer = new SimpleLogExpression(this, callerMethod);
		}
	
		final Node rval = logExpressionReplacer.createReplace(transformer);
		
		if( logExpressionReplacer.hasWarnings() ) {
			this.exceptions.add( logExpressionReplacer.getWarnings() );
		}
		
		return rval;
	}
	
	public boolean hasWarnings() {
		return !this.exceptions.isEmpty();
	}
	
	public Iterator<CompileTimeWarningException> getWarnings() {
		final List<CompileTimeWarningException> warnings = new ArrayList();
		
		for ( final Iterator<List<CompileTimeWarningException>> i = this.exceptions.iterator(); i.hasNext(); ) {
		 	final List<CompileTimeWarningException> listOfExceptions = i.next();
			
		 	for ( final Iterator j = listOfExceptions.iterator(); j.hasNext(); ) {
 				final CompileTimeWarningException warning = (CompileTimeWarningException) j.next();
				
 				warnings.add(warning);
			}
		}
		
		return warnings.iterator();
	}
	
	public Label getScopeStart() {
		return this.scopeStart;
	}

	public Label getScopeEnd() {
		return this.scopeEnd;
	}

	public void setLineNr( int line ) {
		assert(line > 0);
		this.lineNr = line;
	}

	public int getLineNr( ) {
		return this.lineNr;
	}
	
	
	@Override
	public String toString( ) {
		return "void" + getName() + "(String)";
	}
}