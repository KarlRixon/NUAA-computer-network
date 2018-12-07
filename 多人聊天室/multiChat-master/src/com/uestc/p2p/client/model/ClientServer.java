/**
 * 这是客户端连接服务器的后台
 */
package com.uestc.p2p.client.model;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import com.uestc.p2p.client.tools.CliConSerThread;
import com.uestc.p2p.client.tools.ManageCliThread;
import com.uestc.p2p.common.Message;
import com.uestc.p2p.common.MessageType;
import com.uestc.p2p.common.User;
public class ClientServer {

	public Socket s;
	public ClientServer(){
		
	}
	//发送第一次请求
	public String sendLoginMessage(Object o){
		String b = MessageType.m_login_succeed;
		try {
			s = new Socket("127.0.0.1",9999);
		    ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
		    oos.writeObject(o);
		    
		    ObjectInputStream ois =new ObjectInputStream(s.getInputStream());
		    Message ms = (Message)ois.readObject();
		    //这是验证用户登录的地方
		    if(ms.getType().equals(MessageType.m_login_succeed)){
		    	//创建该账号和服务器端保持连接的线程
		    	CliConSerThread ccst = new CliConSerThread(s);
		    	//启动该通讯线程
		    	ccst.start();
		    	ManageCliThread.addCliThread(((User)o).getUserId(), ccst);
		    }else if(ms.getType().equals(MessageType.m_login_duplicate)){
		    	b = MessageType.m_login_duplicate;
		    }else if(ms.getType().equals(MessageType.m_login_failed)){
		    	b = MessageType.m_login_failed;
		    }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return b;
	}
	public void SendMessage(Object o){
		try {
			Socket s= new Socket("127.0.0.1",9999);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			
		}
	}

}
