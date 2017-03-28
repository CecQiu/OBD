package com.hgsoft.common.utils;

import org.apache.struts2.util.StrutsTypeConverter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class DateConverter extends StrutsTypeConverter {

	public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	public static final DateFormat[] ACCEPT_DATE_FORMATS = {
			new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
			new SimpleDateFormat("yyyy-MM-dd HH:mm"),
			new SimpleDateFormat(DEFAULT_DATE_FORMAT),
            new SimpleDateFormat("yyyyMMdd"),
			new SimpleDateFormat("yyyy-MM") };

	@Override
	@SuppressWarnings( { "rawtypes" })
	public Object convertFromString(Map context, String[] values, Class toClass) {

		if (values.length > 0 && values[0] != null
				&& values[0].trim().length() > 0) {
			for (DateFormat format : ACCEPT_DATE_FORMATS) {
				try {
					return format.parse(values[0]);
				} catch (ParseException e) {
					continue;
				}
			}
		}
		return null;
	}

	@Override
	@SuppressWarnings( { "rawtypes" })
	public String convertToString(Map context, Object o) {

		if (o instanceof Date) {
			SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
			return sdf.format((Date) o);
		}
		return "";
	}
}
