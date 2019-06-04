package com.liaoserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class testC2 {
	
	public static void main(String[] args) {
		try {
			
			Socket socket = new Socket("localhost", 9900);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
			
			
			String string;
			while((string = bufferedReader.readLine()) != null)
			{
				printWriter.println(string);
				printWriter.flush();
				if(string.equalsIgnoreCase("bye"))
				{
					break;
				}
				System.out.println("The message has been send.");
			}
			printWriter.close();
			bufferedReader.close();
			socket.close();
			System.out.println("The sockect has been closed.");
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
