package com.yychatserver.control;

import java.net.Socket;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import com.yychatserver.control.ServerReceiverThread;

	/**
	 * ����������ͻ��˵��߳�
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
	     * ���ص�ǰ����ȫ���û�
	     * @return
	     */
	    public static String getOnLineList(){
	        StringBuilder sb = new StringBuilder();
	        String itString="";
	        Iterator it = hmSocket.keySet().iterator();//�õ�������Ϣ�����������ߺ������Ƶ�һ���б�//��������������
	        while(it.hasNext()){//ע���������ѭ����ֻ�ܳ���һ��it.next����ᱨ��
	        	
	        	itString =it.next().toString();
	        	if(hmSocket.get(itString).isHidden()) {//��������ĺ��Ѽ���
	        		continue;
	        	}
	        	sb.append(itString+" ");//onLineFriendΪһ���ո��������ߺ����ַ���                  
	        }
	        return sb.toString();
	    }
	}

