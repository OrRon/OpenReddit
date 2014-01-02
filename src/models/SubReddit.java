package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import DB.DBConn;

public class SubReddit {
	/* 
	 * Subreddit class will represent a single subreddit in our system. 
	 * it will hold all the information about the subreddit' it's admin 
	 * and stories
	 * */
	
	int _subredditID;
	String _name;
	int _adminID;
	
	public SubReddit(){}
	
	public SubReddit( String name, int adminID){
		
		_name = name;
		_adminID = adminID;
	}
	
	public int getId(){
		return _subredditID;
	}
	
	public void setId(int subredditID){
		_subredditID = subredditID;
	}
	
	public String getName(){
		return _name;		
	}
	
	public void setName(String name){
		_name = name;		
	}
	
	public int getAdminID(){
		return _adminID;
	}
	
	public void setAdminID(int adminID){
		_adminID = adminID;
	}
	
	public static SubReddit  extractSubRedditFromResultSet(ResultSet rs) throws SQLException{
		SubReddit subReddit = new SubReddit();
		
		subReddit.setId(rs.getInt("subredditID"));
		subReddit.setName(rs.getString("name"));
		subReddit.setAdminID(rs.getInt("adminID"));
		
		return subReddit;
	}
	
	public static List<SubReddit> getAllSubReddits() {
		Connection conn = DBConn.getConnection();
		List<SubReddit> subReddits = new ArrayList<SubReddit>();
		String command = "SELECT * FROM `subreddits`";
		
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				SubReddit subReddit = extractSubRedditFromResultSet(rs);
				subReddits.add(subReddit);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		
		return subReddits;
	}

	public static SubReddit getSubRedditById(int subredditID) {
		Connection conn = DBConn.getConnection();
		String command = "SELECT FROM `subreddits` WHERE subredditID = " + subredditID + ";";
		
		try {
			Statement statement = conn.createStatement();
			for (ResultSet rs = statement.executeQuery(command); rs.next();) {
				return extractSubRedditFromResultSet(rs);
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return null;
	}

	public static void addSubReddit(SubReddit i_Subreddit) {
		Connection conn = DBConn.getConnection();
		String command = "INSERT INTO `subreddits`"
				   + " (`name`, `adminID`) "
				   + "VALUES(?,?);";
	
		try {
			PreparedStatement preparedStatement = conn.prepareStatement(command);
			preparedStatement.setString(1, i_Subreddit.getName());
			preparedStatement.setLong(2, i_Subreddit.getAdminID());
			preparedStatement.executeUpdate();
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		
	}

	public static void removeSubRedditbyID(int subredditID) {
		try {
			Connection conn = DBConn.getConnection();
			Statement statement = conn.createStatement();
			String command = "DELETE FROM `subreddits` WHERE subredditID = " + subredditID + "";
			int delete = statement.executeUpdate(command);
			
			if (delete != 1) {
				throw new SQLException("Less or more then one row deleted");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
		
	}

}

