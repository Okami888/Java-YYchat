package com.yychatclient.control;
import java.util.Hashtable;
public class ManageAllThreads {
	 private static Hashtable<String,ClientReceiverThread> threads = new Hashtable<>();

	    public static void addThread(String uid,ClientReceiverThread thread){
	        threads.put(uid,thread);
	    }

	    public static ClientReceiverThread getThread(String uid){
	        return threads.get(uid);
	    }

	    public static void removeThread(String uid){
	        threads.remove(uid);
	    }
}
