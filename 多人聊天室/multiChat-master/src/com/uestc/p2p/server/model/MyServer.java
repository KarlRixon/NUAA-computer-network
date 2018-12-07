/**
 * 这是服务器，它在监听，等待某个客户端接入
 */
package com.uestc.p2p.server.model;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import com.uestc.p2p.common.Message;
import com.uestc.p2p.common.MessageType;
import com.uestc.p2p.common.User;
public class MyServer {
   
	public MyServer(){
		try{
			//在该端口下监听
			System.out.println("我是服务器，在9999监听");
			ServerSocket ss = new ServerSocket(9999);
			//阻塞，等待连接
		    while(true){
		    	Socket s = ss.accept();
				//接受客户端发来的信息
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
				User u = (User)ois.readObject();
				System.out.println("服务器接收到的用户名和密码："+u.getUserId()+"和"+u.getPw());
				Message m = new Message();
				ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
				if(u.getPw().equals("shan")){
					//返回一个成功登陆的信息包
					if(ManageSerThread.getThread(u.getUserId())==null){
						m.setType(MessageType.m_login_succeed);
						oos.writeObject(m);
						//单开一个线程，让该线程与该客户端保持通讯
						SerConCliThread scct = new SerConCliThread(s);
					    //启动与该客户端通讯的线程
						ManageSerThread.addThread(u.getUserId(), scct);
						scct.start();
					}else{
						m.setType(MessageType.m_login_duplicate);
						oos.writeObject(m);
						s.close();
					}
					

				}else{
					m.setType("2");
					oos.writeObject(m);
					s.close();
				}
		    }
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			
		}
	}
}
