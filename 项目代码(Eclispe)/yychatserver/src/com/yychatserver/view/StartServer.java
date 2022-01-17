package com.yychatserver.view;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.yychat.model.Message;
import com.yychatserver.control.YYchatServer;
import com.yychatserver.control.ManageAllThreads;
import com.yychatserver.control.ServerReceiverThread;
/**
 * �����������رս���
 */
public class StartServer extends JFrame implements ActionListener ,MouseListener{

	private static final long serialVersionUID = 1L;

	private JButton btn_start, btn_close;//���ܰ�ť
	public static JTextArea textArea_log;//��־��¼���
	private JLabel jlb_border1, jlb_border2, jlb_border3, jlb_border4, jlb_border5;//���ڽ���ָ�
	private JLabel label_log;//��־��¼��ǩ
    private static DateFormat df;//���ڽ���
    private YYchatServer s;

	public static void main(String[] args) {
		new StartServer();
	}

	public StartServer()
	{
        df = new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss");
		//��ȡ��������
        Container c = this.getContentPane();
        //���ò���
        c.setLayout(null);
        //���ϲ���
        label_log = new JLabel();
        label_log.setFont(new Font("΢���ź�",Font.BOLD,14));
        label_log.setText("ϵͳ��־��¼");
        label_log.setBounds(155, 10, 100, 15);
        c.add(label_log);
        //��־��¼���
        textArea_log = new JTextArea();
        JScrollPane scrollPane_log = new JScrollPane(textArea_log);
        scrollPane_log.setBounds(10, 33, 377, 376);
        c.add(scrollPane_log);

        //������������ť
		btn_start = new JButton("����������");
		btn_start.setBounds(45, 450, 120, 24);
		btn_start.addActionListener(this);
		c.add(btn_start);
		//�������رհ�ť
		btn_close = new JButton("�رշ�����");
		btn_close.setBounds(230, 450, 120, 24);
		btn_close.addActionListener(this);
		c.add(btn_close);

        //����ָ��ǩ1
		jlb_border1 = new JLabel();
		jlb_border1.setBounds(392, 0, 2, 500);
		jlb_border1.setOpaque(true);//��͸��
		jlb_border1.setBackground(Color.GREEN);
		c.add(jlb_border1);
        //����ָ��ǩ2
		jlb_border2 = new JLabel();
		jlb_border2.setBounds(5, 425, 404, 2);
		jlb_border2.setOpaque(true);//��͸��
		jlb_border2.setBackground(Color.GREEN);
		c.add(jlb_border2);
        //����ָ��ǩ3
		jlb_border3 = new JLabel();
		jlb_border3.setBounds(3, 0, 2, 502);
		jlb_border3.setOpaque(true);//��͸��
		jlb_border3.setBackground(Color.GREEN);
		c.add(jlb_border3);
        //����ָ��ǩ4
        jlb_border4 = new JLabel();
        jlb_border4.setOpaque(true);
        jlb_border4.setBackground(Color.GREEN);
        jlb_border4.setBounds(5, 500, 404, 2);
        c.add(jlb_border4);
        //����ָ��ǩ5
		jlb_border5 = new JLabel();
		jlb_border5.setBounds(5, 0, 404, 2);
		jlb_border5.setOpaque(true);//��͸��
		jlb_border5.setBackground(Color.GREEN);
		c.add(jlb_border5);

		this.setResizable(false);//����ҳ���С�̶�
		this.setSize(411, 560);//���ô�С
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//Ĭ�Ϲرշ�ʽ
		this.setVisible(true);//ҳ��ɼ�
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {

	}

	public void mouseReleased(MouseEvent e) {

	}

	public void mouseEntered(MouseEvent e) {

	}

	public void mouseExited(MouseEvent e) {

	}

    /**
     * ����ť����¼�
     * @param e
     */
	@Override
	public void actionPerformed(ActionEvent e) {

        if(e.getSource() == btn_start){//����������
            s = new YYchatServer();
            showMsg("����������...");
        }
        if(e.getSource() == btn_close){//�رշ�����
            beforeServerClose();
            showMsg("�������ѹر�...");
        }
	}

    /**
     * ��������־�����ʾ��Ϣ
     */
    public static void showMsg(String s) {
        textArea_log.append(df.format(new Date())+": "+s+"\n"
        +"---------------------------------ServerSocket(3456)---------------------------------"+"\n\n");
    }

    /**
     * �رշ�����ǰ��֪ͨ�����û��������������߳�
     */
    private void beforeServerClose(){
        Message ms = new Message();
        ms.setMessageType(Message.SERVER_CLOSE);
        for(Object o: ManageAllThreads.getServerReceiverThreads().keySet()){
            String toId = o.toString();
            ms.setReceiver(toId);
            ServerReceiverThread th = ManageAllThreads.getServerReceiverThread(toId);
            try {
                ObjectOutputStream out = new ObjectOutputStream(th.getSreceiveThreadS().getOutputStream());
                out.writeObject(ms);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            Thread.sleep(1000);//�ȴ����пͻ�������
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        s.myStop();//������ֹͣ����
    }

}