package com.yychat.model;


public interface MessageType {
	//�淶LOGIN_VALIDATE_SUCCESS��LOGIN_VALIDATE_FAILURE�������Ϊ��֤�ɹ�����֤ʧ�ܵ���Ϣ
	String LOGIN_VALIDATE_SUCCESS="1";
	String LOGIN_VALIDATE_FAILURE="2";
	String ALREAD_LOGIN="3";
	//����淶��ͨ������Ϣ����
	String COMMON_CHAT_MESSAGE="4";
	//���Ӳ�����ʷ��¼����Ϣ����
	String SEEK_PAST_MESSAGE="5";
	//����������Ϣ����
	String REQUEST_ONLINE_FRIEND="6";
	String RESPONSE_ONLINE_FRIEND="7";
	//���������ߺ�����Ϣ,�˳���¼��������
	String NEW_ONLINE_FRIEND_TO_SERVER="8";
	String NEW_ONLINE_FRIENDR="9";//����
	String NOT_ONLINE="10";//������
	String UNLOAD_LOGIN="11";//�˳���¼
	//���ע��ɹ���ʧ��������Ϣ����
	String USER_REGISTER_SUCCESS="12";
	String USER_REGISTER_FAILURE="13";
	//��������º��ѵ���Ϣ����
	String ADD_NEW_FRIEND="14";
	String ADD_NEW_FRIEND_SUCCESS="15";
	String ADD_NEW_FRIEND_FAILURE_NO_USER="16";
	String ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND="17";
	String OTHERS_ADD_YOU="18";
	//����ɾ��������Ϣ
	String REMOVE_FRIEND="19";
	//�����֤������Ϣ
	String VALIDATE_FRIEND="20";
	String IS_FRIEND="21";
	String NOT_FRIEND="22";
	//���Ӽ������������¼��Ϣ
	String LODING_OFFLINE_MESSAGE="23";
	//�����һ�����
	String SEEK_PASSWORD_SUCCESS="24";
	String SEEK_PASSWORD_FAILURE="25";
	//���ӷ������ر���Ϣ
	String SERVER_CLOSE="26";
	
	
}