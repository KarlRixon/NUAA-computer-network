package com.uestc.p2p.server.model;
import java.util.HashMap;
import java.util.Iterator;
public class ManageSerThread {
   public static HashMap hm = new HashMap<String ,SerConCliThread>();
   //向hm中添加一个客户端通讯线程
   public static void addThread(String uid,SerConCliThread scct){
	   hm.put(uid, scct);
   }
   public static SerConCliThread getThread(String uid){
	   return (SerConCliThread)hm.get(uid);
   }
 //返回当前在线好友拼接的字符串
   public static String getOnlineList(){
	   Iterator it = hm.keySet().iterator();
	   String onLineList = "";
	   while(it.hasNext()){
		   onLineList += it.next().toString()+" ";
	   }
	   return onLineList;
   }
}
