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
 * 登录成功后的主页面，显示好友列表，未在线好友头像灰色，
 * 双击某好友即可打开与其聊天界面
 * 点击退出按钮即可退出登录
 */
public class FriendList extends JFrame implements ActionListener,MouseListener,TreeModelListener,ItemListener{

	private static final long serialVersionUID = 1L;
    private Container c;//本窗口面板
    private Point tmp,loc;//记录位置
	private boolean isDragged = false;//是否拖拽
    private String ownerId;//本人qq
    private String myName;//本人昵称
    private String staTus;
    private JTree jtree;//树组件显示好友列表
    
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
	    
	  //获取本窗体容器
	  c = this.getContentPane();
	  //设置窗体大小
	  this.setSize(274,600);
	  //设置布局
	  c.setLayout(null);
	    
		//右上角最小化按钮
		btn_min = new JButton(new ImageIcon("src/images/fmin.png"));
		btn_min.setBounds(217, 0, 28, 28);
		btn_min.setBorderPainted(false);
		btn_min.setContentAreaFilled(false);
		btn_min.addMouseListener(this);		
		//右上角退出按钮
		btn_exit = new JButton(new ImageIcon("src/images/fexit.png"));
		btn_exit.setBounds(245, 0, 28, 28);
		btn_exit.setBorderPainted(false);
		btn_exit.setContentAreaFilled(false);
		btn_exit.addMouseListener(this);	
		//左上角qq标签
		jbl_leftTop = new JLabel(new ImageIcon("src/images/biaoti.png"));
		jbl_leftTop.setBounds(0, 0, 44, 21);	
		//qq头像
		jbl_photo = new JLabel(new ImageIcon("src/images/QQ头像.jpg"));
		jbl_photo.setBounds(10, 23, 78, 78);
		//qq昵称
		jbl_qqName = new JLabel(name+"("+ownerId+")");
		jbl_qqName.setBounds(109, 32, 68, 21);	
		//个性签名
		//JTextField jtf_personalSign = new JTextField();
		//jtf_personalSign.setBounds(110, 63, 167, 21);
		//c.add(jtf_personalSign);
		//在线状态选择列表
		String[] status = {"在线","隐身","离线"};
		online_status = new JComboBox<>(status);	
		online_status.setBounds(195, 32, 63, 21);
		online_status.addItemListener(this);
		//头像下方八个功能按钮
		//按钮1
		btn_h1 = new JButton(new ImageIcon("src/images/tubiao1.png"));
		btn_h1.setBounds(0, 111, 20, 23);
		//按钮2
		btn_h2 = new JButton(new ImageIcon("src/images/tubiao2.png"));
		btn_h2.setBounds(28, 111, 20, 23);
		//按钮3
		btn_h3 = new JButton(new ImageIcon("src/images/tubiao3.png"));
		btn_h3.setBounds(58, 111, 20, 23);
		//按钮4
		btn_h4 = new JButton(new ImageIcon("src/images/tubiao4.png"));
		btn_h4.setBounds(88, 111, 20, 23);
		//按钮5
		btn_h5 = new JButton(new ImageIcon("src/images/tubiao5.png"));
		btn_h5.setBounds(118, 111, 20, 23);
		//按钮6
		btn_h6 = new JButton(new ImageIcon("src/images/tubiao6.png"));
		btn_h6.setBounds(148, 111, 20, 23);
		//按钮7
		btn_h7 = new JButton(new ImageIcon("src/images/tubiao7.png"));
		btn_h7.setBounds(178, 111, 20, 23);
		//按钮8
		btn_h8=new JButton(new ImageIcon("src/images/jiahaoyou.png"));
		btn_h8.setBounds(208, 111, 20, 23);
		btn_h8.addActionListener(this);
		//搜索框
		jtf_search = new JTextField();
		jtf_search.setBounds(0, 134, 247, 23);	
		//搜索按钮
		btn_search = new JButton(new ImageIcon("src/images/search.png"));
		btn_search.setBounds(247, 134, 30, 23);	
		//上半部分背景图
		jbl_background = new JLabel(new ImageIcon("src/images/beijing.png"));
		jbl_background.setBounds(0, 0, 277, 156);
		//底部8个功能按钮
		//按钮1
		btn_l1 = new JButton(new ImageIcon("src/images/mainmenue.png"));
		btn_l1.setBounds(0, 577, 30, 23);
		//按钮2
		btn_l2 = new JButton(new ImageIcon("src/images/shezhi.png"));
		btn_l2.setBounds(30, 577, 30, 23);
		//按钮3
		btn_l3 = new JButton(new ImageIcon("src/images/messagemanage.png"));
		btn_l3.setBounds(60, 577, 30, 23);
		//按钮4
		btn_l4 = new JButton(new ImageIcon("src/images/filehleper.png"));
		btn_l4.setBounds(90, 577, 30, 23);
		//按钮5
		btn_l5 = new JButton(new ImageIcon("src/images/shoucang.png"));
		btn_l5.setBounds(120, 577, 30, 23);
		//按钮6
		btn_l6 = new JButton(new ImageIcon("src/images/tubiao8.png"));
		btn_l6.setBounds(150, 577, 30, 23);
		//按钮7
		btn_l7 = new JButton(new ImageIcon("src/images/tubiao9.png"));
		btn_l7.addActionListener(this);
		btn_l7.setBounds(180, 577, 30, 23);
		//按钮8
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
    	jItem=new JMenuItem("删除好友");
    	jItem.addActionListener(this);
    	jpm.add(jItem);
    	
		//显示好友列表
		if(staTus.equals(Message.NEW_ONLINE_FRIEND_TO_SERVER)) {
			initList(this, msg);//msg先是包含所有friends的信息
			online_status.setSelectedIndex(0);
			sendOnlineMsgToServer();
		}else {		
			initList(this, msg);
			online_status.setSelectedIndex(1);		
		}
		
		
		//去除其定义装饰框
		this.setUndecorated(true);
		//设置窗体可见
		this.setVisible(true); 
		this.setLocationRelativeTo(null);				
        //添加鼠标监听事件
        this.addMouseListener(new java.awt.event.MouseAdapter() { 
        	@Override
            public void mouseReleased(MouseEvent e) {  
                isDragged = false;  
                //拖拽结束图标恢复
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));  
            }  
        	@Override
            public void mousePressed(MouseEvent e) {  
        		//限定范围内可拖拽
            	if(e.getY()<30)
            	{
            		//获取鼠标按下位置
            		tmp = new Point(e.getX(), e.getY());  
            		isDragged = true;
            		//拖拽时更改鼠标图标
            		setCursor(new Cursor(Cursor.MOVE_CURSOR));  
            	}  
            }
        });  
   
        this.addMouseMotionListener(new MouseMotionAdapter() {  
        	@Override
            public void mouseDragged(MouseEvent e) {
                if (isDragged) {  
                	//设置鼠标与窗体相对位置不变
                	loc = new Point(getLocation().x + e.getX() - tmp.x,
                    getLocation().y + e.getY() - tmp.y);  
                    setLocation(loc);  
                }  
            }  

        });
	}
	
	public void sendMessage(Socket s,Message ms) {//从Socket发信息
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
     * 刷新在线好友列表
     * @param msg
     */
    public void updateOnlineFriends(Message msg) {
        this.jtree.setCellRenderer(new FriendTreeRender(msg));//传信息给节点渲染器来重构节点组件,jTree的所有节点名没变
        //this.jtree.revalidate();
    }
	
    /**
     * 将下线消息发送到服务器和其他客户端
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
		//结束线程
		th.myStop();
		ManageAllThreads.removeThread(ownerId);
		
		Set onlineChat=ManageAllChat.chatFrames.keySet();//拿到当前聊天窗口的名字，并删除退出
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
            System.out.println("添加frameName="+frameName);
            ManageAllChat.addChatFrame(frameName, new FriendChat(ownerId, myName, friendId, friendName));
        }else{                                  
            JOptionPane.showMessageDialog(this,"该窗口已存在!");
        }
    }
    /**
     * 以树形结构显示全部好友列表
     * @param msg
     */
	public void initList(JFrame f, Message msg){
        //用HasHtable创建jtree显示好友列表
        Hashtable<String,Object> ht = new Hashtable<>();
        String[] friends = msg.getChatContent().split(" ");//每个元素的格式为weBname(userName)
        ht.put("我的好友",friends);
        jtree = new JTree(ht);//用哈希表来创建树,每个节点名对应Friends里的元素
        jtree.setCellRenderer(new FriendTreeRender(msg));//
        jtree.setToggleClickCount(1);//设置为点击一次就展开
        jtree.setShowsRootHandles(false);//隐藏根柄
        jtree.addMouseListener(new MouseAdapter() {//加监听器使其能创建聊天界面
            @Override
            public void mouseClicked(MouseEvent e) {
                if(e.getClickCount() == 2){
                    JTree tree = (JTree)e.getSource();
                    TreePath path = tree.getSelectionPath();//TreePath相当于一个路径上的所有对象构成的数组，元素为树节点类。现得到所到所选节点的路径
                    if(null != path){
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();//返回最后一个树节点，即当前节点
                        if(node.isLeaf()){
                        	String str = node.toString();//str的格式即为Friends的元素格式
                            //String[] info = node.toString().split("\\(");
                            //String friendId = info[1].substring(0,info[1].length()-1);//
                        	String friendId =str.substring(str.length()-7,str.length()-1);//取出好友ID
                        	String friendName=str.substring(0,str.length()-8);//取出好友网名
                            validatefriend(friendId, friendName);//验证是否为好友来开启对话窗口
                        }
                    }
                }
                if(e.getButton()==MouseEvent.BUTTON3) {
                	JTree tree = (JTree) e.getSource();
                    TreePath path = tree.getSelectionPath();//TreePath相当于一个路径上的所有对象构成的数组，元素为树节点类。现得到所到所选节点的路径
                    if(null != path){
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();//返回最后一个树节点，即当前节点
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
			if(online_status.getSelectedItem().toString().equals("在线")) {
				sendOnlineMsgToServer();
			}else if(online_status.getSelectedItem().toString().equals("隐身")){
				sendNotOnlineMsgToServer();
			}else if(online_status.getSelectedItem().toString().equals("离线")){
				sendUnloadMsgToServer();
			}
		}
		if(e.getSource()==jpm) {
			
		}
		
	}

}
