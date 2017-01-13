package com.amphro.receptionmapper.database;

public class Request {
	private String request;
	private Long id;
	
	public Request(String req, Long id){
		this.request = req;
		this.id = id;
	}
	
	public String getRequest(){
		return this.request;
	}
	
	public Long getID(){
		return this.id;
	}
}
