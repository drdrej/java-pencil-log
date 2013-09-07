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
package com.j2biz.utils;

import java.io.File;
import java.io.IOException;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * This class provide methods to work with files. A code of this class is
 * partialy taked from ANT-Project.
 * 
 * Its a prototype-version. Netware is not supported in this version, cause i
 * won't more dependencies to other classes of ANT.
 */
public class FileUtils {

	public static final boolean ON_NETWARE = false;

	/**
	 * @param basedir
	 *            never NULL.
	 * 
	 * @return normalized, absolute baseDir.
	 * 
	 * @throws IOException
	 *             if a baseDir-File can't be created.
	 * @throws NullPointerException
	 *             if baseDir is NULL.
	 * 
	 */
	public static final File createBasedir(final String basedir)
			throws IOException {
		if (basedir == null) {
			throw new NullPointerException("parameter:baseDir");
		}

		File file = new File(basedir);
		return normalize(file.getAbsolutePath());
	}

	public static final File createFile(final File baseDir, final String path)
			throws IOException {
		return resolveFile(baseDir, path);
	}

	/**
	 * Interpret the filename as a file relative to the given file - unless the
	 * filename already represents an absolute filename.
	 * 
	 * @param file
	 *            the "reference" file for relative paths. This instance must be
	 *            an absolute file and must not contain &quot;./&quot; or
	 *            &quot;../&quot; sequences (same for \ instead of /). If it is
	 *            null, this call is equivalent to
	 *            <code>new java.io.File(filename)</code>.
	 * 
	 * @param filename
	 *            a file name
	 * 
	 * @return an absolute file that doesn't contain &quot;./&quot; or
	 *         &quot;../&quot; sequences and uses the correct separator for the
	 *         current platform.
	 */
	private static File resolveFile(File baseDir, String filename)
			throws IOException {
		if (baseDir == null) {
			throw new NullPointerException("parameter:baseDir");
		}

		if (filename == null) {
			throw new NullPointerException("parameter:filename");
		}

		filename = filename.replace('/', File.separatorChar).replace('\\',
				File.separatorChar);

		// deal with absolute files
		if (!ON_NETWARE) {
			if (filename.startsWith(File.separator)
					|| (filename.length() >= 2
							&& Character.isLetter(filename.charAt(0)) && filename
							.charAt(1) == ':')) {
				return normalize(filename);
			}
		} else {
			// the assumption that the : will appear as the second character in
			// the path name breaks down when NetWare is a supported platform.
			// Netware volumes are of the pattern: "data:\"
			int colon = filename.indexOf(":");
			if (filename.startsWith(File.separator) || (colon > -1)) {
				return normalize(filename);
			}
		}

		File helpFile = baseDir.getAbsoluteFile(); /* new File(file.getAbsolutePath()); */
		StringTokenizer tok = new StringTokenizer(filename, File.separator);
		while (tok.hasMoreTokens()) {
			String part = tok.nextToken();
			if (part.equals("..")) {
				helpFile = helpFile.getParentFile();
				if (helpFile == null) {
					String msg = "The file or path you specified (" + filename
							+ ") is invalid relative to " + baseDir.getPath();
					throw new IOException(msg);
				}
			} else if (part.equals(".")) {
				// Do nothing here
				;
			} else {
				helpFile = new File(helpFile, part);
			}
		}

		return new File(helpFile.getAbsolutePath());
	}

	/**
	 * &quot;normalize&quot; the given absolute path.
	 * 
	 * <p>
	 * This includes:
	 * <ul>
	 * <li>Uppercase the drive letter if there is one.</li>
	 * <li>Remove redundant slashes after the drive spec.</li>
	 * <li>resolve all ./, .\, ../ and ..\ sequences.</li>
	 * <li>DOS style paths that start with a drive letter will have \ as the
	 * separator.</li>
	 * </ul>
	 * Unlike <code>File#getCanonicalPath()</code> it specifically doesn't
	 * resolve symbolic links.
	 * 
	 * @param path
	 *            the path to be normalized
	 * @return the normalized version of the path.
	 * 
	 * @throws java.lang.NullPointerException
	 *             if the file path is equal to null.
	 */
	private static File normalize(String path) throws IOException {
		String orig = path;

		path = path.replace('/', File.separatorChar).replace('\\',
				File.separatorChar);

		// make sure we are dealing with an absolute path
		int colon = path.indexOf(":");

		if (!ON_NETWARE) {
			if (!path.startsWith(File.separator)
					&& !(path.length() >= 2
							&& Character.isLetter(path.charAt(0)) && colon == 1)) {
				String msg = path + " is not an absolute path";
				throw new IOException(msg);
			}
		} else {
			if (!path.startsWith(File.separator) && (colon == -1)) {
				String msg = path + " is not an absolute path";
				throw new IOException(msg);
			}
		}

		boolean dosWithDrive = false;
		String root = null;

		// Eliminate consecutive slashes after the drive spec
		if ((!ON_NETWARE && path.length() >= 2
				&& Character.isLetter(path.charAt(0)) && path.charAt(1) == ':')
				|| (ON_NETWARE && colon > -1)) {

			dosWithDrive = true;

			char[] ca = path.replace('/', '\\').toCharArray();
			StringBuffer sbRoot = new StringBuffer();
			for (int i = 0; i < colon; i++) {
				sbRoot.append(Character.toUpperCase(ca[i]));
			}
			sbRoot.append(':');
			if (colon + 1 < path.length()) {
				sbRoot.append(File.separatorChar);
			}
			root = sbRoot.toString();

			// Eliminate consecutive slashes after the drive spec
			StringBuffer sbPath = new StringBuffer();
			for (int i = colon + 1; i < ca.length; i++) {
				if ((ca[i] != '\\') || (ca[i] == '\\' && ca[i - 1] != '\\')) {
					sbPath.append(ca[i]);
				}
			}
			path = sbPath.toString().replace('\\', File.separatorChar);

		} else {
			if (path.length() == 1) {
				root = File.separator;
				path = "";
			} else if (path.charAt(1) == File.separatorChar) {
				// UNC drive
				root = File.separator + File.separator;
				path = path.substring(2);
			} else {
				root = File.separator;
				path = path.substring(1);
			}
		}

		Stack s = new Stack();
		s.push(root);
		StringTokenizer tok = new StringTokenizer(path, File.separator);
		while (tok.hasMoreTokens()) {
			String thisToken = tok.nextToken();
			if (".".equals(thisToken)) {
				continue;
			} else if ("..".equals(thisToken)) {
				if (s.size() < 2) {
					throw new IOException("Cannot resolve path " + orig);
				} else {
					s.pop();
				}
			} else { // plain component
				s.push(thisToken);
			}
		}

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.size(); i++) {
			if (i > 1) {
				// not before the filesystem root and not after it, since root
				// already contains one
				sb.append(File.separatorChar);
			}
			sb.append(s.elementAt(i));
		}

		path = sb.toString();
		if (dosWithDrive) {
			path = path.replace('/', '\\');
		}
		return new File(path);
	}

}
