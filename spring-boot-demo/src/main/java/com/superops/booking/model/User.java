package com.superops.booking.model;

public class User {
	private String userName;
	private String password;
	private String uid;
	private int isAdmin;
	private ServerResponse serverResponse;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public int getIsAdmin() {
		return isAdmin;
	}
	public void setIsAdmin(int isAdmin) {
		this.isAdmin = isAdmin;
	}
	public ServerResponse getServerResponse() {
		return serverResponse;
	}
	public void setServerResponse(ServerResponse serverResponse) {
		this.serverResponse = serverResponse;
	}
	


}
