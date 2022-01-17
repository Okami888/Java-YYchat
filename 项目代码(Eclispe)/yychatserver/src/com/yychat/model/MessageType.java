package com.yychat.model;


public interface MessageType {
	//规范LOGIN_VALIDATE_SUCCESS和LOGIN_VALIDATE_FAILURE类变量，为验证成功与验证失败的信息
	String LOGIN_VALIDATE_SUCCESS="1";
	String LOGIN_VALIDATE_FAILURE="2";
	String ALREAD_LOGIN="3";
	//定义规范普通聊天信息类型
	String COMMON_CHAT_MESSAGE="4";
	//增加查找历史记录的信息类型
	String SEEK_PAST_MESSAGE="5";
	//新增请求消息类型
	String REQUEST_ONLINE_FRIEND="6";
	String RESPONSE_ONLINE_FRIEND="7";
	//增加新上线好友信息,退出登录，不在线
	String NEW_ONLINE_FRIEND_TO_SERVER="8";
	String NEW_ONLINE_FRIENDR="9";//在线
	String NOT_ONLINE="10";//不在线
	String UNLOAD_LOGIN="11";//退出登录
	//添加注册成功和失败两种消息类型
	String USER_REGISTER_SUCCESS="12";
	String USER_REGISTER_FAILURE="13";
	//增加添加新好友的消息类型
	String ADD_NEW_FRIEND="14";
	String ADD_NEW_FRIEND_SUCCESS="15";
	String ADD_NEW_FRIEND_FAILURE_NO_USER="16";
	String ADD_NEW_FRIEND_FAILURE_ALREADY_FRIEND="17";
	String OTHERS_ADD_YOU="18";
	//增加删除好友信息
	String REMOVE_FRIEND="19";
	//添加验证好友信息
	String VALIDATE_FRIEND="20";
	String IS_FRIEND="21";
	String NOT_FRIEND="22";
	//增加加载离线聊天记录信息
	String LODING_OFFLINE_MESSAGE="23";
	//增加找回密码
	String SEEK_PASSWORD_SUCCESS="24";
	String SEEK_PASSWORD_FAILURE="25";
	//增加服务器关闭信息
	String SERVER_CLOSE="26";
	
	
}