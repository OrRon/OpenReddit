package redis;



import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Protocol;


/*
 * RedisManager is the only class that have access to redis.
 * It contains methods to get/insert/remove information about favorite tsimer of a user.
 */
public class RedisManager {
	private static RedisManager _instance = null;
	private Jedis _jedis = null;
	private static final String StoryIdPrefix = "storyId:";
	private static final String UserIdPrefix = "userId:";
	
	/*
	 * Getter for the redis manager instance (singleton)
	 */
	public static RedisManager getInstance() throws URISyntaxException {
		
		if (_instance == null) {
			_instance = new RedisManager();
		}
		
		return _instance;
	}
	
	/*
	 * Initialize Redis (private constructor - singleton)
	 */
	private RedisManager() throws URISyntaxException {
		
        
			
		_jedis = new Jedis("localhost");
		
	}
	
	private void parseUrlFromEnvVarsAndConnect(String jsonEnvVars) throws URISyntaxException {
		String url = "";
		try {
			URI redisUri = new URI(jsonEnvVars);
			String host 	=  redisUri.getHost();
			int port 	=  redisUri.getPort();
			int timeout = Protocol.DEFAULT_TIMEOUT;
			String name = redisUri.getUserInfo().split(":", 2)[0];
			String password = redisUri.getUserInfo().split(":", 2)[1];
			
			JedisShardInfo shardInfo = new JedisShardInfo(host, port, timeout, name);
			_jedis = new Jedis(shardInfo);
			_jedis.auth(password);
		} 
		catch (URISyntaxException ex) {
			System.err.println("Conn.connect: " + ex.getMessage());
			throw ex;
		}
	}
	
	
	public Set<String> getInvertedIndexForStory(String keyword) {
		
		
		return _jedis.smembers(keyword);
		
	}
	public void addInvertedIndexForStory(String keyword, String json) {
		
		
		_jedis.sadd(keyword, json);
		
	}
	
	
	
	
}
