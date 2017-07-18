package com.example.dell.bzbp_frame.model;

public class Comment {

	private int id;
	private int pid;
	private int uid;
	private String username;
    



	private String context;
    private Double date;

	public Comment() {
	}
	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
 
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getPid() {
		return pid;
	}


	public void setPid(int pid) {
		this.pid = pid;
	}


	public int getUid() {
		return uid;
	}


	public void setUid(int uid) {
		this.uid = uid;
	}


	public String getContext() {
		return context;
	}


	public void setContext(String context) {
		this.context = context;
	}


	public Double getDate() {
		return date;
	}


	public void setDate(Double date) {
		this.date = date;
	}
 
 
}
