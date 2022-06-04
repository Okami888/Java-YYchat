package com.yychat.view;

import com.yychatclient.control.ManageAllChat;
import com.yychatclient.control.ManageAllThreads;
import com.yychat.model.Message;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 聊天界面，点击“消息记录”按钮即可显示聊天记录，再次点击即可切换回图片
 */
public class FriendChat extends JFrame implements ActionListener,MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel_north;//北部区域面板
    private JLabel jbl_touxiang;//头像
    private JLabel jbl_friendname;//好友名称
    private JButton btn_exit, btn_min;//最小化和关闭按钮
    //头像下方7个功能按钮（未实现）
    private JButton btn_func1_north, btn_func2_north, btn_func3_north, btn_func4_north, btn_func5_north, btn_func6_north, btn_func7_north;
    //聊天内容显示面板
    private JTextPane panel_Msg;

    private JPanel panel_south;//南部区域面板
    private JTextPane jtp_input;//消息输入区
    //消息输入区上方9个功能按钮(未实现)
    private JButton btn_func1_south, btn_func2_south, btn_func3_south,btn_func4_south, btn_func5_south, btn_func6_south, btn_func7_south, btn_func8_south, btn_func9_south;
    private JButton recorde_search;//查看消息记录按钮
    private JButton btn_send, btn_close;//消息输入区下方关闭和发送按钮

    private JPanel panel_east;//东部面板
    private CardLayout cardLayout;//卡片布局
    //默认东部面板显示一张图,点击查询聊天记录按钮切换到聊天记录面板
    private final JLabel label1 = new JLabel(new ImageIcon("src/images/dialogimage/jinlun.gif"));
    private JTextPane panel_Record;//聊天记录显示面板

    private boolean isDragged = false;//鼠标拖拽窗口标志
    private Point frameLocation;//记录鼠标点击位置
    private String myId;//本人账号
    private String myName;
    private String friendId;//好友账号
    private String friendName;
    private DateFormat df;//日期解析

    public FriendChat(String myId, String myName, String friendId, String friendName) {

        this.myId = myId;
        this.friendId = friendId;
        this.myName = myName;
        this.friendName=friendName;
        df = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
        //获取窗口容器
        Container c = this.getContentPane();
        //设置布局
        c.setLayout(null);

        //北部面板
        panel_north = new JPanel();
        panel_north.setBounds(0, 0, 729, 92);
        panel_north.setLayout(null);
        //添加北部面板
        c.add(panel_north);
        //左上角灰色头像
        jbl_touxiang = new JLabel(new ImageIcon("src/images/dialogimage/Qtoux.gif"));
        jbl_touxiang.setBounds(10, 10, 42, 42);
        panel_north.add(jbl_touxiang);
        //头像右方正在聊天的对方姓名
        jbl_friendname = new JLabel(friendName+"("+friendId+")");
        jbl_friendname.setBounds(62, 21, 105, 20);
        panel_north.add(jbl_friendname);
        //右上角最小化按钮
        btn_min = new JButton(new ImageIcon ("src/images/dialogimage/fmin.png"));
        btn_min.setBorderPainted(false);
		btn_min.setContentAreaFilled(false);
		btn_min.addMouseListener(this);
        btn_min.setBounds(668, 0, 30, 30);
        panel_north.add(btn_min);
        //右上角关闭按钮
        btn_exit = new JButton(new ImageIcon ("src/images/dialogimage/fexit.png"));
        btn_exit.setBorderPainted(false);
        btn_exit.setContentAreaFilled(false);
        btn_exit.addMouseListener(this);
        btn_exit.setBounds(698, 0, 30, 30);
        panel_north.add(btn_exit);
        //头像下方功能按钮
        //功能按钮1
        btn_func1_north = new JButton(new ImageIcon("src/images/dialogimage/yuyin.png"));
        btn_func1_north.setBounds(10, 62, 36, 30);
        panel_north.add(btn_func1_north);
        //功能按钮2
        btn_func2_north = new JButton(new ImageIcon("src/images/dialogimage/shipin.png"));
        btn_func2_north.setBounds(61, 62, 36, 30);
        panel_north.add(btn_func2_north);
        //功能按钮3
        btn_func3_north = new JButton(new ImageIcon("src/images/dialogimage/tranfile.jpg"));
        btn_func3_north.setBounds(112, 62, 36, 30);
        panel_north.add(btn_func3_north);
        //功能按钮4
        btn_func4_north = new JButton(new ImageIcon("src/images/dialogimage/createteam.jpg"));
        btn_func4_north.setBounds(163, 62, 36, 30);
        panel_north.add(btn_func4_north);
        //功能按钮5
        btn_func5_north = new JButton(new ImageIcon("src/images/dialogimage/yuancheng.png"));
        btn_func5_north.setBounds(214, 62, 36, 30);
        panel_north.add(btn_func5_north);
        //功能按钮6
        btn_func6_north = new JButton(new ImageIcon("src/images/dialogimage/sharedisplay.png"));
        btn_func6_north.setBounds(265, 62, 36, 30);
        panel_north.add(btn_func6_north);
        //功能按钮7
        btn_func7_north = new JButton(new ImageIcon("src/images/dialogimage/yingyong.jpg"));
        btn_func7_north.setBounds(318, 62, 36, 30);
        panel_north.add(btn_func7_north);
        //设置北部面板背景色
        //panel_north.setBackground(new Color(105, 197, 239));
        panel_north.setBackground(new Color(22, 154, 228));

        //中部聊天内容显示部分
        panel_Msg = new JTextPane();
        JScrollPane scrollPane_Msg = new JScrollPane(panel_Msg);
        scrollPane_Msg.setBounds(0, 92, 446, 270);
        c.add(scrollPane_Msg);

        //南部面板
        panel_south = new JPanel();
        panel_south.setBounds(0, 370, 446, 170);
        panel_south.setLayout(null);
        //添加南部面板
        c.add(panel_south);
        //内容输入区
        jtp_input = new JTextPane();
        jtp_input.setBounds(0, 34, 446, 105);
        //添加到南部面板
        panel_south.add(jtp_input);
        //文本输入区上方功能按钮
        //功能按钮1
        btn_func1_south = new JButton(new ImageIcon("src/images/dialogimage/word.png"));
        btn_func1_south.setBounds(10, 0, 30, 30);
        panel_south.add(btn_func1_south);
        //功能按钮2
        btn_func2_south = new JButton(new ImageIcon("src/images/dialogimage/biaoqing.png"));
        btn_func2_south.setBounds(47, 0, 30, 30);
        panel_south.add(btn_func2_south);
        //功能按钮3
        btn_func3_south = new JButton(new ImageIcon("src/images/dialogimage/magic.jpg"));
        btn_func3_south.setBounds(84, 0, 30, 30);
        panel_south.add(btn_func3_south);
        //功能按钮4
        btn_func4_south = new JButton(new ImageIcon("src/images/dialogimage/zhendong.jpg"));
        btn_func4_south.setBounds(121, 0, 30, 30);
        panel_south.add(btn_func4_south);
        //功能按钮5
        btn_func5_south = new JButton(new ImageIcon("src/images/dialogimage/yymessage.jpg"));
        btn_func5_south.setBounds(158, 0, 30, 30);
        panel_south.add(btn_func5_south);
        //功能按钮6
        btn_func6_south = new JButton(new ImageIcon("src/images/dialogimage/dgninput.jpg"));
        btn_func6_south.setBounds(195, 0,30, 30);
        panel_south.add(btn_func6_south);
        //功能按钮7
        btn_func7_south = new JButton(new ImageIcon("src/images/dialogimage/sendimage.jpg"));
        btn_func7_south.setBounds(232, 0, 30, 30);
        panel_south.add(btn_func7_south);
        //功能按钮8
        btn_func8_south = new JButton(new ImageIcon("src/images/dialogimage/diange.jpg"));
        btn_func8_south.setBounds(269, 0, 30, 30);
        panel_south.add(btn_func8_south);
        //功能按钮9
        btn_func9_south = new JButton(new ImageIcon("src/images/dialogimage/jietu.jpg"));
        btn_func9_south.setBounds(306, 0, 30, 30);
        panel_south.add(btn_func9_south);
        //查询聊天记录
        recorde_search = new JButton(new ImageIcon("src/images/dialogimage/recorde.png"));
        recorde_search.addActionListener(e-> {
            System.out.println("点击查找聊天记录");
            cardLayout.next(panel_east);
        });
        recorde_search.setBounds(350, 0, 96, 30);
        panel_south.add(recorde_search);
        //消息关闭按钮
        btn_close = new JButton(new ImageIcon("src/images/dialogimage/close.jpg"));
        btn_close.setBounds(290, 145, 64, 24);
        btn_close.addActionListener(this);
        panel_south.add(btn_close);
        //消息发送按钮
        btn_send = new JButton(new ImageIcon("src/images/dialogimage/send.jpg"));
        btn_send.addActionListener(this);
        btn_send.setBounds(381, 145, 64, 24);
        panel_south.add(btn_send);

        //东部面板(图片和聊天记录)
        panel_east = new JPanel();
        //卡片布局
        cardLayout = new CardLayout(2,2);
        panel_east.setLayout(cardLayout);
        panel_east.setBounds(444, 91, 285, 418);
        //添加东部面板
        c.add(panel_east);
        //显示聊天记录面板
        panel_Record = new JTextPane();
        panel_Record.setText("-----------------------------聊天记录--------------------------\n\n");
        loadingHistory();
        loadingOffline();
        JScrollPane scrollPane_Record = new JScrollPane(panel_Record);
        scrollPane_Record.setBounds(2, 2, 411, 410);
        //添加到东部面板
        panel_east.add(label1);
        panel_east.add(scrollPane_Record);

        //注册鼠标事件监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //鼠标释放
                isDragged = false;
                //光标恢复
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                //鼠标按下
                //获取鼠标相对窗体位置
                frameLocation = new Point(e.getX(),e.getY());
                isDragged = true;
                //光标改为移动形式
                if(e.getY() < 92)
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
        //注册鼠标事件监听器
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //指定范围内点击鼠标可拖拽
                if(e.getY() < 92){
                    //如果是鼠标拖拽移动
                    if(isDragged) {
                        Point loc = new Point(getLocation().x+e.getX()-frameLocation.x,
                                getLocation().y+e.getY()-frameLocation.y);
                        //保证鼠标相对窗体位置不变,实现拖动
                        setLocation(loc);
                    }
                }
            }
        });

        this.setIconImage(new ImageIcon("src/images/Q.png").getImage());//修改窗体默认图标
        this.setSize(728, 553);//设置窗体大小
        this.setUndecorated(true);//去掉自带装饰框
        this.setVisible(true);//设置窗体可见

    }
    @Override
    public void mouseClicked(MouseEvent e) {
    	
    	if(e.getSource()==btn_min) {
			setExtendedState(JFrame.ICONIFIED);
		}else if(e.getSource()==btn_exit) {
			ManageAllChat.removeChatFrame(myId + friendId);
            System.out.println("remove chatFrame="+myId + friendId);
            this.dispose();
		}
    	
		if(e.getSource() == btn_close) {
            ManageAllChat.removeChatFrame(myId + friendId);
            System.out.println("remove chatFrame="+myId + friendId);
            this.dispose();
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

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
        if(e.getSource() == btn_send){
            System.out.println("发送");
            sendMsg(this, this.myName);
        }
    }

    /**
     * 实现消息发送
     * @param f
     */
    public void sendMsg(JFrame f, String senderName){
        String str = jtp_input.getText();
        if(!str.equals("")){
            Message msg = new Message();
            msg.setMessageType(Message.COMMON_CHAT_MESSAGE);
            msg.setSender(this.myId);
            msg.setSenderWebname(senderName);
            msg.setReceiver(this.friendId);
            msg.setChatContent(str);
            msg.setSendTime(df.format(new Date()));
            try {
                ObjectOutput out = new ObjectOutputStream(ManageAllThreads.getThread(this.myId).getCreceiveThreadS().getOutputStream());
                out.writeObject(msg);
                System.out.println("发送成功");
                showMessage(msg,true);
                jtp_input.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(f,"不能发送空内容！");
        }
    }

    /**
     * 将接收到的消息显示出来
     * @param msg
     */
    
    public void showMessage(Message msg, boolean fromSelf) {
        showMessage(panel_Msg, msg, fromSelf);//先显示到聊天内容面板
        showMessage(panel_Record, msg, fromSelf);//再显示到聊天记录面板
    }
    
    public void showOfflineMessage(Message msg) {
    	String [] messages = msg.getChatContent().split(" ");
    	String [] messages2=new String[messages.length/3];
    	int ii=0;
    	for(int i=0;i<=messages.length-3;i=i+3) {
    		messages2[ii]=messages[i]+" "+messages[i+1]+" "+messages[i+2];
    		System.out.println(messages2[ii]);
    		ii+=1;
    	}
    	for(String message:messages2) {
    		Message mss=new Message();
    		mss.setChatContent(message.substring(12,message.length()-22));
    		System.out.println("离线记录内容为"+mss.getChatContent());
    		mss.setSendTime(message.substring(message.length()-22,message.length()));
    		System.out.println("离线记录时间为"+mss.getSendTime());
    		mss.setSender(msg.getSender());
    		mss.setSenderWebname(msg.getSenderWebname());
    		showMessage(panel_Msg, mss, false);	
    	}
    }
    
    public void showHistoryMessage(Message msg) {
    	String [] messages = msg.getChatContent().split(" ");
    	String [] messages2=new String[messages.length/3];
    	int ii=0;
    	for(int i=0;i<=messages.length-3;i=i+3) {
    		messages2[ii]=messages[i]+" "+messages[i+1]+" "+messages[i+2];
    		System.out.println(messages2[ii]);
    		ii+=1;
    	}
    	for(String message:messages2) {
    		Message mss=new Message();
    		mss.setChatContent(message.substring(12,message.length()-22));
    		mss.setSendTime(message.substring(message.length()-22,message.length()));
    		mss.setSender(msg.getSender());
    		mss.setSenderWebname(msg.getSenderWebname());
    		if(message.startsWith(myId)) {
    			showMessage(panel_Record, mss, true);
    		}else {
    			showMessage(panel_Record, mss, false);
    		}  		
    	}
    }
    
    /**
     * 将消息内容显示到指定面板
     * @param jtp
     * @param msg
     * @param fromSelf
     */
    public void showMessage(JTextPane jtp, Message msg, boolean fromSelf) {
        //设置显示格式
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrset, "仿宋");
        StyleConstants.setFontSize(attrset,14);
        Document docs = jtp.getDocument();
        String info = null;
        try {
            if(fromSelf){//发出去的消息内容，显示在自己上时
                info = myName;
                StyleConstants.setForeground(attrset, Color.MAGENTA);//自己账号，紫色
                docs.insertString(docs.getLength(), info, attrset);
                StyleConstants.setForeground(attrset, Color.red);
                
                info = msg.getSendTime()+":\n";//发送时间：黑色
                StyleConstants.setForeground(attrset, Color.black);
                docs.insertString(docs.getLength(), info, attrset);
                
                info = " "+msg.getChatContent()+"\n";//发送内容：绿色
                StyleConstants.setFontSize(attrset,16);
                StyleConstants.setForeground(attrset, Color.green);
                docs.insertString(docs.getLength(), info, attrset);
            }else{//接收到的消息内容，显示在自己上时
                info = msg.getSenderWebname()+"("+msg.getSender()+")  ";//对方的账号：红色
                StyleConstants.setForeground(attrset, Color.red);
                docs.insertString(docs.getLength(), info, attrset);                  
                StyleConstants.setForeground(attrset, Color.red);
                
                info = msg.getSendTime()+":\n";//发送时间：黑色
                StyleConstants.setForeground(attrset, Color.black);
                docs.insertString(docs.getLength(), info, attrset);
                
                info = " "+msg.getChatContent()+"\n";//发送内容：蓝色
                StyleConstants.setFontSize(attrset,16);
                StyleConstants.setForeground(attrset, Color.blue);
                docs.insertString(docs.getLength(), info, attrset);
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
    public void loadingHistory() {
    	
    	Message msg = new Message();
        msg.setSender(myId);
        msg.setReceiver(friendId);
        msg.setSenderWebname(friendName);
        msg.setMessageType(Message.SEEK_PAST_MESSAGE);
        try {
            ObjectOutput out = new ObjectOutputStream(ManageAllThreads.getThread(myId).getCreceiveThreadS().getOutputStream());
            out.writeObject(msg);
            System.out.println("找回历史记录");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadingOffline() {
    	
    	Message msg = new Message();
        msg.setSender(friendId);
        msg.setReceiver(myId);
        msg.setSenderWebname(friendName);
        msg.setMessageType(Message.LODING_OFFLINE_MESSAGE);
        try {
            ObjectOutput out = new ObjectOutputStream(ManageAllThreads.getThread(myId).getCreceiveThreadS().getOutputStream());
            out.writeObject(msg);
            System.out.println("加载离线信息");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}