package com.uestc.p2p.common;

import java.io.Serializable;

public class User implements Serializable {

	private String userId;
	private String pw;
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	
}
