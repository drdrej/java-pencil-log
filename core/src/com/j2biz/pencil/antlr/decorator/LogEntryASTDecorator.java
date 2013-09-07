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
package com.j2biz.pencil.antlr.decorator;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import antlr.collections.AST;

import com.j2biz.pencil.asm.tree.LogMessageNode;
import com.j2biz.utils.ListFactory;

public class LogEntryASTDecorator extends ASTDecorator implements Iterable<LogMsgPartASTDecorator> {

	private List<LogMsgPartASTDecorator> msgParts; 

	public LogEntryASTDecorator(LogMessageNode entry) {
		super(entry.getRootAST());
	}
	
	public Iterator<LogMsgPartASTDecorator> iterator() {
		if( msgParts == null ) 
			return new InitMsgPartIterator( getWrappedVariableToken() );
		else 
			return msgParts.iterator();
	}
	
	class InitMsgPartIterator extends MsgPartIterator {
		InitMsgPartIterator( AST rootNode ) {
			super(rootNode);
			
			assert (LogEntryASTDecorator.this.msgParts == null) : "this Iterator should only be used, if the List of msg parts is not initialized." ;
			
			LogEntryASTDecorator.this.msgParts = ListFactory.create();
		}
		
		@Override
		public LogMsgPartASTDecorator next( ) {
			LogMsgPartASTDecorator nextPart = super.next();
			
			addToCache(nextPart);
			return nextPart;
		}

		private void addToCache( LogMsgPartASTDecorator nextPart ) {
			assert (LogEntryASTDecorator.this.msgParts != null) : "cache should be allready initialize" ;
			assert (nextPart != null) : "NULL can't be added.";
			
			LogEntryASTDecorator.this.msgParts.add(nextPart);
		}
		
		
	}
	

	static class MsgPartIterator implements Iterator<LogMsgPartASTDecorator> {
		private AST cursor = null;
		private boolean hasNext = false;

		MsgPartIterator(AST rootNode) {
			if (rootNode == null) {
				throw new NullPointerException("parameter:rootNode");
			}

			this.cursor = rootNode.getFirstChild();
			this.hasNext = this.cursor != null;
		}

		public boolean hasNext() {
			if (canGetNext()) {
				getNext();
			}

			return this.hasNext;
		}

		private void getNext() {
			this.cursor = this.cursor.getNextSibling();
			this.hasNext = this.cursor != null;
		}

		private boolean canGetNext() {
			return !this.hasNext && this.cursor != null;
		}

		public LogMsgPartASTDecorator next() {
			if (this.hasNext) {
				final AST rval = this.cursor;
				this.hasNext = false;
				return TemplateElementsFactory.getInstance().createLogMsgPart( rval );
			} else {
				throw new NoSuchElementException();
			}
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}

	}
}
