package com.hgsoft.obd.util;

import java.text.SimpleDateFormat;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

public class JsonDateValueProcessor implements JsonValueProcessor {

	private String pattern = "yyyy-MM-dd HH:mm:ss";

	public JsonDateValueProcessor() {
	}

	public JsonDateValueProcessor(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig config) {
		return null;
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig config) {
		if (value == null)
			return "";
		if (value instanceof java.util.Date) {
			String str = new SimpleDateFormat(pattern).format((java.util.Date) value);
			return str;
		}
		return value.toString();
	}

}
