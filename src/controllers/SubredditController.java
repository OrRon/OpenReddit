package controllers;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;

import com.google.gson.Gson;

import models.Response;
import models.Story;
import models.SubReddit;


@Path("/subreddits")
public class SubredditController {
	@GET
	
	//return all the subReddits in a list on JSON
	@Produces("text/plain")
	    public String loadSubredditslist() {
			Gson gson = new Gson();
	        
			List<SubReddit> subReddits = SubReddit.getAllSubReddits();
			Response response = new Response(0,null,subReddits);
			return gson.toJson(response);
	}
	@POST
	
	//Create a new subReddit in the system
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain") 
		public String createSubreddit(
				@FormParam("title") String title,
				@FormParam("adminID") int adminID){
		
			// Initialize JSON PARSER
			Gson gson = new Gson();
			
			SubReddit newSubReddit = new SubReddit( title, adminID);
			SubReddit.addSubReddit(newSubReddit);
			
			Response response = new Response(0,null,newSubReddit);
			return gson.toJson(response);
		}
	@DELETE
	@Path("/{subRedditID}")
	@Produces("text/plain")
	    public String deleteSubreddit(@PathParam("subRedditID") int subRedditID) {
		List<Story> storiesToDelete = Story.getStoriesBySubRedditId(subRedditID);
		
		for(Story story: storiesToDelete){
			Story.removeStoryById(story.getStoryID());
		}
			
			SubReddit.removeSubRedditbyID(subRedditID);
			
			Response response = new Response(0,null,"subreddit deleted");
			return response.toJson();
			
		}
	@GET
	@Path("/{subRedditID}/stories")
	@Produces("text/plain")
	    public String getSubredditStories(@PathParam("subRedditID") int subRedditID) {
		List<Story> stories = Story.getStoriesBySubRedditId(subRedditID);
		
		
			
			
			Response response = new Response(0,null,stories);
			return response.toJson();
			
		}
	
	
}


