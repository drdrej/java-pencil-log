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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import javolution.util.FastList;

import org.grlea.log.SimpleLogger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.attrs.Attributes;

import com.j2biz.info.ErrorStatus;
import com.j2biz.info.ErrorStatusLogger;
import com.j2biz.info.SimpleErrorStatus;
import com.j2biz.pencil.asm.classes.ASMClassInfoNode;
import com.j2biz.pencil.asm.classes.AbstractClassInfoNode;
import com.j2biz.pencil.asm.classes.ClassAnalyzerVisitor;
import com.j2biz.pencil.asm.logexpr.ReflectionClassInfoNode;
import com.j2biz.pencil.asm.tree.ModifiableClassInfoNode;
import com.j2biz.pencil.ex.ClassParseException;
import com.j2biz.pencil.msgKeys.StatusKeys;
import com.j2biz.utils.MapFactory;

/**
 * An instance of this class manages class-resources. Its like a ClassLoader,
 * but its never return a class, but a ClassInfoNode.
 * 
 * @see com.j2biz.pencil.ASMClassInfoNode
 * @see com.j2biz.pencil.ReflectionClassInfoNode
 * 
 * The ClassManager search in the hierarchy in the same way like the
 * ClassLoader.
 * 
 * 
 * @author Andreas Siebert
 * 
 * (c) 2004 by Andreas Siebert / j2biz.com
 */
public class ClassManager {

	public static SimpleLogger LOG = new SimpleLogger( ClassManager.class);
	
	private final ErrorStatusLogger systemLogger;

	/**
	 * contains a list of modifiable resources. (source-files)
	 * 
	 * @see java.net.URL
	 * @see java.io.File
	 * @see com.j2biz.resource.Resource
	 */
	private final Source[] srcList;

	private final Map cache = MapFactory.create();

	private final File output;

	private final File baseDir;

	private final ClassLoader appClassLoader;

	public ClassManager(final File baseDir, final File outputDir,
			final ErrorStatusLogger systemLogger) {
		this(baseDir, outputDir, new Source[0], null, systemLogger);
	}

	public ClassManager(final File baseDir, final File outputDir,
			final Source[] sourceEntries, ClassLoader classLoader,
			final ErrorStatusLogger systemLogger) {
		appClassLoader = classLoader;

		if (sourceEntries == null) {
			throw new NullPointerException("parameter:sourceEntries");
		}
		srcList = sourceEntries;

		if (systemLogger == null) {
			throw new NullPointerException("parameter:systemLogger");
		}
		this.systemLogger = systemLogger;

		if (baseDir == null) {
			throw new NullPointerException("parameter:baseDir");
		}
		this.baseDir = baseDir;

		if (outputDir == null) {
			throw new NullPointerException("parameter:outputDir");
		}
		this.output = outputDir;
	}

	public AbstractClassInfoNode getClass(final String internalClassName) {
		try {
			return findClass(internalClassName);
		} catch (final ClassNotFoundException x) {
			throw new ClassParseException(
					"Class does not exists. Please configure your classpath correctly. Class : "
							+ internalClassName);
		}
	}

	/**
	 * @param internalClassName
	 *            is a name of a class, never NULL.
	 * 
	 * @return a ClassInfoNode, never NULL.
	 * 
	 * @throws NullPointerException
	 *             if className is NULL.
	 * 
	 * @see com.j2biz.pencil.ASMClassInfoNode
	 * @see com.j2biz.pencil.ReflectionClassInfoNode
	 */
	private AbstractClassInfoNode findClass(final String internalClassName)
			throws ClassNotFoundException {
		assert internalClassName != null;

		AbstractClassInfoNode classInfo = (AbstractClassInfoNode) cache
				.get(internalClassName);
		
		// CHECK: check double-check idiom ... (maybe an error)
		if (classInfo == null) {
			synchronized (cache) {
				classInfo = (AbstractClassInfoNode) cache
						.get(internalClassName);
				if (classInfo == null) {
					final String javaClassName = internalClassName.replace('/',
							'.');

					if (internalClassName.startsWith("java.")
							|| internalClassName.startsWith("javax.")) {
						classInfo = loadClassFromCompilerClassLoader(javaClassName);
						if (classInfo != null) {
							cache.put(internalClassName, classInfo);
							return classInfo;
						}
					}

					try {
						classInfo = loadClassFromResources(internalClassName);
					} catch (final IOException x) {
						throw new ClassNotFoundException("className: ["
								+ internalClassName
								+ "];can't load class cause IOE is throwed.", x);
					}

					if (classInfo != null) {
						cache.put(internalClassName, classInfo);
						return classInfo;
					}

					classInfo = loadClassFromClassPath(javaClassName);
					if (classInfo != null) {
						cache.put(internalClassName, classInfo);
						return classInfo;
					}

					final ErrorStatus warning = new SimpleErrorStatus(
							StatusKeys.LOAD_CLASS_FROM_COMPILER_CLASSLOADER,
							ErrorStatus.USER_MSG_WARN
									| ErrorStatus.LOG_MSG_INFO);

					systemLogger.log(warning);

					classInfo = loadClassFromCompilerClassLoaderWithException(javaClassName);
					if (classInfo != null) {
						cache.put(internalClassName, classInfo);
						return classInfo;
					}

					throw new ClassNotFoundException("className:"
							+ internalClassName);
				}
			}
		}

		return classInfo;
	}

	/**
	 * resources are a collection of directories, files or resource-instances
	 * (@see com.j2biz.resource.Resource). the compiler use this collection to
	 * determine which resources it should be modifiing and which not. All
	 * resources, which shouldn't be modified, should be stored in the classpath
	 * of the pencil.
	 * 
	 * to load a ClassInfoNode from the classpath use the
	 * loadClassFromClassPath() method.
	 * 
	 * @param internalClassName
	 *            never NULL
	 * 
	 * @return an instance of type ClassInfoNode.
	 * 
	 * @see com.j2biz.pencil.ClassInfoNode
	 */
	private final AbstractClassInfoNode loadClassFromResources(
			String internalClassName) throws IOException {
		
		
		for (int i = 0; i < srcList.length; i++) {
			final Source source = srcList[i];
			final File fileSourcePath = source.getFile();
			final File fileSource = new File(fileSourcePath, internalClassName
					+ ".class");
			if (fileSource.exists() && fileSource.isFile()) {
				return loadClassFromResource(fileSource);
			}
		}

		return null;
	}

	private ASMClassInfoNode loadClassFromResource(final File fileSource)
			throws IOException {
		LOG.entry( "loadClassFromResource(final File fileSource)" );
		
		final FileInputStream in = new FileInputStream(fileSource);
		final ClassAnalyzerVisitor visitor = new ClassAnalyzerVisitor(this,
				fileSource);

		final ClassReader reader = new ClassReader(in);
		try {
			reader.accept(visitor, Attributes.getDefaultAttributes(), false);
		} catch (Throwable x) {
			LOG.error("class is not parseable by ASM.");
			LOG.error("name of the class-file: " + fileSource);
			LOG.errorException(x);
			
			LOG.exit("loadClassFromResource(final File fileSource)");
			throw new ClassParseException("can't load class: " + fileSource, x);
		}
		
		LOG.exit("loadClassFromResource(final File fileSource)");
		return visitor.getClassInfo();
	}

	/**
	 * @param javalClassName
	 * @return
	 */
	private AbstractClassInfoNode loadClassFromClassPath(
			final String javalClassName) {
		if (appClassLoader != null) {
			try {
				Class cl = appClassLoader.loadClass(javalClassName);
				return new ReflectionClassInfoNode(cl, this);
			} catch (ClassNotFoundException x) {
				return null;
			}
		}

		return null;
	}

	/**
	 * loads a ClassInfoNode from the ClassLoader of the actual instance of the
	 * Pencil. this method should log a warning, if it loads a class, which is
	 * not begins with "java." or "javax." prefix.
	 * 
	 * @param javaClassName
	 * @return
	 */
	private AbstractClassInfoNode loadClassFromCompilerClassLoaderWithException(
			final String javaClassName) throws ClassNotFoundException {
		final ClassLoader loader = this.getClass().getClassLoader();
		final Class loadedClass = loader.loadClass(javaClassName);
		return new ReflectionClassInfoNode(loadedClass, this);
	}

	private AbstractClassInfoNode loadClassFromCompilerClassLoader(
			String javaClassName) {
		try {
			return loadClassFromCompilerClassLoaderWithException(javaClassName);
		} catch (ClassNotFoundException x) {
			return null;
		}
	}

	// Modifiable Iterator Resources....
	private ASMClassInfoNode nextNode = null;

	private boolean hasNextNode = false;

	// resource cache.
	private List cachedDirectory;

	private Iterator allSourcesIterator = null;

	public ASMClassInfoNode getNextSource() {
		if (hasNextSource()) {
			ASMClassInfoNode rval = nextNode;
			nextNode = null;
			hasNextNode = false;
			return rval;
		} else {
			throw new NoSuchElementException();
		}
	}

	public void resetResourceIterator() {
		allSourcesIterator = null;
	}

	public void clearResourceCache() {
		cachedDirectory = null;
		allSourcesIterator = null;
	}

	private void fillDirectoryCache() {
		cachedDirectory = new FastList();
		final StringBuffer nameBuffer = new StringBuffer();

		for (int i = 0; i < srcList.length; i++) {
			Source source = srcList[i];
			fillDirectoryCache(source, source.getFile(), nameBuffer);
		}
	}

	private void fillDirectoryCache(final Source srcStruture, final File dir,
			final StringBuffer nameBuffer) {
		ClassNameReader nameReader = new ClassNameReader();
		if (dir.isFile()) {
			try {
				FileInputStream in = new FileInputStream(dir);
				final String internalName = nameReader.readClassName(in);

				if (!srcStruture.checkPackage(internalName)) {
					return;
				}

				ModifiableClassInfoNode classInfoNode = (ModifiableClassInfoNode) this.cache
						.get(internalName);

				if (classInfoNode == null) {
					synchronized (cache) {
						classInfoNode = loadClassFromResource(dir);
						this.cache.put(internalName, classInfoNode);
					}
				}

				synchronized (cachedDirectory) {
					cachedDirectory.add(classInfoNode);
				}

			} catch (IOException x) {
				SimpleErrorStatus status = new SimpleErrorStatus(
						StatusKeys.CANT_READ_CLASS_FILE,
						ErrorStatus.LOG_MSG_ERROR, new String[] { String
								.valueOf(x) });
				this.systemLogger.log(status);
			}
		} else {
			File[] files = dir.listFiles();

			if (files == null) {
				return;
			}

			for (int i = 0; i < files.length; i++) {
				if (files[i].getName().endsWith(".class")
						|| files[i].isDirectory()) {
					fillDirectoryCache(srcStruture, files[i], nameBuffer);
				}
			}
		}
	}

	public boolean hasNextSource() {
		if (hasNextNode)
			return true;

		initializeSourceCache();

		if (allSourcesIterator.hasNext()) {
			nextNode = (ASMClassInfoNode) allSourcesIterator.next();
			hasNextNode = true;
			return true;
		}

		return false;
	}

	private void initializeSourceCache() {
		if (allSourcesIterator == null) {
			
			if (cachedDirectory == null) 
				fillDirectoryCache();
			
			allSourcesIterator = cachedDirectory.iterator();
		}
	}

	public ErrorStatusLogger getLogger() {
		return this.systemLogger;
	}

	public File getOutputDir() {
		return this.output;
	}
}