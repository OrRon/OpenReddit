package models;

import com.google.gson.Gson;

public class Response {
	int _error;
	String _errorMsg;
	Object _results;
	
	
	
	public Response(int error, String errorMsg, Object result)
	{
		_error = error;
		_errorMsg = errorMsg;
		_results = result;
		
	}
	public String toJson ()
	{
		Gson gson = new Gson();
		return gson.toJson(this);
	}
}


