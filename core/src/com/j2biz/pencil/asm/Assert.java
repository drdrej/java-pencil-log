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

import java.util.List;



public class Assert {

	public static final void isNotNull(Object value) {
		if( value == null ) {
			throw new NullPointerException();
		}
	}
	
	public static final void shouldNeverReachHere(final String msg) {
		final String notNullMsg = msg == null ? "" : msg;
		throw new IllegalStateException(notNullMsg);
	}

	public static void stringIsNotEmpty(String stringValue) {
		if( stringValue.trim().length() < 1) 
			throw new IllegalArgumentException("the string is empty.");	
	}

	public static void isTheSame(Object obj1, Object obj2) {
		if( obj1 == obj2 )
			throw new IllegalArgumentException("both references point to the same instance. instance ==" + obj1);
	}

	public static void indexIsNotLessThanZero(int index) {
		if( index < 0)
			throw new IllegalArgumentException("an index should be never less than 0. index == " + index);
	}

	public static void listIsNotEmpty(List list) {
		if( list == null )
			throw new NullPointerException("list is NULL.");
		
		if( list.isEmpty() )
			throw new IllegalArgumentException("list is empty.");
	}

}
