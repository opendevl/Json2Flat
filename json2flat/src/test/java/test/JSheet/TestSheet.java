package test.JSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.github.opendevl.JFlat;

public class TestSheet {
	public static void main(String[] args) throws IOException{
		System.out.println("Heloo");
		
		/*String str = new String(Files.readAllBytes(Paths.get("/home/aptus/workspace/mvgitproj/Json2Flat/json2flat/src/main/resources/tmp.json")));
		JFlat flatMe = new JFlat(str);*/
		
		//get the 2D representation of JSON document
		//List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
		
		//write the 2D representation in csv format
		//flatMe.write2csv("/home/aptus/Desktop/json2csv.csv");
		/*
		 * OR
		 * */
		//directly write the JSON document to CSV
		//flatMe.json2Sheet().write2csv("/home/Desktop/json2csv.csv");
		
		//directly write the JSON document to CSV but with delimiter
		//flatMe.json2Sheet().write2csv("/home/aptus/Desktop/json2csv.csv", ',');
	}

}
