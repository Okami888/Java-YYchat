package com.yychat.view;

import com.yychatclient.control.ClientReceiverThread;
import com.yychatclient.control.ManageAllChat;
import com.yychatclient.control.ManageAllThreads;




import com.yychat.model.Message;

import com.yychat.model.FriendTreeRender;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

import javax.swing.*;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

/**
 * ��¼�ɹ������ҳ�棬��ʾ�����б�δ���ߺ���ͷ���ɫ��
 * ˫��ĳ���Ѽ��ɴ������������
 * ����˳���ť�����˳���¼
 */
public class FriendList extends JFrame implements ActionListener,MouseListener,TreeModelListener,ItemListener{

	private static final long serialVersionUID = 1L;
    private Container c;//���������
    private Point tmp,loc;//��¼λ��
	private boolean isDragged = false;//�Ƿ���ק
    private String ownerId;//����qq
    private String myName;//�����ǳ�
    private String staTus;
    private JTree jtree;//�������ʾ�����б�
    
    private JButton btn_min,btn_exit;
    private JLabel jbl_leftTop,jbl_photo,jbl_qqName;
    private JComboBox<String> online_status;
    private JButton btn_h1,btn_h2,btn_h3,btn_h4,btn_h5,btn_h6,btn_h7,btn_h8;
    private JTextField jtf_search;
    private JButton btn_search;
    private JLabel jbl_background;
    private JButton btn_l1,btn_l2,btn_l3,btn_l4,btn_l5,btn_l6,btn_l7,btn_l8;
    private JPopupMenu jpm ;
    private JMenuItem jItem;
    
    
      
	public FriendList(String name, String ownerId, Message msg) {
	  
		this.ownerId = ownerId;
	    this.myName = name;
	    this.staTus=msg.getStatus();
	    
	  //��ȡ����������
	  c = this.getContentPane();
	  //���ô����С
	  this.setSize(274,600);
	  //���ò���
	  c.setLayout(null);
	    
		//���Ͻ���С����ť
		btn_min = new JButton(new ImageIcon("src/images/fmin.png"));
		btn_min.setBounds(217, 0, 28, 28);
		btn_min.setBorderPainted(false);
		btn_min.setContentAreaFilled(false);
		btn_min.addMouseListener(this);		
		//���Ͻ��˳���ť
		btn_exit = new JButton(new ImageIcon("src/images/fexit.png"));
		btn_exit.setBounds(245, 0, 28, 28);
		btn_exit.setBorderPainted(false);
		btn_exit.setContentAreaFilled(false);
		btn_exit.addMouseListener(this);	
		//���Ͻ�qq��ǩ
		jbl_leftTop = new JLabel(new ImageIcon("src/images/biaoti.png"));
		jbl_leftTop.setBounds(0, 0, 44, 21);	
		//qqͷ��
		jbl_photo = new JLabel(new ImageIcon("src/images/QQͷ��.jpg"));
		jbl_photo.setBounds(10, 23, 78, 78);
		//qq�ǳ�
		jbl_qqName = new JLabel(name+"("+ownerId+")");
		jbl_qqName.setBounds(109, 32, 68, 21);	
		//����ǩ��
		//JTextField jtf_personalSign = new JTextField();
		//jtf_personalSign.setBounds(110, 63, 167, 21);
		//c.add(jtf_personalSign);
		//����״̬ѡ���б�
		String[] status = {"����","����","����"};
		online_status = new JComboBox<>(status);	
		online_status.setBounds(195, 32, 63, 21);
		online_status.addItemListener(this);
		//ͷ���·��˸����ܰ�ť
		//��ť1
		btn_h1 = new JButton(new ImageIcon("src/images/tubiao1.png"));
		btn_h1.setBounds(0, 111, 20, 23);
		//��ť2
		btn_h2 = new JButton(new ImageIcon("src/images/tubiao2.png"));
		btn_h2.setBounds(28, 111, 20, 23);
		//��ť3
		btn_h3 = new JButton(new ImageIcon("src/images/tubiao3.png"));
		btn_h3.setBounds(58, 111, 20, 23);
		//��ť4
		btn_h4 = new JButton(new ImageIcon("src/images/tubiao4.png"));
		btn_h4.setBounds(88, 111, 20, 23);
		//��ť5
		btn_h5 = new JButton(new ImageIcon("src/images/tubiao5.png"));
		btn_h5.setBounds(118, 111, 20, 23);
		//��ť6
		btn_h6 = new JButton(new ImageIcon("src/images/tubiao6.png"));
		btn_h6.setBounds(148, 111, 20, 23);
		//��ť7
		btn_h7 = new JButton(new ImageIcon("src/images/tubiao7.png"));
		btn_h7.setBounds(178, 111, 20, 23);
		//��ť8
		btn_h8=new JButton(new ImageIcon("src/images/jiahaoyou.png"));
		btn_h8.setBounds(208, 111, 20, 23);
		btn_h8.addActionListener(this);
		//������
		jtf_search = new JTextField();
		jtf_search.setBounds(0, 134, 247, 23);	
		//������ť
		btn_search = new JButton(new ImageIcon("src/images/search.png"));
		btn_search.setBounds(247, 134, 30, 23);	
		//�ϰ벿�ֱ���ͼ
		jbl_background = new JLabel(new ImageIcon("src/images/beijing.png"));
		jbl_background.setBounds(0, 0, 277, 156);
		//�ײ�8�����ܰ�ť
		//��ť1
		btn_l1 = new JButton(new ImageIcon("src/images/mainmenue.png"));
		btn_l1.setBounds(0, 577, 30, 23);
		//��ť2
		btn_l2 = new JButton(new ImageIcon("src/images/shezhi.png"));
		btn_l2.setBounds(30, 577, 30, 23);
		//��ť3
		btn_l3 = new JButton(new ImageIcon("src/images/messagemanage.png"));
		btn_l3.setBounds(60, 577, 30, 23);
		//��ť4
		btn_l4 = new JButton(new ImageIcon("src/images/filehleper.png"));
		btn_l4.setBounds(90, 577, 30, 23);
		//��ť5
		btn_l5 = new JButton(new ImageIcon("src/images/shoucang.png"));
		btn_l5.setBounds(120, 577, 30, 23);
		//��ť6
		btn_l6 = new JButton(new ImageIcon("src/images/tubiao8.png"));
		btn_l6.setBounds(150, 577, 30, 23);
		//��ť7
		btn_l7 = new JButton(new ImageIcon("src/images/tubiao9.png"));
		btn_l7.addActionListener(this);
		btn_l7.setBounds(180, 577, 30, 23);
		//��ť8
		btn_l8 = new JButton(new ImageIcon("src/images/apl.png"));
		btn_l8.addActionListener(this);
		btn_l8.setBounds(210, 577, 64, 23);
		
		c.add(btn_min);c.add(btn_exit);
		c.add(jbl_leftTop);c.add(jbl_photo);c.add(jbl_qqName);
		c.add(online_status);
		c.add(btn_h1);c.add(btn_h2);c.add(btn_h3);c.add(btn_h4);c.add(btn_h5);c.add(btn_h6);c.add(btn_h7);c.add(btn_h8);
		c.add(jtf_search);
		c.add(btn_search);
		c.add(jbl_background);
		c.add(btn_l1);c.add(btn_l2);c.add(btn_l3);c.add(btn_l4);c.add(btn_l5);c.add(btn_l6);c.add(btn_l7);c.add(btn_l8);
		
		jpm=new JPopupMenu();
    	jItem=new JMenuItem("ɾ������");
    	jItem.addActionListener(this);
    	jpm.add(jItem);
    	
		//��ʾ�����б�
		if(staTus.equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
			initList(this, msg);//msg���ǰ�������friends����Ϣ
			online_status.setSelectedIndex(0);
			sendOnlineMsgToServer();
		}else {		
			initList(this, msg);
			online_status.setSelectedIndex(1);		
		}
		
		
		//ȥ���䶨��װ�ο�
		this.setUndecorated(true);
		//���ô���ɼ�
		this.setVisible(true); 
		this.setLocationRelativeTo(null);				
        //����������¼�
        this.addMouseListener(new java.awt.event.MouseAdapter() { 
        	@Override
            public void mouseReleased(MouseEvent e) {  
                isDragged = false;  
                //��ק����ͼ��ָ�
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
        	@Override
            public void mousePressed(MouseEvent e) {  
        		//�޶���Χ�ڿ���ק
            	if(e.getY()<30)
            	{
            		//��ȡ��갴��λ��
            		tmp = new Point(e.getX(), e.getY());  
            		isDragged = true;
            		//��קʱ�������ͼ��
            		setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            	}  
            }
        });  
   
        this.addMouseMotionListener(new MouseMotionAdapter() {  
        	@Override
            public void mouseDragged(MouseEvent e) {
                if (isDragged) {  
                	//��������봰�����λ�ò���
                	loc = new Point(getLocation().x + e.getX() - tmp.x,
                    getLocation().y + e.getY() - tmp.y);  
                    setLocation(loc);  
                }  
            }  

        });
	}
	
	public void sendMessage(Socket s,Message ms) {//��Socket����Ϣ
		OutputStream os;	
		try {
			os=s.getOutputStream();
			ObjectOutputStream oos= new ObjectOutputStream(os);
			oos.writeObject(ms);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	/**
     * ˢ�����ߺ����б�
     * @param msg
     */
    public void updateOnlineFriends(Message msg) {
        this.jtree.setCellRenderer(new FriendTreeRender(msg));//����Ϣ���ڵ���Ⱦ�����ع��ڵ����,jTree�����нڵ���û��
        //this.jtree.revalidate();
    }
	
    /**
     * ��������Ϣ���͵��������������ͻ���
     */
    public void sendOnlineMsgToServer() {
	    Message msg = new Message();
	    msg.setSender(ownerId);
	    msg.setMessageType(Message.NEW_ONLINE_FRIEND_TO_SERVER);
	    
        ClientReceiverThread th = ManageAllThreads.getThread(ownerId);
		sendMessage(th.getCreceiveThreadS(), msg);
    }
    public void sendNotOnlineMsgToServer() {
	    Message msg = new Message();
	    msg.setSender(ownerId);
	    msg.setMessageType(Message.NOT_ONLINE);
	    
        ClientReceiverThread th = ManageAllThreads.getThread(ownerId);
		sendMessage(th.getCreceiveThreadS(), msg);
    }
    
    public void sendUnloadMsgToServer() {
    	Message msg = new Message();
	    msg.setSender(ownerId);
	    msg.setMessageType(Message.NOT_ONLINE);
	    
        ClientReceiverThread th = ManageAllThreads.getThread(ownerId);
		sendMessage(th.getCreceiveThreadS(), msg);
		
		msg.setMessageType(Message.UNLOAD_LOGIN);
		sendMessage(th.getCreceiveThreadS(), msg);
		//�����߳�
		th.myStop();
		ManageAllThreads.removeThread(ownerId);
		
		Set onlineChat=ManageAllChat.chatFrames.keySet();//�õ���ǰ���촰�ڵ����֣���ɾ���˳�
		Iterator it=onlineChat.iterator();
		while(it.hasNext()) {
			String chatname=(String)it.next();
			if(chatname.startsWith(ownerId)) {
				ManageAllChat.getChatFrame(chatname).dispose();
				ManageAllChat.removeChatFrame(chatname);
			}
		}
		
        this.dispose();
        System.exit(0);
    }
    public void updateList(JFrame f,Message ms) {
    	jtree.removeAll();
    	initList(f, ms);
    	jtree.revalidate();
    }
    public void removeFriend(String friendId) {
    	Message msg = new Message();
	    msg.setSender(ownerId);
	    msg.setReceiver(friendId);
	    msg.setMessageType(Message.REMOVE_FRIEND);
	    ClientReceiverThread th = ManageAllThreads.getThread(ownerId);
		sendMessage(th.getCreceiveThreadS(), msg);
    }
    public void validatefriend(String friendId,String friendName) {
    	Message msg = new Message();
	    msg.setSender(friendName);
	    msg.setReceiver(friendId);
	    msg.setChatContent(ownerId+friendId);
	    msg.setMessageType(Message.VALIDATE_FRIEND);
	    ClientReceiverThread th = ManageAllThreads.getThread(ownerId);
		sendMessage(th.getCreceiveThreadS(), msg);
	}
    public void openChat(Message ms) {
    	
    	String frameName =ms.getChatContent();
    	String friendId = ms.getReceiver();
    	String friendName=ms.getSender();
        if(ManageAllChat.getChatFrame(frameName) == null){
            System.out.println("���frameName="+frameName);
            ManageAllChat.addChatFrame(frameName, new FriendChat(ownerId, myName, friendId, friendName));
        }else{                                  
            JOptionPane.showMessageDialog(this,"�ô����Ѵ���!");
        }
    }
    /**
     * �����νṹ��ʾȫ�������б�
     * @param msg
     */
	public void initList(JFrame f, Message msg){
        //��HasHtable����jtree��ʾ�����б�
        Hashtable<String,Object> ht = new Hashtable<>();
        String[] friends = msg.getChatContent().split(" ");//ÿ��Ԫ�صĸ�ʽΪweBname(userName)
        ht.put("�ҵĺ���",friends);
        jtree = new JTree(ht);//�ù�ϣ����������,ÿ���ڵ�����ӦFriends���Ԫ��
        jtree.setCellRenderer(new FriendTreeRender(msg));//
        jtree.setToggleClickCount(1);//����Ϊ���һ�ξ�չ��
        jtree.setShowsRootHandles(false);//���ظ���
        jtree.addMouseListener(new MouseAdapter() {//�Ӽ�����ʹ���ܴ����������
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    JTree tree = (JTree)e.getSource();
                    TreePath path = tree.getSelectionPath();//TreePath�൱��һ��·���ϵ����ж��󹹳ɵ����飬Ԫ��Ϊ���ڵ��ࡣ�ֵõ�������ѡ�ڵ��·��
                    if(null != path){
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();//�������һ�����ڵ㣬����ǰ�ڵ�
                        if(node.isLeaf()){
                        	String str = node.toString();//str�ĸ�ʽ��ΪFriends��Ԫ�ظ�ʽ
                            //String[] info = node.toString().split("\\(");
                            //String friendId = info[1].substring(0,info[1].length()-1);//
                        	String friendId =str.substring(str.length()-7,str.length()-1);//ȡ������ID
                        	String friendName=str.substring(0,str.length()-8);//ȡ����������
                            validatefriend(friendId, friendName);//��֤�Ƿ�Ϊ�����������Ի�����
                        }
                    }
                }
                if(e.getButton()==MouseEvent.BUTTON3) {
                	JTree tree = (JTree) e.getSource();
                    TreePath path = tree.getSelectionPath();//TreePath�൱��һ��·���ϵ����ж��󹹳ɵ����飬Ԫ��Ϊ���ڵ��ࡣ�ֵõ�������ѡ�ڵ��·��
                    if(null != path){
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();//�������һ�����ڵ㣬����ǰ�ڵ�
                        if(node.isLeaf()){
                        	jpm.show(jtree,e.getX(),e.getY());
                        }
                    }
                	//TreePath path=jtree.getPathForLocation(e.getX(), e.getY());
                	//TreeNode node =(TreeNode)path.getLastPathComponent();
                	//jpm.show(jtree,e.getX(),e.getY());
                }
            }
            });
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setViewportView(jtree);
        scrollPane.setBounds(0, 157, 274, 422);
        c.add(scrollPane);
    }
    

	@Override
	public void treeNodesChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesInserted(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeNodesRemoved(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void treeStructureChanged(TreeModelEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==btn_min) {
			setExtendedState(JFrame.ICONIFIED);
		}else if(e.getSource()==btn_exit) {
			sendUnloadMsgToServer();
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if(e.getSource()==btn_min) {
			btn_min.setContentAreaFilled(true);
			btn_min.setBackground(Color.GRAY);
		}else if(e.getSource()==btn_exit) {
			btn_exit.setContentAreaFilled(true);
			btn_exit.setBackground(Color.RED);
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if(e.getSource()==btn_min) {
			btn_min.setContentAreaFilled(false);
		}else if(e.getSource()==btn_exit) {
			btn_exit.setContentAreaFilled(false);
		}
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btn_h8) {
			AddFriend addFriend=new AddFriend(ownerId);		
		}
		if(e.getSource()==jItem) {
            TreePath path = jtree.getSelectionPath();
            if(null != path){
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                String str = node.toString();
            	String friendId =str.substring(str.length()-7,str.length()-1);
            	removeFriend(friendId);
                
            }		
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource()==online_status) {
			if(online_status.getSelectedItem().toString().equals("����")) {
				sendOnlineMsgToServer();
			}else if(online_status.getSelectedItem().toString().equals("����")){
				sendNotOnlineMsgToServer();
			}else if(online_status.getSelectedItem().toString().equals("����")){
				sendUnloadMsgToServer();
			}
		}
		if(e.getSource()==jpm) {
			
		}
		
	}

}
