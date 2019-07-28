package it.fmoon.fxapp.support;

import java.util.Arrays;
import java.util.List;

public class PropertyUtils {

	@SuppressWarnings("unchecked")
	public static <T> T getProperty(Object object, Object... path) {
		if (path.length==0) {
			return (T)object;
		}
		Object nextObj = getSingleProperty(object, path[0]);
		if (path.length==1) {
			return (T)nextObj;
		}
		return getProperty(nextObj, Arrays.copyOfRange(path, 1, path.length));
	}

	private static Object getSingleProperty(Object object, Object property) {
		if (object instanceof Object[]) {
			return ((Object[])object)[asInt(property)];
		}
		if (object instanceof List<?>) {
			return ((List<?>)object).get(asInt(property));
		}
		try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(object,String.valueOf(property));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private static int asInt(Object property) {
		if (property instanceof Number) {
			return ((Number)property).intValue();
		}
		if (property instanceof String) {
			return Integer.parseInt((String)property);
		}
		throw new IllegalArgumentException();
	}
	
	
	
}
