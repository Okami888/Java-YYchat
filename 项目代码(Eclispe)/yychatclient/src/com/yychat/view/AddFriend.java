package com.yychat.view;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.yychat.model.Message;

import com.yychatclient.control.ManageAllThreads;

public class AddFriend extends JFrame implements ActionListener {
		
		/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		JButton jb1,jb2;
		JPanel jp1,jp2;
		JTextField jTF1;
		JLabel jl1,jl2;
		
		String sender;
		
			
		public AddFriend(String id){
			
			sender=id;
			
			jl1=new JLabel(new ImageIcon("src/images/head.gif"));
			
			jl2 = new JLabel("�������º��ѵ�id��");
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
			this.setTitle("��Ӻ���");                        //���ڱ���
			this.setIconImage(new ImageIcon("src/images/duck2.gif").getImage() );//���ô���ͼ��
			this.setLocationRelativeTo(null);
			this.setVisible(true);
			
		}

		public static void main(String[] args) {

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if(e.getSource()==jb1) {
				
				String newFriendName=jTF1.getText().trim();//���ǰ��Ŀհ�
				
				if(!(newFriendName.equals("")||newFriendName==null)) {
					Message ms=new Message();
					ms.setSender(sender);
					ms.setReceiver(newFriendName);
					ms.setChatContent(newFriendName);
					ms.setMessageType(Message.ADD_NEW_FRIEND);
					OutputStream os;
					try {
						os=ManageAllThreads.getThread(sender).getCreceiveThreadS().getOutputStream();
						ObjectOutputStream oos=new ObjectOutputStream(os);
						oos.writeObject(ms);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}else {
					JOptionPane.showMessageDialog(this,"����������˺ţ�");
				}
					
			}
			
			if(e.getSource()==jb2) {
				this.dispose();
				//this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));
			}
			
			
		}

	

}
