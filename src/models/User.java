package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import DB.DBConn;


public class User {
	/* 
	 * Story class will represent a single story in our system. 
	 * it will hold all the information about the user
	 * */
	
	int _userID;
	String _email;
	String _username;
	String _password;
	
	/*
	 * INIT METHODS
	 */
	public User(){}
	
	public User(String email, String username, String password){
		
		_email = email;
		_username = username;
		_password = password;	
	}
	
	public int getID(){
		return _userID;
	}
	
	public void setId(int userID){
		_userID = userID;
	}
	
	public String getEmail(){
		return _email;
	}
	
	public void setEmail(String email){
		_email = email;
	}
	
	public String getUsername(){
		return _username;
	}
	
	public void setUserName(String username){
		_username = username;
	}
	
	public String getPassword(){
		return _password;
	}
	
	public void setPassword(String password){
		_password = password;
	}
	
	public static User extractUserFromResultSet(ResultSet rs) throws SQLException{
		User user = new User();
		
		user.setId(rs.getInt("userID"));
		user.setEmail(rs.getString("email"));
		user.setUserName(rs.getString("username"));
		user.setPassword(rs.getString("password"));
		return user;
	}

	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		Connection conn = DBConn.getConnection();
		String command = "SELECT * FROM `users`";
		
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				User user = extractUserFromResultSet(rs);
				users.add(user);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}

		return users;
	}

	public static User getUserByID(int userID) {
		Connection conn = DBConn.getConnection();
		String command = "SELECT * FROM `users` WHERE userID = " + userID + "";
		
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				return extractUserFromResultSet(rs);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}
	public static User getUserByUsernameAndPassword(String username, String password) {
		Connection conn = DBConn.getConnection();
		String command = "SELECT * FROM  `users` WHERE username = '" + username + "' AND password = '"+ password + "'";
		System.out.println(command);
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				return extractUserFromResultSet(rs);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}

	public static User getUserByUsername(String username) {
		Connection conn = DBConn.getConnection();
		String command = "SELECT * FROM  `users` WHERE username = '" + username + "'";
		
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				return extractUserFromResultSet(rs);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}

	public static void insertUser(User user) {
		Connection conn = DBConn.getConnection();
		String command = "INSERT INTO `users`"
					   + " (`email`, `username`, `password`) "
					   + "VALUES(?,?,?)";
		
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(command);
			
			preparedStatement.setString(1, user.getEmail());
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.executeUpdate();
			
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public static void removeUserById(int userID) {
		try {
			Connection conn = DBConn.getConnection();
			Statement statement = conn.createStatement();
			String command = "DELETE FROM `users` WHERE userID = " + userID + "";
			int delete = statement.executeUpdate(command);
			
			if (delete != 1) {
				throw new SQLException("Less or more then one row deleted");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
}
