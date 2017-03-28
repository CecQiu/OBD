package com.hgsoft.test;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class SendUtil {
	private static SendUtil sendUtil;
	static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>();
	public static SendUtil getInstance(){
		threadLocal.set(0);
		if(sendUtil == null){
			sendUtil = new SendUtil();
		}
		return sendUtil;
	}
	private AtomicInteger count = new AtomicInteger(0);//计数
	public int nn = 3;
	public String doWork(String str){
		//...
		double mostWait = 5.0;
		double mostWaitCount = (mostWait-1)/nn;
		try {
			long n = (long) (mostWaitCount*1000 + threadLocal.get()*1000/3);
//			System.out.println(mostWaitCount*1000);
//			System.out.println(threadLocal.get()*1000/12);
			System.out.println(Thread.currentThread().getName()+" "+threadLocal.get()+" sleep:"+n);
			Thread.sleep(n);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "";
	}
	public void send(String str){
		String str1 = doWork(str);
		threadLocal.set(threadLocal.get()+1);
		System.out.println(Thread.currentThread().getName()+" count:"+threadLocal.get());
		if((str1 == null || str1 == "") && threadLocal.get() < nn){
			send(str);
		}
	}
}