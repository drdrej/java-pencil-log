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
package com.j2biz.pencil;

import java.lang.instrument.ClassDefinition;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassManagerClassLoader extends URLClassLoader {

	public ClassManagerClassLoader(URL[] urls) {
		super(urls);
	}
	
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
//		java.lang.instrument.
		new Instrumentation() {

			public void addTransformer(ClassFileTransformer transformer) {
				// TODO Auto-generated method stub
				
			}

			public boolean removeTransformer(ClassFileTransformer transformer) {
				// TODO Auto-generated method stub
				return false;
			}

			public boolean isRedefineClassesSupported() {
				// TODO Auto-generated method stub
				return false;
			}

			public void redefineClasses(ClassDefinition[] definitions) throws ClassNotFoundException, UnmodifiableClassException {
				// TODO Auto-generated method stub
				
			}

			public Class[] getAllLoadedClasses() {
				// TODO Auto-generated method stub
				return null;
			}

			public Class[] getInitiatedClasses(ClassLoader loader) {
				// TODO Auto-generated method stub
				return null;
			}

			public long getObjectSize(Object objectToSize) {
				// TODO Auto-generated method stub
				return 0;
			}
			
		};
		
		return super.loadClass(name);
	}
	
	
}
