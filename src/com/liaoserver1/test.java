package com.liaoserver1;

public class test {

	public static void main(String[] args) {
		
		LiaoServer1 server1 = new LiaoServer1();
		server1.loadConfigFile();
		server1.loadDataFile();
		server1.loadServersocket();
		
	}

}
