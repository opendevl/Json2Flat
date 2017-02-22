package test.JSheet.spi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonStreamParser;
import com.google.gson.stream.JsonReader;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import test.JSheet.Parse;

public class TestSPI {
	public static void main(String[] args) throws UnsupportedEncodingException{
		InputStream is = Parse.class.getResourceAsStream("/test.json") ;  
		Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		/*JsonElement x = new JsonParser().parse(reader);
		
		Configuration conf = Configuration.defaultConfiguration()
											.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
											.addOptions(Option.SUPPRESS_EXCEPTIONS);
		Configuration pathConf = Configuration.defaultConfiguration()
												.addOptions(Option.AS_PATH_LIST)
												.addOptions(Option.ALWAYS_RETURN_LIST);
		DocumentContext tt = JsonPath.using(conf).parse(x.toString());
		DocumentContext tt_path = JsonPath.using(pathConf).parse(x.toString());
		
		List<String> pathList = tt_path.read("$..*");*/
		
		/*for(String o : pathList){
			System.out.println(o);
		}*/
		
		
		 //JsonStreamParser parser = new JsonStreamParser("['first', '2nd'] {'second':10} 'third'");
		JsonStreamParser parser = new JsonStreamParser(reader);
		 JsonElement element;
		 /*synchronized (parser) {  // synchronize on an object shared by threads
		   if (parser.hasNext()) {
		     element = parser.next();
		     System.out.println(element.toString());
		   }
		 }*/
		 
		 JsonReader test = new JsonReader(reader);
		 try {
			test.beginObject();
			//test.beginArray();
			while(test.hasNext()){
				
				//System.out.println(test.peek().toString());
				try{
				String name = test.nextName();
				System.out.println(name);
				}catch(Exception ex){
					ex.printStackTrace();
					//test.
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
