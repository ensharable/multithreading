package com.ensharable.multithreading;

/**
 * Java Memory: CPU1 -> Cache1 -> Memory CPU2 -> Cache2 -> Same Memory as above
 * 
 * Without volatile keyword, the thread in CPU may only read/write the copy of
 * the data in cache. This may cause two threads reading the expired data
 * 
 * With volatile keyword, the thread in CPU will read/write will read/write and
 * data in cache and syn to memory each time. This may not enough for solve race
 * condition. It still need synchronized.
 * 
 * @author yuehu_ou
 *
 */
public class VolatileSample {
	public static void main(String[] args) {
		VolatileSample vs = new VolatileSample();
		ShareData data = vs.new ShareData();
		Thread t1 = new Thread(vs.new MyThreadWithRunnable(data), "Thread One");
		Thread t2 = new Thread(vs.new MyThreadWithRunnable(data), "Thread Two");

		// use start(), don't use run()
		t1.start();
		t2.start();

		System.out.println("Main: " + Thread.currentThread().getName());
	}

	/*
	 * volatile keyword is to make sure the thread will read and write data from
	 * main memory
	 */
	class ShareData {
		private volatile int counter = 0; //use volatile keyword, it is not enough for race condition

		public int getCounter() {
			return counter;
		}
		
		
		public void setCounter(int counter) {
			synchronized (this) {				//synchronized block
				this.counter =  counter;
			}
		}

		public synchronized void increaseCounter() {  //synchronized method to avoid race condition
			this.counter++;
			System.out.println("Current counter:"+counter);
		}

	}

	class MyThreadWithRunnable implements Runnable {
		private ShareData data;

		public MyThreadWithRunnable(ShareData data) {
			this.data = data;
		}

		@Override
		public void run() {
			data.increaseCounter();
			System.out.println("Name:" + Thread.currentThread().getName()+" :"+data.getCounter());
		}
	}
}