package controllers;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.POST;

import javax.ws.rs.PathParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.core.Context;

import redis.RedisManager;



import com.google.gson.Gson;

import models.Response;
import models.Story;


@Path("/stories")
public class StoriesController {
	@GET
	
	//return all the stories in a list on JSON
	@Produces("text/plain")
	    public String loadStorylist() {
			
	        // Return some textual content
			List<Story> stories = Story.getStoryList();
			Response response = new Response(0, null, stories);
	        return response.toJson();
			
	}
	@POST
	
	//Create a new story in the system
	@Consumes("application/x-www-form-urlencoded")
	@Produces("text/plain") 
		public String createStory(
				@FormParam("title") String title, 
				@FormParam("text") String text,
				@FormParam("subredditID") int subredditID,
				@Context HttpServletRequest req) throws URISyntaxException{
			RedisManager redis = RedisManager.getInstance();
			
			Gson gson = new Gson();
			HttpSession session= req.getSession(true);
			Object loggedInUser = session.getAttribute("userID");
			if (loggedInUser!=null) {
				//create a new story
				Story newStory = Story.insertStory(title, text, Integer.parseInt(loggedInUser.toString()), subredditID);
				
				//save in reddis
				String[] keywords = title.split(" ");
				for (int i = 0; i< keywords.length;i++)
				{
					redis.addInvertedIndexForStory(keywords[i],gson.toJson(newStory));
				}
				Response response = new Response(0, null, newStory);
				return response.toJson();
			}
			
			Response response = new Response(1, "not Logged in", null);
			return response.toJson();
			
		}
	@DELETE
	@Path("/{storyID}")
	//return all the stories in a list on JSON
	@Produces("text/plain")
	    public String deleteStory(@PathParam("storyID") int storyID) {
			/*
			 * DELETE STORY
			 */
			Story.removeStoryById(storyID);
			Response response = new Response(0, null, "Story Deleted");
			return response.toJson();
			
		}
	@GET
	@Path("/{storyID}")
	
    public String getStory(@PathParam("storyID") int storyID) {
		
        // Return some textual content
		Story newStory = Story.getStoryById(storyID);
		Response response = new Response(0, null, newStory);
		return response.toJson();
	}
	@GET
	@Path("/search/{keyword}")
	
    public String searchStory(@PathParam("keyword") String keyword) throws URISyntaxException {
		
		RedisManager redis = RedisManager.getInstance();
		Set<String> set = redis.getInvertedIndexForStory(keyword);
		Response response = new Response(0, null, set);
		return response.toJson();
	}
	@GET
	@Path("/new")
	
    public String newStories()  {
		
		// Return some textual content
		List<Story> stories = Story.getStoryListByDate();
		Response response = new Response(0, null, stories);
		return response.toJson();
	}
	@PUT
	@Path("/{storyID}/up")
	
    public String upVote(@PathParam("storyID") int storyID) throws URISyntaxException {
		
		Story.upVoteStoryById(storyID);
		Response response = new Response(0, null, "vote up the story");
		return response.toJson();
	}
	@PUT
	@Path("/{storyID}/down")
	
    public String upDown(@PathParam("storyID") int storyID) throws URISyntaxException {
		
		Story.downVoteStoryById(storyID);
		Response response = new Response(0, null, "vote down the story");
		return response.toJson();
	}
	
}
