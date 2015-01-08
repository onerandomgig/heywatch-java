package com.heywatch.api;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import java.util.Map;

public class HeyWatchAPI {

	private static final String USER_AGENT = "HeyWatch/2.0.0 (Java)";
	private static final String HEYWATCH_DOMAIN = "heywatch.com";
	private static final String HEYWATCH_URL = "https://heywatch.com";

	protected String apiKey;
	protected String configContent;

  /**
   * Class constructor specifying the config content, api key.
   */
	public HeyWatchAPI(String configContent, String apiKey) {
		this.apiKey = apiKey;
		this.configContent = configContent;
	}

  	public HeyWatchObject submit() throws HeyWatchException {
  		return request("/api/v1/job", "POST", this.configContent).parseObject();
  	}

  	public static HeyWatchObject submit(String configContent, String apiKey) throws HeyWatchException {
  		return new HeyWatchAPI(configContent, apiKey).submit();
  	}

  	public static HeyWatchObject submit(String configContent) throws HeyWatchException {
  		Map<String, String> env = System.getenv();

  		String apiKey = env.get("HEYWATCH_API_KEY");

  		return new HeyWatchAPI(configContent, apiKey).submit();
  	}

	private Executor createExecutor() {
		return Executor.newInstance()
		        .auth(new HttpHost(HeyWatchAPI.HEYWATCH_DOMAIN), this.apiKey, "");
	}

	private HeyWatchResponse request(String resource, String method, String body) throws HeyWatchException {
		return createRequest(resource, method, body);
	}

	private HeyWatchResponse createRequest(String resource, String method, String body) throws HeyWatchException {
		String response = "";
		try {
			Executor executor = createExecutor();

			if(method.equals("POST")) {
				response = executor.execute(Request.Post(HeyWatchAPI.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("Content-Type", "text/plain")
						.addHeader("User-Agent", HeyWatchAPI.USER_AGENT)
						.bodyString(body, ContentType.DEFAULT_TEXT))
				        .returnContent().asString();
			} else if(method.equals("PUT")) {
				response = executor.execute(Request.Put(HeyWatchAPI.HEYWATCH_URL + "/" + resource)
						.addHeader("Accept", "application/json")
						.addHeader("Content-Type", "text/plain")
						.addHeader("User-Agent", HeyWatchAPI.USER_AGENT)
						.bodyString(body, ContentType.DEFAULT_TEXT))
				        .returnContent().asString();
			}

			String jsonString = response;
			return new HeyWatchResponse(jsonString);
		} catch(Exception e) {
			throw new HeyWatchException(e);
		}
	}
}
