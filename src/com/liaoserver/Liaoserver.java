package com.liaoserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;



public class Liaoserver {
	
	public static String CONFIG_FILE = "liao.conf";
	
	private BufferedReader bufferedReader;
	
	private HashMap<String, String> confvaluesHashMap;
	private HashMap<String, Thread> onlineusersHashMap;
	
	private ServerSocket serverSocket;
	
	class serversocketThread extends Thread{
		public serversocketThread() {
			try {
				serverSocket = new ServerSocket(Integer.parseInt(confvaluesHashMap.get("Port")));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		@Override
		public void run() {
			while(!serverSocket.isClosed())
			{
				try {
					Socket socket = serverSocket.accept();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			super.run();
		}
	}
	
	class onlineUserThread extends Thread{
		
		Socket socket;
		
		public onlineUserThread(Socket socket) {
			this.socket = socket;
		}
		
		@Override
		public void run() {
			super.run();
		}
	}
	
	public Liaoserver() {
		
		confvaluesHashMap = new HashMap<String, String>();
		onlineusersHashMap = new HashMap<String, Thread>();
		
	}
	
	public int loadConfigFile()
	{
		try {
			bufferedReader = new BufferedReader(new FileReader(CONFIG_FILE));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		String tmpString;
		String[] splitedStrings;
		try {
			while((tmpString = bufferedReader.readLine()) != null)
			{
				splitedStrings = tmpString.split("=", 2);
				confvaluesHashMap.put(splitedStrings[0], splitedStrings[1]);
			}
			bufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
		return 1;
	}
	
	public int startServering()
	{
		return 1;
	}
	
}
