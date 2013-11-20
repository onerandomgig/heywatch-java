package com.heywatch.api;

import org.json.JSONException;

public class HeyWatchResponse {
	public String response;
	public HeyWatchResponse(String jsonString) {
		this.response = jsonString;
	}
	
	public HeyWatchObjectList parseArray() {
		if(this.response.startsWith("[")) {
			try {
				return new HeyWatchObjectList(this.response);
			} catch (JSONException e) {
	            e.printStackTrace();
	            return new HeyWatchObjectList(); 
			}
		} else {
			return new HeyWatchObjectList(); 
		}
	}
	
	public HeyWatchObject parseObject() {
		if(this.response.startsWith("{")) {
			try {
				return new HeyWatchObject(this.response);
			} catch (JSONException e) {
	            e.printStackTrace();
	            return new HeyWatchObject(); 
			}
		} else {
			return new HeyWatchObject();
		}
	}
}