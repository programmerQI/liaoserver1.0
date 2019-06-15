package com.liaoserver1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import com.sun.jdi.event.ThreadDeathEvent;

public class ServersocketThread extends Thread{
	
	public static final String POLLING_CODE = "*poll*";
	public static final int STATUS_LISTENNING = 301;
	public static final int STATUS_STOP = 302;
	
	private int port;
	
	private int status;
	
	private ServerSocket serverSocket;
//	private Socket userSocket;
	
//	private BufferedReader bufferedReader;
//	private InputStreamReader inputStreamReader;
	
	private LiaoServer1 server;
	
	
	public ServersocketThread(LiaoServer1 server, int port) {
		
		this.setPort(port);
		this.setStatus(STATUS_STOP);
		
		serverSocket = null;
//		userSocket = null;
		
//		bufferedReader = null;
//		inputStreamReader = null;
		
		this.server = server;
		
		System.out.println("The server socket has been initialized.");
		
	}
	
	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
//	private void quietCloseStreams() {
//		
//		if(bufferedReader != null)
//		{
//			try {
//				
//				bufferedReader.close();
//				
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//				
//			}
//		}
//		
//		if(inputStreamReader != null)
//		{
//			
//			try {
//				
//				inputStreamReader.close();
//				
//			} catch (IOException e) {
//				
//				e.printStackTrace();
//				
//			}
//			
//		}
//		
//		System.out.println("Server socket's streams have been closed.");
//
//	}
	
//	private void quietCloseUserSocket() {
//		
//		if(userSocket != null)
//		{
//			if(!userSocket.isClosed())
//			{
//				try {
//					
//					userSocket.close();
//					
//				} catch (IOException e) {
//
//					e.printStackTrace();
//					
//				}
//			}
//		}
//		
//		System.out.println("Server socket has been closed.");
//
//	}
	
	

	// Accept the incoming user
	@Override
	public void run() {
		
		try {
			
			// Establish the server socket
			serverSocket = new ServerSocket(port);
			setStatus(STATUS_LISTENNING);
			
			System.out.println("Sever socket is listenning at " + port);
			
			
			
		} catch (IOException e) {
			
			setStatus(STATUS_STOP);
			
			System.out.println("Fail to establish the server socket.");
			
			e.printStackTrace();
			
			interrupt();
			
			return;
			
		} 
		
		while(true)
		{
			
			System.out.println("Server socket is waiting for connection.");
			
			try {
				
				Socket userSocket = serverSocket.accept();
				
//				System.out.println("Connection recived.");
//				
//				inputStreamReader = new InputStreamReader(userSocket.getInputStream());
//				bufferedReader = new BufferedReader(inputStreamReader);
//				
//				String username = Toolkit.rot13_decrypt(bufferedReader.readLine());
//				
//				if(username == null)
//				{
//					System.out.println("The user fail to join, because no user name.");
//					quietCloseStreams();
//					
//				}
//				else
//				{
//					server.addOnlineUser(new OnlineUserThread(username, userSocket));
//				}
				new Thread() {
					
					@Override
					public void run() {
						
						super.run();
						
						try {
							
							BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(userSocket.getInputStream()));
							
							String username = Toolkit.rot13_decrypt(bufferedReader.readLine());
							
							if(username.compareTo(POLLING_CODE) == 0)
							{
								System.out.println("Pulling...");
								username = Toolkit.rot13_decrypt(bufferedReader.readLine());
								PrintWriter printWriter = new PrintWriter(userSocket.getOutputStream());
								printWriter.println( Toolkit.rot13_encrypt("" + server.checkPollingList(username)) );
								System.out.println("pulling..." + username);
								printWriter.flush();
								
//								printWriter.close();
							}
							else
							{
								server.addOnlineUser(new OnlineUserThread(username, userSocket));
							}
							
//							bufferedReader.close();
							
						} catch (IOException e) {

							System.out.println("The user failed to connect to the server.");
							
							e.printStackTrace();
							
						}
						
					}
					
				}.start();
				
			} catch (IOException e) {
				
				System.out.println("The user failed to connect to the server.");
				
				e.printStackTrace();

//				quietCloseStreams();
				
//				quietCloseUserSocket();
				
			}
			
			System.out.println("Connection recived.");
			
			
			
		}
		
	}
	
	
	// 
	
	
	@Override
	public void interrupt() {
		
//		quietCloseUserSocket();
		
		try {
			
			if(serverSocket != null)
			{
				
				if(!serverSocket.isClosed())
				{
					
					serverSocket.close();
					
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		}
		
		System.out.println("Server socket thread is interrupting.");
		
		super.interrupt();
		
	}
	
	
}


