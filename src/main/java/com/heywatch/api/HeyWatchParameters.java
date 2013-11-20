package com.heywatch.api;

import java.util.Hashtable;
import java.util.Set;

import org.apache.http.client.fluent.Form;

public class HeyWatchParameters extends Hashtable<String, String> {

	private static final long serialVersionUID = 1L;
	
	public Form toBodyForm() {
		Form form = Form.form();

		Set<String> keys = this.keySet();
        for(String key: keys){
            form.add(key, this.get(key));
        }
		
		return form;
	}
}
