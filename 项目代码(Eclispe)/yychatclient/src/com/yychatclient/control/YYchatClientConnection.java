package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.yychat.model.Message;
import com.yychat.model.User;


public class YYchatClientConnection { 
	
	public static Socket clientS;
	public YYchatClientConnection() {
		
	try {
			
			clientS = new Socket("127.0.0.1",3456);//127.0.0.1为回测地址，即转到本机IP地址。连接到服务器所在ip地址与服务器端口号。地址与名字
			System.out.println("与服务器连接成功"+clientS);
			
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	public Message loginValidate(User user) {		
		OutputStream os;	//可优化	
		Message ms=null;
		try {			
			os=clientS.getOutputStream();//节点流
			ObjectOutputStream oos=new ObjectOutputStream(os);//封装流
			oos.writeObject(user);//序列化
			
			//接受来自Server端的信息
			ObjectInputStream ois=new ObjectInputStream(clientS.getInputStream());
			ms=(Message)ois.readObject();
			if(ms.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
				//登陆成功后应该准备接受聊天信息的线程
				ClientReceiverThread clientReceiverThread=new ClientReceiverThread(clientS);
				clientReceiverThread.start();
				ManageAllThreads.addThread(ms.getSender(), clientReceiverThread);
			}		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return ms;
	}
	
	
	public String registerUser(User user) {
		String userName="";
		OutputStream os;	
		Message ms=null;
		try {		
			os=clientS.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(user);
			
			ObjectInputStream ois=new ObjectInputStream(clientS.getInputStream());
			ms=(Message)ois.readObject();
			if(ms.getMessageType().equals(Message.USER_REGISTER_SUCCESS)) {
				userName=ms.getChatContent();
			}
			//clientS.close();		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return userName;		
	}
	public boolean seekPassword(User user) {
		boolean seekSuccess=false;
		OutputStream os;	
		Message ms=null;
		try {		
			os=clientS.getOutputStream();
			ObjectOutputStream oos=new ObjectOutputStream(os);
			oos.writeObject(user);
			
			ObjectInputStream ois=new ObjectInputStream(clientS.getInputStream());
			ms=(Message)ois.readObject();
			if(ms.getMessageType().equals(Message.SEEK_PASSWORD_SUCCESS)) {
				seekSuccess=true;
			}
			//clientS.close();		
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return seekSuccess;		
	}
	
}
