package com.liaoserver;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Vector;



public class Liaoserver {
	
	public static String CONFIG_FILE = "liao.conf";
	
	private Vector<String> namesStrings;
	private HashMap<String, String> confvaluesHashMap;
	private HashMap<String, Thread> onlineusersHashMap;
	
	private ServerSocket serverSocket;
	
	
	class ServersocketThread extends Thread{
		
		private BufferedReader bufferedReader;
		private InputStreamReader inputStreamReader;
		
		public ServersocketThread() {
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
					String nameString = getUserInfo(socket);
					
					namesStrings.add(nameString);
					OnlineUserThread newOnlineUserThread = new OnlineUserThread(socket, nameString);
					onlineusersHashMap.put(nameString, newOnlineUserThread);
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				

			}
			super.run();
		}
		
		public String getUserInfo(Socket socket)
		{
			try {
				
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				
				String string = rot13(bufferedReader.readLine());
				
				bufferedReader.close();
				inputStreamReader.close();
				
				return string;
				
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		
	}
	
	class OnlineUserThread extends Thread{
		
		private Socket socket;
		private String nameString;
		
		public OnlineUserThread(Socket socket, String nameString) {
			
			this.socket = socket;
			this.nameString = nameString;
			
		}
		
		@Override
		public void run() {
			super.run();
		}
	}
	
	public Liaoserver() {
		
		namesStrings = new Vector<String>();
		confvaluesHashMap = new HashMap<String, String>();
		onlineusersHashMap = new HashMap<String, Thread>();
		
	}
	
	public int loadConfigFile()
	{

		try {
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new FileReader(CONFIG_FILE));
			
			String tmpString;
			String[] splitedStrings;
			while((tmpString = bufferedReader.readLine()) != null)
			{
				splitedStrings = tmpString.split("=", 2);
				confvaluesHashMap.put(splitedStrings[0], splitedStrings[1]);
			}
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return -1;
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
	
	public String rot13(String iString)
	{
		if(iString.isEmpty())
		{
			return "";
		}
		
		int len = iString.length();
		String oString = "";
		for(int i = 0; i < len ; i ++)
		{
			oString = oString + (iString.charAt(i) - 13);
		}
		return oString;
	}
	
}
