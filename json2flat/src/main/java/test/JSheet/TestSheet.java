package test.JSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.github.opendevl.JFlat;

/**
 * @author aptus
 * Yahoo
 */
public class TestSheet {
	/**
	 * Just for testing
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException{
		
		
		String str = new String(Files.readAllBytes(Paths.get("/home/workspace/mvgitproj/Json2Flat/json2flat/src/main/resources/test.json")));
		JFlat flatMe = new JFlat(str);
		
		List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();
		
		flatMe.write2csv("/home/Desktop/json2csv.csv");
		/*
		 * OR
		 * */
		
		flatMe.json2Sheet().write2csv("/home/Desktop/json2csv.csv");
	}

}
