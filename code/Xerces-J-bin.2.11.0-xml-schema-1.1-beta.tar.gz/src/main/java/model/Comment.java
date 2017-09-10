package model;

public class Comment {

	private int id;
	private int pid;
	private int rid;
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


	public int getRid() {
		return rid;
	}


	public void setRid(int rid) {
		this.rid = rid;
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
