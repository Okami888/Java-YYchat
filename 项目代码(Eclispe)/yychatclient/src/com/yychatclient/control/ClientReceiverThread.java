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

	public ClientReceiverThread(Socket creceiveThreadS) {//依附每个客户端socket
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
			while(isRunning) {//客户端多次接受服务器端发来的信息
				ois = new ObjectInputStream(CreceiveThreadS.getInputStream());
				Message ms=(Message)ois.readObject();
				String sender=ms.getSender();
				String receiver=ms.getReceiver();			
				
				//接收新上线好友的名字
				if(ms.getMessageType().equals(Message.NEW_ONLINE_FRIENDR)) {
					System.out.println(sender+"上线了，图标被激活");
					//激活新上线好友的图标
					FriendList friendList=ManageAllList.getFriendListFrame(receiver);
					friendList.updateOnlineFriends(ms);
				}		
				//处理下线或者隐身好友
				else if(ms.getMessageType().equals(Message.NOT_ONLINE)) {
					System.out.println(sender+"下线了，图标变灰");
					//激活新上线好友的图标
					FriendList friendList=ManageAllList.getFriendListFrame(receiver);
					friendList.updateOnlineFriends(ms);
				}
				
				else if(ms.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					System.out.println(ms.getSender()+"对"+ms.getReceiver()+"说："+ms.getChatContent());
					//在接收方的好友聊天界面显示服务器端转发来的聊天信息
					//拿到接收方的好友聊天界面对象
					String frameName=receiver+sender;//注意sender与receiver的位置互换	
					ManageAllChat.getChatFrame(frameName).showMessage(ms, false);			
					//把message的消息内容在friendChat上显示处理
					//friendChat.appendMessagee(ms);
				}
				
				
				//激活已经在线好友
				else if(ms.getMessageType().equals(Message.RESPONSE_ONLINE_FRIEND)) {
					System.out.println(sender+"的在线好友的名字："+ms.getChatContent()+"除了自己");
					
					FriendList friendList=ManageAllList.getFriendListFrame(sender);
					friendList.updateOnlineFriends(ms);				
				}
				
				//在客户端处理添加好友是否成功的代码
				if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_NO_USER)) {
					JOptionPane.showMessageDialog(null, "该用户不存在，添加好友失败！");
				}else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND)) {
					JOptionPane.showMessageDialog(null, "该用户已经是好友，请不要重复添加！");
				}else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND_SUCCESS)) {
					JOptionPane.showMessageDialog(null, "添加好友成功！请重启验证好友列表！");
					//更新好友列表图标
					FriendList friendList=ManageAllList.getFriendListFrame(sender);
					friendList.updateList(friendList, ms);
				}else if(ms.getMessageType().equals(Message.OTHERS_ADD_YOU)) {
					JOptionPane.showMessageDialog(null, ms.getSender()+"把你加为好友");
					FriendList friendList=ManageAllList.getFriendListFrame(ms.getReceiver());
					friendList.updateList(friendList, ms);
				}
				
				else if(ms.getMessageType().equals(Message.IS_FRIEND)) {
					String ownerId=ms.getChatContent().substring(0,6);
					FriendList friendList=ManageAllList.getFriendListFrame(ownerId);
					friendList.openChat(ms);
				}else if(ms.getMessageType().equals(Message.NOT_FRIEND)) {
					JOptionPane.showMessageDialog(null,"你需要添加对方为好友，才能给对方发送会话信息");
				}else if(ms.getMessageType().equals(Message.REMOVE_FRIEND)) {
					JOptionPane.showMessageDialog(null, "删除好友成功！请重启验证好友列表！");
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
						JOptionPane.showMessageDialog(friendChat, "收到新消息！");
					}
				}
				
				
				if(ms.getMessageType().equals(Message.SERVER_CLOSE)) {
					String toId = receiver;
					//自动下线
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
