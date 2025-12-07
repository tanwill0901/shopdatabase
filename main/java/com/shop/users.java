package com.shop;

public class users {
	private String username;
	private String userpass;
	private String userstatus;

	public users(String username, String userpass, String userstatus) {
		this.username = username;
		this.userpass = userpass;
		this.userstatus = userstatus;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return userpass;
	}

	public void setUserpass(String userpass) {
		this.userpass = userpass;
	}

	public String getUserStat() {
		return userstatus;
	}

	public void setUserstatus(String userstatus) {
		this.userstatus = userstatus;
	}

	@Override
	public String toString() {
		return String.format("User{username='%s', userpass='%s', userstatus='%s'}", username, userpass, userstatus);
	}
}
