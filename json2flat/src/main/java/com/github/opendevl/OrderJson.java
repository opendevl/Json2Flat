package com.github.opendevl;

import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

public class OrderJson {
	
	Type type = new TypeToken<Map<String, Object>>(){}.getType();
	
	Map<String, Object> origMap = null;
	
	Map<String, Object> jsonPre = null;
	Map<String, Object> jsonArr = null;
	Map<String, Object> jsonObj = null;
	
	Gson jsonToFro = null;
	
	public OrderJson(){
		
		jsonToFro = new Gson();
	}
	
	public JsonElement orderJson(JsonElement ele){
		
		origMap  = new LinkedHashMap<String, Object>();
		
		jsonPre = new LinkedHashMap<String, Object>();
		jsonArr = new LinkedHashMap<String, Object>();
		jsonObj = new LinkedHashMap<String, Object>();
		
		//converting JsonElement to Map
		origMap = jsonToFro.fromJson(ele, type);
		
		//Iterating the Map object to to get type of Object
		for(Map.Entry<String, Object> entry : origMap.entrySet()){
			
			try{
				//adding check if value of key in json is null
				if(entry.getValue() == null || 
						entry.getValue().getClass().getSimpleName().equals("ArrayList")){
					
					//if Object is of type ArrayList push it to jsonArr Map
					jsonArr.put(entry.getKey(), entry.getValue());
					
				}else{
					
					//if Object is of type Premitive push it to jsonPre.
					jsonPre.put(entry.getKey(), entry.getValue());
				}
			}catch(Exception ex){
				ex.printStackTrace();
			}
			//Yet to descide about if type is of JsonObject
			
		}
		
		/* Keeping Order - 
		 * 		1) JSON premitive
		 * 		2) JSON Array
		 * 		3) JSON Object ( order of JSON Object is yet to be descided)
		 * */
		
		//appending jsonArr map to jsonPre map in order to mantain order.
		jsonPre.putAll(jsonArr);
		
		//reconstructing the JSON from Map Objects and returning
		
		return jsonToFro.toJsonTree(jsonPre, LinkedHashMap.class);
		
	}
	
	
}
