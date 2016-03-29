package com.ensharable.multithreading;

public class WaitAndNotify {

	public static void main(String[] args) {
		WaitAndNotify localObject = new WaitAndNotify();
		ShareData data = localObject.new ShareData();
		Thread t1 = new Thread(localObject.new WaitThread(data), "Thread Wait");
		Thread t2 = new Thread(localObject.new NotifyThread(data), "Thread Notify");

		// use start(), don't use run()
		t1.start();

		try {
			Thread.currentThread().sleep(1000); //make sure the t1 thread call wait first, so sleep
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//after above sleep, t2 notify t1 to wake up
		t2.start();
	}

	/*
	 * The share data object act like connector between two thread
	 */
	class ShareData {
	
	}

	class WaitThread implements Runnable {
		private ShareData data;

		public WaitThread(ShareData data) {
			this.data = data;
		}

		@Override
		public void run() {
			SimpleThreads.threadMessage("Now Enter Wait");
			synchronized (data) {
				try {
					data.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			SimpleThreads.threadMessage("Exit Wait");
		}
	}

	class NotifyThread implements Runnable {
		private ShareData data;

		public NotifyThread(ShareData data) {
			this.data = data;
		}

		@Override
		public void run() {
			synchronized (data) {
				data.notify();
			}
			SimpleThreads.threadMessage("Notified other thread");
		}
	}
}
