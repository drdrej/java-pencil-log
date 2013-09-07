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
package com.j2biz.pencil.el;

import antlr.collections.AST;

/**
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class AntlrUtils {

	public static final String createScopeId(AST scopeAST) {
		final String scope;
		if (isScopeToken(scopeAST)) {
			final String firstId = scopeAST.getText();
			if (firstId == null) {
				throw new IllegalStateException(
						"scope-token is occured, but this token has no text");
			}

			scope = constructScopeIdString(firstId, scopeAST);
		} else {
			// TODO: DEBUG. entweder AST null, also kein SCOPE angegeben oder
			// der Knoten ist kein SCOPE-TYPE, was heisst, dass kein SCOPE
			// angegeben wurde.
			scope = null;
		}

		return scope;
	}

	private static boolean isScopeToken(AST scopeAST) {
		return scopeAST != null
				&& scopeAST.getType() == TemplateTreeParserTokenTypes.SCOPE;
	}

	private static String constructScopeIdString(final String firstId,
			final AST scopeAST) {
		final StringBuffer buffer = new StringBuffer(firstId.length() * 5);
		buffer.append(firstId);

		for (AST nextId = scopeAST.getFirstChild(); nextId != null; nextId = nextId
				.getFirstChild()) {
			final String nextPartText = nextId.getText();
			if (nextPartText == null) {
				throw new IllegalStateException(
						"scope-token is occured, but this token has no text");
			}

			buffer.append('/');
			buffer.append(nextPartText);
		}

		return buffer.toString();
	}
}
