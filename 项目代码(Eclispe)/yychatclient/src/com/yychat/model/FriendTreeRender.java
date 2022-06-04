package com.yychat.model;

import com.yychat.model.Message;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/** 
 * �Զ�����������,������ÿ���ڵ����óɲ�ͬ��ͼ��
 * ��Ҫ���ڶԺ����Ƿ����ߵ�������ʾ
 * 
 */  
public class FriendTreeRender extends DefaultTreeCellRenderer {//���еĽڵ������Ⱦ���࣬��ÿ����㶼������ж������ƽڵ����

    private static final long serialVersionUID = 1L;

    private Message msg;

    public FriendTreeRender(Message msg){
        this.msg = msg;
    }

    /**
     * ��дgetTreeCellRendererComponent()
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel,boolean expanded,
    										      boolean leaf, int row,boolean hasFocus) {
        
    	//ִ�и���ԭ�Ͳ���
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,row, hasFocus);               						   
        //�õ�ÿ���ڵ��TreeNode
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        
        //�õ�ÿ���ڵ��text
        String str = node.toString();//str��ΪFriends��Ԫ�ظ�ʽ��jTreeû��str�൱��û��
        //���ýڵ������չ��ʱ��ͼ��
        if(!expanded) {
        	setIcon(new ImageIcon("src/images/lie.png"));
        }else {
        	setIcon(new ImageIcon("src/images/lie2.png"));
        }
        //��¼�ɹ�ʱ��ʼ���б�
		if(msg.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
            if (node.isLeaf()) {
                this.setIcon(new ImageIcon("src/images/4.jpg"));//��ʼ�����ѵ�ͷ��Ϊ������
            }/*else {
            	this.setIcon(new ImageIcon("src/images/lie.png"));//��������ȥʱ��ͼ��
            }*/            
        //�ѻ�ȡ�����ߺ����б�
        }

    	String [] onlineFriends = msg.getChatContent().split(" ");
    	if(!(str.equals("")||str==null)) {//�Է�û����ʱ�����
    		if (node.isLeaf()) {
                //�õ����е�userName����   		
                str = str.substring(str.length()-7,str.length()-1);
        		this.setIcon(new ImageIcon("src/images/4.jpg"));
                for (String onlineFriend : onlineFriends) {//���и��²�����onLinefriend����ÿ�����ߵĺ���
                    if (str.equals(onlineFriend))		   //strΪÿ��ԭ���ĺ���userName����ÿ�����ߺ������Ա�
                        this.setIcon(new ImageIcon("src/images/3.jpg"));//�������ߺ���ͼ��
                }
        	}
    	}
    	      	 

        return this;  
    }  
  
} 
