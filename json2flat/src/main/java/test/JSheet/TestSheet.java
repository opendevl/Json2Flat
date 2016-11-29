package test.JSheet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import com.dev.json2flat.JFlat;

public class TestSheet {
	public static void main(String[] args) throws IOException{
		
		
		String str = new String(Files.readAllBytes(Paths.get("/home/aptus/workspace/mvgitproj/Json2Flat/json2flat/src/main/resources/EPL_JSON.json")));
		JFlat x = new JFlat(str);
		
		List<Object[]> json2csv = x.jsonToSheet(); 
		
		System.out.println();
		for(Object[] o : json2csv){
			for(Object t : o){
				if(t==null)
					System.out.print("--");
				else
					System.out.print(t.toString());
				System.out.print("\t|\t");
			}
			System.out.println();
		}
	}

}
