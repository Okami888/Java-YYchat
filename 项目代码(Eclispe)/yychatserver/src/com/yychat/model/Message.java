package com.yychat.model;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable,MessageType{
	String messageType;
	String sender;
	String senderWebname;	
	String receiver;
	String chatContent;
	String status;
	String sendTime;
	
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
	public String getSenderWebname() {
		return senderWebname;
	}

	public void setSenderWebname(String senderWebname) {
		this.senderWebname = senderWebname;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	public String getOnLineFriend() {
		return status;
	}

	public void setOnLineFriend(String onLineFriend) {
		this.status = onLineFriend;
	}
	
	
	
	
	
	
}
