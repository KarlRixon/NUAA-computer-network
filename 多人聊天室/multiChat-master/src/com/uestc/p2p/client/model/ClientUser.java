package com.uestc.p2p.client.model;

import com.uestc.p2p.common.*;

public class ClientUser {
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	public String checkUser(User u){
		return new ClientServer().sendLoginMessage(u);
	}

}
