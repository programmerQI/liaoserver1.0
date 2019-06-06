package com.liaoserver1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import com.sun.jdi.event.ThreadDeathEvent;

public class LiaoServer1 implements Observeble{
	
	
	//
	public static final String FILE_SERVERCONFIG = "liao.conf";
	public static final String PATH_DATASTORAGE = "data";
	
	
	//
	private DateFormat dateFormat;
	private String currentDate;
	//private File serverConfFile;
	private File dataFile;

	
	// Containers
	private Map<String, String> confValuesMap; 
	private Vector<String> historyDataStrings;
	private Vector<OnlineUserThread> onlineUsers;
	
	
	// Server socket thread
	private ServersocketThread serversocketThread;
	//private Thread broadcastThread;
	//private ChatingroomThread chatingroomThread;
	
	
	
	
	//
	public LiaoServer1() {
		
		dateFormat = new SimpleDateFormat("yyyy_MM_dd");
		currentDate = dateFormat.format(new Date());
		
		confValuesMap = new HashMap<String, String>();
		
		historyDataStrings = new Vector<String>();
		
		onlineUsers = new Vector<OnlineUserThread>();
		
	}
	
	
	//
	public int loadConfigFile()
	{
		
		

		try {
			
			System.out.println("Read the configuration files.");
			
			BufferedReader bufferedReader;
			bufferedReader = new BufferedReader(new FileReader(FILE_SERVERCONFIG));
			
			String tmpString;
			String[] splitedStrings;
			while((tmpString = bufferedReader.readLine()) != null)
			{
				
				// Skip the comments.
				if(tmpString.charAt(0) == '#')
				{
					continue;
				}
				
				// Split the string into sub strings.
				splitedStrings = tmpString.split("=");
				if(splitedStrings.length != 2)
				{
					continue;
				}
				
				System.out.println(splitedStrings[0] + "=" + splitedStrings[1]);
				
				// Store the values
				confValuesMap.put(splitedStrings[0], splitedStrings[1]);
				
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
	
	
	// Load the data file base on the current date
	public int loadDataFile()
	{
		
		// Create the data storage path
		File path_datastorage = new File(PATH_DATASTORAGE);
		if(!path_datastorage.exists())
		{
			path_datastorage.mkdir();
			System.out.println("The data storage path has been created.");
		}
		
		// Create the data file
		dataFile = new File(path_datastorage, currentDate + ".data");
		
		try {
			
			if(dataFile.createNewFile())
			{
				System.out.println("\"" + currentDate + "\"" + " has been created.");
			}
			else 
			{
				System.out.println("\"" + currentDate + "\"" + " already exists.");
			}
			
			System.out.println(dataFile.getAbsolutePath());
			
			BufferedReader bufferedReader = new BufferedReader(new FileReader(dataFile));
			
			String tmpString;
			while( (tmpString = bufferedReader.readLine()) != null )
			{
				historyDataStrings.add(tmpString);
			}
			
			bufferedReader.close();
			
		} catch (IOException e) {
			
			System.out.println("The IO error occured while creating the data file.");
			e.printStackTrace();
			
			return -1;
		}
		
		
		return 1;
	}
	
	
	//
	public void loadServersocket() {
		
		serversocketThread = new ServersocketThread(this, Integer.parseInt(confValuesMap.get("Port")));
		
		serversocketThread.start();
		
	}
	
	
	//
	public void addOnlineUser(OnlineUserThread onlineUserThread) {
		
		onlineUserThread.setServer(this);
		
		//System.out.println("8261" + onlineUserThread.socket.isClosed());
		
		onlineUserThread.start();

		//System.out.println("9011" + onlineUserThread.socket.isClosed());
		
		onlineUsers.add(onlineUserThread);

		//System.out.println("1011" + onlineUserThread.socket.isClosed());
		
		System.out.println("The user has been added to the vector.");
		
	}
	
	// 
	public synchronized void delOnlineUser(OnlineUserThread onlineUserThread)
	{
		
		new Thread() {
			@Override
			public void run() {
				
				super.run();

				onlineUserThread.interrupt();
				
				System.out.println(onlineUserThread.getUsername() + "s thread has been interrupted.");
				
				//chatingroomThread.remove(onlineUserThread);
				
				onlineUsers.remove(onlineUserThread);
				
				System.out.println("The thread has been removed.");
				
			}
		}.start();
		
	}


	@Override
	public void register(Observer observer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void remove(Observer observer) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public synchronized void broadcast(String message) {
		
		historyDataStrings.add(message);
		
		new Thread() {
			@Override
			public void run() {
				super.run();
				
				for(OnlineUserThread i : onlineUsers) {
					
					i.getMessage(message);
					
				}
				
			}
		}.start();
		
	}
	
	
	public synchronized void sendHistoryMessage(OnlineUserThread onlineUserThread) {
		
		System.out.println("Sending the history message.");
		
		for(String i : historyDataStrings) {
			
			System.out.println(i);
			
			onlineUserThread.getMessage(i);
			
		}
		System.out.println();
		
	}
	
	
	private void saveDataTofile() {
		
		try {
			
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(dataFile)));
			
			for(String i : historyDataStrings)
			{
				
				printWriter.println(i);
				
			}
			
			printWriter.flush();
			
			System.out.println("The data has already been saved to the file.");
			
			printWriter.close();
			
		} catch (IOException e) {
			
			System.out.println("Fail to write data to file.");
			
			e.printStackTrace();
		}

	}
	
	
	public void stopServering()
	{
		
		serversocketThread.interrupt();
		
		System.out.println("Server socket thread has been interrupt.");
		
		for(OnlineUserThread i : onlineUsers)
		{
			i.interrupt();
		}
	}

}
