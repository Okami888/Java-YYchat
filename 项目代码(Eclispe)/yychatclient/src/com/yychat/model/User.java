package com.yychat.model;//

import java.io.Serializable;

public class User implements Serializable,UserType{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String userName;
	String passWord;
	String webName;
	String Email;
	String userType;
	Byte Image;	
	public Byte getImage() {
		return Image;
	}
	public void setImage(Byte image) {
		Image = image;
	}
	public String getWebName() {
		return webName;
	}
	public void setWebName(String webName) {
		this.webName = webName;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}		
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassWord() {
		return passWord;
	}
	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
}
