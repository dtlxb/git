package model;

import java.sql.Date;

public class Photo {

 
	private String name;
	private String content;

	public Photo() {
	}

	public Photo(String name, String content) {
		this.name = name;
		this.content = content;

	}
 
	public String getname() {
		return name;
	}

	public void setname(String name) {
		this.name = name;
	}

	public String getcontent() {
		return content;
	}

	public void setcontent(String content) {
		this.content = content;
	}
 

}
