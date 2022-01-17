package com.yychatserver.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;
import com.yychatserver.view.StartServer;

public class ServerReceiverThread extends Thread{
	private Socket SreceiveThreadS;
	private volatile boolean isRunning;
	private volatile boolean isHidden;
	
	public boolean isHidden() {
		return isHidden;
	}
	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}
	public ServerReceiverThread(Socket SreceiveThreadS) {//����ÿ���ͻ��ķ����socket
		this.SreceiveThreadS=SreceiveThreadS;
		this.isRunning=true;
	}
	public Socket getSreceiveThreadS() {
        return SreceiveThreadS;
    }
	public void myStop(){
        isRunning = false;
    }
	
	//��дrun()�����������ܿͻ��˷�������Message����

	@Override
	public void run() {
		
		try {
			while(isRunning) {//���������ܶ�η��͵���Ϣ
				ObjectInputStream ois = new ObjectInputStream(SreceiveThreadS.getInputStream());
				Message ms=(Message)ois.readObject();
				String sender=ms.getSender();
							
				//�õ������ߺ��ѵ��������ߺ�������
				if(ms.getMessageType().equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
					System.out.println(sender+"�����ˣ�׼��ת��������Ϣ");
					setHidden(false);
					Set onlineFriendSet=ManageAllThreads.hmSocket.keySet();//�õ�������Ϣ�����������ߺ������Ƶ�һ���б�
					Iterator it=onlineFriendSet.iterator();//��������������
					while(it.hasNext()) {
						String friendName=(String)it.next();
						//�õ����������ߺ��ѵ�Socket
						if(!(friendName.equals(sender))) {
							ServerReceiverThread receiverSocket=ManageAllThreads.getServerReceiverThread(friendName);
							ms.setReceiver(friendName);
							ms.setChatContent(ManageAllThreads.getOnLineList());
							ms.setMessageType(Message.NEW_ONLINE_FRIENDR);
							//�����������ߺ��ѷ���sender���ߵ���Ϣ
							sendMessage(receiverSocket, ms);
						}
					}
					System.out.println(sender+"�����ˣ������߳��ڵ���"+isHidden);	
				}
				//�յ����߻���������Ϣ
				else if(ms.getMessageType().equals(Message.NOT_ONLINE)) {
					System.out.println(sender+"�����ˣ�׼��ͼ����");
					setHidden(true);
					Set onlineFriendSet=ManageAllThreads.hmSocket.keySet();//�õ�������Ϣ�����������ߺ������Ƶ�һ���б�
					Iterator it=onlineFriendSet.iterator();//��������������
					while(it.hasNext()) {
						String friendName=(String)it.next();
						//�õ����������ߺ��ѵ�Socket
						if(!(friendName.equals(sender))) {
							ServerReceiverThread receiverSocket=ManageAllThreads.getServerReceiverThread(friendName);
							ms.setReceiver(friendName);
							ms.setChatContent(ManageAllThreads.getOnLineList());
							//�����������ߺ��ѷ���sender���ߵ���Ϣ
							sendMessage(receiverSocket, ms);
						}
					}
					System.out.println(sender+"�����ˣ������߳��ڵ���"+isHidden);
				}
						
				//���ܲ�ת��������Ϣ
				else if(ms.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					
					String receiver=ms.getReceiver();
					String content=ms.getChatContent();
					String sendtime=ms.getSendTime();
					ServerReceiverThread serverReceiverThread=ManageAllThreads.getServerReceiverThread(receiver);
					if(null==serverReceiverThread) {
						DBUtil.savetempMessage(sender, receiver, content, sendtime);
						DBUtil.tempToMessage(sender, receiver);
					}else {
						//����������Ϣ�����ݿ��message����
						DBUtil.saveMessage(sender,receiver,content,sendtime);	
						sendMessage(serverReceiverThread, ms);
					}				
					//����ǰ�汣��õ��û����Ͷ�Ӧ��socket����ȡ�������ߵ�socket����			
				}
				
				//�������˽��յ���Ӻ��ѵ���Ϣ�����ж��Ƿ�����Ӻ���
				else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND)) {
					String newFriendName=ms.getChatContent();
					System.out.println("����Ӻ��ѣ�"+newFriendName);
					if (DBUtil.seekUser(newFriendName)) {//Ϊ��˵�����û�����
						if (DBUtil.seekUserRelation(sender,newFriendName,"1")) {//Ϊ��˵���Ѿ��Ǻ��ѣ������ظ����
							ms.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
						}else {//Ϊ��˵�������Ǻ��ѣ��������
							DBUtil.insertIntoUserRelation(sender, newFriendName,"1");
							DBUtil.insertIntoUserRelation(newFriendName, sender,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							ms.setChatContent(allFriend);
							ms.setMessageType(Message.ADD_NEW_FRIEND_SUCCESS);
							//������Ϣ���ͻ��˴���
							if(null != ManageAllThreads.getServerReceiverThread(newFriendName)) {
								String allFriend2=DBUtil.seekAllFriend(ms.getReceiver());
								ms.setChatContent(allFriend2);
								ms.setMessageType(Message.OTHERS_ADD_YOU);
								sendMessage(ManageAllThreads.getServerReceiverThread(newFriendName), ms);
							}else {
								sendMessage(SreceiveThreadS, ms);
							}
						}
					}else {//Ϊ��˵�����û�������
						ms.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_NO_USER);
					}										
				}
				else if (ms.getMessageType().equals(Message.VALIDATE_FRIEND)) {
					String ownerId=ms.getChatContent().substring(0,6);
					String validateId=ms.getReceiver();
					if (DBUtil.seekUserRelation(ownerId,validateId,"1")) {
						ms.setMessageType(Message.IS_FRIEND);
					}else {
						ms.setMessageType(Message.NOT_FRIEND);
					}
					sendMessage(SreceiveThreadS, ms);
				}
				else if (ms.getMessageType().equals(Message.SEEK_PAST_MESSAGE)) {
					String friendID=ms.getReceiver();
					String Content=DBUtil.seekMessage(sender, friendID);
					ms.setChatContent(Content);
					sendMessage(SreceiveThreadS, ms);
				}
				else if (ms.getMessageType().equals(Message.LODING_OFFLINE_MESSAGE)) {
					String userID=ms.getReceiver();//�����Լ�
					String friendID=sender;		
					String Content=DBUtil.seekTempMessage(userID, friendID);
					ms.setChatContent(Content);
					DBUtil.deleteTempMessage(userID, friendID);//�����ʱ��
					
					sendMessage(SreceiveThreadS, ms);
				}
				else if(ms.getMessageType().equals(Message.REMOVE_FRIEND)){
					String receiver=ms.getReceiver();
					DBUtil.deleteFromUserRelation(sender, receiver);
					DBUtil.deleteFromUserRelation(receiver, sender);
					String allFriend=DBUtil.seekAllFriend(sender);
					ms.setChatContent(allFriend);
					sendMessage(SreceiveThreadS, ms);
				}
				
				
				//�õ����ߺ�������
				if(ms.getMessageType().equals(Message.REQUEST_ONLINE_FRIEND)) {
					System.out.println("�������յ�"+sender+"���͵������ȡ���ߺ������Ƶ���Ϣ");
					ms.setChatContent(ManageAllThreads.getOnLineList());							
					//�����ߺ��ѷ����ͻ���
					ms.setMessageType(Message.RESPONSE_ONLINE_FRIEND);
					sendMessage(SreceiveThreadS, ms);
				}
				
				
				
				if(ms.getMessageType().equals(Message.UNLOAD_LOGIN)) {									
					ManageAllThreads.removeServerReceiverThread(sender);
					myStop();
					StartServer.showMsg("�û�"+sender+"�˳���¼��");
				}		
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public void sendMessage(Socket s,Message ms) {	
		ObjectOutputStream oos;
		try {
			oos=new ObjectOutputStream(s.getOutputStream());
			oos.writeObject(ms);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	public void sendMessage(ServerReceiverThread s,Message ms) {	
		ObjectOutputStream oos;
		try {
			oos=new ObjectOutputStream(s.getSreceiveThreadS().getOutputStream());
			oos.writeObject(ms);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
}
