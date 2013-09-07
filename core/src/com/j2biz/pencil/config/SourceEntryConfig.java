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
package com.j2biz.pencil.config;

import java.io.File;
import java.util.List;

import javolution.xml.XmlElement;
import javolution.xml.XmlFormat;

import org.grlea.log.SimpleLogger;

import com.j2biz.pencil.Source;

/**
 * @author andrej
 *
 */
public class SourceEntryConfig extends XmlFormat {

	public static SimpleLogger LOG = new SimpleLogger( SourceEntryConfig.class);
	
	public static final XmlFormat XML = new SourceEntryConfig();
	
	/* (non-Javadoc)
	 * @see javolution.xml.XmlFormat#format(java.lang.Object, javolution.xml.XmlElement)
	 */
	public void format(Object obj, XmlElement xml) {
		throw new UnsupportedOperationException();		
	}

	/* (non-Javadoc)
	 * @see javolution.xml.XmlFormat#parse(javolution.xml.XmlElement)
	 */
	public Object parse(final XmlElement xml) {
		LOG.entry("parse(XmlElement xml)");
		final CharSequence path = xml.getAttribute( ConfigManager.ATTR_PATH );
		
		if( path == null ) 
			throw new NullPointerException( "attribute:" + ConfigManager.ATTR_PATH + " tag:" + ConfigManager.TAG_SRC_ENTRY);
		
		LOG.debugObject("found a path of a source-entry", path.toString());
		
		final File srcFile = new File(path.toString());
		LOG.debugObject("create file object", srcFile);
		
		final List packages = xml.getContent();
		LOG.debugObject("list of packages defined in the source-entry", packages);
		LOG.debugObject("number of packages defined in this source-entry", packages.size());
		
		if( hasPackagesSelected(packages)) {
			return new Source( srcFile, parsePackages(packages) );
		} else 
			return new Source( srcFile );
		
	}

	private boolean hasPackagesSelected( final List packages ) {
		return packages != null && !packages.isEmpty();
	}

	private String[] parsePackages( final List packages ) {
		LOG.entry("parsePackages( final List packages )");
		int size = packages.size();
		final String[] packageNames = new String[ size ];
		
		for(int i = 0; i < size; i++) {
			convertPackageNameToPath(packages, packageNames, i);
		}
		
		LOG.exit("parsePackages( final List packages )");
		return packageNames;
	}

	private void convertPackageNameToPath( final List packages, final String[] packageNames, int i ) {
		String name = String.valueOf( packages.get(i));
		packageNames[i] = name.replace('.', '/');
	}

}
