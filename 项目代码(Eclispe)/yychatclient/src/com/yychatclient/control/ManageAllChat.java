package com.yychatclient.control;

import java.util.Hashtable;
import com.yychat.view.FriendChat;

public class ManageAllChat {
	 public static Hashtable<String,FriendChat> chatFrames = new Hashtable<>();

	    public static void addChatFrame(String frameName,FriendChat chat){
	    	chatFrames.put(frameName,chat);
	    }

	    public static FriendChat getChatFrame(String frameName){
	        return chatFrames.get(frameName);
	    }
	    
	    public static FriendChat removeChatFrame(String frameName){
	        return chatFrames.remove(frameName);
	    }
}
