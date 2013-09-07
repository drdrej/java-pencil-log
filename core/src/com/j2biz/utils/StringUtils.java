package com.j2biz.utils;

public class StringUtils {

	public static String getClassName(final Object object) {
		return object == null ? null : object.getClass().getName();
	}
}
