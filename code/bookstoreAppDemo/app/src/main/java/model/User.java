package model;

import java.sql.Date;

public class User {

	private int id;
	private String username;
	private String password;
	private String role;

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



	public String getPassword() {
		return password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
