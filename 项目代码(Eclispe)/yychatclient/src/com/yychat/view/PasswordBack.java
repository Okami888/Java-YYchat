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
		
		jl2 = new JLabel("���һ��˺ţ�");
		jTF1=new JTextField(25);
		jp1=new JPanel();
		jp1.add(jl2);
		jp1.add(jTF1);
		
		
		jp2=new JPanel();
		jb1=new JButton("ȷ��");
		jb1.addActionListener(this);
		jb2=new JButton("ȡ��");	
		jb2.addActionListener(this);
		
		jp2.add(jb1);
		jp2.add(jb2);
		
		this.add(jl1,"North");
		this.add(jp2,"South");
		this.add(jp1,"Center");
		
		//this.add(new JLabel("s"));
		
		this.setSize(350,250);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);//�رմ��ڵ�ͬʱ�˳�����
		this.setTitle("�һ�����");                        //���ڱ���
		this.setIconImage(new ImageIcon("src/images/duck2.gif").getImage() );//���ô���ͼ��
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
			
			String userName=jTF1.getText().trim();//���ǰ��Ŀհ�
			if(!(userName.equals("")||userName==null)) {
				User user=new User();
				user.setUserName(userName);

				user.setUserType(User.USER_SEEK_PASSWORD);
				
				Boolean seekSuccess=new YYchatClientConnection().seekPassword(user);
				
				if(seekSuccess) {//��������˽������Ӳ����л�����User���󣬽��е�¼��֤
					JOptionPane.showMessageDialog(this,"���������ѷ������ܱ����䣬��ע����գ�");
				}else {
					JOptionPane.showMessageDialog(this,"������˺��������˺�û�����䣡");
				}
			}else {
				JOptionPane.showMessageDialog(this,"�������˺ţ�");
			}
			
				
		}
		
		if(e.getSource()==jb2) {
			this.dispose();
			//this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
		}
		
		
	}

}
