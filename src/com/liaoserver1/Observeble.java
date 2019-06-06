package com.liaoserver1;

public interface Observeble {
	
	//
	public abstract void register(Observer observer);
	
	//
	public abstract void remove(Observer observer);
	
	//
	public abstract void broadcast(String message);

}
