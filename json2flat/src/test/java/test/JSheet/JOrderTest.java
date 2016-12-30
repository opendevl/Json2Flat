package test.JSheet;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

public class JOrderTest {
	
	public static void main(String args[]) throws IOException{
		String source = TSheet.class.getResource("/tmp.json").getPath();
		String jsonString = new String(Files.readAllBytes(Paths.get(source)));
		Type type = new TypeToken<Map<String, Object>>(){}.getType();
		
		Gson gson = new Gson();
		Map<String, Object> myMap = gson.fromJson(jsonString, type);
		
		Map<String, Object> tmpPre = new LinkedTreeMap<String, Object>();
		Map<String, Object> tmpArr = new LinkedTreeMap<String, Object>();
		Map<String, Object> tmpObj = new LinkedTreeMap<String, Object>();
		
		for(Map.Entry<String, Object> xyz : myMap.entrySet()){
			System.out.println(xyz.getKey()+ " --> " + xyz.getValue() + "\n");
			System.out.println(xyz.getValue().getClass().getSimpleName());
			if(xyz.getValue().getClass().getSimpleName().equals("ArrayList")){
				tmpArr.put(xyz.getKey(), xyz.getValue());
			}else{
				tmpPre.put(xyz.getKey(), xyz.getValue());
			}
			
		}
		tmpPre.putAll(tmpArr);
		
		JsonObject xxx = gson.toJsonTree(tmpPre, LinkedTreeMap.class).getAsJsonObject();
		
		JsonObject adf = new JsonParser().parse(jsonString).getAsJsonObject();
		System.out.println("======================================================");
		for(Map.Entry<String, JsonElement> xyz : adf.entrySet()){
			System.out.println(xyz.getKey()+"-->"+xyz.getValue());
		}
		
		
		System.out.println("****************************************************");
		for(Map.Entry<String, JsonElement> xyz : xxx.entrySet()){
			System.out.println(xyz.getKey()+"-->"+xyz.getValue());
		}
	}
}