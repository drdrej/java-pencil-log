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

import javolution.xml.XmlElement;
import javolution.xml.XmlException;
import javolution.xml.XmlFormat;

public class ClasspathEntryConfig extends XmlFormat {

	public static final XmlFormat XML = new ClasspathEntryConfig();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javolution.xml.XmlFormat#format(java.lang.Object,
	 *      javolution.xml.XmlElement)
	 */
	public void format(Object obj, XmlElement xml) {
		throw new UnsupportedOperationException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javolution.xml.XmlFormat#parse(javolution.xml.XmlElement)
	 */
	public Object parse(final XmlElement xml) {
		final CharSequence path = xml.getAttribute(ConfigManager.ATTR_PATH);
		if (path == null) {
			throw new XmlException("tag:" + ConfigManager.TAG_CLASSPATH_ENTRY
					+ "/attribute:" + ConfigManager.ATTR_PATH + " not found.");
		}

		final CharSequence type = xml.getAttribute(ConfigManager.ATTR_TYPE);
		if (type == null) {
			throw new XmlException("tag:" + ConfigManager.TAG_CLASSPATH_ENTRY
					+ "/attribute:" + ConfigManager.ATTR_TYPE + " not found.");
		}

		final String typeStr = type.toString();
		char lastChar = path.charAt(path.length() - 1);
		if ("jar".compareToIgnoreCase(typeStr) == 0) {
			if (lastChar == '/' || lastChar == '\\') {
				throw new XmlException("tag:"
						+ ConfigManager.TAG_CLASSPATH_ENTRY + "/attribute:"
						+ ConfigManager.ATTR_PATH + " path is wrong. path = "
						+ path);
			} else {
				return new File(path.toString());
			}
		} else if ("dir".compareToIgnoreCase(typeStr) == 0) {

			if (lastChar == '/' || lastChar == '\\') {
				return new File(path.toString());
			} else {
				return new File(path + "/");
			}
		} else {
			throw new XmlException("tag:" + ConfigManager.TAG_CLASSPATH_ENTRY
					+ "/attribute:" + ConfigManager.ATTR_TYPE
					+ " unknown type. type = " + typeStr);
		}
	}

}
