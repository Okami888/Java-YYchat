package com.yychat.view;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.*;


import com.yychat.model.Message;
import com.yychat.model.User;
import com.yychatclient.control.ManageAllList;
import com.yychatclient.control.YYchatClientConnection;

public class ClientLogin extends JFrame implements ActionListener,MouseListener,FocusListener,ItemListener{//实现监听接口
	
	//创建HashMap用于取出保存用户名和对应的密码
	private static HashMap hmPw= new HashMap<String,String>();
	//用于定位鼠标位置
	private int mouseAtX=0;
	private int mouseAtY=0;
	//定义北部
	private JLabel JL_1;
	//定义中部
	private JTabbedPane jTabbedPane;                        //中部为选项卡
	private JPanel jPanel_YYLogin,jPanel_YYLogin2;          //有两个选项，每个选项包含两个jPanel
	JPanel jPanel_YYRegister,jPanel_YYRegister2;	
	//定义中部的中部
	private JLabel jl1,jl2,jl3,jl4,jl5,jl6,jl7,jl8;
	private JTextField JT_registerWebname,JT_registerEmail;                  //用户输入账号地方是文本框
	private JPasswordField jPf_importPw,JPf_registerPw1,JPf_registerPw2;     //用户输入密码地方是密码框
	private JButton jb_clearNum;
	private JCheckBox jc1,jc2;
	private JComboBox<String> jT_importYYNum;			
	//定义中部的南部
	private JPanel jPanel_Action1,jPanel_Action2;
	private JButton jB_Login,jB_Register,jB_Cancel;		
	
	public ClientLogin(String title) {
		
		//JFrame北区
		JL_1=new JLabel(new ImageIcon("src/images/head.gif"));      //Java教学聊天标签
		this.add(JL_1,"North");                                     //边界布局BorderLayout，添加到北区	
		
		//JFrame中部
		jTabbedPane=new JTabbedPane();
		jPanel_YYLogin=new JPanel(new BorderLayout());
		jPanel_YYLogin2=new JPanel(new GridLayout(3,3));            //网格布局，待会9个组件依次添加到这个面板
		jPanel_Action1=new JPanel();                                //jPanel默认FLowLayout流式布局

		jPanel_YYRegister=new JPanel(new BorderLayout());
		jPanel_YYRegister2=new JPanel(new GridLayout(4,2));
		jPanel_Action2=new JPanel();
		//中部组件
		//1.第一个选项
		//1.1中部
		jl1=new JLabel("YY号码：",JLabel.CENTER);
		jl2=new JLabel("YY密码：",JLabel.CENTER);
		jl3=new JLabel("找回密码",JLabel.CENTER);
		jl3.addMouseListener(this);
		jl3.setForeground(Color.blue);
		jl4=new JLabel("显示密码",JLabel.CENTER);
		jl4.addMouseListener(this);
		
		jT_importYYNum=new JComboBox<String>();        
		jT_importYYNum.addItem("YY号/邮箱");			   //初始化jCombox的选项，用作提示
		createJcombox(jT_importYYNum);				   //再调用方法来构建选项
		jT_importYYNum.setEditable(true);			   //设置jCombox可编辑，jT_importYYNum.setForeground(Color.GRAY);//设置颜色无效，要用到comboxeditor	
		jT_importYYNum.addItemListener(this);		   //注册选项监听，jT_importYYNum.addFocusListener(this);//在可编辑下，注册焦点监听无效，
		
		
		jPf_importPw=new JPasswordField(25);
		jPf_importPw.setText("密码");					   //初始化jPasswordField的值，用作提示
		jPf_importPw.setForeground(Color.GRAY);
		jPf_importPw.setEchoChar((char)0);             //显示出“密码”，否则会自动被原点遮掩		
		jPf_importPw.addFocusListener(this);           //注册焦点监听
		
		jb_clearNum=new JButton("清除号码");
		jb_clearNum.setContentAreaFilled(false);       //设置按钮透明
		jb_clearNum.addActionListener(this);	       
		
		jc1=new JCheckBox("隐身登录");                    
		jc2=new JCheckBox("记住密码",true);			   //复选框“记住密码”默认勾选	
		//1.2南部
		jB_Login=new JButton(new ImageIcon("src/images/login.gif"));
		jB_Login.setContentAreaFilled(false);
		jB_Login.addActionListener(this);                                 //注册动作事件（按下去会有事件发生），并从当前窗体监听
		jB_Cancel=new JButton(new ImageIcon("src/images/cancel.gif"));
		jB_Cancel.setContentAreaFilled(false);
		jB_Cancel.addActionListener(this);
		
		//2.第二个选项
		//2.1中部
		jl5=new JLabel("起个网名：",JLabel.CENTER);
		jl6=new JLabel("设置密码：",JLabel.CENTER);
		jl7=new JLabel("确认密码：",JLabel.CENTER);
		jl8=new JLabel("密保邮箱：",JLabel.CENTER);
		
		JT_registerWebname=new JTextField("你的网名（必填）");
		JT_registerWebname.setForeground(Color.GRAY);
		JT_registerWebname.addFocusListener(this);
		
		JPf_registerPw1=new JPasswordField("你的密码（必填）");
		JPf_registerPw1.setForeground(Color.GRAY);
		JPf_registerPw1.setEchoChar((char)0);
		JPf_registerPw1.addFocusListener(this);
		JPf_registerPw2=new JPasswordField("请输入相同密码");
		JPf_registerPw2.setForeground(Color.GRAY);
		JPf_registerPw2.setEchoChar((char)0);
		JPf_registerPw2.addFocusListener(this);
		
		JT_registerEmail=new JTextField("可选项");
		JT_registerEmail.setForeground(Color.GRAY);
		JT_registerEmail.addFocusListener(this);
		//2.2南部	
		jB_Register=new JButton(new ImageIcon("src/images/register.gif"));
		jB_Register.setContentAreaFilled(false);
		jB_Register.addActionListener(this);
		
		//组件的添加，网格布局的要按顺序添加	
		jPanel_YYLogin2.add(jl1);
		jPanel_YYLogin2.add(jT_importYYNum);
		jPanel_YYLogin2.add(jb_clearNum);
		jPanel_YYLogin2.add(jl2);
		jPanel_YYLogin2.add(jPf_importPw);
		jPanel_YYLogin2.add(jl4);
		jPanel_YYLogin2.add(jc1);
		jPanel_YYLogin2.add(jc2);
		jPanel_YYLogin2.add(jl3);	
		jPanel_YYRegister2.add(jl5);
		jPanel_YYRegister2.add(JT_registerWebname);
		jPanel_YYRegister2.add(jl6);
		jPanel_YYRegister2.add(JPf_registerPw1);
		jPanel_YYRegister2.add(jl7);
		jPanel_YYRegister2.add(JPf_registerPw2);
		jPanel_YYRegister2.add(jl8);
		jPanel_YYRegister2.add(JT_registerEmail);

		jPanel_Action1.add(jB_Login);
		jPanel_Action1.add(jB_Cancel);
		jPanel_Action2.add(jB_Register);
		
		jPanel_YYLogin.add(jPanel_YYLogin2,"Center");
		jPanel_YYLogin.add(jPanel_Action1,"South");
		jPanel_YYRegister.add(jPanel_YYRegister2,"Center");
		jPanel_YYRegister.add(jPanel_Action2,"South");
		
		jTabbedPane.add(jPanel_YYLogin,"登录账号");
		jTabbedPane.add(jPanel_YYRegister,"注册账号");
		//jTabbedPane.setOpaque(false);
		//jPanel_YYLogin.setOpaque(false);
		//setOpaque(false);
		this.add(jTabbedPane,"Center");
		
		
		//this.getLayeredPane().add(background, Integer.valueOf(Integer.MIN_VALUE));
		//jPanel = (JPanel)this.getContentPane();
		//jPanel.setOpaque(false); 					//将最上层设置为透明
		
		//设置窗体
		this.setSize(350,250);	
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);//关闭窗口的同时退出程序
		this.setTitle(title);                        //窗口标题
		this.setResizable(false);					 //设置不可改变大小
		//this.setUndecorated(true);				 //去边框
		this.setIconImage(new ImageIcon("src/images/duck2.gif").getImage() );//设置窗口图标
		this.setLocationRelativeTo(null);
		this.setVisible(true);	
		//实现窗口可拖拽
		this.addMouseListener(new MouseAdapter() {
			@Override
			public  void mousePressed(MouseEvent e) {
				mouseAtX = e.getPoint().x;
                mouseAtY = e.getPoint().y;
			}
		});
		this.addMouseMotionListener(new MouseMotionAdapter()
        {
            public void mouseDragged(MouseEvent e) 
            {
                setLocation((e.getXOnScreen()-mouseAtX),(e.getYOnScreen()-mouseAtY));//设置拖拽后，窗口的位置
            }
        });
	}
	
	public void savePw(String userName , String passWord){//不用注册表，简单模拟保存账号密码到本地
		 try{
		      String data =userName+" "+passWord+"\r";
		      File file =new File("src/Pw.txt");
		      if(!file.exists()){
		       file.createNewFile();
		      }
		      FileWriter fileWritter = new FileWriter(file,true);
		      fileWritter.write(data);
		      fileWritter.close();
		      System.out.println("成功保存密码");
		     }catch(IOException e){
		      e.printStackTrace();
		     }
	}
	public void createJcombox(JComboBox<String> jComboBox) {//模拟读取注册表来创建jCombox的选项
		File file =new File("src/Pw.txt");
		String str1;
		String str2;
		Scanner sc1;
		try {
			sc1 = new Scanner(file);
			while(sc1.hasNextLine()) {//获取账号
				
				String line1=sc1.nextLine();
				str1=line1.substring(0,6);
				
				jComboBox.addItem(str1);//账号来作为项
				
				str2=line1.substring(7,line1.length());//获取密码并保存备用	
				hmPw.put(str1, str2);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}		
	}
	public void clearNum(String string) {//清除对应账号的注册表信息
		File file =new File("src/Pw.txt");
		String str1="";
		Scanner sc1;
		try {
			sc1 = new Scanner(file);
			while(sc1.hasNextLine()) {
				String line1=sc1.nextLine();
				if(line1.startsWith(string)) {
					continue;
				}else {
					str1+=line1+"\r";			
				}
			}
			FileWriter fileWritter = new FileWriter(file,false);
		    fileWritter.write(str1);
		    fileWritter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
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
	public static void main(String[] args) {
		ClientLogin cl= new ClientLogin("YY聊天");	
	}	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==jb_clearNum) {
			clearNum(jT_importYYNum.getSelectedItem().toString());
			if(!jT_importYYNum.getSelectedItem().toString().equals("YY号/邮箱")) {
				jT_importYYNum.removeItem(jT_importYYNum.getSelectedItem());		//移除已经清除注册表的账号选项
				//jT_importYYNum.addItem("YY号/邮箱");
				//createJcombox(jT_importYYNum);
				//jT_importYYNum.revalidate();
			}		
		}
		
		if(e.getSource()==jB_Cancel) {
			this.dispose();
		}
		
		if(e.getSource()==jB_Register) {
			
			String webName=JT_registerWebname.getText().trim();//清除前后的空白
			String passWord=new String(JPf_registerPw1.getPassword());
			String Email=JT_registerEmail.getText().trim();
			if(webName.equals("")||webName==null||webName.equals("起个网名：")) {//判断网名是否为空
				JOptionPane.showMessageDialog(this, "请起个网名！");
			}else {
				if(passWord.equals("")||passWord==null||passWord.equals("设置密码：")) {
					JOptionPane.showMessageDialog(this, "请设置密码！");
				}else if(!passWord.equals(new String(JPf_registerPw2.getPassword()))) {
					JOptionPane.showMessageDialog(this, "密码不一致！");
				}else {
					if(((!(Email.equals("")||Email==null))&&(Email.endsWith("@qq.com")))||Email.equals("可选项")) {//密保邮箱为可选项，现在只支持qq邮箱的注册与找回
						if(Email.equals("可选项")) {
							Email="";
						}
						
						User user=new User();
						user.setWebName(webName);
						user.setPassWord(passWord);
						user.setEmail(Email);
						user.setUserType(User.USER_REGISTER);
						
						String userName=new YYchatClientConnection().registerUser(user);//获取注册的账号
						
						JOptionPane.showMessageDialog(this, "注册成功！您的账号为： "+userName+" !");
					}else {
							
						JOptionPane.showMessageDialog(this, "请输入正确的邮箱地址！");//现只支持qq邮箱
					}										
				}
			}				
		}
		
		if(e.getSource()==jB_Login) {
			
			String userName=jT_importYYNum.getSelectedItem().toString().trim();
			String passWord=new String(jPf_importPw.getPassword()).trim();
			
			User user=new User();											//与服务器端建立连接并序列化传递User对象，进行登录验证
			user.setUserName(userName);
			user.setPassWord(passWord);
			user.setUserType(User.USER_LOGIN_VALIDATE);
			
			Message ms=new YYchatClientConnection().loginValidate(user);    //验证返回的是信息类
			
			if(ms.getMessageType().equals(Message.LOGIN_VALIDATE_SUCCESS)) {
				
				if(jc1.isSelected()) {
					ms.setStatus(Message.NOT_ONLINE);
				}else {
					ms.setStatus(Message.NEW_ONLINE_FRIEND_TO_SERVER);
				}
				if(jc2.isSelected()) {
					savePw(userName, passWord);
				}
				
				
				String allFriend = ms.getChatContent();
				String webname=ms.getSenderWebname();
				ms.setSender(userName);
				System.out.println(webname);
				//创建好友列表
				FriendList friendList=new FriendList(webname,userName,ms);
				//保存每个登录成功的用户名与其对应的好友列表对象
				ManageAllList.addFriendListFrame(userName, friendList);
				//登录成功后向服务器发送请求获取好友的信息		
				ms.setMessageType(Message.REQUEST_ONLINE_FRIEND);
				sendMessage(YYchatClientConnection.clientS, ms);
				//向服务器发送自己上线的信息（要求其他好友反应）
				//ms.setMessageType(Message.NEW_ONLINE_FRIEND_TO_SERVER);
				//sendMessage(YYchatClientConnection.clientS,ms);			
				
				this.dispose();//使登录界面消失
			}else if(ms.getMessageType().equals(Message.LOGIN_VALIDATE_FAILURE)) {
				JOptionPane.showMessageDialog(this, "密码错误！请重新登录！");
			}else {
				JOptionPane.showMessageDialog(this, "该账号已经登录，请勿重复登录！");
			}				
		}
	}
	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getSource()==jl3) {
			if(e.getClickCount()==1) {
				PasswordBack passwordBack= new PasswordBack();//创建找回密码的窗体
			}
		}
	}
	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getSource()==jl4) {
			jPf_importPw.setEchoChar((char)0);//按下去显示密码
		}	
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getSource()==jl4) {
			jPf_importPw.setEchoChar('•');//松开掩盖密码
		}
	}
	@Override
	public void mouseEntered(MouseEvent e) {
		
	}
	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	@Override
	public void focusGained(FocusEvent e) {//获取焦点，使准备输入时提示消失
		
		if(e.getSource()==jPf_importPw) {
			if(jPf_importPw.getText().equals("密码")) {			
				jPf_importPw.setText("");
				jPf_importPw.setEchoChar('•');
				jPf_importPw.setForeground(Color.BLACK);
			}	
		}
		if(e.getSource()==JT_registerWebname) {
			if(JT_registerWebname.getText().equals("你的网名（必填）")) {
				JT_registerWebname.setText("");
				JT_registerWebname.setForeground(Color.BLACK);
			}
		}
		if(e.getSource()==JT_registerEmail) {
			if(JT_registerEmail.getText().equals("可选项")) {
				JT_registerEmail.setText("");
				JT_registerEmail.setForeground(Color.BLACK);
			}
		}
		if(e.getSource()==JPf_registerPw1) {
			if(JPf_registerPw1.getText().equals("你的密码（必填）")) {			
				JPf_registerPw1.setText("");
				JPf_registerPw1.setEchoChar('•');
				JPf_registerPw1.setForeground(Color.BLACK);
			}	
		}
		if(e.getSource()==JPf_registerPw2) {
			if(JPf_registerPw2.getText().equals("请输入相同密码")) {			
				JPf_registerPw2.setText("");
				JPf_registerPw2.setEchoChar('•');
				JPf_registerPw2.setForeground(Color.BLACK);
			}	
		}
	}
	@Override
	public void focusLost(FocusEvent e) {//失去焦点时，使提示恢复
		
		if(e.getSource()==jPf_importPw) {
			if(jPf_importPw.getText().equals("")) {		
				jPf_importPw.setForeground(Color.GRAY);
				jPf_importPw.setEchoChar((char)0);
				jPf_importPw.setText("密码");
			}		
		}
		if(e.getSource()==JT_registerWebname) {
			if(JT_registerWebname.getText().equals("")) {		
				JT_registerWebname.setForeground(Color.GRAY);
				JT_registerWebname.setText("你的网名（必填）");
			}
		}
		if(e.getSource()==JT_registerEmail) {
			if(JT_registerEmail.getText().equals("")) {		
				JT_registerEmail.setForeground(Color.GRAY);
				JT_registerEmail.setText("可选项");
			}
		}
		if(e.getSource()==JPf_registerPw1) {
			if(JPf_registerPw1.getText().equals("")) {		
				JPf_registerPw1.setForeground(Color.GRAY);
				JPf_registerPw1.setEchoChar((char)0);
				JPf_registerPw1.setText("你的密码（必填）");
			}		
		}
		if(e.getSource()==JPf_registerPw2) {
			if(JPf_registerPw2.getText().equals("")) {		
				JPf_registerPw2.setForeground(Color.GRAY);
				JPf_registerPw2.setEchoChar((char)0);
				JPf_registerPw2.setText("请输入相同密码");
			}		
		}
	}
	@Override
	public void itemStateChanged(ItemEvent e) {//实现输入账号，对应的密码自动填充
		if(e.getSource()==jT_importYYNum) {
			if(!jT_importYYNum.getSelectedItem().toString().equals("YY号/邮箱")) {
				jPf_importPw.setText((String)hmPw.get(jT_importYYNum.getSelectedItem().toString()));
				jPf_importPw.setEchoChar('•');
				jPf_importPw.setForeground(Color.BLACK);
			}else {
				jPf_importPw.setText("");
				jPf_importPw.setForeground(Color.GRAY);
				jPf_importPw.setEchoChar((char)0);
			}					
		}
		
	}

}
