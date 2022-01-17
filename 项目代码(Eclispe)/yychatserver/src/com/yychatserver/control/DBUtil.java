package com.yychatserver.control;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;



public class DBUtil {
	public static final String URL = "jdbc:sqlserver://localhost:1433;DatabaseName=yychat;";
	public static final String USERNAME="sa";
	public static final String PASSWORD="123456";
	
	public static Connection getConnection() {
		Connection conn=null;
		try {
			
			//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(URL ,USERNAME,PASSWORD);	
			
		} catch (/*ClassNotFoundException |*/ SQLException e) {
			e.printStackTrace();
		}
		
		return conn;
	}
	
	
	
	public static String seekAllFriend(String userName) {
		String allFriend="";
		Connection conn=getConnection();	
		String seek_All_Friend_Sql="select webname , slaveuser from userrelation,[user] where masteruser=? and relationtype='1' and username=slaveuser";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_All_Friend_Sql);
			ptmt.setString(1, userName);
			ResultSet rs=ptmt.executeQuery();
			while(rs.next()) {
				allFriend=allFriend+rs.getString(1)+"("+rs.getString(2)+") ";//获取查询结果的第一列数据
			}
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return allFriend;
	}
	
	public static String seekMessage(String userID,String friendID) {
		String Message="";
		Connection conn=getConnection();	
		String seek_All_Friend_Sql="exec SeekMessage ?,? ";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_All_Friend_Sql);
			ptmt.setString(1, userID);
			ptmt.setString(2, friendID);
			ResultSet rs=ptmt.executeQuery();
			while(rs.next()) {
				Message=Message+rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4)+" ";//获取查询结果的第一列数据
			}
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return Message;
	}
	
	public static String seekTempMessage(String userID,String friendID) {
		String Message="";
		Connection conn=getConnection();	
		String seek_All_Friend_Sql="exec SeekOfflineMessage ?,? ";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_All_Friend_Sql);
			ptmt.setString(1, userID);
			ptmt.setString(2, friendID);
			ResultSet rs=ptmt.executeQuery();
			while(rs.next()) {
				Message=Message+rs.getString(1)+rs.getString(2)+rs.getString(3)+rs.getString(4)+" ";//获取查询结果的第一列数据
			}
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return Message;
	}
	
	public static String seekWebname(String userName) {
		String webname="";
		Connection conn=getConnection();	
		String seek_Webname_Sql="select webname from [user] where username = ?";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_Webname_Sql);
			ptmt.setString(1,userName);
			ResultSet rs=ptmt.executeQuery();
			while(rs.next()) {
				webname=rs.getString(1);//获取查询结果的第一列数据
			}		
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return webname;
	}
	
	
	public static boolean seekUser(String userName) {
		boolean seekSuccess=false;
		Connection conn=getConnection();	
		String seek_User_Sql="select * from [user] where username=?";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_User_Sql);
			ptmt.setString(1, userName);
			ResultSet rs=ptmt.executeQuery();
			seekSuccess=rs.next();
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return seekSuccess;
	}
	
	
	public static boolean seekUserRelation(String masterUser,String slaveUser,String relationType) {
		boolean friendRelation=false;
		Connection conn=getConnection();	
		String seek_User_Relation_Sql="select * from userrelation where masteruser=? and slaveuser=? and relationtype=?";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_User_Relation_Sql);
			ptmt.setString(1, masterUser);
			ptmt.setString(2, slaveUser);
			ptmt.setString(3, relationType);
			ResultSet rs=ptmt.executeQuery();
			friendRelation=rs.next();
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return friendRelation;
	}
	
	public static boolean seekEmail(String userName) {
		boolean seekSuccess=false;
		Connection conn=getConnection();
		String seek_Email_Sql="select * from [user] where username = ? and email is not null ";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_Email_Sql);
			ptmt.setString(1, userName);
			ResultSet rs=ptmt.executeQuery();
			seekSuccess=rs.next();
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return seekSuccess;
	}
	
	public static void seekPassword(String userName) {
		Connection conn=getConnection();
		String seek_Password_Sql="exec SendPasswordEmail ? ";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(seek_Password_Sql);
			ptmt.setString(1, userName);
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void updateUser(String userName,String webName,String passWord,String email) {
		
		Connection conn=getConnection();	
		String update_User_Sql="update [user] set webname=? ,[password]=? ,email=? where username=?" ;
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(update_User_Sql);
			ptmt.setString(1, webName);
			ptmt.setString(2, passWord);
			ptmt.setString(3, email);
			ptmt.setString(4, userName);
			ptmt.executeUpdate();//执行插入或更新操作，返回一个int，插入多少条数据返回多少条
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	
	
	public static void insertIntoUserRelation(String masterUser,String slaveUseer,String relationType) {
		Connection conn=getConnection();	
		String insert_Into_UserRelation_Sql="insert into userrelation(masteruser,slaveuser,relationtype) values(?,?,?)";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(insert_Into_UserRelation_Sql);
			ptmt.setString(1, masterUser);
			ptmt.setString(2, slaveUseer);
			ptmt.setString(3, relationType);
			ptmt.executeUpdate();//返回一个int，插入多少条数据返回多少条
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void deleteTempMessage(String userID,String friendID) {
		
		Connection conn=getConnection();	
		String delete_Temp_Message_Sql="delete from tempmessage where sender=? and receiver=? " ;
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(delete_Temp_Message_Sql);
			ptmt.setString(1, friendID);
			ptmt.setString(2, userID);
			ptmt.executeUpdate();
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void deleteFromUserRelation(String masterUser,String slaveUseer) {
		Connection conn=getConnection();	
		String delete_From_UserRelation_Sql="delete from userrelation where masteruser=? and slaveuser=? ";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(delete_From_UserRelation_Sql);
			ptmt.setString(1, masterUser);
			ptmt.setString(2, slaveUseer);
			ptmt.executeUpdate();
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void saveMessage(String sender,String receiver,String content,String sendtime) {
		Connection conn=getConnection();	
		String insert_Into_Message_Sql="insert into chatmessage values(?,?,?,?)";//sqlserver的timestamp只能系统添加
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(insert_Into_Message_Sql);
			ptmt.setString(1, sender);
			ptmt.setString(2, receiver);
			ptmt.setString(3, content);
			ptmt.setString(4, sendtime);
			//ptmt.setTimestamp(4,new java.sql.Timestamp(date.getTime()));//sqlserver的timestamp类型是二进制数据，datetime才等价于平时的timestamp时间类型
			ptmt.executeUpdate();//返回一个int，插入多少条数据返回多少条
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void savetempMessage(String sender,String receiver,String content,String sendtime) {
		Connection conn=getConnection();	
		String insert_Into_Temp_Message_Sql="insert into tempmessage values(?,?,?,?)";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(insert_Into_Temp_Message_Sql);
			ptmt.setString(1, sender);
			ptmt.setString(2, receiver);
			ptmt.setString(3, content);
			ptmt.setString(4, sendtime);
			ptmt.executeUpdate();
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static void tempToMessage(String sender,String receiver) {
		Connection conn=getConnection();	
		String Temp_To_Message_Sql="exec tempTomessage ?,?";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(Temp_To_Message_Sql);
			ptmt.setString(1, sender);
			ptmt.setString(2, receiver);
			ptmt.executeUpdate();
			closeDB(conn, ptmt);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
	}
	
	public static boolean loginValidate(String userName,String password) {
		boolean loginSuccess=false;
		Connection conn=getConnection();
		
		String user_Login_Sql="select * from [user] where username=? and password=?";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(user_Login_Sql);
			ptmt.setString(1, userName);
			ptmt.setString(2, password);
			ResultSet rs=ptmt.executeQuery();
			loginSuccess=rs.next();
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return loginSuccess;
			
	}
	public static String Register() {
		String userName="";
		Connection conn=getConnection();	
		String Register_Sql="exec Register";
		PreparedStatement ptmt;
		try {
			ptmt=conn.prepareStatement(Register_Sql);
			ResultSet rs=ptmt.executeQuery();	
			while(rs.next()) {
				userName=rs.getString(1);//获取查询结果的第一列数据
			}
			closeDB(conn, ptmt, rs);
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		return userName;
	}
	
	public static void closeDB(Connection conn,PreparedStatement ptmt,ResultSet rs) {
		if(conn!=null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ptmt!=null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(rs!=null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public static void closeDB(Connection conn,PreparedStatement ptmt) {
		if(conn!=null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ptmt!=null) {
			try {
				conn.close();
			}catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
