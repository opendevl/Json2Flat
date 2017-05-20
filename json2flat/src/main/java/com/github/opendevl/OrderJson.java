package com.github.opendevl;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

public class OrderJson {
	
	Type type = new TypeToken<Map<String, Object>>(){}.getType();
	
	Map<String, JsonElement> jsonPre = null;
	Map<String, JsonElement> jsonArr = null;
	Map<String, JsonElement> jsonObj = null;

	Gson jsonToFro = null;
	
	public OrderJson(){
		jsonToFro =  new Gson();
	}
	
	public JsonElement orderJson(JsonElement ele){
		
		jsonPre = new LinkedHashMap<String, JsonElement>();
		jsonArr = new LinkedHashMap<String, JsonElement>();
		jsonObj = new LinkedHashMap<String, JsonElement>();

		JsonObject jsonObject = ele.getAsJsonObject();
		for(Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
			try{
				//adding check if value of key in json is null
				if(entry.getValue() == null ||
						entry.getValue().getClass().equals(JsonArray.class)){

					//if Object is of type ArrayList push it to jsonArr Map
					jsonArr.put(entry.getKey(), entry.getValue());

				}else{

					//if Object is of type Premitive push it to jsonPre.
					jsonPre.put(entry.getKey(), entry.getValue());
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			//Yet to decide about if type is of JsonObject
		}

		
		/* Keeping Order - 
		 * 		1) JSON premitive
		 * 		2) JSON Array
		 * 		3) JSON Object ( order of JSON Object is yet to be decided)
		 * */
		
		//appending jsonArr map to jsonPre map in order to maintain order.
		jsonPre.putAll(jsonArr);
		
		//reconstructing the JSON from Map Objects and returning
		
		return jsonToFro.toJsonTree(jsonPre, LinkedHashMap.class);

	}
	
}
