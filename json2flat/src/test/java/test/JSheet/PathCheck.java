package test.JSheet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class PathCheck {
	public static void main(String[] args) throws IOException{
		InputStream is = Parse.class.getResourceAsStream("/test.json");
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		
		JsonElement x = new JsonParser().parse(reader);
		
		//Configuration conf123 = Configuration.builder().options(Option.AS_PATH_LIST).build();
		Configuration conf123 = Configuration.defaultConfiguration().addOptions(Option.AS_PATH_LIST).addOptions(Option.ALWAYS_RETURN_LIST);
				
		DocumentContext tt = JsonPath.using(conf123).parse(x.toString());
		
		/*List<String> pathList = JsonPath.using(conf123).parse(new File("/home/aptus/workspace/mvgitproj/Json2Flat/json2flat/src/test/resources/test.json"))
				.read("$['store']");*/
		
		List<String> pathList = tt.read("$..*");
		
		for(String o : pathList){
			System.out.println(o);
		}
	}

}
