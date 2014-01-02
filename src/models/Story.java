package models;

import DB.DBConn;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.PreparedStatement;

import com.mysql.jdbc.ResultSetMetaData;


public class Story {
	/* 
	 * Story class will represent a single story in our system. 
	 * it will hold all the information about the story the user who wrote it and
	 * the subreddit
	 */
	int _storyID;
	String _title;
	String _text;
	int _userID;
	int _subredditID;
	int _votes;
	String _categoryName;
	String _subredditName;
	String _userName;
	
	/*
	 * INIT METHODS
	 */
	public Story()
	{
		
	}
	public Story( int storyID,
			String title,
			String text,
			int userID,
			int subreddit,
			int votes) {
		
		
		_storyID = storyID;
		_title = title;
		_userID = userID;
		_text = text;
		_subredditID = subreddit;
		_votes = votes;
		

		
	}
	public static boolean hasColumn(ResultSet rs, String columnName) throws SQLException {
	    ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
	    int columns = rsmd.getColumnCount();
	    for (int x = 1; x <= columns; x++) {
	        if (columnName.equals(rsmd.getColumnName(x))) {
	            return true;
	        }
	    }
	    return false;
	}
	public void setNames(String subredditName, String userName) {
		this._userName = userName;
		this._subredditName = subredditName;
	}
	public int getStoryID(){
		return _storyID;
	}
	public String getTitle() {
		return _title;
	}
	public String getText() {
		return _text;
	}
	public static List<Story> getStoryList()
	{
		Connection conn = DBConn.getConnection();
		String sql = "SELECT * FROM  `Stories` NATURAL JOIN `subreddits` NATURAL JOIN `users` ORDER BY votes desc;";
		
		List<Story> stories = null;
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			
			stories = extractStoriesFromResultSet(rs);
			
			
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return stories;
	}
	public static List<Story> getStoryListByDate()
	{
		Connection conn = DBConn.getConnection();
		String sql = "SELECT * FROM  `Stories` NATURAL JOIN `subreddits` NATURAL JOIN `users` ORDER BY storyID desc;";
		
		List<Story> stories = null;
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			
			stories = extractStoriesFromResultSet(rs);
			
			
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return stories;
	}
	
	public static Story getStoryById(int storyID){
		Story story = null;
		Connection conn = DBConn.getConnection();
		String sql = "SELECT * FROM `Stories` WHERE storyID = " + storyID + ";";
		
		List<Story> stories = null;
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(sql);
			
			stories = extractStoriesFromResultSet(rs);
			if (stories != null && stories.size() == 1) {
				story = stories.get(0);
			}
			
			
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return story;
	}
	public static List<Story> extractStoriesFromResultSet(ResultSet resultSet) {
		List<Story> list = new ArrayList<Story>();
		try {
				
			while (resultSet.next()) {
	            int storyID = resultSet.getInt("storyID");
	            String title = resultSet.getString("title");
	            String text = resultSet.getString("text");
	            int userID = resultSet.getInt("userID");
	            int subreddit = resultSet.getInt("subredditID");
	            int votes = resultSet.getInt("votes");
	            
	            
	            Story story = new Story(storyID,title,text, userID,subreddit,votes);
	            
	           if (hasColumn(resultSet, "name") && hasColumn(resultSet, "username")) {
	            story.setNames(resultSet.getString("name"), resultSet.getString("username"));
	           }
	           
	            
	            list.add(story);
	        }
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return list;
	}
	public static List<Story> getStoriesWithCategoriesByUserId(int userId) {
		Connection conn = DBConn.getConnection();
		List<Story> stories = new ArrayList<Story>();
		String command = "SELECT s.subredditID,storyID,title,text,userID,name from Stories S natural join  subreddits SU WHERE userID = " + userId + ";";
		System.out.println(command);
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(command);
			
			stories = extractStoriesFromResultSet(rs);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return stories;
	}	
	public static List<Story> getStoriesByUserId(int userId) {
		Connection conn = DBConn.getConnection();
		List<Story> stories = new ArrayList<Story>();
		String command = "SELECT * FROM `Stories` WHERE userID = " + userId + ";";
		System.out.println(command);
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(command);
			
			stories = extractStoriesFromResultSet(rs);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return stories;
	}	
	
	public static List<Story> getStoriesBySubRedditId(int subredditID) {
		Connection conn = DBConn.getConnection();
		List<Story> stories = new ArrayList<Story>();
		String command = "SELECT * FROM `Stories` WHERE subredditID = " + subredditID + " ORDER BY votes desc;";
		System.out.println(command);
		try (Statement st = conn.createStatement()) {
			ResultSet rs = st.executeQuery(command);
			
			stories = extractStoriesFromResultSet(rs);
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		
		return stories;
	}	
	
	public static Story insertStory(
			String title,
			String text,
			int userID,
			int subredditID)
	{
		Story newStory = null;
		Connection conn = DBConn.getConnection();
		String insertSql = "INSERT INTO `Stories` (`title`,`text`, `userID`, `subredditID`,`votes`) VALUES(?,?,?,?,?)";
		try (PreparedStatement pst = conn.prepareStatement(insertSql,Statement.RETURN_GENERATED_KEYS)) {
			pst.clearParameters();
			pst.setString(1,title);
			pst.setString(2,text);
			pst.setInt(3,  userID);
			pst.setInt(4,subredditID);
			pst.setInt(5,0);
			
			pst.executeUpdate();
			ResultSet rs = pst.getGeneratedKeys();
			if (rs != null && rs.next()) {
			   newStory = new Story(rs.getInt(1),title,text, userID,subredditID,0);
			    
			}
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
		}
		return newStory;
	}
	
	public static void removeStoryById(int ID) {
		try {
			Connection conn = DBConn.getConnection();
			Statement statement = conn.createStatement();
			String command = "DELETE FROM `Stories` WHERE storyID = " + ID + "";
			int delete = statement.executeUpdate(command);
			
			if (delete != 1) {
				throw new SQLException("Less or more then one row deleted");
			}
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static void upVoteStoryById(int storyID) {
		try {
			Connection conn = DBConn.getConnection();
			Statement statement = conn.createStatement();
			String command = "UPDATE `Stories` SET votes = votes + 1 where storyID = " + storyID + "";
			statement.executeUpdate(command);
			
			
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	public static void downVoteStoryById(int storyID) {
		try {
			Connection conn = DBConn.getConnection();
			Statement statement = conn.createStatement();
			String command = "UPDATE `Stories` SET votes = votes - 1 where storyID = " + storyID + "";
			statement.executeUpdate(command);
			
			
		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}
	
}
