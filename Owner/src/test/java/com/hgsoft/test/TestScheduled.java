package com.hgsoft.test;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestScheduled {
	public static void main(String[] args) throws InterruptedException {

		/**
		 * 延迟多久执行一次
		 */
		/*ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture scheduledFuture = service.schedule(new Runnable() {
			@Override
			public void run() {
				String tip = String.format(new Date()+":定时");
		        System.out.println(tip);
			}
		}, 5000, TimeUnit.MILLISECONDS);
		
		Thread.sleep(20000);*/
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		ScheduledFuture scheduledFuture = service.scheduleWithFixedDelay(new Runnable() {
			@Override
			public void run() {
				String tip = String.format(new Date()+":定时");
		        System.out.println(tip);
			}
		}, 5000, 5000, TimeUnit.MILLISECONDS);
		
		Thread.sleep(20000);
		scheduledFuture.cancel(true);
	}
}
