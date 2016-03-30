package com.ensharable.multithreading;


/**
 * Two threads cause deadlock because they get one resource and wait for another resource
 * 
 * Thread 1 get resource (lock1)
 * Thread 2 get resource (lock2)
 * Thread 1 wait for resource (lock2) which Thread2 is holding
 * Thread 2 wait for resource (lock1) which Thread1 is holding
 * Deadlock happen
 * 
 * To avoid deadlock, thread get the resource at the same sequence lock1 then lock2
 * 
 * @author yuehu_ou
 *
 */
public class DeadLockSample {

	public static void main(String[] args) {
		Object lock1 = new Object();
		Object lock2 = new Object();

		DeadLockSample localObject = new DeadLockSample();

		Thread t1 = new Thread(localObject.new ThreadOne(lock1, lock2),
				"Thread One");
		Thread t2 = new Thread(localObject.new ThreadTwo(lock1, lock2),
				"Thread Two");

		// use start(), don't use run()
		t1.start();
		t2.start();
	}

	
	class ThreadOne implements Runnable {
		private Object lock1;
		private Object lock2;

		public ThreadOne(Object lock1, Object lock2) {
			this.lock1 = lock1;
			this.lock2 = lock2;
		}

		@Override
		public void run() {
			synchronized (lock1) {							//the sequence is get lock1 then lock2, different than ThreadTwo
				SimpleThreads.threadMessage("Got the Lock1");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock2) {
					SimpleThreads.threadMessage("Got the Lock1 and Lock2");
				}
			}
		}
	}

	class ThreadTwo implements Runnable {
		private Object lock1;
		private Object lock2;

		public ThreadTwo(Object lock1, Object lock2) {
			this.lock1 = lock1;
			this.lock2 = lock2;
		}

		@Override
		public void run() {
			synchronized (lock2) {					//the sequence is get lock2 then lock1, different than ThreadOne
				SimpleThreads.threadMessage("Got the Lock2");
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				synchronized (lock1) {
					SimpleThreads.threadMessage("Got the Lock2 and Lock1");
				}

			}
		}
	}
}
