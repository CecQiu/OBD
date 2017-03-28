package com.hgsoft.common.utils;

import org.apache.commons.logging.Log;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressWarnings("rawtypes")
public abstract class LogJob {

	protected final static SimpleDateFormat dateTimeSdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public final static void writeLog(Exception e, Log logger, Class clazz) {
		StringWriter sw = null;
		PrintWriter pw = null;
		try {
			sw = new StringWriter();
			pw = new PrintWriter(sw, true);
			e.printStackTrace(pw);
			final String str = sw.toString();
			// logger.error("["+clazz.getName()+","+dateTimeSdf.format(new
			// Date())+"]: "+str);
			logger.error("[" + clazz.getName() + ","
					+ dateTimeSdf.format(new Date()) + "]: " + str);
		} finally {
			try {
				if (sw != null) {
					sw.close();
					sw = null;
				}
				if (pw != null) {
					pw.close();
					pw = null;
				}
			} catch (IOException io) {
				io.printStackTrace();
			}
		}
	}

}
