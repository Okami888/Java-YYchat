package com.yychatserver.control;



import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Closeable;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;


import com.yychat.model.Message;
import com.yychat.model.User;
import com.yychatserver.control.ManageAllThreads;
import com.yychatserver.control.ServerReceiverThread;
import com.yychatserver.view.StartServer;

public class YYchatServer implements Runnable {

	private ServerSocket ss;
	Socket serverS;
	ObjectInputStream ois;
	ObjectOutputStream oos;
	private volatile boolean isRunning;
	
	public YYchatServer() {
        isRunning = true;
        new Thread(this).start();
	}
	
	/**
     * 结束线程运行
     */
    public void myStop() {
        isRunning = false;
        close(ss);
    }
	
	@Override
	public void run() {
		try {			
			ss=new ServerSocket(3456);//只有服务端的端口号能指定，用户端端口每次都会自动改变

			//用while能多次登录用户
			while(true) {
				serverS=ss.accept();//等待客户端的连接，程序阻塞，一次accept只能和一个客户端连接
				System.out.println("一个客户端建立连接成功"+serverS);/*与服务器连接成功Socket[addr=/127.0.0.1,port=3456,localport=63038]
			                                  			port为远端连接的端口号，local的为当前的端口号*/
			
				InputStream is=serverS.getInputStream();
				ois=new ObjectInputStream(is);
				User user=(User) ois.readObject();
				String passWord=user.getPassWord();
				String userName=user.getUserName();
				String webName=user.getWebName();
				String email=user.getEmail();
				String userType=user.getUserType();
							
				Message ms =new Message();
				
				if(userType.equals(User.USER_REGISTER)) {		
					userName=DBUtil.Register();//创建并获取新用户的账号
					DBUtil.updateUser(userName, webName, passWord, email);//把新用户的密码邮箱都写进数据库
					ms.setMessageType(Message.USER_REGISTER_SUCCESS);//注册是必成功的
					ms.setChatContent(userName);
				}
				
				//Message消息类来承载success和failure的验证信息，进行传输，来解决这个问题
				if(userType.equals(User.USER_LOGIN_VALIDATE)) {
					if(ManageAllThreads.getServerReceiverThread(userName)==null) {//判断该账户是否已经登录
						if(DBUtil.loginValidate(userName,passWord)) {
							
							String allFriend=DBUtil.seekAllFriend(userName);
							String webnames=DBUtil.seekWebname(userName);
							
							System.out.println("username=["+userName+"]");
							System.out.println("webname=["+webnames+"]");
							
							ms.setChatContent(allFriend);
							ms.setSenderWebname(webnames);
							ms.setSender(userName);
							ms.setMessageType(Message.LOGIN_VALIDATE_SUCCESS);
							
							ServerReceiverThread sr=new ServerReceiverThread(serverS);
							sr.start();//为每个登录成功用户建立一个服务端线程		
							ManageAllThreads.addServerReceiverThread(userName,sr);//保存每个登录成功的用户名和对应的Socket对象
							StartServer.showMsg("用户"+userName+"成功登录！");

						}else {
							ms.setMessageType(Message.LOGIN_VALIDATE_FAILURE);
						}
					}else {
						ms.setMessageType(Message.ALREAD_LOGIN);
					}										
					//serverS.close();不能关闭，相当于关闭了线程，就不能持续聊天。
				}
				
				if(userType.equals(User.USER_SEEK_PASSWORD)) {
					if(DBUtil.seekEmail(userName)) {
						DBUtil.seekPassword(userName);
						ms.setMessageType(Message.SEEK_PASSWORD_SUCCESS);
					}
					else {
						ms.setMessageType(Message.SEEK_PASSWORD_FAILURE);
					}
				}
				
				oos =new ObjectOutputStream(serverS.getOutputStream());
				oos.writeObject(ms);
				//close(oos,ois,serverS);
				//serverS.close();不能关闭，相当于关闭了线程，就不能持续聊天。
			}
			
		} catch (IOException e) {			
			close(oos,ois,serverS,ss);
		}catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
	}
	private void close(Closeable... ios) {//可变长参数
        for(Closeable io: ios) {
            try {
                if(null != io)
                    io.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
	

}
