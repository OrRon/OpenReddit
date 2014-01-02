package controllers;

import java.sql.SQLException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;


import DB.DBManager;

@Path("/config")
public class ConfigController {
	@GET
	//return all the stories in a list on JSON
	@Produces("text/plain")
	public String sayHello() throws SQLException {
        // Return some textual content
		try {
			DBManager.createTables();
		}
		catch (SQLException ex)
		{
			return "Error: " + ex.getMessage();
		}
		
        return "Worked!";
	}
}
