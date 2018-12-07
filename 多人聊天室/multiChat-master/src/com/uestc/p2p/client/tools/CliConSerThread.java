/*
 * 这是客户端和服务器端保持通讯的线程
 */
package com.uestc.p2p.client.tools;

import java.io.ObjectInputStream;
import java.net.Socket;

import com.uestc.p2p.client.view.Chat;
import com.uestc.p2p.client.view.FriendList;
import com.uestc.p2p.client.view.GroupChat;
import com.uestc.p2p.common.Message;
import com.uestc.p2p.common.MessageType;
public class CliConSerThread extends Thread{
   private Socket s;
  
   public CliConSerThread(Socket s){
	   this.s=s;
   }
   public void run(){
	   while(true){
		   try {
			  ObjectInputStream ois = new ObjectInputStream(s.getInputStream());
		      Message m = (Message)ois.readObject();
		      
		      //把服务器获得的消息显示到聊天界面
              if(m.getType().equals(MessageType.m_info)){
            	  System.out.println("读取到从服务器发来的消息"+m.getSender()+"给"+m.getReceiver()+"说："+m.getContent());
            	  String key =m.getReceiver()+"_"+m.getSender();
            	  Chat c =ManageChat.getChat(key);
            	  c.showMessage(m);
              }else if(m.getType().equals(MessageType.m_return_onLine)){
            	  String receiver = m.getReceiver();
            	  //修改相应的好友列表
            	  System.out.println("我要更新_"+receiver+"_好友信息了"+m.getContent());
            	  FriendList fl = ManageFriends.getFriendList(receiver);
            	  if(fl!=null)
            		  fl.dispose();
            	  fl = new FriendList(receiver);
                  fl.updateFriendList(m);
                  ManageFriends.addFriendList(receiver, fl);
            	 
              }else if(m.getType().equals(MessageType.m_group_receive)){
            	  System.out.println("服务器发来的消息群消息"+m.getSender()+"说："+m.getContent());
            	  String key =m.getReceiver()+" group";
            	  GroupChat gc = ManageGroupChat.getGChat(key);
            	  gc.showMessage(m);
              }
		   }catch(Exception e) {
			  e.printStackTrace();
		   }
	   }
   }
   public Socket getS() {
		return s;
	}
}
