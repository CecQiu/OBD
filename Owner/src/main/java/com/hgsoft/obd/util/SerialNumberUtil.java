/**
 * 
 */
package com.hgsoft.obd.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 流水号生成工具
 * 
 * @author sujunguang 2015年12月15日 上午11:33:43
 */
public class SerialNumberUtil {
	//流水号是1个byte：8bit=0~255
	private static Map<String, Integer> serialNumMap = new ConcurrentHashMap<String, Integer>();

	// 加锁，防止同步访问问题
	public synchronized static Integer getSerialnumber(String obdSn) {
		int serialNum = 0;
		if (serialNumMap.containsKey(obdSn)) {
			serialNum = serialNumMap.get(obdSn);
			if (serialNum == 255)
				serialNum = 0;
			serialNum++;
		}
		serialNumMap.put(obdSn, serialNum);
		return serialNum;
	}

	public static void main(String[] args) {
		final CountDownLatch countDownLatch = new CountDownLatch(5);
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++)
					System.out.println("111:"
							+ SerialNumberUtil.getSerialnumber("111"));
				countDownLatch.countDown();
			}
		};
		new Thread(r1).start();
		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++)
					System.out.println("111:"
							+ SerialNumberUtil.getSerialnumber("111"));
				countDownLatch.countDown();
			}
		};
		new Thread(r2).start();
		Runnable r3 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++)
					System.out.println("111:"
							+ SerialNumberUtil.getSerialnumber("111"));
				countDownLatch.countDown();
			}
		};
		new Thread(r3).start();
		Runnable r4 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++)
					System.out.println("444:"
							+ SerialNumberUtil.getSerialnumber("444"));
				countDownLatch.countDown();
			}
		};
		new Thread(r4).start();
		Runnable r5 = new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 20; i++)
					System.out.println("555:"
							+ SerialNumberUtil.getSerialnumber("555"));
				countDownLatch.countDown();
			}
		};
		new Thread(r5).start();

		try {
			countDownLatch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("finished...");
	}
}
