package com.liaoserver;

import java.util.Scanner;

public class liao {

	public static void main(String[] args) {
		
		Liaoserver liaoserver = new Liaoserver();
		liaoserver.loadConfigFile();
		liaoserver.loadDataFile();
		liaoserver.startServering();
//		Scanner scanner = new Scanner(System.in);
//		String string  = scanner.nextLine();
		
		liaoserver.stopLiaoServer();
	}
	
}
