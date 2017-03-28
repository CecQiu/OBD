package com.hgsoft.test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestSendUtil2 {

	public static void main(String[] args) {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				SendUtil sendUtil = SendUtil.getInstance();
				System.out.println(sendUtil);
				long begin = System.currentTimeMillis();
				sendUtil.send("1123");
				System.out.println(Thread.currentThread().getName()+"end-begin:"+(System.currentTimeMillis()-begin));
			}
		};
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				SendUtil sendUtil = SendUtil.getInstance();
				System.out.println(sendUtil);
				long begin = System.currentTimeMillis();
				sendUtil.send("1123");
				System.out.println(Thread.currentThread().getName()+"end-begin:"+(System.currentTimeMillis()-begin));
			}
		};
		Runnable r3 = new Runnable() {
			@Override
			public void run() {
				SendUtil sendUtil = SendUtil.getInstance();
				System.out.println(sendUtil);
				long begin = System.currentTimeMillis();
				sendUtil.send("1123");
				System.out.println(Thread.currentThread().getName()+"end-begin:"+(System.currentTimeMillis()-begin));
			}
		};
		Runnable r4 = new Runnable() {
			@Override
			public void run() {
				SendUtil sendUtil = SendUtil.getInstance();
				System.out.println(sendUtil);
				long begin = System.currentTimeMillis();
				sendUtil.send("1123");
				System.out.println(Thread.currentThread().getName()+"end-begin:"+(System.currentTimeMillis()-begin));
			}
		};
		Runnable r5 = new Runnable() {
			@Override
			public void run() {
				SendUtil sendUtil = SendUtil.getInstance();
				System.out.println(sendUtil);
				long begin = System.currentTimeMillis();
				sendUtil.send("1123");
				System.out.println(Thread.currentThread().getName()+"end-begin:"+(System.currentTimeMillis()-begin));
			}
		};
		ExecutorService executor = Executors.newFixedThreadPool(50); 
		for (int i = 0; i < 2; i++) {
			executor.submit(r1);
//			executor.submit(r2);
//			executor.submit(r3);
//			executor.submit(r4);
//			executor.submit(r5);
		}
		executor.shutdown();
	}
}
