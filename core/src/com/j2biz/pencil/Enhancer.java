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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.grlea.log.SimpleLogger;
import org.objectweb.asm.Attribute;

import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.pencil.asm.LogTransformer;
import com.j2biz.pencil.asm.Transformer;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.ex.EnhancementException;

/**
 * This class is the main class.
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class Enhancer {

	private static final SimpleLogger LOG = new SimpleLogger(Enhancer.class);
	
	private final ErrorStatusLogger logger;

	/**
	 * a prototype of attributes used by the compiler/analyzer
	 */
	private static final Attribute[] PARSED_ATTRIBUTES = new Attribute[] { new LogAttribute(
			1) };

	private final ClassManager manager;

	/**
	 * Every instance of the Enhancer class contains a ClassManager and
	 * ErrorStatusLogger. This attributes describes a state of the enhancer. to
	 * reset the state of the enhancer themethod reset() of this class should be
	 * used.
	 * 
	 * @param src
	 *            is a source file, which should be modified.
	 * 
	 * @param statusLogger
	 *            contains all messages, which are collected during the
	 *            enhancement-process.
	 * 
	 * @throws NullPointerException
	 *             if the parameter src or the parameter statusLogger is NULL.
	 */
	public Enhancer(final File src, final ErrorStatusLogger statusLogger)
			throws NullPointerException {
		if (src == null) {
			throw new NullPointerException("parameter:src");
		}
		
		if (!src.exists()) {
			throw new IllegalArgumentException(
					"parameter:src: FileNotFound. src == " + src);
		}
		
		final Source source = new Source(src);
		
		manager = new ClassManager(new File(""), src, new Source[] { source },
				null, statusLogger);

		if (statusLogger == null) {
			throw new NullPointerException("parameter:statusLogger");
		}
		
		logger = statusLogger;
	}

	public Enhancer(final File src, final String[] packages,
			final ErrorStatusLogger statusLogger) throws NullPointerException {
		if (src == null) {
			throw new NullPointerException("parameter:src");
		}
		if (!src.exists()) {
			throw new IllegalArgumentException(
					"parameter:src: FileNotFound. src == " + src);
		}
		Source source = new Source(src, packages);
		manager = new ClassManager(new File(""), src, new Source[] { source },
				null, statusLogger);

		if (statusLogger == null) {
			throw new NullPointerException("parameter:statusLogger");
		}
		logger = statusLogger;
	}

	public Enhancer(final ClassManager classManager,
			final ErrorStatusLogger statusLogger) {
		if (classManager == null) {
			throw new NullPointerException("parameter:classManager");
		}
		manager = classManager;

		if (statusLogger == null) {
			throw new NullPointerException("parameter:statusLogger");
		}
		logger = statusLogger;
	}

	/**
	 * enhance all classes in the classSrc-directory. this method should be used
	 * to enhance code at compile time or before runnig.
	 * 
	 */
	public void enhance() {
		int nrOfResources = 0;
		for (; this.manager.hasNextSource(); nrOfResources++) {
			final ASMClassInfoNode classInfo = this.manager.getNextSource();

			final Transformer t = new LogTransformer(true, this.logger);
			byte[] bc = classInfo.modify(t);

			final String packageName = classInfo.getPackageName();
			final File resource = getOutputFile(classInfo);
			try {
				if (!resource.exists())
					resource.createNewFile();

				final FileOutputStream out = new FileOutputStream(resource);
				out.write(bc);
				out.close();
			} catch (FileNotFoundException x) {
				throw new EnhancementException("file does not exist: " + resource, x);
			} catch (IOException x) {
				throw new EnhancementException("can'r write enhanced code to the file: " + resource, x);
			}
		}
		
		LOG.infoObject( "number of analyzed class-files", nrOfResources);
	}

	private File getOutputFile(final ASMClassInfoNode classInfo) {
		
		final File resource = classInfo.getResource();
		final File outputDir = this.manager.getOutputDir();

		if (outputDir != null) {
			if (outputDir.isFile())
				throw new IllegalArgumentException(
						"output-dir is not a directory.");

			if (!outputDir.exists()) {
				outputDir.mkdir();
			}

			final String packageName = classInfo.getPackageName();
			if (packageName.length() < 1)
				return new File(outputDir, resource.getName());
			else {
				File pckDir = new File(outputDir, packageName);
				if (!pckDir.exists())
					pckDir.mkdirs();

				return new File(pckDir, resource.getName());
			}
		}

		return resource;
	}

	/**
	 * resets the state of an instance of the Enhancer. For example: the
	 * execution of this method implies the reset of the ErrorStatusLogger.
	 */
	public void reset() {
		;
	}
}