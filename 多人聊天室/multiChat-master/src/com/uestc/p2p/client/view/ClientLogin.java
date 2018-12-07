/**
 * 客户端登录界面
 */
package com.uestc.p2p.client.view;


import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.uestc.p2p.client.model.ClientUser;
import com.uestc.p2p.client.tools.ManageCliThread;
import com.uestc.p2p.client.tools.ManageFriends;
import com.uestc.p2p.common.*;
//import java.awt.event.*;
public class ClientLogin extends JFrame implements ActionListener {

	//定义上面的
	JLabel jbll;
	//定义中间的
	JLabel jbl21,jbl22,jbl23;
	JTextField jtf;//账号
	JPasswordField jpf;//密码
	//定义下面的
	JPanel jp1,jp2;
	JButton jb1,jb2,jb3;
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ClientLogin clientLogin = new ClientLogin();	
	}
	public ClientLogin(){
		//上面的
		jbll = new JLabel(new ImageIcon("image/shou.gif"));	
		//中间的
		jp2 = new JPanel(new GridLayout(2,3));
		jbl21 = new JLabel("账号:",JLabel.CENTER);
		jbl22 = new JLabel("密码:",JLabel.CENTER);
		jbl23 = new JLabel("忘记密码",JLabel.CENTER);
		jbl23.setForeground(Color.BLUE);
		jb1 = new JButton(new ImageIcon("image/clear.gif"));
		jtf = new JTextField();
		jpf = new JPasswordField();
		//将控件按照顺序加入到jp2
		jp2.add(jbl21);
		jp2.add(jtf);
		jp2.add(jb1);
		jp2.add(jbl22);
		jp2.add(jpf);
		jp2.add(jbl23);
		//下面的按钮
		jp1 = new JPanel();
		jb1 = new JButton(new ImageIcon("image/denglu.gif"));
		//响应用户登录
		jb1.addActionListener(this);
		jb2 = new JButton(new ImageIcon("image/quxiao.gif"));
		jp1.add(jb1);
		jp1.add(jb2);
		
		
		//整合加载
		this.add(jbll,"North");
		this.add(jp2,"Center");
		this.add(jp1,"South");

		this.setSize(340,200);
		this.setLocation(300, 150);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== jb1){
			ClientUser cu = new ClientUser();
		    User u = new User();
		    u.setUserId(jtf.getText().trim());
		    u.setPw(new String(jpf.getPassword()));
		    String check = cu.checkUser(u);
		    if(check.equals(MessageType.m_login_succeed)){
		    	
		    	try {
		    		//发送要求返回在线好友的包
					ObjectOutputStream oos =new ObjectOutputStream
					(ManageCliThread.getThread(u.getUserId()).getS().getOutputStream());
					
					//构造message
					Message m = new Message();
					//请求特定账号的好友列表
					m.setType(MessageType.m_get_onLine);
					m.setSender(u.getUserId());
					oos.writeObject(m);
		    	} catch (IOException e1) {
					e1.printStackTrace();
				}
		    	
		    	//关闭登录界面
		    	this.dispose();
		    }else if(check.equals(MessageType.m_login_duplicate)){
		    	JOptionPane.showMessageDialog(this,"您不可重复登录");
		    }else if(check.equals(MessageType.m_login_failed)){
		    	JOptionPane.showMessageDialog(this,"你输入的密码有误");
		    }
		}
	}
}
