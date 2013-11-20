package com.heywatch.api;

import org.json.JSONArray;

public class HeyWatchObjectList extends JSONArray {

	public HeyWatchObjectList(String response) {
		super(response);
	}

	public HeyWatchObjectList() {
		super();
	}

}
