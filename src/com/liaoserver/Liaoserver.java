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

import com.sun.jdi.event.ThreadDeathEvent;



public class Liaoserver {
	
	public static int LINES = 15;
	public static String CONFIG_FILE = "liao.conf";
	
	private File dataFile;
	private Vector<String> historyDataVectors;
	private int dataFilesIndex;
	
	private String dateString;
	private DateFormat dateFormat;
	
	private Vector<String> namesStrings;
	private Vector<Thread> userThreads;
	private HashMap<String, String> confvaluesHashMap;
	private HashMap<String, Thread> onlineusersHashMap;
	private int numUsers;
	
	private ServerSocket serverSocket;
	
	
	
	// Create server socket class as a thread
	class ServersocketThread extends Thread{
		
		private BufferedReader bufferedReader;
		private InputStreamReader inputStreamReader;
		
		public ServersocketThread() {
			try {
				
				// Creating a socket based on the port number that stored in liao.conf file
				int port = Integer.parseInt(confvaluesHashMap.get("Port"));
				serverSocket = new ServerSocket(port);
				System.out.println("The server is listenning at port " + port + ".");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Add the connected users.
		public void addUser(String userName, Socket userSocket) {
			
			namesStrings.add(userName);
			OnlineUserThread newOnlineUserThread = new OnlineUserThread(userSocket, userName);
			userThreads.add(newOnlineUserThread);
			onlineusersHashMap.put(userName, newOnlineUserThread);
			
		}
		
		@Override
		public void run() {
			while(!serverSocket.isClosed())
			{
				try {
					
					// Waiting for incoming connection
					System.out.println("Waiting for connections...");
					Socket socket = serverSocket.accept();
					
					System.out.println("A user has connected!");
					String nameString = getUserInfo(socket);
					
					addUser(nameString, socket);
					System.out.println(nameString + " has joined.");
					
				} catch (IOException e) {
					
					System.out.println("The user failed to connect.");
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
			System.out.println(nameString + "'thread has bee created.");
			
		}
		
		@Override
		public void run() {
			
			try {
				
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferedReader = new BufferedReader(inputStreamReader);
				
				System.out.println("Waiting for " + nameString + " input data...");
				String tmpString;
				while( (tmpString = rot13_decrypt(bufferedReader.readLine())) != null && tmpString.equals("bye") )
				{
					addData(tmpString);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			super.run();
		}
		
		synchronized private void addData(String string)
		{
			System.out.println(nameString + " says " + string);
			historyDataVectors.add(string);
			System.out.println("Successfully adding data!");
		}
		
		public String getNameString()
		{
			return nameString;
		}
		
		public int close()
		{
			int ans = 1;
			try {
				inputStreamReader.close();
				bufferedReader.close();
			} catch (IOException e1) {
				System.out.println("Unable to close the stream of " + nameString );
				e1.printStackTrace();
				ans = -1;
			}
			
			try {
				socket.close();
			} catch (IOException e) {
				System.out.println( "Fail to close the socket of " + nameString );
				e.printStackTrace();
				ans = -1;
			}
			if(ans == 1)
			{
				System.out.println("The stream of " + nameString + " has been closed.");
				System.out.println("The socket of " + nameString + " has been closed.");
			}

			return ans;
		}
	}
	
	public Liaoserver() {
		
		dataFilesIndex = 0;
		
		namesStrings = new Vector<String>();
		historyDataVectors = new Vector<String>();
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
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
			
			String tmpString;
			while( (tmpString = bufferedReader.readLine()) != null )
			{
				historyDataVectors.add(tmpString);
			}
			
			bufferedReader.close();
			
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
	
	public void startServering()
	{
		ServersocketThread serversocketThread = new ServersocketThread();
		serversocketThread.run();
	}
	
	public void stopLiaoServer()
	{
		for(Thread idThread : userThreads)
		{
			((OnlineUserThread)idThread).close();
			idThread.interrupt();
		}
		System.out.println("All users threads have been stoped.");
		
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
