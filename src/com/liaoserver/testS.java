package com.liaoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class testS {

	public static void main(String[] args) {
		
		try {
			
			System.out.println("Open Socket...");
			ServerSocket serverSocket = new ServerSocket(9900);
			System.out.println(serverSocket.getInetAddress().toString());
			
			System.out.println("Waiting for connection...");
			Socket socket = serverSocket.accept();
			System.out.println("Accept connection...");
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
			String string = "";
//			while(!string.equalsIgnoreCase("bye!"))
//			{
//				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//				System.out.println(string);
//				bufferedReader.close();
//			}
			while((string = bufferedReader.readLine()) != null && !string.equalsIgnoreCase("bye!"))
			{
				System.out.println(string);
			}
			
			if(socket.isClosed())
			{
				System.out.println("s closed.");
			}
			
			if(!socket.isConnected())
			{
				System.out.println("s disconnected.");
			}
			
			//bufferedReader.close();
			socket.close();
			serverSocket.close();
			
			
			System.out.println("Socket has been closed!");
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
	}

}
