package controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;
import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;

import models.Story;
import models.User;
import models.Response;


@Path("/users")
public class UsersController {
	@GET
	
	//return all the users in a list on JSON
	@Produces("text/plain")
	    public String loadUserslist(@Context HttpServletRequest req) {
			
			HttpSession session= req.getSession(true);
			Object loggedInUser = session.getAttribute("username");
			if (loggedInUser!=null) {
	    		System.out.println(loggedInUser.toString());
	    		List<User> users = User.getAllUsers();
				Response response = new Response(0, null, users);
		        return response.toJson();
	    	}
			
			Response response = new Response(1, "not logged in", null);
	        return response.toJson();
	}
	@POST
	
	//Create a new user in the system
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain") 
		public String createUser(
				@FormParam("email") String email, 
				@FormParam("username") String username,
				@FormParam("password") String password){
		
			
			
			
			User newUser = new User(email, username, password);
			User.insertUser(newUser);
			
			Response response = new Response(0, null, newUser);
			return response.toJson();
		}
	@DELETE
	@Path("/{userID}")
	@Produces("text/plain")
	    public String deleteUser(@PathParam("userID") int userID) {
			List<Story> storiesToDelete = Story.getStoriesByUserId(userID);
			
			for(Story story: storiesToDelete){
				Story.removeStoryById(story.getStoryID());
			}
			
			User.removeUserById(userID);
			Response response = new Response(0, null, "User Deleted");
			return response.toJson();
			
		}
	@GET
	@Path("/{userID}")
	
    public String getUser(@PathParam("userID") int userID) {
		
		
		User user = User.getUserByID(userID);
		Response response = new Response(0, null, user);
		return response.toJson();	
	}
	@GET
	@Path("/{userID}/stories")
	public String getUserStories(@PathParam("userID") int userID) {
		
		List<Story> storiesWithCat = Story.getStoriesWithCategoriesByUserId(userID);
		Response response = new Response(0, null, storiesWithCat);
		
		return response.toJson();	
	}
	
	@GET
	@Path("/login")
	public String login(@QueryParam("user") String username,@QueryParam("password") String password,@Context HttpServletRequest req) {
		
		User user = User.getUserByUsernameAndPassword(username,password);
		if (user !=null) {
			HttpSession session= req.getSession(true);		
	    	session.setAttribute("userID", Integer.toString(user.getID()));
	    	session.setAttribute("username", user.getUsername());
	    	Response response = new Response(0, null, user);
			return response.toJson();	
		} 
		
    	
    	
		Response response = new Response(1, "Wrong login information", user);
		return response.toJson();	
	}
	@GET
	@Path("/logout")
	public String logout(@Context HttpServletRequest req) {
		
		
		HttpSession session= req.getSession(true);		
    	session.removeAttribute("userID");
    	session.removeAttribute("username");
	    		
		Response response = new Response(0, "loggedout", null);
		return response.toJson();	
	}
	
}
