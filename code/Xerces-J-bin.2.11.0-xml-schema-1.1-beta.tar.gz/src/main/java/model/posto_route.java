package model;

import java.sql.Date;

public class posto_route {
	private int pid;
	private int rid;

 
	public posto_route() {
	}

	public posto_route(int pid, int rid ) {
		this.pid = pid;
		this.rid = rid;
 
	}
 

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getRid() {
		return rid;
	}

	public void setRid(int rid) {
		this.rid = rid;
	}
 

}
