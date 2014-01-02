package DB;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;



/* This class is responsible to connect to the database 
 * and every other class which use the DB should get an handle
 * to the connection using this class.
 * IT HAS ONLY STATIC MEMBERS 
 */
public class DBConn {
	
	static Connection conn;
	
	// User name and pwd only for local testing
	private static final String USER_NAME	= "root";
	private static final String PASSWORD	= "";
	   
	// Static initialization block
	static
	{
		openConnection();
	}
	
	private static void openConnection() {
		try
	    {
	        System.out.println("Loading the driver...");
	        Class.forName("com.mysql.jdbc.Driver");
	        System.out.println("Loading successed ---------------");
	        
	        String jsonEnvVars = java.lang.System.getenv("VCAP_SERVICES");
	        if (jsonEnvVars != null) {
	        	// Runs on cloud
	        	
				
			} else {
				// Runs locally - only for maintenance
				
				// Create database if non exists
				Connection tempConn = null;
				System.out.println("Creating database `reddit` if non exists");
				try {
					String url = "jdbc:mysql://localhost/";
					tempConn = DriverManager.getConnection(url, USER_NAME, PASSWORD);
					
					Statement stmt = tempConn.createStatement();
					String sql = "CREATE DATABASE IF NOT EXISTS reddit;";
					stmt.executeUpdate(sql);
					System.out.println("Creating database successed");
				} finally {
					if (tempConn != null) {
						tempConn.close();
					}
				}
				
				String url = "jdbc:mysql://localhost/reddit?useUnicode=yes&characterEncoding=UTF-8";
				System.out.println("Connected local host url=" + url);
				conn= DriverManager.getConnection(url, USER_NAME, PASSWORD);
				
				try (Statement statement = conn.createStatement()) {
					String sql = "SET character_set_client = utf8";
					sql = "SET character_set_results = utf8";
					sql = "SET character_set_connection = utf8";
					
					statement.executeUpdate(sql);
				} catch (SQLException ex) {
					System.err.println(ex.getMessage());
					throw ex;
				}
			}
	
	        System.out.println((new StringBuilder("conn successed. conn=")).append(conn).toString());
	    }
	    catch(ClassNotFoundException ex)
	    {
	        System.err.println((new StringBuilder("error loading:")).append(ex.getMessage()).toString());
	    }
	    catch(SQLException ex)
	    {
	        System.err.println((new StringBuilder("error loading:")).append(ex.getMessage()).toString());
	    }
	}
	
	/*
	 * Parse the json received from cloud services 
	 */
    
    
    /*
     * Gets connection (trys opening if closed)
     */
    public static Connection getConnection() {
    	try {
			if (conn == null || conn.isClosed() || !conn.isValid(5)) {
				openConnection();
			}
		} catch (SQLException e) {
			return null;
		}
    	
    	return conn;
    }

}
