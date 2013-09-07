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

import java.io.File;

public class Source {
	private File srcDir;

	private final String[] packageNames;

	public Source(final File srcDir) {
		this(srcDir, new String[0]);
	}

	public Source(final File sourceDir, final String[] packageNames) {
		if( sourceDir == null ) {
			throw new NullPointerException( "parameter:sourceDir");
		}
		this.srcDir = sourceDir;

		if (packageNames == null) {
			throw new NullPointerException("parameter:packageNames");
		}

		final String[] parsedNames;
		parsedNames = new String[packageNames.length];

		for (int i = 0; i < parsedNames.length; i++) {
			parsedNames[i] = packageNames[i].replace('.', '/');
		}

		this.packageNames = parsedNames;
	}

	public File getFile() {
		return srcDir;
	}
	
	/**
	 * @param internalName
	 */
	public boolean checkPackage(final String internalName) {
		if (this.packageNames.length == 0) {
			return true;
		}

		for (int i = 0; i < this.packageNames.length; i++) {
			if (internalName.startsWith(this.packageNames[i])) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @param baseDir
	 */
	public void setFile(File newSource) {
		this.srcDir = newSource;
	}

}