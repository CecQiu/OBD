package com.hgsoft.common.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ThreadLocalDateUtil {
	 /** 锁对象 */
    private static final Object lockObj = new Object();
    /** 存放不同的日期模板格式的sdf的Map */
    private static Map<String, ThreadLocal<SimpleDateFormat>> sdfMap = new HashMap<String, ThreadLocal<SimpleDateFormat>>();
    /**
     * 返回一个ThreadLocal的sdf,每个线程只会new一次sdf
     * 
     * @param pattern
     * @return
     */
    private static SimpleDateFormat getSdf(final String pattern) {
        ThreadLocal<SimpleDateFormat> tl = sdfMap.get(pattern);

        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (tl == null) {
            synchronized (lockObj) {
                tl = sdfMap.get(pattern);
                if (tl == null) {
                    // 只有Map中还没有这个pattern的sdf才会生成新的sdf并放入map
//                    System.out.println("put new sdf of pattern " + pattern + " to map");

                    // 这里是关键,使用ThreadLocal<SimpleDateFormat>替代原来直接new SimpleDateFormat
                    tl = new ThreadLocal<SimpleDateFormat>() {

                        @Override
                        protected SimpleDateFormat initialValue() {
//                            System.out.println("thread: " + Thread.currentThread() + " init pattern: " + pattern);
                            return new SimpleDateFormat(pattern);
                        }
                    };
                    sdfMap.put(pattern, tl);
                }
            }
        }

        return tl.get();
    }
    
	private static ThreadLocal<DateFormat> threadLocal = new ThreadLocal<DateFormat>();
	
	public static DateFormat getDateFormat(String pattern){
		DateFormat df = threadLocal.get();
		if(df == null){
			df = new SimpleDateFormat(pattern);
			threadLocal.set(df);
		}
		return df;
	}
	
	public static Date parse(String pattern,String strDate) throws ParseException{
//		return getDateFormat(pattern).parse(strDate);
        return getSdf(pattern).parse(strDate);
	}
	
	public static String formatDate(String pattern,Date date){
//		return getDateFormat(pattern).format(date);
		 return getSdf(pattern).format(date);
	}
	
	public static void remove(){
		threadLocal.remove();
	}

    public static void main(String[] args) {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("r1:"+ThreadLocalDateUtil.formatDate("yyyy-MM-dd",new Date()));
			}
		};
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				System.out.println("r2:"+ThreadLocalDateUtil.formatDate("yyyy-MM-dd HH:mm:ss",new Date()));
			}
		};
		
		for (int i = 0; i < 100; i++) {
			new Thread(r1).start();
			new Thread(r2).start();
			new Thread(r1).start();
		}
	}
}

class DateUtil1 {
    
    private static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static  String formatDate(Date date)throws ParseException{
        return sdf.format(date);
    }
    
    public static Date parse(String strDate) throws ParseException{

        return sdf.parse(strDate);
    }
    
}
