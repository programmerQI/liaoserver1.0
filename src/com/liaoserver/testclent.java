package com.liaoserver;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class testclent {

	public static void main(String[] args) {
		java.util.Scanner scanner = new java.util.Scanner(System.in);
		try {
			Socket socket = new Socket("localhost", 9900);
			System.out.println(socket.getInetAddress().toString());
			String string;
			string = scanner.nextLine();
			System.out.println("->");
			socket.close();
			System.out.println("Closed!");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
