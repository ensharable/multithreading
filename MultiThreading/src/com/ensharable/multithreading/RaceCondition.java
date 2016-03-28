package com.ensharable.multithreading;

public class RaceCondition {
	public static void main(String[] args){
		SharedData sd = new SharedData();
		Thread t1 = new Thread(new MyThread(sd), "Thread One");
		Thread t2 = new Thread(new MyThread(sd), "Thread Two");
		
		//race condition happend. The value sometime is not increase correctly
		t1.start();
		t2.start();
	}
}

class SharedData{
	private int counter=0;
	
	public void increse(){
		counter++;
	}
	
	public int getCounter(){
		return counter;
	}
}

class MyThread implements Runnable {
	private SharedData sd;

	public MyThread(SharedData sd) {
		this.sd=sd;
	}

	@Override
	public void run() {
		this.sd.increse();
		System.out.println("Name:"+Thread.currentThread().getName() + " :"+sd.getCounter());
	}
}