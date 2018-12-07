/**
 * 我的好友列表（包括好友列表，黑名单）
 */
package com.uestc.p2p.client.view;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.uestc.p2p.client.tools.ManageChat;
import com.uestc.p2p.client.tools.ManageGroupChat;
import com.uestc.p2p.common.Message;
public class FriendList extends JFrame implements ActionListener,MouseListener {
	//处理第一张卡片
	JPanel jphy1,jphy2,jphy3;
	JButton jphy_jb1,jphy_jb2,jphy_jb3;
	JScrollPane jsp1;
	String ownerId;
	JLabel []jbls;
	//处理第二张卡片（陌生人）
	JPanel jpmsr1,jpmsr2,jpmsr3;
	JButton jpmsr_jb1,jpmsr_jb2,jpmsr_jb3;
	JScrollPane jsp2;
	CardLayout c1;
	int i = 0;
	//把整个JFrame设置成CardLayout

	public FriendList(String ownerId){
		//=========处理第一张卡片-好友列表
		this.ownerId = ownerId;
		jphy_jb1 = new JButton("我的好友");
		jphy_jb2 = new JButton("陌生人");
		jphy_jb2.addActionListener(this);
		jphy_jb3 = new JButton("进入群聊");
		jphy_jb3.addActionListener(this);
		jphy1 = new JPanel(new BorderLayout());
		//假定有10个好友
		jphy2 = new JPanel(new GridLayout(10,1,4,4));
		jbls = new JLabel[10];
		jbls[i] = new JLabel(ownerId+"",new ImageIcon("image/tu1.jpg"),JLabel.LEFT);
		jbls[i].setEnabled(true);
		jbls[i].addMouseListener(this);
		jphy2.add(jbls[i]);	
		
		jphy3 = new JPanel(new GridLayout(2, 1));
		//把两个按钮加到jphy3
		jphy3.add(jphy_jb2);
		jphy3.add(jphy_jb3);
		
		jsp1 = new JScrollPane(jphy2);	
		jphy1.add(jphy_jb1,"North");
		jphy1.add(jsp1,"Center");
		jphy1.add(jphy3,"South");
		
		//========处理第二张卡片-黑名单
		jpmsr_jb1 = new JButton("我的好友");
		jpmsr_jb1.addActionListener(this);
		jpmsr_jb2 = new JButton("陌生人");
		jpmsr_jb3 = new JButton("进入群聊");
		jpmsr1 = new JPanel(new BorderLayout());

		jpmsr2 = new JPanel(new GridLayout(10,1,4,4));
		//给jphy2,初始化10个陌生人
		JLabel []jbls2 = new JLabel[10];
		for(int i=0; i<jbls2.length; i++){
			jbls2[i] = new JLabel(i+1+"",new ImageIcon("image/tu2.jpg"),JLabel.LEFT);
			jbls2[i].addMouseListener(this);
			jpmsr2.add(jbls2[i]);
		}
		
		jpmsr3 = new JPanel(new GridLayout(2, 1));
		//把两个按钮加到jphy3
		jpmsr3.add(jpmsr_jb1);
		jpmsr3.add(jpmsr_jb2);
		
		jsp2 = new JScrollPane(jpmsr2);
		jpmsr1.add(jpmsr3,"North");
		jpmsr1.add(jsp2,"Center");
		jpmsr1.add(jpmsr_jb3,"South");
		
		
		c1 = new CardLayout();
		this.setLayout(c1);
		this.add(jphy1,"1");
		this.add(jpmsr1,"2");
		//在窗口显示自己的编号
		this.setSize(200, 400);
		this.setLocation(200, 200);
		this.setVisible(true);
		this.setTitle(ownerId);
	}
	//更新在线好友
	public void updateFriendList(Message m ){	
		String onlinefriend[] = m.getContent().split(" ");
		for(int j=0; j<onlinefriend.length; j++){
			if(!onlinefriend[j].equals(ownerId)){
	            System.out.println("我在执行里面");
				jbls[++i] = new JLabel(onlinefriend[j]+"",new ImageIcon("image/tu1.jpg"),JLabel.LEFT);
			    jbls[i].setEnabled(true);
				jbls[i].addMouseListener(this);
				jphy2.add(jbls[i]);
			}
				
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==jphy_jb2){
		   c1.show(this.getContentPane(), "2");
		}else if(e.getSource()==jpmsr_jb1){
		   c1.show(this.getContentPane(), "1");
		}else if(e.getSource()==jphy_jb3){
			if(ManageGroupChat.getGChat(ownerId+" group")==null){
				ManageGroupChat.addChat(ownerId+" group", new GroupChat(ownerId));
			}
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		// 响应用户双击的事件，并得到好友的编号。
		if(e.getClickCount()==2){
		   //得到好友的编号
		   String friendNo = ((JLabel)e.getSource()).getText();
		   //System.out.println("你希望和"+friendNo+"聊天");
		   String key =ownerId+"_"+friendNo;
		   if(ManageChat.getChat(key)!=null){
			   JOptionPane.showMessageDialog(this,"提示：您已经打开了一个与该用户的会话窗口，无需再次打开！");
		   }else{
			   Chat chat = new Chat(ownerId,friendNo);
			   //将聊天界面加入到管理类
			   ManageChat.addChat(key,chat);
		   }     
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		jl.setForeground(Color.BLUE);
	}
	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		JLabel jl = (JLabel)e.getSource();
		jl.setForeground(Color.black);
	}
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
