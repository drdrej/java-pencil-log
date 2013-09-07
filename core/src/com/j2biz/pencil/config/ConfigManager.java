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
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javolution.xml.ObjectReader;
import javolution.xml.XmlFormat;

import org.grlea.log.SimpleLogger;

import com.j2biz.pencil.ClassManager;

/**
 * This class represents a configuration of pencil (root-object of the
 * config-document)
 * 
 * @author andrej
 */
public class ConfigManager {

	public static SimpleLogger LOG = new SimpleLogger( ConfigManager.class);
	
	public static final String ATTR_OUTPUT = "output-path";

	public static final String ATTR_TYPE = "type";

	public static final String ATTR_BASEDIR = "basedir";

	public static final String TAG_PACKAGE = "package";

	public static final String ATTR_NAME = "name";

	public static final String TAG_ROOT = "pencil-config";

	public static final String TAG_SRC_CLASSES = "src-classes";

	public static final String ATTR_PATH = "path";

	public static final String TAG_SRC_ENTRY = "source";

	public static final String TAG_CLASSLOADER = "classloader";

	public static final String TAG_CLASSPATH_ENTRY = "classpath-entry";

	/**
	 * this method initialize all xml-mappings.
	 */
	public static final void initialize() {
		XmlFormat.setInstance(ClassManagerConfig.XML, ClassManager.class);
		XmlFormat.setAlias(ClassManager.class, TAG_ROOT);

		XmlFormat.setInstance(SourcesConfig.XML, SourcesConfig.class);
		XmlFormat.setAlias(SourcesConfig.class, TAG_SRC_CLASSES);

		XmlFormat.setInstance(SourceEntryConfig.XML, SourceEntryConfig.class);
		XmlFormat.setAlias(SourceEntryConfig.class, TAG_SRC_ENTRY);

		XmlFormat.setInstance(PackageNameConfig.XML, PackageNameConfig.class);
		XmlFormat.setAlias(PackageNameConfig.class, TAG_PACKAGE);
		
		XmlFormat.setInstance(ClassLoaderConfig.XML, ClassLoaderConfig.class);
		XmlFormat.setAlias(ClassLoaderConfig.class, TAG_CLASSLOADER);
		
		XmlFormat.setInstance(ClasspathEntryConfig.XML, ClasspathEntryConfig.class);
		XmlFormat.setAlias(ClasspathEntryConfig.class, TAG_CLASSPATH_ENTRY);
	}

	/**
	 * this method provide a functionality to read a configuration-file. but
	 * first of all the xml-mappings should be initialized with the initialize
	 * method.
	 * 
	 * @param fileName
	 *            of the file to read.
	 * @throws FileNotFoundException
	 *             if the configuration file does not exist.
	 */
	public static final ClassManager readConfiguration(String fileName)
			throws FileNotFoundException {
		LOG.entry("readConfiguration(String fileName)");
		
		File file = new File(fileName);
		LOG.infoObject("configuration file", file.getAbsolutePath());
		
		LOG.exit("readConfiguration(String fileName)");
		return readConfiguration(file);
	}

	public static final ClassManager readConfiguration(final File file)
			throws FileNotFoundException {
		ClassManager config = (ClassManager) new ObjectReader()
				.read(new FileInputStream(file));

		return config;
	}
}
