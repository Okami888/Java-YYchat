package com.yychatserver.control;

import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import com.yychatserver.control.ServerReceiverThread;

	/**
	 * 管理所有与客户端的线程
	 */
	public class ManageAllThreads {

	    public static Hashtable<String,ServerReceiverThread> hmSocket = new Hashtable<>();
	    
	 
		public static Hashtable<String, ServerReceiverThread> getServerReceiverThreads() {
	        return hmSocket;
	    }
		public static ServerReceiverThread getServerReceiverThread(String uid){
	        return hmSocket.get(uid);
	    }
		
		
	    public static void addServerReceiverThread(String uid, ServerReceiverThread thread){
	    	hmSocket.put(uid,thread);
	    }    
	    public static void removeServerReceiverThread(String uid){
	    	hmSocket.remove(uid);
	    }

	    /**
	     * 返回当前在线全部用户
	     * @return
	     */
	    public static String getOnLineList(){
	        StringBuilder sb = new StringBuilder();
	        String itString="";
	        Iterator it = hmSocket.keySet().iterator();//拿到键的信息，即所有上线好友名称的一张列表//创建迭代器对象
	        while(it.hasNext()){//注意迭代器的循环中只能出现一次it.next否则会报错
	        	
	        	itString =it.next().toString();
	        	if(hmSocket.get(itString).isHidden()) {//跳过隐身的好友检索
	        		continue;
	        	}
	        	sb.append(itString+" ");//onLineFriend为一串空格间隔的在线好友字符串                  
	        }
	        return sb.toString();
	    }
	}

