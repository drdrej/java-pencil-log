/*
 * Copyright 2004 Andreas Siebert (j2biz community)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 * http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.j2biz.pencil.test.additional;

/**
 * This class is specialy constructod for unit-tests.
 */
public class TestLog {

	public static final String SCOPE_CLINIT = ".<clinit>() = ";

	public static final String SCOPE_CLINIT_PRIVATE = ".<clinit>().private = ";

	public static final String SCOPE_CLINIT_PROTECTED = ".<clinit>().protected = ";

	public static final String SCOPE_CLINIT_PUBLIC = ".<clinit>().public = ";

	public static final String SCOPE_CLINIT_LOCAL = ".<clinit>().<local> = ";

	public static final String SCOPE_CLINIT_LOCAL_PRIVATE = ".<clinit>().<local>.private = ";

	public static final String SCOPE_CLINIT_LOCAL_PROTECTED = ".<clinit>().<local>.protected = ";

	public static final String SCOPE_CLINIT_LOCAL_PUBLIC = ".<clinit>().<local>.public = ";

	public static final String SCOPE_INIT = ".<init>() = ";

	public static final String SCOPE_INIT_PRIVATE = ".<init>().private = ";

	public static final String SCOPE_INIT_PROTECTED = ".<init>().protected = ";

	public static final String SCOPE_INIT_PUBLIC = ".<init>().public = ";

	public static final String SCOPE_INIT_LOCAL = ".<init>().<local> = ";

	public static final String SCOPE_INIT_LOCAL_PRIVATE = ".<init>().<local>.private = ";

	public static final String SCOPE_INIT_LOCAL_PROTECTED = ".<init>().<local>.protected = ";

	public static final String SCOPE_INIT_LOCAL_PUBLIC = ".<init>().<local>.public = ";

	public static final String SCOPE_MAIN = ".main() = ";

	public static final String SCOPE_MAIN_PRIVATE = ".main().private = ";

	public static final String SCOPE_MAIN_PROTECTED = ".main().protected = ";

	public static final String SCOPE_MAIN_PUBLIC = ".main().public = ";

	public static final String SCOPE_MAIN_LOCAL = ".main().<local> = ";

	public static final String SCOPE_MAIN_LOCAL_PRIVATE = ".main().<local>.private = ";

	public static final String SCOPE_MAIN_LOCAL_PROTECTED = ".main().<local>.protected = ";

	public static final String SCOPE_MAIN_LOCAL_PUBLIC = ".main().<local>.public = ";

	public static final String SCOPE_NONSTATIC = ".nonStatic() = ";

	public static final String SCOPE_NONSTATIC_PRIVATE = ".nonStatic().private = ";

	public static final String SCOPE_NONSTATIC_PROTECTED = ".nonStatic().protected = ";

	public static final String SCOPE_NONSTATIC_PUBLIC = ".nonStatic().public = ";

	public static final String SCOPE_NONSTATIC_LOCAL = ".nonStatic().<local> = ";

	public static final String SCOPE_NONSTATIC_LOCAL_PRIVATE = ".nonStatic().<local>.private = ";

	public static final String SCOPE_NONSTATIC_LOCAL_PROTECTED = ".nonStatic().<local>.protected = ";

	public static final String SCOPE_NONSTATIC_LOCAL_PUBLIC = ".nonStatic().<local>.public = ";

	public static final String SCOPE_STATIC = ".static() = ";

	public static final String SCOPE_STATIC_PRIVATE = ".static().private = ";

	public static final String SCOPE_STATIC_PROTECTED = ".static().protected = ";

	public static final String SCOPE_STATIC_PUBLIC = ".static().public = ";

	public static final String SCOPE_STATIC_LOCAL = ".static().<local> = ";

	public static final String SCOPE_STATIC_LOCAL_PRIVATE = ".static().<local>.private = ";

	public static final String SCOPE_STATIC_LOCAL_PROTECTED = ".static().<local>.protected = ";

	public static final String SCOPE_STATIC_LOCAL_PUBLIC = ".static().<local>.public = ";

	public static final void debug(final String msg) {
		System.err.println(msg);
	}

	public static final String getKey(Class clazz, String scope) {
		final Class encClass = clazz.getEnclosingClass();
		return getKey(encClass, clazz, scope);
	}

	public static final String getKey(Class encClass, Class clazz, String scope) {
		final String name = filterClassName(clazz);

		if (encClass != null) {
			return filterPackage(encClass) + "." + filterClassName(encClass)
					+ "." + name + scope;
		} else {
			return filterPackage(clazz) + "." + name + scope;
		}
	}

	private static String filterPackage(Class clazz) {
		final String pckName = clazz.getPackage().getName();
		final String prefixName = TestLog.class.getPackage().getName();
		final String pckSuffix = pckName.substring(prefixName.length()+1);
		return pckSuffix.replace(".impl", "");
	}

	public static final String getKey(Class encClass, String prefix,
			Class clazz, String scope) {
		final String name = filterClassName(clazz);

		if (encClass != null) {
			return filterPackage(encClass) + "." + filterClassName(encClass) + prefix + "." + name + scope;
		} else {
			return filterPackage(clazz) + "." + prefix + name + scope;
		}
	}

	/**
	 * @param clazz
	 * @return
	 */
	private static String filterClassName(Class clazz) {
		final String clName = clazz.getName();

		int pos = clName.lastIndexOf('$');
		if (pos < 0) {
			pos = clName.lastIndexOf('.');
			if (pos < 0) {
				return clName;
			} else {
				return clName.substring(pos + 1);
			}
		} else {
			return clName.substring(pos + 1);
		}
	}

	public String getPropertyKey() {
		return null;
	}
}
