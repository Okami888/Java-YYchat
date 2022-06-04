package com.yychat.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yychat.model.User;
import com.yychatclient.control.YYchatClientConnection;

public class PasswordBack extends JFrame implements ActionListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JButton jb1,jb2;
	JPanel jp1,jp2;
	JTextField jTF1;
	JLabel jl1,jl2;
		
	public PasswordBack(){
		
		jl1=new JLabel(new ImageIcon("src/images/head.gif"));
		
		jl2 = new JLabel("需找回账号：");
		jTF1=new JTextField(25);
		jp1=new JPanel();
		jp1.add(jl2);
		jp1.add(jTF1);
		
		
		jp2=new JPanel();
		jb1=new JButton("确定");
		jb1.addActionListener(this);
		jb2=new JButton("取消");	
		jb2.addActionListener(this);
		
		jp2.add(jb1);
		jp2.add(jb2);
		
		this.add(jl1,"North");
		this.add(jp2,"South");
		this.add(jp1,"Center");
		
		//this.add(new JLabel("s"));
		
		this.setSize(350,250);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//关闭窗口的同时退出程序
		this.setTitle("找回密码");                        //窗口标题
		this.setIconImage(new ImageIcon("src/images/duck2.gif").getImage() );//设置窗口图标
		this.setLocationRelativeTo(null);
		this.setVisible(true);
		
	}
	
	public void display() {}

	public static void main(String[] args) {
		//PasswordBack passwordBack=new PasswordBack();

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(e.getSource()==jb1) {
			
			String userName=jTF1.getText().trim();//清除前后的空白
			if(!(userName.equals("")||userName==null)) {
				User user=new User();
				user.setUserName(userName);

				user.setUserType(User.USER_SEEK_PASSWORD);
				
				Boolean seekSuccess=new YYchatClientConnection().seekPassword(user);
				
				if(seekSuccess) {//与服务器端建立连接并序列化传递User对象，进行登录验证
					JOptionPane.showMessageDialog(this,"您的密码已发送至密保邮箱，请注意查收！");
				}else {
					JOptionPane.showMessageDialog(this,"输入的账号有误或该账号没绑定邮箱！");
				}
			}else {
				JOptionPane.showMessageDialog(this,"请输入账号！");
			}
			
				
		}
		
		if(e.getSource()==jb2) {
			this.dispose();
			//this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		}
		
		
	}

}
