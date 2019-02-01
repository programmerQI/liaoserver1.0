package com.liaoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;



public class Liaoserver {
	
	public static int LINES = 15;
	public static String CONFIG_FILE = "liao.conf";
	
	private File dataFile;
	private Vector<File> dataFiles;
	private int dataFilesIndex;
	
	private String dateString;
	private DateFormat dateFormat;
	
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
				
				String string = rot13_decrypt(bufferedReader.readLine());
				
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
		private BufferedReader bufferedReader;
		private InputStreamReader inputStreamReader;
		
		public OnlineUserThread(Socket socket, String nameString) {
			
			this.socket = socket;
			this.nameString = nameString;
			
		}
		
		@Override
		public void run() {
			
			try {
				
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				
				String string;
				while( (string = rot13_decrypt(bufferedReader.readLine())) != null && string.equals("bye") )
				{
					
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.run();
		}
		
		public String getNameString()
		{
			return nameString;
		}
	}
	
	public Liaoserver() {
		
		dataFilesIndex = 0;
		
		dataFiles = new Vector<File>();
		namesStrings = new Vector<String>();
		confvaluesHashMap = new HashMap<String, String>();
		onlineusersHashMap = new HashMap<String, Thread>();
		
		dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		dateString = dateFormat.format(new Date());
		
	}
	
	public int loadDataFile()
	{
		
		dataFile = new File(dateString);
		
		try {
			if(dataFile.createNewFile())
			{
				System.out.println("\"" + dateString + "\"" + " has been created.");
			}
			else 
			{
				System.out.println("\"" + dateString + "\"" + " already exists.");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
			
		return 1;
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
	
	public String rot13_decrypt(String iString)
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
	
	public String rot13_encrypt(String iString)
	{
		if(iString.isEmpty())
		{
			return "";
		}
		
		int len = iString.length();
		String oString = "";
		for(int i = 0; i < len ; i ++)
		{
			oString = oString + (iString.charAt(i) + 13);
		}
		return oString;
	}
	
}
