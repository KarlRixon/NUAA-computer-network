/*
 * 这是一个管理客户端和服务器通讯的线程
 */
package com.uestc.p2p.client.tools;
import java.util.HashMap;
public class ManageCliThread {
   private static HashMap hm = new HashMap<String, CliConSerThread>();
   //把创建好的CliConSerThread放入到hashmap中
   public static void addCliThread(String uid,CliConSerThread ccst){
	   hm.put(uid, ccst);
   }
   //可以通过uid,取得该线程
   public static CliConSerThread getThread(String uid){
	   return (CliConSerThread)hm.get(uid);
   }
   
}
