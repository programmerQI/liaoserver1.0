package com.liaoserver1;

import java.util.Vector;

public class ChatingroomThread extends Thread implements Observeble{
	
	private Vector<Observer> users;
	private Vector<String> chatHistory; 
	
	public ChatingroomThread(Vector<String> chatHistory) {
		
		users = new Vector<Observer>();
		this.chatHistory = chatHistory;
		
	}
	
	@Override
	public void run() {
		
		
		
		super.run();
	}

	@Override
	public void register(Observer observer) {
		
		users.add(observer);
		
		new Thread() {
			
			@Override
			public void run() {
				
				super.run();
				
				try {
					
					for(String i : chatHistory)
					{
						observer.getMessage(i);
					}
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			}
		}.start();
		
	}

	@Override
	public void remove(Observer observer) {
		
		users.remove(observer);
		
	}

	@Override
	public void broadcast(String message) {
		
		for(Observer i : users)
		{
			
			i.getMessage(message);
			
		}
		
	}

}
