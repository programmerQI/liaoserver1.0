package com.liaoserver1;

public class Toolkit {

	
	// Rot13 encryption algorithm
	public static String rot13_encrypt(String iString)
	{
		if(iString.isEmpty())
		{
			return "";
		}
		
		int len = iString.length();
		String oString = "";
		for(int i = 0; i < len ; i ++)
		{
			oString = oString + (iString.charAt(i) + 13);
		}
		return oString;
	}
	
	// Rot13 decryption algorithm
	public static String rot13_decrypt(String iString)
	{
		if(iString.isEmpty())
		{
			return "";
		}
		
		int len = iString.length();
		String oString = "";
		for(int i = 0; i < len ; i ++)
		{
			oString = oString + (iString.charAt(i) - 13);
		}
		return oString;
	}

}
