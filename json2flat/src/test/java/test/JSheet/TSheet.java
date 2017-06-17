package test.JSheet;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.opendevl.JFlat;

public class TSheet {

	public static void main(String[] args) throws Exception{
		
		String source = TSheet.class.getResource("/sample.json").getPath();
		
		String jsonString = new String(Files.readAllBytes(Paths.get(source)));
	
		//JFlat flatMe = new JFlat("https://raw.githubusercontent.com/kamalpradhan95/test_repo/master/store.json",fetchMode.URL);
		JFlat flatMe = new JFlat("[{\"uploadTimeStamp\":\"1488793033624\",\"PDID\":\"123\",\"data\":[{\"Data\":{\"unit\":\"rpm\",\"value\":\"100\"},\"EventID\":\"E1\",\"PDID\":\"123\",\"Timestamp\":1488793033624,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}},{\"Data\":{\"heading\":\"N\",\"loc1\":\"false\",\"loc2\":\"00.001\",\"loc3\":\"00.004\",\"loc4\":\"false\",\"speed\":\"10\"},\"EventID\":\"E2\",\"PDID\":\"123\",\"Timestamp\":1488793033624,\"Timezone\":330,\"Version\":\"1.1\",\"pii\":{}},{\"Data\":{\"xvalue\":\"1.1\",\"yvalue\":\"1.2\",\"zvalue\":\"2.2\"},\"EventID\":\"E3\",\"PDID\":\"123\",\"Timestamp\":1488793033624,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}},{\"EventID\":\"E4\",\"Data\":{\"value\":\"50\",\"unit\":\"percentage\"},\"Version\":\"1.0\",\"Timestamp\":1488793033624,\"PDID\":\"123\",\"Timezone\":330},{\"Data\":{\"unit\":\"kmph\",\"value\":\"70\"},\"EventID\":\"E5\",\"PDID\":\"123\",\"Timestamp\":1488793033624,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}}]},{\"uploadTimeStamp\":\"1488793167598\",\"PDID\":\"124\",\"data\":[{\"Data\":{\"unit\":\"rpm\",\"value\":\"100\"},\"EventID\":\"E1\",\"PDID\":\"124\",\"Timestamp\":1488793167598,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}},{\"Data\":{\"heading\":\"N\",\"loc1\":\"false\",\"loc2\":\"00.001\",\"loc3\":\"00.004\",\"loc4\":\"false\",\"speed\":\"10\"},\"EventID\":\"E2\",\"PDID\":\"124\",\"Timestamp\":1488793167598,\"Timezone\":330,\"Version\":\"1.1\",\"pii\":{}},{\"Data\":{\"xvalue\":\"1.1\",\"yvalue\":\"1.2\",\"zvalue\":\"2.2\"},\"EventID\":\"E3\",\"PDID\":\"124\",\"Timestamp\":1488793167598,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}},{\"EventID\":\"E4\",\"Data\":{\"value\":\"50\",\"unit\":\"percentage\"},\"Version\":\"1.0\",\"Timestamp\":1488793167598,\"PDID\":\"124\",\"Timezone\":330},{\"Data\":{\"unit\":\"kmph\",\"value\":\"70\"},\"EventID\":\"E5\",\"PDID\":\"124\",\"Timestamp\":1488793167598,\"Timezone\":330,\"Version\":\"1.0\",\"pii\":{}}]}]");
		
		
		
		//get the 2D representation of JSON document
		//List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
		
		//write the 2D representation in csv format
		//flatMe.write2csv(destination);
		/*
		 * OR
		 * */
		//directly write the JSON document to CSV
		//flatMe.json2Sheet().write2csv(destination);
		
		//directly write the JSON document to CSV but with delimiter
		//flatMe.json2Sheet().write2csv("/home/aptus/Desktop/test.csv", ',');
		flatMe
				.json2Sheet()
				.headerSeparator("/")
				.write2csv("/home/aptus/Desktop/test.csv");
		//System.out.println(flatMe.json2Sheet().getUniqueFields());
		
	}
}
