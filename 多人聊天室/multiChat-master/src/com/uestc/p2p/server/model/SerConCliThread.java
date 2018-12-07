/*
 * 服务器和某个客户端的通信线程
 */
package com.uestc.p2p.server.model;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;

import com.uestc.p2p.common.Message;
import com.uestc.p2p.common.MessageType;
public class SerConCliThread extends Thread {

	Socket s;
	public SerConCliThread(Socket s){
		//把服务器和该客户端的连接赋给s
		this.s = s;
	}
	//通知其他用户
	public void notifyOther(){
		//得到所有在线的人的线程
		HashMap hm =ManageSerThread.hm;
		Iterator it = hm.keySet().iterator();
		while(it.hasNext()){
			Message m = new Message();
			String res = ManageSerThread.getOnlineList();
	    	m.setType(MessageType.m_return_onLine);
	    	m.setContent(res);
			String uid_online = it.next().toString();
			System.out.println("我来通知一下"+uid_online+"更新好友信息");
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManageSerThread.getThread(uid_online).s.getOutputStream());
			    m.setReceiver(uid_online);
			    oos.writeObject(m);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	public void showGroupMessage(Message m){
		//得到所有在线的人的线程
		HashMap hm =ManageSerThread.hm;
		Iterator it = hm.keySet().iterator();
		while(it.hasNext()){
			String res = ManageSerThread.getOnlineList();
	    	m.setType(MessageType.m_group_receive);
			String uid_online = it.next().toString();
			try {
				if(!uid_online.equals(m.getSender())){
					ObjectOutputStream oos = new ObjectOutputStream(ManageSerThread.getThread(uid_online).s.getOutputStream());
				    m.setReceiver(uid_online);
				    oos.writeObject(m);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	public void run(){
		while(true){
			//这里该线程就可以接收客户端的信息
			try {
				ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
			    Message m = (Message)ois.readObject();
			    
			    //对客户端取得的消息进行类型判断，然后做相应的处理
			    if(m.getType().equals(MessageType.m_info)){
			    	//接下来转发，取得接受人的通讯线程
			    	System.out.println("我是服务器的："+m.getSender()+"给"+m.getReceiver()+"say:"+m.getContent());
				    SerConCliThread scct = ManageSerThread.getThread(m.getReceiver());
				    ObjectOutputStream oos = new ObjectOutputStream(scct.s.getOutputStream());
				    oos.writeObject(m);
			    }else if(m.getType().equals(MessageType.m_get_onLine)){
			    	//把在服务器的好友返回
				    notifyOther();
			    }else if(m.getType().equals(MessageType.m_group_send)){
			    	System.out.println("我是服务器群组的："+m.getSender()+"say:"+m.getContent()+"to us");
			    	showGroupMessage(m);
			    }
			    
			    
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
