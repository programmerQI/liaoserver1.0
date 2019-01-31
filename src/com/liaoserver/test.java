package com.liaoserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class test {

	Map<String, Thread> onlineusersMap;
	public static void main(String[] args) {
		/*try {
			ServerSocket serverSocket = new ServerSocket(9900);
			System.out.println("Waiting...");
			Thread thread = new Thread(ServerSocket ss) {
				@Override
				public void run() {
					super.run();
				}
			};
			Socket socket  = serverSocket.accept();
			System.out.println("Connected!");
			//serverSocket.close();
			while(!socket.isClosed())
			{
				
			}
			System.out.println("Disconnnected!");
			serverSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("Hello", "Hi");
		System.out.println(map.get("Hello").toString());
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader("liao.conf"));
			String string;
			while((string = bufferedReader.readLine()) != null)
			{
				System.out.println(string);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
