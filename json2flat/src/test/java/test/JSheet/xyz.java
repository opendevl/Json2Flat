package test.JSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class xyz {
public static void main(String[] args) throws IOException{
		
		String source = TSheet.class.getResource("/new.json").getPath();
		
		String destination = TSheet.class.getResource("/json2csv.csv").getPath().split("target")[0]
				+"src/test/resources"
				+TSheet.class.getResource("/json2csv.csv").getPath().split("test-classes")[1];
		
		String jsonString = new String(Files.readAllBytes(Paths.get(source)));
		
		//JFlat flatMe = new JFlat(jsonString,fetchMode.FILE);
		
		
		
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
		
	}
}
