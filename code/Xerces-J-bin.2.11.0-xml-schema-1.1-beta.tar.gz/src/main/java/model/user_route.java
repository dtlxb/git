package model;

import java.sql.Date;

public class user_route {
	private String name;
	private int rid;

 
	public user_route() {
	}

	public user_route(String name, int rid ) {
		this.name = name;
		this.rid = rid;
 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}
 

}
