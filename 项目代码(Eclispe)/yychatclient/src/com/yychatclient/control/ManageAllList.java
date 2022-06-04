package com.yychatclient.control;
import java.util.Hashtable;

import com.yychat.view.FriendList;
public class ManageAllList {
	
	private static Hashtable<String, FriendList> friendListFrames = new Hashtable<>();

    public static void addFriendListFrame(String frameName,FriendList fl){
        friendListFrames.put(frameName,fl);
    }

    public static FriendList getFriendListFrame(String frameName){
        return friendListFrames.get(frameName);
    }

    public static FriendList removeFriendListFrame(String frameName){
        return friendListFrames.remove(frameName);
    }
}
