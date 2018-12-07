/*
 * 获取登录成功后的好友界面
 */
package com.uestc.p2p.client.tools;
import java.util.HashMap;

import com.uestc.p2p.client.view.FriendList;
public class ManageFriends {
   private static HashMap hm = new HashMap<String ,FriendList>();
   public static void addFriendList(String uid ,FriendList fl){
	   hm.put(uid,fl);
   }
   public static FriendList getFriendList(String uid){
	   return (FriendList)hm.get(uid);
   }
}
