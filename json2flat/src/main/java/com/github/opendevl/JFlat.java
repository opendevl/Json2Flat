package com.github.opendevl;

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
 * This class describes some methods which converts a Json document in a 2D matrix.
 * @author skap
 * @version 1.0
 *
 */
public class JFlat {
	
	String jsonString = null;
	
	List<Object[]> sheetMatrix = null;
	
	List<String> pathList = null;
	
	Configuration conf = null;
	Configuration pathConf = null;
	
	DocumentContext parse = null;
	DocumentContext parsePath = null;
	
	HashSet<String> primitivePath = null;
	HashSet<String> primitiveUniquePath = null;
	List<String> unique = null;
	
	String regex = "(\\[[0-9]*\\]$)";
	Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
	
	JsonElement ele = null;
	
	String tmpPath = null;
	
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
	 * This method 
	 * @return
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
			// System.out.println(o);
			Object tmp = parse.read(o);
			
			if(tmp==null){
				primitivePath.add(o);
				
			}else{
				String dataType = tmp.getClass().getSimpleName();
				if(dataType.equals("Boolean") || dataType.equals("Integer") || dataType.equals("String") || dataType.equals("Double") || dataType.equals("Long")){
					primitivePath.add(o);
				}else{
					//System.out.println(dataType);
				}
			}
		}
		
		for(String o : primitivePath){
			
			Matcher m = pattern.matcher(o);
			//System.out.println(o);
			if(m.find()){
				//System.out.println(o);
				//System.out.println(m.group());
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
	
	public Object[] make2D(Object[] cur, Object[] old, JsonElement ele, String path){
		cur = old.clone();
		
		boolean gotArray = false;
		
		if(ele.isJsonObject()){
			
			for(Map.Entry<String, JsonElement> entry : ele.getAsJsonObject().entrySet()){
				
				if(entry.getValue().isJsonPrimitive()){
					tmpPath = path+entry.getKey();
					tmpPath = tmpPath.replaceAll("(\\/\\/[0-9]+)", "/").replaceAll("\\/\\/+", "/");
					tmpPath = tmpPath.replaceAll("\\/[0-9]+\\/", "/");
					//tmpPath = tmpPath.replaceAll("\\(obj\\)\\/", "/");
					//System.out.println(tmpPath);
					if(unique.contains(tmpPath)){
						int index = unique.indexOf(tmpPath);
						//cur[index] = entry.getValue().getAsJsonPrimitive().getAsString();
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
					//System.out.println(tmpPath);
					if(unique.contains(tmpPath)){
						int index = unique.indexOf(tmpPath);
						//cur[index] = tmp.getAsJsonPrimitive().getAsString();
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
	
	public boolean isInnerArray(JsonElement ele){
		
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
	 * 
	 * @return
	 */
	public List<Object[]> getJsonAsSheet(){
		return this.sheetMatrix;
	}
	
	/**
	 * 
	 * @param destination
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination) throws FileNotFoundException, UnsupportedEncodingException{
		this.write2csv(destination, ',');
	}
	
	/**
	 * 
	 * @param destination
	 * @param delimiter
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination, char delimiter) throws FileNotFoundException, UnsupportedEncodingException{
		PrintWriter writer = new PrintWriter(destination, "UTF-8");
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