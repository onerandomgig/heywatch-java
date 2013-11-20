package com.heywatch.api;

import java.io.BufferedReader;
import java.io.FileReader;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

public class HeyWatchClient {
	
	private static final String USER_AGENT = "HeyWatch Java/0.1.0";
	private static final String HEYWATCH_DOMAIN = "heywatch.com";
	private static final String HEYWATCH_URL = "https://heywatch.com";

	protected String username, apiKey;
	
	/**
	 * Class constructor specifying your API key.
	 */
	public HeyWatchClient(String apiKey) {
		this.username = "HW-API-Key";
		this.apiKey = apiKey;
	}
	
	/**
	 * Class constructor specifying your user name and password.
	 */
	public HeyWatchClient(String username, String password) {
		this.username = username;
		this.apiKey = password;
	}
	
	/**
	 * Returns your account details 
	 *
	 * @return the account detail in HeyWatchObject
	 * @throws HeyWatchException 
	 */
	public HeyWatchObject account() throws HeyWatchException {
		return request("account").parseObject();
	}
	
	/**
	 * Returns an Object information by giving a resource name and an ID 
	 *
	 * @param resource the resource name: download, job, video, encoded_video, format, robot/job, hls/job
	 * @param id the object's id
	 * @return the object info in HeyWatchObject
	 * @throws HeyWatchException 
	 */
	public HeyWatchObject info(String resource, long id) throws HeyWatchException {
		return request(resource+"/"+id).parseObject();
	}
	
	/**
	 * Returns a list of Objects by giving a resource
	 *
	 * @param resource the resource name: download, job, video, encoded_video, format, robot/job
	 * @return the list of objects in HeyWatchObjectList
	 * @throws HeyWatchException 
	 */
	public HeyWatchObjectList all(String resource) throws HeyWatchException {
		return request(resource).parseArray();
	}
	
	/**
	 * Create an Object by giving a resource name and parameters
	 *
	 * @param resource the resource name: download, job, video, encoded_video, format, robot/job, hls/job, preview/thumbnails, preview/animation, preview/storyboard
	 * @param parameters an HeyWatchParameters list
	 * @return the object info in HeyWatchObject
	 * @throws HeyWatchException 
	 */
	public HeyWatchObject create(String resource, HeyWatchParameters params) throws HeyWatchException {
		return request(resource, "POST", params).parseObject();
	}
	
	/**
	 * Create an Object by giving a resource name and a body string
	 *
	 * @param resource the resource name: robot/job
	 * @param body the content of an INI file (Robot Instruction File)
	 * @return the object info in HeyWatchObject
	 * @throws HeyWatchException 
	 */
	public HeyWatchObject create(String resource, String body) throws HeyWatchException {
		return request(resource, "POST", body).parseObject();
	}
	
	/**
	 * Create a Robot Job by giving a path to the instruction file (INI)
	 *
	 * @param path to the INI file
	 * @return the Robot job info in HeyWatchObject
	 * @throws HeyWatchException 
	 */
	public HeyWatchObject createRobotJob(String iniFile) throws HeyWatchException {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(iniFile));
			String line, iniContent = "";
			while( ( line = reader.readLine() ) != null)
			{
				iniContent += line+"\n";
			}
			reader.close();
			return create("robot/job", iniContent);
		} catch (java.io.IOException e) {
			throw new HeyWatchException(e);
		}
	}
	
	/**
	 * Update an existing Object by giving a resource name, an ID and a list of parameters
	 *
	 * @param resource the resource name: format
	 * @param id the object's ID
	 * @param parameters an HeyWatchParameters list
	 * @return true if updated successfully or false if failed
	 */
	public boolean update(String resource, long id, HeyWatchParameters params) {
		try {
			request(resource+"/"+id, "PUT", params);
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	/**
	 * Delete an Object by giving a resource name and an ID
	 *
	 * @param resource the resource name: format, video, encoded_video
	 * @param id the object's ID
	 * @return true if deleted successfully or false if failed
	 */
	public boolean delete(String resource, long id) {
		try {
			request(resource+"/"+id, "DELETE");
			return true;
		} catch(Exception e) {
			return false;
		}
	}
	
	private Executor createExecutor() {
		return Executor.newInstance()
		        .auth(new HttpHost(HeyWatchClient.HEYWATCH_DOMAIN), this.username, this.apiKey);
	}
	
	private HeyWatchResponse request(String resource, String method, HeyWatchParameters params) throws HeyWatchException {
		String jsonString = createRequest(resource, method, params);
		return new HeyWatchResponse(jsonString);
	}
	
	private HeyWatchResponse request(String resource, String method, String body) throws HeyWatchException {
		String jsonString = createRequest(resource, method, body);
		return new HeyWatchResponse(jsonString);
	}
	
	private HeyWatchResponse request(String resource, String method) throws HeyWatchException {
		String jsonString = createRequest(resource, method, new HeyWatchParameters());
		return new HeyWatchResponse(jsonString);
	}
	
	private HeyWatchResponse request(String resource) throws HeyWatchException {
		String jsonString = createRequest(resource, "GET", new HeyWatchParameters());
		return new HeyWatchResponse(jsonString);
	}
	
	private String createRequest(String resource, String method, HeyWatchParameters params) throws HeyWatchException {
		String response = "";
		try {
			Executor executor = createExecutor();

			if(method.equals("GET")) {
				response = executor.execute(Request.Get(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT))
				        .returnContent().asString();
			} else if(method.equals("DELETE")) {
				response = executor.execute(Request.Delete(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT))
				        .returnContent().asString();
			} else if(method.equals("POST")) {
				response = executor.execute(Request.Post(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT)
						.bodyForm(params.toBodyForm().build()))
				        .returnContent().asString();
			} else if(method.equals("PUT")) {
				response = executor.execute(Request.Put(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT)
						.bodyForm(params.toBodyForm().build()))
				        .returnContent().asString();
			}

			return response;
		} catch(Exception e) {
			throw new HeyWatchException(e);
		}
	}
	
	private String createRequest(String resource, String method, String body) throws HeyWatchException {
		String response = "";
		try {
			Executor executor = createExecutor();

			if(method.equals("POST")) {
				response = executor.execute(Request.Post(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT)
						.bodyString(body, ContentType.DEFAULT_TEXT))
				        .returnContent().asString();
			} else if(method.equals("PUT")) {
				response = executor.execute(Request.Put(HeyWatchClient.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("User-Agent", HeyWatchClient.USER_AGENT)
						.bodyString(body, ContentType.DEFAULT_TEXT))
				        .returnContent().asString();
			}

			return response;
		} catch(Exception e) {
			throw new HeyWatchException(e);
		}
	}
}
