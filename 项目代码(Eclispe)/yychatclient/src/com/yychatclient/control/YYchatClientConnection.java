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
			
			clientS = new Socket("127.0.0.1",3456);//127.0.0.1Ϊ�ز��ַ����ת������IP��ַ�����ӵ�����������ip��ַ��������˿ںš���ַ������
			System.out.println("����������ӳɹ�"+clientS);
			
		} catch (UnknownHostException e) {			
			e.printStackTrace();
		} catch (IOException e) {			
			e.printStackTrace();
		}
		
		
	}
	public Message loginValidate(User user) {		
		OutputStream os;	//���Ż�	
		Message ms=null;
		try {			
			os=clientS.getOutputStream();//�ڵ���
			ObjectOutputStream oos=new ObjectOutputStream(os);//��װ��
			oos.writeObject(user);//���л�
			
			//��������Server�˵���Ϣ
			ObjectInputStream ois=new ObjectInputStream(clientS.getInputStream());
			ms=(Message)ois.readObject();
			if(ms.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
				//��½�ɹ���Ӧ��׼������������Ϣ���߳�
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
