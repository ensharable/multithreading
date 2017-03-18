package com.ensharable.multithreading.server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


/*
 * Start the program and click: http://localhost:8008
 * 
 */
public class SingleThreadServer implements Runnable {
	private int portNum = 8080;
	private ServerSocket serverSocket = null;
	private boolean isStop = false;
	private Thread serverThread = null;

	public SingleThreadServer(int portNum) {
		this.portNum = portNum;
	}
	

	public void run(){
		this.serverThread = Thread.currentThread();
		
		//open socket
		try {
            this.serverSocket = new ServerSocket(this.portNum);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port "+portNum, e);
        }
		
		System.out.println("Server starts");
		while(!isStop()){
			
			Socket clientSocket = null;
			try{
				clientSocket = this.serverSocket.accept();
			}catch (IOException e){
				if(isStop()){
					System.out.println("Server is already stopped.");
					return;
				}
				throw new RuntimeException("Exception Error to accept client connection" ,e);
			}
			try{
				processClientRequest(clientSocket);
			}catch (IOException e){
				System.out.println("Error when proccess cient request");
			}
		}
		System.out.println("Server stoppped");
	}
	
	private void processClientRequest(Socket clientSocket) throws IOException {
		InputStream input = clientSocket.getInputStream();
		OutputStream output = clientSocket.getOutputStream();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		System.out.println(dateFormat.format(date)); //2016/11/16 12:08:43
		
		String respond="HTTP/1.1 200 OK\n\n<html><body>" +
                "My Singlethreaded Server: " +
                dateFormat.format(date) +
                "</body></html>";
		output.write(respond.getBytes());
		output.close();
		input.close();
		System.out.println("Request processed: " + dateFormat.format(date));
	}

	private synchronized boolean isStop() {
		return this.isStop;
	}

	private synchronized void stop() {
		this.isStop = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}
	
	public static void main(String[] args){
		SingleThreadServer ss = new SingleThreadServer(8080);
		Thread myServerThread = new Thread(ss);
		myServerThread.start();
		
		try{
			Thread.sleep(15*1000); //10 second
		}catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("To stope server now");
		ss.stop();
	}
}
