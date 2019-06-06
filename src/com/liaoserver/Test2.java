package com.liaoserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Test2 {
	
	public static void main(String[] args) {
//		Map<String, String> map = new HashMap();
//		map.put("This", "That");
//		
//		System.out.println(map.get("Thas"));
		
//		String str1 = "你好";
//		
//		String str2 = com.liaoserver1.Toolkit.rot13_encrypt(str1);
//		
//		String str3 = com.liaoserver1.Toolkit.rot13_decrypt(str2);
//		
//		System.out.println(str3);
		
		try {
			
			PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter("test")));
			
			for(int i = 0; i < 1000; i ++)
			{
				printWriter.println(i + " Fuckme!");
			}
			
			printWriter.flush();
			
			
			printWriter.close();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}

}
