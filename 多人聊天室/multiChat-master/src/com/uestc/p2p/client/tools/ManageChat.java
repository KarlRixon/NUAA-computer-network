/**
 * 这是一个管理用户聊天的界面
 */
package com.uestc.p2p.client.tools;
import java.util.HashMap;
import java.util.Iterator;

import com.uestc.p2p.client.view.Chat;
public class ManageChat {
  
    private static HashMap hm=new HashMap<String, Chat>();
	
	//加入
	public static void addChat(String key,Chat chat)
	{
		hm.put(key, chat);
	}
	//取出
	public static Chat getChat(String key )
	{
		return (Chat)hm.get(key);
	}
	public static void show(){
		Iterator iter = hm.keySet().iterator();
		while(iter.hasNext()){
			System.out.println("有"+iter.next().toString());
		}
	}
}
