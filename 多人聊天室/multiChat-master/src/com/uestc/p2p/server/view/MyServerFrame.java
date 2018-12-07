package com.uestc.p2p.server.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.uestc.p2p.server.model.MyServer;
public class MyServerFrame extends JFrame implements ActionListener {

	JPanel jpl;
	JButton jb1,jb2;
	public static void main(String[] args) {
		new MyServerFrame();
	}
	public MyServerFrame(){
		jpl = new JPanel();
		jb1 = new JButton("启动服务器");
		jb1.addActionListener(this);
		jb2 = new JButton("关闭服务器");
		jpl.add(jb1);
		jpl.add(jb2);
		
		this.add(jpl);
		this.setTitle("服务器启动界面");
		this.setLocation(320, 250);
		this.setSize(500,300);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()== jb1){
			new MyServer();
		}
	}

}
