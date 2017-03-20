package test.JSheet;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.github.opendevl.JFlat;

public class TSheet {

	public static void main(String[] args) throws Exception{
		
		String source = TSheet.class.getResource("/tmp.json").getPath();
		
		String jsonString = new String(Files.readAllBytes(Paths.get(source)));
	
		//JFlat flatMe = new JFlat("https://raw.githubusercontent.com/kamalpradhan95/test_repo/master/store.json",fetchMode.URL);
		JFlat flatMe = new JFlat(jsonString);
		
		
		
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
				.headerSeparator()
				.write2csv("/home/aptus/Desktop/test.csv");
		//System.out.println(flatMe.json2Sheet().getUniqueFields());
		
	}
}
