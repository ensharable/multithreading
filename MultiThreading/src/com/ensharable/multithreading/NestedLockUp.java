package com.ensharable.multithreading;

/**
 * Two threads cause deadlock because they get one resource and wait for another
 * resource
 * 
 * Thread 1 get first level lock, and get the second level lock, later it call wait(), 
 * 	and release second level lock.
 * 
 * Thread 2 try to release the second leve lock, but it can't because first level lock is hold by thread one
 *	
 * Thread 1 will never wake up 
 *
 * 
 * @author yuehu_ou
 *
 */
public class NestedLockUp {

	public static void main(String[] args) {
		NestedLockUp localObject = new NestedLockUp();
		Lock lock = localObject.new Lock();

		Thread t1 = new Thread(localObject.new ThreadOne(lock), "Thread One");
		Thread t2 = new Thread(localObject.new ThreadTwo(lock), "Thread Two");

		// use start(), don't use run()
		t1.start();
		t2.start();
	}

	class Lock {
		private Object monitor = new Object();
		private boolean isLocked = false;

		public synchronized void lock() {			//first level lock
			while (isLocked) {
				synchronized (this.monitor) {		//second level lock
					try {
						this.monitor.wait();		//wait(), so release the second level lock, but first level lock didn't release 
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			isLocked = true;
		}

		public synchronized void unlock() {			//first level lock
			this.isLocked = false;
			synchronized (this.monitor) {			//second leve lock
				this.monitor.notify();
			}
		}

	}

	class ThreadOne implements Runnable {
		private Lock lock;

		public ThreadOne(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			lock.lock();
			SimpleThreads.threadMessage("Get the Lock, now sleep.");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SimpleThreads.threadMessage("Just wake up, now call unlock");
			lock.unlock();
			SimpleThreads.threadMessage("Unlocked");

		}
	}

	class ThreadTwo implements Runnable {
		private Lock lock;

		public ThreadTwo(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			SimpleThreads.threadMessage("Try to get lock");
			lock.lock();
			SimpleThreads.threadMessage("Got the lock");

		}
	}
}
