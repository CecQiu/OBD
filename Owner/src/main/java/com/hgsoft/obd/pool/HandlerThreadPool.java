package com.hgsoft.obd.pool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hgsoft.common.utils.PropertiesUtil;
/**
 * 处理线程池
 * @author sujunguang
 * 2016年8月1日
 * 下午4:16:34
 */
public class HandlerThreadPool {
	private final static String nCoreStr = PropertiesUtil.getInstance("params.properties").readProperty("nCore", "2");
	private final static String firstRateStr = PropertiesUtil.getInstance("params.properties").readProperty("firstRate", "0.2");

	private final static int core = Runtime.getRuntime().availableProcessors();//CPU 个数
	private final static int  nCore = Integer.valueOf(nCoreStr);//N倍 CPU个数
	private final static double firstRate = Double.valueOf(firstRateStr);//优先级高的比率
	/**
	 * 优先级高
	 */
	public final static ExecutorService FirstHandlerPool = Executors.newFixedThreadPool((int)Math.round(core*nCore*firstRate)); 
	/**
	 * 优先级稍低
	 */
	public final static ExecutorService SecondHandlerPool = Executors.newFixedThreadPool((int)Math.round(core*nCore*(1-firstRate))); 
	
	/**
	 * 升级线程池
	 */
	public final static ExecutorService UpgradeHandlerPool = Executors.newFixedThreadPool(8); 
	/**
	 * OBD设备状态线程池
	 */
	public final static ExecutorService ObdLinePool = Executors.newFixedThreadPool(4); 
	/**
	 * OBD疲劳驾驶线程池
	 */
	public final static ExecutorService ObdTiredDrivePool = Executors.newFixedThreadPool(4); 
	

}
