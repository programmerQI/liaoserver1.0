package com.liaoserver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;


public class Liaoserver {
	
	private String configfile = "liao.conf";
	
	
	private Map<String, String> conf;
	private Map<String, Thread> onlineUsers;
	private ServerSocket serverSocket;
	
	class onlineUserThread extends Thread{
		public onlineUserThread() {
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				Socket socket = serverSocket.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			super.run();
		}
	}
	
	public Liaoserver() {
	}
	
	public int startServering()
	{
		return 1;
	}
	
}
