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
 * ������棬�������Ϣ��¼����ť������ʾ�����¼���ٴε�������л���ͼƬ
 */
public class FriendChat extends JFrame implements ActionListener,MouseListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel panel_north;//�����������
    private JLabel jbl_touxiang;//ͷ��
    private JLabel jbl_friendname;//��������
    private JButton btn_exit, btn_min;//��С���͹رհ�ť
    //ͷ���·�7�����ܰ�ť��δʵ�֣�
    private JButton btn_func1_north, btn_func2_north, btn_func3_north, btn_func4_north, btn_func5_north, btn_func6_north, btn_func7_north;
    //����������ʾ���
    private JTextPane panel_Msg;

    private JPanel panel_south;//�ϲ��������
    private JTextPane jtp_input;//��Ϣ������
    //��Ϣ�������Ϸ�9�����ܰ�ť(δʵ��)
    private JButton btn_func1_south, btn_func2_south, btn_func3_south,btn_func4_south, btn_func5_south, btn_func6_south, btn_func7_south, btn_func8_south, btn_func9_south;
    private JButton recorde_search;//�鿴��Ϣ��¼��ť
    private JButton btn_send, btn_close;//��Ϣ�������·��رպͷ��Ͱ�ť

    private JPanel panel_east;//�������
    private CardLayout cardLayout;//��Ƭ����
    //Ĭ�϶��������ʾһ��ͼ,�����ѯ�����¼��ť�л��������¼���
    private final JLabel label1 = new JLabel(new ImageIcon("src/images/dialogimage/jinlun.gif"));
    private JTextPane panel_Record;//�����¼��ʾ���

    private boolean isDragged = false;//�����ק���ڱ�־
    private Point frameLocation;//��¼�����λ��
    private String myId;//�����˺�
    private String myName;
    private String friendId;//�����˺�
    private String friendName;
    private DateFormat df;//���ڽ���

    public FriendChat(String myId, String myName, String friendId, String friendName) {

        this.myId = myId;
        this.friendId = friendId;
        this.myName = myName;
        this.friendName=friendName;
        df = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
        //��ȡ��������
        Container c = this.getContentPane();
        //���ò���
        c.setLayout(null);

        //�������
        panel_north = new JPanel();
        panel_north.setBounds(0, 0, 729, 92);
        panel_north.setLayout(null);
        //��ӱ������
        c.add(panel_north);
        //���Ͻǻ�ɫͷ��
        jbl_touxiang = new JLabel(new ImageIcon("src/images/dialogimage/Qtoux.gif"));
        jbl_touxiang.setBounds(10, 10, 42, 42);
        panel_north.add(jbl_touxiang);
        //ͷ���ҷ���������ĶԷ�����
        jbl_friendname = new JLabel(friendName+"("+friendId+")");
        jbl_friendname.setBounds(62, 21, 105, 20);
        panel_north.add(jbl_friendname);
        //���Ͻ���С����ť
        btn_min = new JButton(new ImageIcon ("src/images/dialogimage/fmin.png"));
        btn_min.setBorderPainted(false);
		btn_min.setContentAreaFilled(false);
		btn_min.addMouseListener(this);
        btn_min.setBounds(668, 0, 30, 30);
        panel_north.add(btn_min);
        //���Ͻǹرհ�ť
        btn_exit = new JButton(new ImageIcon ("src/images/dialogimage/fexit.png"));
        btn_exit.setBorderPainted(false);
        btn_exit.setContentAreaFilled(false);
        btn_exit.addMouseListener(this);
        btn_exit.setBounds(698, 0, 30, 30);
        panel_north.add(btn_exit);
        //ͷ���·����ܰ�ť
        //���ܰ�ť1
        btn_func1_north = new JButton(new ImageIcon("src/images/dialogimage/yuyin.png"));
        btn_func1_north.setBounds(10, 62, 36, 30);
        panel_north.add(btn_func1_north);
        //���ܰ�ť2
        btn_func2_north = new JButton(new ImageIcon("src/images/dialogimage/shipin.png"));
        btn_func2_north.setBounds(61, 62, 36, 30);
        panel_north.add(btn_func2_north);
        //���ܰ�ť3
        btn_func3_north = new JButton(new ImageIcon("src/images/dialogimage/tranfile.jpg"));
        btn_func3_north.setBounds(112, 62, 36, 30);
        panel_north.add(btn_func3_north);
        //���ܰ�ť4
        btn_func4_north = new JButton(new ImageIcon("src/images/dialogimage/createteam.jpg"));
        btn_func4_north.setBounds(163, 62, 36, 30);
        panel_north.add(btn_func4_north);
        //���ܰ�ť5
        btn_func5_north = new JButton(new ImageIcon("src/images/dialogimage/yuancheng.png"));
        btn_func5_north.setBounds(214, 62, 36, 30);
        panel_north.add(btn_func5_north);
        //���ܰ�ť6
        btn_func6_north = new JButton(new ImageIcon("src/images/dialogimage/sharedisplay.png"));
        btn_func6_north.setBounds(265, 62, 36, 30);
        panel_north.add(btn_func6_north);
        //���ܰ�ť7
        btn_func7_north = new JButton(new ImageIcon("src/images/dialogimage/yingyong.jpg"));
        btn_func7_north.setBounds(318, 62, 36, 30);
        panel_north.add(btn_func7_north);
        //���ñ�����屳��ɫ
        //panel_north.setBackground(new Color(105, 197, 239));
        panel_north.setBackground(new Color(22, 154, 228));

        //�в�����������ʾ����
        panel_Msg = new JTextPane();
        JScrollPane scrollPane_Msg = new JScrollPane(panel_Msg);
        scrollPane_Msg.setBounds(0, 92, 446, 270);
        c.add(scrollPane_Msg);

        //�ϲ����
        panel_south = new JPanel();
        panel_south.setBounds(0, 370, 446, 170);
        panel_south.setLayout(null);
        //����ϲ����
        c.add(panel_south);
        //����������
        jtp_input = new JTextPane();
        jtp_input.setBounds(0, 34, 446, 105);
        //��ӵ��ϲ����
        panel_south.add(jtp_input);
        //�ı��������Ϸ����ܰ�ť
        //���ܰ�ť1
        btn_func1_south = new JButton(new ImageIcon("src/images/dialogimage/word.png"));
        btn_func1_south.setBounds(10, 0, 30, 30);
        panel_south.add(btn_func1_south);
        //���ܰ�ť2
        btn_func2_south = new JButton(new ImageIcon("src/images/dialogimage/biaoqing.png"));
        btn_func2_south.setBounds(47, 0, 30, 30);
        panel_south.add(btn_func2_south);
        //���ܰ�ť3
        btn_func3_south = new JButton(new ImageIcon("src/images/dialogimage/magic.jpg"));
        btn_func3_south.setBounds(84, 0, 30, 30);
        panel_south.add(btn_func3_south);
        //���ܰ�ť4
        btn_func4_south = new JButton(new ImageIcon("src/images/dialogimage/zhendong.jpg"));
        btn_func4_south.setBounds(121, 0, 30, 30);
        panel_south.add(btn_func4_south);
        //���ܰ�ť5
        btn_func5_south = new JButton(new ImageIcon("src/images/dialogimage/yymessage.jpg"));
        btn_func5_south.setBounds(158, 0, 30, 30);
        panel_south.add(btn_func5_south);
        //���ܰ�ť6
        btn_func6_south = new JButton(new ImageIcon("src/images/dialogimage/dgninput.jpg"));
        btn_func6_south.setBounds(195, 0,30, 30);
        panel_south.add(btn_func6_south);
        //���ܰ�ť7
        btn_func7_south = new JButton(new ImageIcon("src/images/dialogimage/sendimage.jpg"));
        btn_func7_south.setBounds(232, 0, 30, 30);
        panel_south.add(btn_func7_south);
        //���ܰ�ť8
        btn_func8_south = new JButton(new ImageIcon("src/images/dialogimage/diange.jpg"));
        btn_func8_south.setBounds(269, 0, 30, 30);
        panel_south.add(btn_func8_south);
        //���ܰ�ť9
        btn_func9_south = new JButton(new ImageIcon("src/images/dialogimage/jietu.jpg"));
        btn_func9_south.setBounds(306, 0, 30, 30);
        panel_south.add(btn_func9_south);
        //��ѯ�����¼
        recorde_search = new JButton(new ImageIcon("src/images/dialogimage/recorde.png"));
        recorde_search.addActionListener(e-> {
            System.out.println("������������¼");
            cardLayout.next(panel_east);
        });
        recorde_search.setBounds(350, 0, 96, 30);
        panel_south.add(recorde_search);
        //��Ϣ�رհ�ť
        btn_close = new JButton(new ImageIcon("src/images/dialogimage/close.jpg"));
        btn_close.setBounds(290, 145, 64, 24);
        btn_close.addActionListener(this);
        panel_south.add(btn_close);
        //��Ϣ���Ͱ�ť
        btn_send = new JButton(new ImageIcon("src/images/dialogimage/send.jpg"));
        btn_send.addActionListener(this);
        btn_send.setBounds(381, 145, 64, 24);
        panel_south.add(btn_send);

        //�������(ͼƬ�������¼)
        panel_east = new JPanel();
        //��Ƭ����
        cardLayout = new CardLayout(2,2);
        panel_east.setLayout(cardLayout);
        panel_east.setBounds(444, 91, 285, 418);
        //��Ӷ������
        c.add(panel_east);
        //��ʾ�����¼���
        panel_Record = new JTextPane();
        panel_Record.setText("-----------------------------�����¼--------------------------\n\n");
        loadingHistory();
        loadingOffline();
        JScrollPane scrollPane_Record = new JScrollPane(panel_Record);
        scrollPane_Record.setBounds(2, 2, 411, 410);
        //��ӵ��������
        panel_east.add(label1);
        panel_east.add(scrollPane_Record);

        //ע������¼�������
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                //����ͷ�
                isDragged = false;
                //���ָ�
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
            @Override
            public void mousePressed(MouseEvent e) {
                //��갴��
                //��ȡ�����Դ���λ��
                frameLocation = new Point(e.getX(),e.getY());
                isDragged = true;
                //����Ϊ�ƶ���ʽ
                if(e.getY() < 92)
                    setCursor(new Cursor(Cursor.MOVE_CURSOR));
            }
        });
        //ע������¼�������
        this.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                //ָ����Χ�ڵ��������ק
                if(e.getY() < 92){
                    //����������ק�ƶ�
                    if(isDragged) {
                        Point loc = new Point(getLocation().x+e.getX()-frameLocation.x,
                                getLocation().y+e.getY()-frameLocation.y);
                        //��֤�����Դ���λ�ò���,ʵ���϶�
                        setLocation(loc);
                    }
                }
            }
        });

        this.setIconImage(new ImageIcon("src/images/Q.png").getImage());//�޸Ĵ���Ĭ��ͼ��
        this.setSize(728, 553);//���ô����С
        this.setUndecorated(true);//ȥ���Դ�װ�ο�
        this.setVisible(true);//���ô���ɼ�

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
            System.out.println("����");
            sendMsg(this, this.myName);
        }
    }

    /**
     * ʵ����Ϣ����
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
                System.out.println("���ͳɹ�");
                showMessage(msg,true);
                jtp_input.setText("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            JOptionPane.showMessageDialog(f,"���ܷ��Ϳ����ݣ�");
        }
    }

    /**
     * �����յ�����Ϣ��ʾ����
     * @param msg
     */
    
    public void showMessage(Message msg, boolean fromSelf) {
        showMessage(panel_Msg, msg, fromSelf);//����ʾ�������������
        showMessage(panel_Record, msg, fromSelf);//����ʾ�������¼���
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
    		System.out.println("���߼�¼����Ϊ"+mss.getChatContent());
    		mss.setSendTime(message.substring(message.length()-22,message.length()));
    		System.out.println("���߼�¼ʱ��Ϊ"+mss.getSendTime());
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
     * ����Ϣ������ʾ��ָ�����
     * @param jtp
     * @param msg
     * @param fromSelf
     */
    public void showMessage(JTextPane jtp, Message msg, boolean fromSelf) {
        //������ʾ��ʽ
        SimpleAttributeSet attrset = new SimpleAttributeSet();
        StyleConstants.setFontFamily(attrset, "����");
        StyleConstants.setFontSize(attrset,14);
        Document docs = jtp.getDocument();
        String info = null;
        try {
            if(fromSelf){//����ȥ����Ϣ���ݣ���ʾ���Լ���ʱ
                info = myName;
                StyleConstants.setForeground(attrset, Color.MAGENTA);//�Լ��˺ţ���ɫ
                docs.insertString(docs.getLength(), info, attrset);
                StyleConstants.setForeground(attrset, Color.red);
                
                info = msg.getSendTime()+":\n";//����ʱ�䣺��ɫ
                StyleConstants.setForeground(attrset, Color.black);
                docs.insertString(docs.getLength(), info, attrset);
                
                info = " "+msg.getChatContent()+"\n";//�������ݣ���ɫ
                StyleConstants.setFontSize(attrset,16);
                StyleConstants.setForeground(attrset, Color.green);
                docs.insertString(docs.getLength(), info, attrset);
            }else{//���յ�����Ϣ���ݣ���ʾ���Լ���ʱ
                info = msg.getSenderWebname()+"("+msg.getSender()+")  ";//�Է����˺ţ���ɫ
                StyleConstants.setForeground(attrset, Color.red);
                docs.insertString(docs.getLength(), info, attrset);                  
                StyleConstants.setForeground(attrset, Color.red);
                
                info = msg.getSendTime()+":\n";//����ʱ�䣺��ɫ
                StyleConstants.setForeground(attrset, Color.black);
                docs.insertString(docs.getLength(), info, attrset);
                
                info = " "+msg.getChatContent()+"\n";//�������ݣ���ɫ
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
            System.out.println("�һ���ʷ��¼");
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
            System.out.println("����������Ϣ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}