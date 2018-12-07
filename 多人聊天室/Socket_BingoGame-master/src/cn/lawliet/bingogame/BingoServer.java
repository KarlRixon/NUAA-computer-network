package cn.lawliet.bingogame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.HashSet;

/**
 * 游戏的服务端
 * 
 * @author Lawliet
 *
 */
public class BingoServer extends Thread {

	Socket socket;
	static int res = 1;
	//static int res = (int) (Math.random() * 10);
	static boolean flag = false;
	
	static HashSet<String> ips = new HashSet<String>();
	public BingoServer(Socket socket){
		this.socket = socket;
	}
	
	@Override
		public void run() {
			try {
				//获取到Socket的输入流对象
				BufferedReader socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				// 获取到Socket输出流对象
				OutputStreamWriter socketOut = new OutputStreamWriter(socket.getOutputStream());
				// 获取键盘的输入流对象
				BufferedReader keyReader = new BufferedReader(new InputStreamReader(System.in));
				// 读取客户端的数据
				OutputStream ops = socket.getOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(ops);
				String line = null;
				while ((line = socketReader.readLine()) != null) {
					String ip = socket.getInetAddress().getHostAddress();
		            if(ips.add(ip)) {
		                System.out.println("恭喜" + ip + "同学成功连接服务器！");
		            }
					System.out.println(ip + "同学发来的数字是  ：" + line);
					String bingo = Integer.toString(res);
					if (line.equals(bingo)) {
						line = "恭喜你猜对了！";
						flag = true;
						oos.writeObject(flag);
						socketOut.write(line + "\r\n");
						socketOut.flush();
					} else {
						line = "很遗憾，没有猜对，再试一次！";
						oos.writeObject(flag);
						socketOut.write(line + "\r\n");
						socketOut.flush();
					}
					if(flag){
						line = "恭喜" + ip + "同学获得胜利！";
						socketOut.write(line + "\r\n");
					}
					
				}
				// 关闭资源
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
				//System.out.println("出现了异常！");
			}
		}	
	
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(9090);
		while(true){
			Socket socket = serverSocket.accept();
			new BingoServer(socket).start();
		}
	}
	
}