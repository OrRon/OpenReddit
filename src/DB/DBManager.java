package DB;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
	public static void createTables() throws SQLException {
		/*
		 * Generating tables
		 */
		Connection conn = DBConn.getConnection();
		System.out.println("Generating tables");
		try (Statement st = conn.createStatement()) {
						
			String SQL;
			SQL = "CREATE  TABLE IF NOT EXISTS `Stories` " +
					"( `storyID` INT NOT NULL AUTO_INCREMENT , " +
					"`title` VARCHAR(255) NOT NULL , " +
					"`text` TEXT NOT NULL , " +
					"`userID` INT , " +
					"`votes` INT ," + 
					"`subredditID` INT , " +
					"PRIMARY KEY (`storyID`)) ENGINE = InnoDB  CHARACTER SET utf8 COLLATE utf8_bin;";
			st.executeUpdate(SQL);

			SQL = "CREATE  TABLE IF NOT EXISTS `users` " +
					"( `userID` INT NOT NULL AUTO_INCREMENT , " +
					"`email` VARCHAR(255) NOT NULL , " +
					"`username` VARCHAR(255) NOT NULL , " +
					"`password` VARCHAR(255) NOT NULL , " +
					"PRIMARY KEY (`userID`)) ENGINE = InnoDB  CHARACTER SET utf8 COLLATE utf8_bin;";
			st.executeUpdate(SQL);
			
			SQL = "CREATE  TABLE IF NOT EXISTS `subreddits` " +
					"( `subredditID` INT NOT NULL AUTO_INCREMENT , " +
					"`name` VARCHAR(255) NOT NULL , " +
					"`adminID` INT NOT NULL , " +
					"PRIMARY KEY (`subredditID`)) ENGINE = InnoDB  CHARACTER SET utf8 COLLATE utf8_bin;";
			st.executeUpdate(SQL);
			
			System.out.println(SQL);
			System.out.println("Cool");
		} catch (SQLException ex) {
			System.err.println(ex.getMessage());
			throw ex;
		}
		
	}
}
