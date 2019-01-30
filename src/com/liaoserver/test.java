package com.liaoserver;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
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
		InputStream iStream = new InputStream() {
			
			@Override
			public int read() throws IOException {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		File file =
	}

}
