package com.ensharable.multithreading;

import java.util.ArrayDeque;

public class JobQueue {
	
	public static void main(String[] args){
		JobQueue localObj=new JobQueue();
		
		ArrayDeque<Job> queue = new ArrayDeque<>();
		Thread jobThread=new Thread(localObj.new JobDistributor(queue), "Job Distributor Thread");
		jobThread.start();
		
		int count=0;
		synchronized (queue) {  //main thread get the queue (resource)
			while(count<10){
				queue.offer(localObj.new Job(count));
				queue.notify();		//notify job distributer to assign jobs
				SimpleThreads.threadMessage("Added Job - "+count);
				count++;
				try {
					queue.wait(1000000);
					//Thread.sleep(1000);   //can't use it, because it doesn't release the lock
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class JobDistributor implements Runnable{
		ArrayDeque<Job> queue;
		
		public JobDistributor(ArrayDeque<Job> queue){
			this.queue=queue;
		}
		
		@Override
		public void run(){
			while(true){
				//always looks for job
				synchronized (queue) {
					while(queue.isEmpty()){
						try {
							queue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Job j=queue.poll();
					Thread worker = new Thread(new JobWorker(j));
					worker.start();
					SimpleThreads.threadMessage("Assigned Job - " + j.getValue());
				}
			}
		}
	}
	
	class JobWorker implements Runnable{
		Job j;
		public JobWorker(Job j){
			this.j=j;
		}
		@Override
		public void run(){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			SimpleThreads.threadMessage("Proccess Job - " +j.getValue());
			
		}
	}
	
	
	class Job {
		private int value;
		
		public int getValue() {
			return value;
		}

		public void setValue(int value) {
			this.value = value;
		}

		public Job(int value){
			this.value=value;
		}
	}
}
