package com.liaoserver1;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class OnlineUserThread extends Thread implements Observer{
	
	private String username;
	private Socket socket;
	
	private BufferedReader bufferedReader;
	private InputStreamReader inputStreamReader;
	private PrintWriter printWriter;
	private BufferedWriter bufferedWriter;
	private OutputStreamWriter outputStreamWriter;
	
	private LiaoServer1 server;
	
	public String getUsername() {
		
		return username;
		
	}
	
	public OnlineUserThread(String username, Socket socket) {
		
		System.out.println(username + " userthread has been created.");
		
		this.username = username;
		
		this.socket = socket;
		
		bufferedReader = null;
		
	}
	
	public void setServer(LiaoServer1 server) {
		
		this.server = server;
		
	}
	
	private void initChannel() throws IOException {		
			
			inputStreamReader = new InputStreamReader(socket.getInputStream());
			
			bufferedReader = new BufferedReader(inputStreamReader);
			
			outputStreamWriter = new OutputStreamWriter(socket.getOutputStream());
			
			bufferedWriter = new BufferedWriter(outputStreamWriter);

			printWriter = new PrintWriter(bufferedWriter);

	}
	
	// Close the stream
	private void quietCloseStreams() {
		
		if(inputStreamReader != null)
		{
			
			try {
				
				inputStreamReader.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
		}
		
		if(bufferedReader != null)
		{
			
			try {
				
				bufferedReader.close();
				
			} catch (IOException e) {
				
				e.printStackTrace();
				
			}
		}
		
		if(bufferedWriter != null)
        {
            try {

                bufferedWriter.close();

            } catch (IOException e) {

                e.printStackTrace();

            }
        }

        if(outputStreamWriter != null)
        {
            try {
                outputStreamWriter.close();
            } catch (IOException e) {

                e.printStackTrace();

            }
        }


        if(printWriter != null)
        {

            printWriter.close();

        }
		
		if(printWriter != null)
		{
			
			printWriter.close();
			
		}
		
		System.out.println(username + "s streams have been closed.");

	}
	
	private void quietCloseSocket() {
		
		if(socket != null)
		{
			if(!socket.isClosed())
			{
				try {
					
					socket.close();
					
				} catch (IOException e) {

					e.printStackTrace();
					
				}
			}
		}
		
		System.out.println(username + "s socket has been closed.");

	}
	
	private void gettingMessagesfromUser() throws IOException {
		
		String message = "";
		while( (message = bufferedReader.readLine()) != null)
		{
			server.broadcast(Toolkit.rot13_decrypt(message));
		}

	}
	
	@Override
	public void run() {
		
		super.run();
		
		try {
			
			initChannel();
			
		} catch (IOException e1) {
			
			System.out.print(username + " fail to init channel.");
			
			server.delOnlineUser(this);
			
			e1.printStackTrace();
			
			return;
		}
		
		try {
			
			server.sendHistoryMessage(this);
			
			gettingMessagesfromUser();
			
		} catch (IOException e) {
			
			System.out.println("Fail to get message from " + username);
			
			server.delOnlineUser(this);
			
			e.printStackTrace();
			
		}
		
		// Server will close the socket by calling the "interrupt"
		server.delOnlineUser(this);
		
	}

	@Override
	public synchronized void getMessage(String message) {
		
		System.out.println(username + " gets " + message);
		
		printWriter.println(Toolkit.rot13_encrypt(message));
		printWriter.flush();
		
	}
	
	@Override
	public void interrupt() {
		
		quietCloseStreams();
		
		quietCloseSocket();
		
		System.out.println(username + "s socket thread is interrupting.");
		
		super.interrupt();
	}
	

}
