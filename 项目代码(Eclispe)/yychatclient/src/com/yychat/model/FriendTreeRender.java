package com.yychat.model;

import com.yychat.model.Message;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

/** 
 * 自定义树描述类,将树的每个节点设置成不同的图标
 * 主要用于对好友是否在线的区分显示
 * 
 */  
public class FriendTreeRender extends DefaultTreeCellRenderer {//树中的节点类的渲染器类，对每个结点都会进行判断来绘制节点组件

    private static final long serialVersionUID = 1L;

    private Message msg;

    public FriendTreeRender(Message msg){
        this.msg = msg;
    }

    /**
     * 重写getTreeCellRendererComponent()
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value,boolean sel,boolean expanded,
    										      boolean leaf, int row,boolean hasFocus) {
        
    	//执行父类原型操作
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,row, hasFocus);               						   
        //得到每个节点的TreeNode
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        
        //得到每个节点的text
        String str = node.toString();//str即为Friends的元素格式，jTree没变str相当于没变
        //设置节点收起和展开时的图标
        if(!expanded) {
        	setIcon(new ImageIcon("src/images/lie.png"));
        }else {
        	setIcon(new ImageIcon("src/images/lie2.png"));
        }
        //登录成功时初始化列表
		if(msg.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
            if (node.isLeaf()) {
                this.setIcon(new ImageIcon("src/images/4.jpg"));//初始化好友的头像为不在线
            }/*else {
            	this.setIcon(new ImageIcon("src/images/lie.png"));//设置缩回去时的图标
            }*/            
        //已获取到在线好友列表
        }

    	String [] onlineFriends = msg.getChatContent().split(" ");
    	if(!(str.equals("")||str==null)) {//以防没好友时会出错
    		if (node.isLeaf()) {
                //得到其中的userName部分   		
                str = str.substring(str.length()-7,str.length()-1);
        		this.setIcon(new ImageIcon("src/images/4.jpg"));
                for (String onlineFriend : onlineFriends) {//进行更新操作，onLinefriend接收每个上线的好友
                    if (str.equals(onlineFriend))		   //str为每个原来的好友userName，与每个上线好友做对比
                        this.setIcon(new ImageIcon("src/images/3.jpg"));//设置上线好友图标
                }
        	}
    	}
    	      	 

        return this;  
    }  
  
} 
