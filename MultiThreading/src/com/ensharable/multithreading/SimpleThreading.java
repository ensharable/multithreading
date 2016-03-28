package com.ensharable.multithreading;

/*
 * Which of these idioms should you use? The first idiom, which employs a Runnable object, 
 * is more general, because the Runnable object can subclass a class other than Thread. 
 * The second idiom is easier to use in simple applications, but is limited by the fact 
 * that your task class must be a descendant of Thread. This lesson focuses on the first approach, 
 * which separates the Runnable task from the Thread object that executes the task. 
 * Not only is this approach more flexible, but it is applicable to the high-level thread management APIs covered later.
 * The Thread class defines a number of methods useful for thread management. 
 * These include static methods, which provide information about, or affect the status of, 
 * the thread invoking the method. The other methods are invoked from other threads involved 
 * in managing the thread and Thread object. We'll examine some of these methods in the following sections
 */
public class SimpleThreading {

	public static void main(String[] args) {
		MyLocalThread t1 = new MyLocalThread(1);
		MyLocalThread t2 = new MyLocalThread(2);
		Thread t3 = new Thread(new MyThreadWithRunnable(3));
		Thread t4 = new Thread(new MyThreadWithRunnable(4));

		t1.start();
		t2.start();
		t3.start();
		t4.start();
	}
}

/**
 * 
 * @author Marcus create class extends Thread class
 */
class MyLocalThread extends Thread {
	private int id = 0;

	MyLocalThread(int id) {
		this.id = id;
	}

	public void run() {
		System.out.println("Thread Id:" + id);
	}
}

/**
 * This is a better way to do for threading
 * 
 * @author Marcus
 *
 */
class MyThreadWithRunnable implements Runnable {
	private int id = 0;

	public MyThreadWithRunnable(int id) {
		this.id = id;
	}

	@Override
	public void run() {
		System.out.println("Thread Id:" + id);
	}
}
