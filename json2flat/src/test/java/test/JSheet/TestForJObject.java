package test.JSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import com.github.opendevl.OrderJson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class TestForJObject {
	public static void main(String args[]) throws IOException{
		OrderJson haveOrder = new OrderJson();
		
		String source = TSheet.class.getResource("/tmp.json").getPath();
		String jsonString = new String(Files.readAllBytes(Paths.get(source)));
		
		JsonElement test = new JsonParser().parse(jsonString);
		
		for(Map.Entry<String, JsonElement> entry : test.getAsJsonObject().entrySet()){
			System.out.println(entry.getKey()+" --> "+entry.getValue());
		}
		
		//now ordering the json according to need
		System.out.println("=================================================================\n"
				+ "Ordered JSON\n"
				+ "===========================================================================\n");
		
		JsonElement orderTest = haveOrder.orderJson(test);
		for(Map.Entry<String, JsonElement> entry : orderTest.getAsJsonObject().entrySet()){
			System.out.println(entry.getKey()+" --> "+entry.getValue());
		}
	}
}
