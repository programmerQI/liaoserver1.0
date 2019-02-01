package com.liaoserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class testC {
	
	public static void main(String[] args) {
		try {
			
			Socket socket = new Socket("localhost", 9900);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
			
			String string;
			while((string = bufferedReader.readLine()) != null)
			{
				printWriter.println(string);
				printWriter.flush();
				if(string.equalsIgnoreCase("bye"))
				{
					break;
				}
			}
			printWriter.close();
			bufferedReader.close();
			socket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
