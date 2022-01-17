package com.yychatclient.control;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import com.yychat.model.Message;
import com.yychat.view.ClientLogin;
import com.yychat.view.FriendChat;
import com.yychat.view.FriendList;

public class ClientReceiverThread extends Thread {
	
	private Socket CreceiveThreadS;
	private volatile boolean isRunning;

	public ClientReceiverThread(Socket creceiveThreadS) {//����ÿ���ͻ���socket
		CreceiveThreadS = creceiveThreadS;
		this.isRunning=true;
	}
	
	public Socket getCreceiveThreadS() {
		return CreceiveThreadS;
	}
	
	public void myStop(){
		isRunning=false;
	}

	@Override
	public void run() {
		ObjectInputStream ois;
		try {
			while(isRunning) {//�ͻ��˶�ν��ܷ������˷�������Ϣ
				ois = new ObjectInputStream(CreceiveThreadS.getInputStream());
				Message ms=(Message)ois.readObject();
				String sender=ms.getSender();
				String receiver=ms.getReceiver();			
				
				//���������ߺ��ѵ�����
				if(ms.getMessageType().equals(Message.NEW_ONLINE_FRIENDR)) {
					System.out.println(sender+"�����ˣ�ͼ�걻����");
					//���������ߺ��ѵ�ͼ��
					FriendList friendList=ManageAllList.getFriendListFrame(receiver);
					friendList.updateOnlineFriends(ms);
				}		
				//�������߻����������
				else if(ms.getMessageType().equals(Message.NOT_ONLINE)) {
					System.out.println(sender+"�����ˣ�ͼ����");
					//���������ߺ��ѵ�ͼ��
					FriendList friendList=ManageAllList.getFriendListFrame(receiver);
					friendList.updateOnlineFriends(ms);
				}
				
				else if(ms.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					System.out.println(ms.getSender()+"��"+ms.getReceiver()+"˵��"+ms.getChatContent());
					//�ڽ��շ��ĺ������������ʾ��������ת������������Ϣ
					//�õ����շ��ĺ�������������
					String frameName=receiver+sender;//ע��sender��receiver��λ�û���	
					ManageAllChat.getChatFrame(frameName).showMessage(ms, false);			
					//��message����Ϣ������friendChat����ʾ����
					//friendChat.appendMessagee(ms);
				}
				
				
				//�����Ѿ����ߺ���
				else if(ms.getMessageType().equals(Message.RESPONSE_ONLINE_FRIEND)) {
					System.out.println(sender+"�����ߺ��ѵ����֣�"+ms.getChatContent()+"�����Լ�");
					
					FriendList friendList=ManageAllList.getFriendListFrame(sender);
					friendList.updateOnlineFriends(ms);				
				}
				
				//�ڿͻ��˴�����Ӻ����Ƿ�ɹ��Ĵ���
				if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_NO_USER)) {
					JOptionPane.showMessageDialog(null, "���û������ڣ���Ӻ���ʧ�ܣ�");
				}else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND)) {
					JOptionPane.showMessageDialog(null, "���û��Ѿ��Ǻ��ѣ��벻Ҫ�ظ���ӣ�");
				}else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_SUCCESS)) {
					JOptionPane.showMessageDialog(null, "��Ӻ��ѳɹ�����������֤�����б�");
					//���º����б�ͼ��
					FriendList friendList=ManageAllList.getFriendListFrame(sender);
					friendList.updateList(friendList, ms);
				}else if(ms.getMessageType().equals(Message.OTHERS_ADD_YOU)) {
					JOptionPane.showMessageDialog(null, ms.getSender()+"�����Ϊ����");
					FriendList friendList=ManageAllList.getFriendListFrame(ms.getReceiver());
					friendList.updateList(friendList, ms);
				}
				
				else if(ms.getMessageType().equals(Message.IS_FRIEND)) {
					String ownerId=ms.getChatContent().substring(0,6);
					FriendList friendList=ManageAllList.getFriendListFrame(ownerId);
					friendList.openChat(ms);
				}else if(ms.getMessageType().equals(Message.NOT_FRIEND)) {
					JOptionPane.showMessageDialog(null,"����Ҫ��ӶԷ�Ϊ���ѣ����ܸ��Է����ͻỰ��Ϣ");
				}else if(ms.getMessageType().equals(Message.REMOVE_FRIEND)) {
					JOptionPane.showMessageDialog(null, "ɾ�����ѳɹ�����������֤�����б�");
					FriendList friendList=ManageAllList.getFriendListFrame(sender);
					friendList.updateList(friendList, ms);
				}
				
				else if (ms.getMessageType().equals(Message.SEEK_PAST_MESSAGE)) {
					FriendChat friendChat=ManageAllChat.getChatFrame(sender+receiver);
					String content=ms.getChatContent();
					if(content.equals("")||content==null){				
					}else {
						ms.setSender(receiver);
						friendChat.showHistoryMessage(ms);
					}
				}
				else if (ms.getMessageType().equals(Message.LODING_OFFLINE_MESSAGE)) {
					FriendChat friendChat=ManageAllChat.getChatFrame(receiver+sender);
					String content=ms.getChatContent();
					if(!(content.equals("")||content==null)){				
						friendChat.showOfflineMessage(ms);
						JOptionPane.showMessageDialog(friendChat, "�յ�����Ϣ��");
					}
				}
				
				
				if(ms.getMessageType().equals(Message.SERVER_CLOSE)) {
					String toId = receiver;
					//�Զ�����
					ManageAllList.getFriendListFrame(toId).sendUnloadMsgToServer();
					ManageAllThreads.removeThread(toId);
					ManageAllList.removeFriendListFrame(toId);
				}
				
			}			
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
}
