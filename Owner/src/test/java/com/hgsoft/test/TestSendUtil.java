package com.hgsoft.test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.hgsoft.obd.server.OBDException;
import com.hgsoft.obd.util.SendUtil;

public class TestSendUtil {

	public static void main(String[] args) {
		final Set<String> set = new HashSet<String>();
		final CountDownLatch countDownLatch = new CountDownLatch(200);
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				try {
//					new SendUtil().msgSend("1123", 11, "00");
					SendUtil sendUtil = SendUtil.getInstance();
					System.out.println(sendUtil);
					set.add(sendUtil.toString());
					sendUtil.msgSend("1123", 11, "00");
//					countDownLatch.countDown();
				} catch (OBDException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				try {
//					new SendUtil().msgSend("4456", 22, "cc");
					SendUtil sendUtil = SendUtil.getInstance();
					System.out.println(sendUtil);
					set.add(sendUtil.toString());
					sendUtil.msgSendGetResult("4456", 22, "cc", 3.0);
//					countDownLatch.countDown();
				} catch (OBDException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable r3 = new Runnable() {
			@Override
			public void run() {
				try {
//					new SendUtil().msgSend("7789", 33, "dd");
					SendUtil sendUtil = SendUtil.getInstance();
					System.out.println(sendUtil);
					set.add(sendUtil.toString());
					sendUtil.msgSend("7789", 33, "dd");
//					countDownLatch.countDown();
				} catch (OBDException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable r4 = new Runnable() {
			@Override
			public void run() {
				try {
					SendUtil sendUtil = SendUtil.getInstance();
					System.out.println(sendUtil);
					set.add(sendUtil.toString());
					sendUtil.msgSendGetResult("bb12", 33, "dd", 2.0);
//					countDownLatch.countDown();
				} catch (OBDException e) {
					e.printStackTrace();
				}
			}
		};
		Runnable r5 = new Runnable() {
			@Override
			public void run() {
				try {
					SendUtil sendUtil = SendUtil.getInstance();
					System.out.println(sendUtil);
					set.add(sendUtil.toString());
					sendUtil.msgSend("cc13", 33, "dd");
//					countDownLatch.countDown();
				} catch (OBDException e) {
					e.printStackTrace();
				}
			}
		};
		
		ExecutorService executor = Executors.newFixedThreadPool(50); 
		for (int i = 0; i < 1; i++) {
			executor.submit(r1);
			executor.submit(r2);
			executor.submit(r3);
			executor.submit(r4);
			executor.submit(r5);
	
//		try {
//			countDownLatch.await();
//			countDownLatch.await();
//			countDownLatch.await();
//			countDownLatch.await();
//			countDownLatch.await();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		}

		executor.shutdown();
		System.out.println("set:"+set);
	}
}
