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
     * �����߳�����
     */
    public void myStop() {
        isRunning = false;
        close(ss);
    }
	
	@Override
	public void run() {
		try {			
			ss=new ServerSocket(3456);//ֻ�з���˵Ķ˿ں���ָ�����û��˶˿�ÿ�ζ����Զ��ı�

			//��while�ܶ�ε�¼�û�
			while(true) {
				serverS=ss.accept();//�ȴ��ͻ��˵����ӣ�����������һ��acceptֻ�ܺ�һ���ͻ�������
				System.out.println("һ���ͻ��˽������ӳɹ�"+serverS);/*����������ӳɹ�Socket[addr=/127.0.0.1,port=3456,localport=63038]
			                                  			portΪԶ�����ӵĶ˿ںţ�local��Ϊ��ǰ�Ķ˿ں�*/
			
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
					userName=DBUtil.Register();//��������ȡ���û����˺�
					DBUtil.updateUser(userName, webName, passWord, email);//�����û����������䶼д�����ݿ�
					ms.setMessageType(Message.USER_REGISTER_SUCCESS);//ע���Ǳسɹ���
					ms.setChatContent(userName);
				}
				
				//Message��Ϣ��������success��failure����֤��Ϣ�����д��䣬������������
				if(userType.equals(User.USER_LOGIN_VALIDATE)) {
					if(ManageAllThreads.getServerReceiverThread(userName)==null) {//�жϸ��˻��Ƿ��Ѿ���¼
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
							sr.start();//Ϊÿ����¼�ɹ��û�����һ��������߳�		
							ManageAllThreads.addServerReceiverThread(userName,sr);//����ÿ����¼�ɹ����û����Ͷ�Ӧ��Socket����
							StartServer.showMsg("�û�"+userName+"�ɹ���¼��");

						}else {
							ms.setMessageType(Message.LOGIN_VALIDATE_FAILURE);
						}
					}else {
						ms.setMessageType(Message.ALREAD_LOGIN);
					}										
					//serverS.close();���ܹرգ��൱�ڹر����̣߳��Ͳ��ܳ������졣
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
				//serverS.close();���ܹرգ��൱�ڹر����̣߳��Ͳ��ܳ������졣
			}
			
		} catch (IOException e) {			
			close(oos,ois,serverS,ss);
		}catch (ClassNotFoundException e) {			
			e.printStackTrace();
		}
	}
	private void close(Closeable... ios) {//�ɱ䳤����
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
