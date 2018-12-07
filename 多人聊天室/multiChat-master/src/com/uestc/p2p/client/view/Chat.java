/*
 * 与好友聊天界面
 */
package com.uestc.p2p.client.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.uestc.p2p.client.tools.ManageCliThread;
import com.uestc.p2p.common.Message;
import com.uestc.p2p.common.MessageType;
public class Chat extends JFrame implements ActionListener {

	/**
	 * @param args
	 */
	JTextArea jta;
	JTextField jtf;
	JButton jb;
	JPanel jp;
	String sender;
	String receiver;
	
	public Chat(String ownerId ,String friend){
		this.sender = ownerId;
		this.receiver = friend;
		jta = new JTextArea();
		jta.setForeground(Color.BLUE);
		jtf = new JTextField(18);
		jtf.setBackground(Color.lightGray);
		jb = new JButton("发送");
		jb.addActionListener(this);
		jp = new JPanel();
		jp.add(jtf);
		jp.add(jb);
		
		this.add(jta,"Center");
		this.add(jp,"South");
		this.setIconImage((new ImageIcon("image/fly.jpg")).getImage());
		this.setTitle("chat:'"+ownerId+"'正在和'"+friend+"'聊天");
		this.setSize(300,200);
		this.setLocation(320, 100);
		this.setVisible(true);
	}
    public String GetNowTime(){
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }
	@Override
	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == jb){
			//点击发送
			Message m = new Message();
			m.setSender(sender);
			m.setType(MessageType.m_info);
			m.setReceiver(receiver);
			m.setContent(jtf.getText());
			m.setTime(GetNowTime().toString());
			//发送给服务器
			try {
				ObjectOutputStream oos = new ObjectOutputStream(ManageCliThread.getThread(sender).getS().getOutputStream());
				String info = "-------------------\n"+"我："+"\t"+m.getTime()+"\n"+m.getContent()+"\n";
			    this.jta.append(info);
			    this.jtf.setText("");
				oos.writeObject(m);
			} catch (Exception e1) {
				e1.printStackTrace();
			}	
		}
	}
	//显示消息
	public void showMessage(Message m){
		String info = "-------------------\n"+m.getSender()+":\t"+m.getTime()+"\n:"+m.getContent()+"\n";
	    this.jta.append(info);
	    this.jtf.setText("");
	}
}
