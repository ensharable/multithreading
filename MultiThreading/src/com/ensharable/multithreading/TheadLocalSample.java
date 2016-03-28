package com.ensharable.multithreading;

public class TheadLocalSample {
	public static void main(String[] args) {
		TheadLocalSample localObject = new TheadLocalSample();
		ShareData data = localObject.new ShareData();
		Thread t1 = new Thread(localObject.new MyThreadWithRunnable(data), "Thread One");
		Thread t2 = new Thread(localObject.new MyThreadWithRunnable(data), "Thread Two");

		// use start(), don't use run()
		t1.start();
		t2.start();

		System.out.println("Main: " + Thread.currentThread().getName());
	}

	/*
	 * 
	 */
	class ShareData {
		//ThreadLocal is a container to hold value for each thread, so each thread has its own copy of the value
		//TheadLocal is week map to hold value. thread to value mapping.
		//It should clear the memory after use
		private ThreadLocal<Integer> threadLocal =
	               new ThreadLocal<Integer>();

		public void initCounter() {
			int val=(int)(Math.random()*100D);
			threadLocal.set(val);      //put the value into threadlocal
			System.out.println("Name:" + Thread.currentThread().getName() + " Current counter:"+threadLocal.get());
		}
		
		public int getCounter() {
			return threadLocal.get();
		}
		
		public void setCounter(int counter) {
			synchronized (this) {				//synchronized block
				threadLocal.get();
			}
		}

		public synchronized void increaseCounter() {  //synchronized method to avoid race condition
			threadLocal.set(threadLocal.get()+1);
		}

	}

	class MyThreadWithRunnable implements Runnable {
		private ShareData data;

		public MyThreadWithRunnable(ShareData data) {
			this.data = data;
		}

		@Override
		public void run() {
			data.initCounter();
			data.increaseCounter();
			System.out.println("Name:" + Thread.currentThread().getName()+" :"+data.getCounter());
		}
	}
}
