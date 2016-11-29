package test.JSheet;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.dev.json2flat.JFlat;

public class TestSheet {
	public static void main(String[] args) throws IOException{
		
		
		String str = new String(Files.readAllBytes(Paths.get("/home/aptus/workspace/mvgitproj/Json2Flat/json2flat/src/main/resources/test.json")));
		JFlat flatMe = new JFlat(str);
		
		List<Object[]> json2csv = flatMe.jsonToSheet(); 
		
		PrintWriter writer = new PrintWriter("/home/aptus/Desktop/json2csv.csv", "UTF-8");
		boolean comma = false;
		for(Object[] o : json2csv){
			comma = false;
			for(Object t : o){
				if(t==null){
					writer.print(comma == true ? "," : "");
				}
				else{
					writer.print(comma == true ? ","+t.toString() : t.toString());
				}
				if(comma == false)
					comma = true;
			}
			writer.println();
		}
		writer.close();
	}

}
