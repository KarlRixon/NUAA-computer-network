/**
 * 定义包的种类
 */
package com.uestc.p2p.common;

public interface MessageType {
  String m_login_succeed = "1";
  String m_login_duplicate = "0";
  String m_login_failed = "2";
  String m_info = "3";
  String m_get_onLine = "4";
  String m_return_onLine = "5";
  String m_group_send = "6";
  String m_group_receive = "7";
}
