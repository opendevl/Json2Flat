package com.github.opendevl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

/**
 * This class converts a Json document in a 2D matrix format like CSV.
 * @author opendevl
 * @version 1.0.1
 */
public class JFlat {
	
	private String jsonString = null;
	
	private List<Object[]> sheetMatrix = null;
	
	private List<String> pathList = null;
	
	private Configuration conf = null;
	private Configuration pathConf = null;
	
	private DocumentContext parse = null;
	private DocumentContext parsePath = null;
	
	private HashSet<String> primitivePath = null;
	private HashSet<String> primitiveUniquePath = null;
	private List<String> unique = null;
	
	private String regex = "(\\[[0-9]*\\]$)";
	private Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	
	private JsonElement ele = null;
	
	private String tmpPath = null;
	
	/**
	 * This constructor takes a Json as string.
	 * @param jsonString it takes Json as string.
	 */
	public JFlat(String jsonString){
		
		this.jsonString = jsonString;
		
		this.conf = Configuration.defaultConfiguration()
				.addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
				.addOptions(Option.SUPPRESS_EXCEPTIONS);
		
		this.pathConf = Configuration.defaultConfiguration()
				.addOptions(Option.AS_PATH_LIST)
				.addOptions(Option.ALWAYS_RETURN_LIST);
	}
	
	
	/**
	 * This method does some pre processing and then calls make2D() to get the 2D representation of Json document.
	 * @return returns a JFlat object
	 */
	public JFlat json2Sheet(){
		
		sheetMatrix = new ArrayList<Object[]>();
		
		ele = new JsonParser().parse(this.jsonString);
		
		parse = JsonPath.using(conf).parse(this.jsonString);
		parsePath = JsonPath.using(pathConf).parse(this.jsonString);
		
		pathList = parsePath.read("$..*");
		
		primitivePath = new LinkedHashSet<String>();
		primitiveUniquePath = new LinkedHashSet<String>();
		
		for(String o : pathList){
			Object tmp = parse.read(o);
			
			if(tmp==null){
				primitivePath.add(o);
				
			}else{
				String dataType = tmp.getClass().getSimpleName();
				if(dataType.equals("Boolean") || dataType.equals("Integer") || dataType.equals("String") || dataType.equals("Double") || dataType.equals("Long")){
					primitivePath.add(o);
				}else{
				}
			}
		}
		
		for(String o : primitivePath){
			
			Matcher m = pattern.matcher(o);

			if(m.find()){
				String tmp[] = o.replace("$", "").split("(\\[[0-9]*\\]$)");
				tmp[0] = tmp[0].replaceAll("(\\[[0-9]*\\])", "");
				primitiveUniquePath.add("/"+(tmp[0]+m.group()).replace("'][", "/").replace("[", "").replace("]", "").replace("''", "/").replace("'", ""));
			}else{
				primitiveUniquePath.add("/"+o.replace("$", "").replaceAll("(\\[[0-9]*\\])", "").replace("[", "").replace("]", "").replace("''", "/").replace("'", ""));
			}
		}
		
		unique = new ArrayList<String>(primitiveUniquePath);
		
		Object[] header = new Object[unique.size()];
		int i = 0;
		for(String o : unique){
			header[i] = o;
			i++;
		}
		
		sheetMatrix.add(header);
		
		sheetMatrix.add(make2D(new Object[unique.size()], new Object[unique.size()], ele, "/"));
		
		Object last[] = sheetMatrix.get(sheetMatrix.size()-1);
		Object secondLast[] = sheetMatrix.get(sheetMatrix.size()-2);
		
		boolean delete = true;
		
		for(Object o : last){
			if(o!=null){
				delete = false;
				break;
			}
		}
		
		if(!delete){
			delete = true;
			for(int DEL=0; DEL< last.length; DEL++){
				if(last[DEL] != null && !last[DEL].equals(secondLast[DEL])){
					delete = false;
					break;
				}
			}
		}
		
		if(delete)
			sheetMatrix.remove(sheetMatrix.size()-1);
		
		return this;
	}
	
	/**
	 * This method is the core algorithm which converts the Json document to its 2D representation. 
	 * @param cur its the logical current row of the Json being processed
	 * @param old it keeps the old row which is always assigned to the current row.
	 * @param ele this keeps the part of json being parsed to 2D.
	 * @param path this mantains the path of the Json element being processed.
	 * @return
	 */
	private Object[] make2D(Object[] cur, Object[] old, JsonElement ele, String path){
		cur = old.clone();
		
		boolean gotArray = false;
		
		if(ele.isJsonObject()){
			
			for(Map.Entry<String, JsonElement> entry : ele.getAsJsonObject().entrySet()){
				
				if(entry.getValue().isJsonPrimitive()){
					tmpPath = path+entry.getKey();
					tmpPath = tmpPath.replaceAll("(\\/\\/[0-9]+)", "/").replaceAll("\\/\\/+", "/");
					tmpPath = tmpPath.replaceAll("\\/[0-9]+\\/", "/");
					if(unique.contains(tmpPath)){
						int index = unique.indexOf(tmpPath);
						cur[index] = entry.getValue().getAsJsonPrimitive();
					}
					tmpPath = null;
				}
				else if(entry.getValue().isJsonObject()){
					cur = (make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonObject(), path + entry.getKey() + "/"));
				}
				else if(entry.getValue().isJsonArray()){
					cur = make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonArray(), path + entry.getKey() + "/");
				}
			}
			
		}
		else if(ele.isJsonArray()){
			int arrIndex = 0;
			
			
			for(JsonElement tmp : ele.getAsJsonArray()){
				
				if(tmp.isJsonPrimitive()){
					tmpPath = path + arrIndex;
					tmpPath = tmpPath.replaceAll("(\\/\\/[0-9]+)", "/").replaceAll("\\/\\/+", "/");
					tmpPath = tmpPath.replaceAll("[0-9]+\\/", "");
					if(unique.contains(tmpPath)){
						int index = unique.indexOf(tmpPath);
						cur[index] = tmp.getAsJsonPrimitive();
					}
					tmpPath = null;
				}
				else{					
					if(tmp.isJsonObject()){
						gotArray = isInnerArray(tmp);
						sheetMatrix.add(make2D(new Object[unique.size()], cur, tmp.getAsJsonObject(), path + arrIndex + "/"));
						if(gotArray){
							sheetMatrix.remove(sheetMatrix.size()-1);
						}
					}else if(tmp.isJsonArray()){
						make2D(new Object[unique.size()], cur, tmp.getAsJsonArray(), path + arrIndex + "//");
					}
				}
				arrIndex++;
			}
		}
		return cur;
	}
	
	/**
	 * This method checks whether object inside an array contains an array or not.
	 * @param ele it a Json object inside an array
	 * @return it returns true if Json object inside an array contains an array or else false
	 */
	private boolean isInnerArray(JsonElement ele){
		
		for(Map.Entry<String, JsonElement> entry : ele.getAsJsonObject().entrySet()){
			if(entry.getValue().isJsonArray()){
				if(entry.getValue().getAsJsonArray().size()>0)
					
					for(JsonElement checkPrimitive : entry.getValue().getAsJsonArray()){
						
						if(checkPrimitive.isJsonObject()){
							return true;
						}
					}
			}
				
			
		}
		return false;
	}
	
	/**
	 * This method returns the sheet matrix.
	 * @return List<Object>
	 */
	public List<Object[]> getJsonAsSheet(){
		return this.sheetMatrix;
	}
	
	/**
	 * This method writes the 2D representation in csv format with ',' as default delimiter.
	 * @param destination it takes the destination path for the csv file.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination) throws FileNotFoundException, UnsupportedEncodingException{
		this.write2csv(destination, ',');
	}
	
	/**
	 * This method writes the 2D representation in csv format with custom delimiter set by user.
	 * @param destination it takes the destination path for the csv file.
	 * @param delimiter it represents the delimiter set by user.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination, char delimiter) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(new File(destination), "UTF-8");
		boolean comma = false;
		for(Object[] o : this.sheetMatrix){
			comma = false;
			for(Object t : o){
				if(t==null){
					writer.print(comma == true ? delimiter : "");
				}
				else{
					writer.print(comma == true ? delimiter+t.toString() : t.toString());
				}
				if(comma == false)
					comma = true;
			}
			writer.println();
		}
		writer.close();
	}
}