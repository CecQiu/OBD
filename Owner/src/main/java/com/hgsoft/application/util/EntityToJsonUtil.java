package com.hgsoft.application.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import net.sf.json.JSONArray;

public class EntityToJsonUtil {
	private EntityToJsonUtil() {}
	
	@SuppressWarnings("rawtypes")
	public static String toJson(Object entity) {
		StringBuffer sb = new StringBuffer("{");
		Field[] fields = entity.getClass().getDeclaredFields();
		for(Field field: fields) {
			field.setAccessible(true);
			String fieldName = field.getName();
			if(fieldName == null || fieldName.equals("serialVersionUID")) {
				continue;
			}
			char[] ch = fieldName.toCharArray();
			ch[0] = Character.toUpperCase(ch[0]);
			String s = new String(ch);
			Class typeName = field.getType();
			Method driveMtd;
			try {
				driveMtd = entity.getClass().getDeclaredMethod("get" + s);
				Object object = driveMtd.invoke(entity);
				sb.append("\"");
				sb.append(fieldName);
				sb.append("\":");
				if(object == null) {
					if(Collection.class.isAssignableFrom(typeName)) {
						sb.append("[]");
					} else if(Number.class.isAssignableFrom(typeName)) {
					} else if(String.class.isAssignableFrom(typeName)) {
						sb.append("\"\"");
					} else {
						sb.append("{}");
					}
				} else if(typeName == Date.class) {
					sb.append("\"");
					SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					sb.append(sf.format((Date)object));
					sb.append("\"");
				} else if(object instanceof Collection) {
					JSONArray array = new JSONArray();
					array.addAll((Collection) driveMtd.invoke(entity));
					sb.append(array.toString());
				} else if(object instanceof Number) {
					sb.append(driveMtd.invoke(entity));
				} else if(object instanceof String) {
					sb.append("\"");
					sb.append(driveMtd.invoke(entity));
					sb.append("\"");
				} else {
					sb.append(toJson(object));
				}
				sb.append(",");
			} catch (NoSuchMethodException e) {
				System.err.println(e.getMessage());
			} catch (IllegalArgumentException e) {
				System.err.println(e.getMessage());
			} catch (IllegalAccessException e) {
				System.err.println(e.getMessage());
			} catch (InvocationTargetException e) {
				System.err.println(e.getMessage());
			}
		}
		if(sb.length() == 1) {
			sb.append("}");
		} else {
			sb.replace(sb.length()-1, sb.length(), "}");
		}
		return sb.toString();
	}
}
