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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import javolution.util.FastList;
import javolution.xml.XmlElement;
import javolution.xml.XmlException;
import javolution.xml.XmlFormat;

import com.j2biz.info.LoggerManager;
import com.j2biz.pencil.ClassManager;
import com.j2biz.pencil.Source;
import com.j2biz.utils.FileUtils;

/**
 * This class provide the xml-mapping for the ClassManager.
 */
public class ClassManagerConfig extends XmlFormat {

	public ClassManagerConfig() {
		super(ClassManager.class);
	}

	public static final XmlFormat XML = new ClassManagerConfig();

	/*
	 * (non-Javadoc)
	 * 
	 * @see javolution.xml.XmlFormat#format(java.lang.Object,
	 *      javolution.xml.XmlElement)
	 */
	public void format(final Object obj, final XmlElement xml) {
		throw new UnsupportedOperationException(
				"Pencil only reads configuration, but never write.");
	}

	public Object parse(final XmlElement xml) {
		final CharSequence baseDir = xml
				.getAttribute(ConfigManager.ATTR_BASEDIR);

		final File baseDirFile;
		try {
			if (baseDir == null) {
				baseDirFile = new File("");
			} else {
				baseDirFile = FileUtils.createBasedir(baseDir.toString());
			}
		} catch (IOException x) {
			// LOG: falscher pfad.
			throw new XmlException("attribute:" + ConfigManager.ATTR_BASEDIR
					+ " tag:" + ConfigManager.TAG_ROOT + " wrong path.", x);
		}

		final CharSequence outputPath = xml
				.getAttribute(ConfigManager.ATTR_OUTPUT);
		if (outputPath == null) {
			// LOG: Attribute fehlt.
			throw new XmlException("the configuration lacks the attribute "
					+ ConfigManager.ATTR_OUTPUT + " in the tag:"
					+ ConfigManager.TAG_ROOT);
		}

		final File output;
		try {
			output = FileUtils.createFile(baseDirFile, outputPath.toString());
		} catch (IOException e) {
			// LOG: output file doesnt exist
			throw new XmlException("attribute:" + ConfigManager.ATTR_OUTPUT
					+ " tag:" + ConfigManager.TAG_ROOT + " path doesn't exist.");
		}

		final FastList content = xml.getContent();

		Source[] sourceEntries = null;
		URLClassLoader classLoader = null;

		if (content != null) {
			int size = content.size();
			for (int i = 0; i < size; i++) {
				Object child = content.get(i);
				if (child instanceof Source[]) { // sources
					if (sourceEntries != null) {
						throw new XmlException("tag:"
								+ ConfigManager.TAG_SRC_CLASSES
								+ " occurs more than just once.");
					}

					sourceEntries = (Source[]) child;
					prepareSources(sourceEntries, baseDirFile);
				} else if (child instanceof File[]) {
					if (classLoader != null) {
						throw new XmlException("tag:"
								+ ConfigManager.TAG_CLASSLOADER
								+ " occurs more than just once.");
					}

					File[] cpe = (File[]) child;
					URL[] urls = prepareClassPathURLs(cpe, baseDirFile);
					classLoader = new URLClassLoader(urls);
				}
			}
		}

		if (sourceEntries == null) {
			throw new XmlException("tag:" + ConfigManager.TAG_SRC_CLASSES
					+ " is not declared.");
		}

		final ClassManager classManager = new ClassManager(baseDirFile, output,
				sourceEntries, classLoader, LoggerManager.getLogger());

		return classManager;
	}

	/**
	 * @param cpe
	 * @return
	 */
	private URL[] prepareClassPathURLs(File[] cpe, final File baseDir) {
		URL[] urls = new URL[cpe.length];
		for (int i = 0; i < cpe.length; i++) {
			try {
				File absFile = FileUtils.createFile(baseDir, cpe[i].toString());
				if (!absFile.exists()) {
					throw new XmlException("tag:"
							+ ConfigManager.TAG_CLASSPATH_ENTRY + "/attribute:"
							+ ConfigManager.ATTR_PATH
							+ " file does not exist. path = " + cpe[i]);
				}
				
				urls[i] = absFile.toURL();
			} catch (MalformedURLException x) {
				// LOG: exception
				throw new XmlException("tag:"
						+ ConfigManager.TAG_CLASSPATH_ENTRY + "/attribute:"
						+ ConfigManager.ATTR_PATH
						+ " can't be converted to a URL. path = " + cpe[i]);
			} catch (IOException x) {
				// LOG: exception
				throw new XmlException("tag:"
						+ ConfigManager.TAG_CLASSPATH_ENTRY + "/attribute:"
						+ ConfigManager.ATTR_PATH
						+ " can't create an absolute path. path = " + cpe[i]
						+ ", basedir = " + baseDir);
			}
		}

		return urls;
	}

	/**
	 * @param classManager
	 * @param list
	 */
	private void prepareSources(final Source[] sourceEntries, final File baseDir) {
		for (int i = 0; i < sourceEntries.length; i++) {
			Source source = sourceEntries[i];
			File file = source.getFile();
			if (!file.isAbsolute()) {
				final File absoluteSource;

				try {
					absoluteSource = FileUtils.createFile(baseDir, file
							.toString());
					
					if (!absoluteSource.exists()) {
						throw new XmlException("tag:"
								+ ConfigManager.TAG_SRC_ENTRY + "/attribute:"
								+ ConfigManager.ATTR_PATH
								+ " file does not exist. path = " + file);
					}
				} catch (IOException e) {
					throw new XmlException("tag:" + ConfigManager.TAG_SRC_ENTRY
							+ "/attribute:" + ConfigManager.ATTR_PATH
							+ " can't calc an absolute source-path. path = "
							+ file);
				}
				source.setFile(absoluteSource);
			}
		}
	}

}
