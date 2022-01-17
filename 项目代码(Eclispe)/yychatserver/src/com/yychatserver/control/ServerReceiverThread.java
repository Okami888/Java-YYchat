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
	public ServerReceiverThread(Socket SreceiveThreadS) {//依附每个客户的服务端socket
		this.SreceiveThreadS=SreceiveThreadS;
		this.isRunning=true;
	}
	public Socket getSreceiveThreadS() {
        return SreceiveThreadS;
    }
	public void myStop(){
        isRunning = false;
    }
	
	//重写run()方法，来接受客户端发送来的Message对象

	@Override
	public void run() {
		
		try {
			while(isRunning) {//服务器接受多次发送的信息
				ObjectInputStream ois = new ObjectInputStream(SreceiveThreadS.getInputStream());
				Message ms=(Message)ois.readObject();
				String sender=ms.getSender();
							
				//拿到新上线好友的其他在线好友名字
				if(ms.getMessageType().equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
					System.out.println(sender+"上线了，准备转发上线消息");
					setHidden(false);
					Set onlineFriendSet=ManageAllThreads.hmSocket.keySet();//拿到键的信息，即所有上线好友名称的一张列表
					Iterator it=onlineFriendSet.iterator();//创建迭代器对象
					while(it.hasNext()) {
						String friendName=(String)it.next();
						//拿到其他已上线好友的Socket
						if(!(friendName.equals(sender))) {
							ServerReceiverThread receiverSocket=ManageAllThreads.getServerReceiverThread(friendName);
							ms.setReceiver(friendName);
							ms.setChatContent(ManageAllThreads.getOnLineList());
							ms.setMessageType(Message.NEW_ONLINE_FRIENDR);
							//向其他已上线好友发送sender上线的信息
							sendMessage(receiverSocket, ms);
						}
					}
					System.out.println(sender+"上线了，它的线程遮挡是"+isHidden);	
				}
				//收到下线或者隐身信息
				else if(ms.getMessageType().equals(Message.NOT_ONLINE)) {
					System.out.println(sender+"下线了，准备图标变灰");
					setHidden(true);
					Set onlineFriendSet=ManageAllThreads.hmSocket.keySet();//拿到键的信息，即所有上线好友名称的一张列表
					Iterator it=onlineFriendSet.iterator();//创建迭代器对象
					while(it.hasNext()) {
						String friendName=(String)it.next();
						//拿到其他已上线好友的Socket
						if(!(friendName.equals(sender))) {
							ServerReceiverThread receiverSocket=ManageAllThreads.getServerReceiverThread(friendName);
							ms.setReceiver(friendName);
							ms.setChatContent(ManageAllThreads.getOnLineList());
							//向其他已上线好友发送sender上线的信息
							sendMessage(receiverSocket, ms);
						}
					}
					System.out.println(sender+"下线了，它的线程遮挡是"+isHidden);
				}
						
				//接受并转发好友信息
				else if(ms.getMessageType().equals(Message.COMMON_CHAT_MESSAGE)) {
					
					String receiver=ms.getReceiver();
					String content=ms.getChatContent();
					String sendtime=ms.getSendTime();
					ServerReceiverThread serverReceiverThread=ManageAllThreads.getServerReceiverThread(receiver);
					if(null==serverReceiverThread) {
						DBUtil.savetempMessage(sender, receiver, content, sendtime);
						DBUtil.tempToMessage(sender, receiver);
					}else {
						//保存聊天信息到数据库的message表中
						DBUtil.saveMessage(sender,receiver,content,sendtime);	
						sendMessage(serverReceiverThread, ms);
					}				
					//利用前面保存好的用户名和对应的socket对象，取出接收者的socket对象			
				}
				
				//服务器端接收到添加好友的信息，并判断是否能添加好友
				else if (ms.getMessageType().equals(Message.ADD_NEW_FRIEND)) {
					String newFriendName=ms.getChatContent();
					System.out.println("欲添加好友："+newFriendName);
					if (DBUtil.seekUser(newFriendName)) {//为真说明该用户存在
						if (DBUtil.seekUserRelation(sender,newFriendName,"1")) {//为真说明已经是好友，不能重复添加
							ms.setMessageType(Message.ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND);
						}else {//为假说明还不是好友，可以添加
							DBUtil.insertIntoUserRelation(sender, newFriendName,"1");
							DBUtil.insertIntoUserRelation(newFriendName, sender,"1");
							String allFriend=DBUtil.seekAllFriend(sender);
							ms.setChatContent(allFriend);
							ms.setMessageType(Message.ADD_NEW_FRIEND_SUCCESS);
							//发送信息到客户端处理
							if(null != ManageAllThreads.getServerReceiverThread(newFriendName)) {
								String allFriend2=DBUtil.seekAllFriend(ms.getReceiver());
								ms.setChatContent(allFriend2);
								ms.setMessageType(Message.OTHERS_ADD_YOU);
								sendMessage(ManageAllThreads.getServerReceiverThread(newFriendName), ms);
							}else {
								sendMessage(SreceiveThreadS, ms);
							}
						}
					}else {//为假说明该用户不存在
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
					String userID=ms.getReceiver();//就是自己
					String friendID=sender;		
					String Content=DBUtil.seekTempMessage(userID, friendID);
					ms.setChatContent(Content);
					DBUtil.deleteTempMessage(userID, friendID);//清除临时表
					
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
				
				
				//拿到在线好友名字
				if(ms.getMessageType().equals(Message.REQUEST_ONLINE_FRIEND)) {
					System.out.println("服务器收到"+sender+"发送的请求获取在线好友名称的信息");
					ms.setChatContent(ManageAllThreads.getOnLineList());							
					//把在线好友发给客户端
					ms.setMessageType(Message.RESPONSE_ONLINE_FRIEND);
					sendMessage(SreceiveThreadS, ms);
				}
				
				
				
				if(ms.getMessageType().equals(Message.UNLOAD_LOGIN)) {									
					ManageAllThreads.removeServerReceiverThread(sender);
					myStop();
					StartServer.showMsg("用户"+sender+"退出登录！");
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
