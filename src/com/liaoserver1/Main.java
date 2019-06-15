package com.liaoserver1;

public class Main {

	public static void main(String args[]) {
		
		LiaoServer1 server = new LiaoServer1();
		server.loadConfigFile();
		server.loadDataFile();
		server.loadServersocket();
		
	}

}
