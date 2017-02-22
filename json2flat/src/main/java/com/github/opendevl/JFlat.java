package com.github.opendevl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

/**
 * This class converts a Json document in a 2D matrix format like CSV.
 * 
 * @author opendevl
 * @version 1.0.3-SNAPSHOT
 */
public class JFlat {
	 
	private String jsonString = null;

	private List<Object[]> sheetMatrix = null;

	private List<String> pathList = null;

	/*
	 * private Configuration conf = null; private Configuration pathConf = null;
	 */

	// private DocumentContext parse = null;
	// private DocumentContext parsePath = null;

	private String tmp[] = null;

	private HashSet<String> primitivePath = null;
	private HashSet<String> primitiveUniquePath = null;
	private List<String> unique = null;

	private String regex = "(\\[[0-9]*\\]$)";
	private Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);

	private JsonElement ele = null;

	private String tmpPath = null;

	private OrderJson makeOrder = new OrderJson();
	
	
	/*public static enum fetchMode {
		STRING,URL,FILE
	}*/

	/*
	 * static{ Configuration.setDefaults(new Configuration.Defaults() { private
	 * final JsonProvider jsonProvider = new JacksonJsonProvider(); private
	 * final MappingProvider mappingProvider = new JacksonMappingProvider();
	 * 
	 * //@Override public JsonProvider jsonProvider() { return jsonProvider; }
	 * 
	 * //@Override public MappingProvider mappingProvider() { return
	 * mappingProvider; }
	 * 
	 * //@Override public Set options() { return EnumSet.noneOf(Option.class); }
	 * }); }
	 */

	/**
	 * This constructor takes a Json as string.
	 * 
	 * @param jsonString
	 *            it takes Json as string.
	 */
	
	/*public JFlat(String jsonString,fetchMode sourceType) {

		if(sourceType.equals(fetchMode.URL)){
			try{
				URL url = new URL(new String(jsonString));
				this.jsonString = IOUtils.toString(url.openStream());
			}catch (Exception e) {
				e.printStackTrace();
			}
		}else if(sourceType.equals(fetchMode.FILE)){
			try {
				this.jsonString = new String(Files.readAllBytes(Paths.get(jsonString)));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			this.jsonString = jsonString;
		}
	}*/

	public JFlat(String jsonString) {

		this.jsonString = jsonString;

	}
	
	

	/**
	 * This method does some pre processing and then calls make2D() to get the
	 * 2D representation of Json document.
	 * 
	 * @return returns a JFlat object
	 */
	public JFlat json2Sheet() {

		Configuration.setDefaults(new Configuration.Defaults() {
			private final JsonProvider jsonProvider = new JacksonJsonProvider();
			private final MappingProvider mappingProvider = new JacksonMappingProvider();

			// @Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			// @Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			// @Override
			public Set options() {
				return EnumSet.noneOf(Option.class);
			}
		});

		Configuration conf = Configuration.defaultConfiguration().addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL)
				.addOptions(Option.SUPPRESS_EXCEPTIONS);

		Configuration pathConf = Configuration.defaultConfiguration().addOptions(Option.AS_PATH_LIST)
				.addOptions(Option.ALWAYS_RETURN_LIST);

		DocumentContext parse = null;

		sheetMatrix = new ArrayList<Object[]>();

		ele = new JsonParser().parse(this.jsonString);

		pathList = JsonPath.using(pathConf).parse(this.jsonString).read("$..*");

		parse = JsonPath.using(conf).parse(this.jsonString);
		/*
		 * parse = JsonPath.using(conf).parse(this.jsonString); parsePath =
		 * JsonPath.using(pathConf).parse(this.jsonString);
		 * 
		 * pathList = parsePath.read("$..*");
		 */

		primitivePath = new LinkedHashSet<String>();
		primitiveUniquePath = new LinkedHashSet<String>();

		for (String o : pathList) {
			Object tmp = parse.read(o);

			if (tmp == null) {
				primitivePath.add(o);

			} else {
				String dataType = tmp.getClass().getSimpleName();
				if (dataType.equals("Boolean") || dataType.equals("Integer") || dataType.equals("String")
						|| dataType.equals("Double") || dataType.equals("Long")) {
					primitivePath.add(o);
				} else {
					// its not a primitive data type
				}
			}
		}

		for (String o : primitivePath) {

			Matcher m = pattern.matcher(o);

			if (m.find()) {
				tmp = o.replace("$", "").split("(\\[[0-9]*\\]$)");
				tmp[0] = tmp[0].replaceAll("(\\[[0-9]*\\])", "");
				primitiveUniquePath.add("/" + (tmp[0] + m.group()).replace("'][", "/").replace("[", "").replace("]", "")
						.replace("''", "/").replace("'", ""));
			} else {
				primitiveUniquePath.add("/" + o.replace("$", "").replaceAll("(\\[[0-9]*\\])", "").replace("[", "")
						.replace("]", "").replace("''", "/").replace("'", ""));
			}
		}

		unique = new ArrayList<String>(primitiveUniquePath);

		Object[] header = new Object[unique.size()];
		int i = 0;
		for (String o : unique) {
			header[i] = o;
			i++;
		}

		sheetMatrix.add(header);

		sheetMatrix.add(make2D(new Object[unique.size()], new Object[unique.size()], ele, "$"));

		Object last[] = sheetMatrix.get(sheetMatrix.size() - 1);
		Object secondLast[] = sheetMatrix.get(sheetMatrix.size() - 2);

		boolean delete = true;

		for (Object o : last) {
			if (o != null) {
				delete = false;
				break;
			}
		}

		if (!delete) {
			delete = true;
			for (int DEL = 0; DEL < last.length; DEL++) {
				if (last[DEL] != null && !last[DEL].equals(secondLast[DEL])) {
					delete = false;
					break;
				}
			}
		}

		if (delete)
			sheetMatrix.remove(sheetMatrix.size() - 1);

		return this;
	}

	/**
	 * This method is the core algorithm which converts the Json document to its
	 * 2D representation.
	 * 
	 * @param cur
	 *            its the logical current row of the Json being processed
	 * @param old
	 *            it keeps the old row which is always assigned to the current
	 *            row.
	 * @param ele
	 *            this keeps the part of json being parsed to 2D.
	 * @param path
	 *            this mantains the path of the Json element being processed.
	 * @return
	 */
	private Object[] make2D(Object[] cur, Object[] old, JsonElement ele, String path) {

		cur = old.clone();

		boolean gotArray = false;

		if (ele.isJsonObject()) {

			/*
			 * applying order to JSON. Order - 1) JSON premitive 2) JSON Array
			 * 3) JSON Object ( order of JSON Object is yet to be descided)
			 */
			// OrderJson makeOrder = new OrderJson();
			ele = makeOrder.orderJson(ele);

			for (Map.Entry<String, JsonElement> entry : ele.getAsJsonObject().entrySet()) {

				if (entry.getValue().isJsonPrimitive()) {
					/*tmpPath = path + entry.getKey();
					tmpPath = tmpPath.replaceAll("(\\/\\/[0-9]+)", "/").replaceAll("\\/\\/+", "/");
					tmpPath = tmpPath.replaceAll("\\/[0-9]+\\/", "/");*/
					
					
					tmpPath = path +"['"+ entry.getKey()+"']";
					Matcher m = pattern.matcher(tmpPath);
					// //System.out.println(o);
					if (m.find()) {
						////System.out.println(o);
						////System.out.println(m.group());
						String[] tmp = tmpPath.replace("$", "").split("(\\[[0-9]*\\]$)");
						tmp[0] = tmp[0].replaceAll("(\\[[0-9]*\\])", "");
						tmpPath = ("/" + (tmp[0] + m.group()).replace("'][", "/").replace("[", "")
								.replace("]", "").replace("''", "/").replace("'", ""));
					} else {
						tmpPath = ("/" + tmpPath.replace("$", "").replaceAll("(\\[[0-9]*\\])", "").replace("[", "")
								.replace("]", "").replace("''", "/").replace("'", ""));
					}
					
					if (unique.contains(tmpPath)) {
						int index = unique.indexOf(tmpPath);
						cur[index] = entry.getValue().getAsJsonPrimitive();
					}
					tmpPath = null;
				} else if (entry.getValue().isJsonObject()) {
					/*cur = (make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonObject(),
							path + entry.getKey() + "/"));*/
					cur = make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonObject(),
							path +"['" + entry.getKey()+"']");
				} else if (entry.getValue().isJsonArray()) {
					/*cur = make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonArray(),
							path + entry.getKey() + "/");*/
					cur = make2D(new Object[unique.size()], cur, entry.getValue().getAsJsonArray(),
							path +"['" + entry.getKey()+"']");
				}
			}

		} else if (ele.isJsonArray()) {
			int arrIndex = 0;

			for (JsonElement tmp : ele.getAsJsonArray()) {

				if (tmp.isJsonPrimitive()) {
					/*tmpPath = path + arrIndex;
					tmpPath = tmpPath.replaceAll("(\\/\\/[0-9]+)", "/").replaceAll("\\/\\/+", "/");
					tmpPath = tmpPath.replaceAll("[0-9]+\\/", "");*/
					
					tmpPath = path +"['"+ arrIndex +"']";
					Matcher m = pattern.matcher(tmpPath);
					// //System.out.println(o);
					if (m.find()) {
						////System.out.println(o);
						////System.out.println(m.group());
						String tmp1[] = tmpPath.replace("$", "").split("(\\[[0-9]*\\]$)");
						tmp1[0] = tmp1[0].replaceAll("(\\[[0-9]*\\])", "");
						tmpPath = ("/" + (tmp1[0] + m.group()).replace("'][", "/").replace("[", "")
								.replace("]", "").replace("''", "/").replace("'", ""));
					} else {
						tmpPath = ("/" + tmpPath.replace("$", "").replaceAll("(\\[[0-9]*\\])", "").replace("[", "")
								.replace("]", "").replace("''", "/").replace("'", ""));
					}
					
					if (unique.contains(tmpPath)) {
						int index = unique.indexOf(tmpPath);
						cur[index] = tmp.getAsJsonPrimitive();
					}
					tmpPath = null;
				} else {
					if (tmp.isJsonObject()) {
						gotArray = isInnerArray(tmp);
						/*sheetMatrix.add(
								make2D(new Object[unique.size()], cur, tmp.getAsJsonObject(), path + arrIndex + "/"));*/
						sheetMatrix.add(make2D(new Object[unique.size()], cur, tmp.getAsJsonObject(), path +"[" + arrIndex + "]"));
						if (gotArray) {
							sheetMatrix.remove(sheetMatrix.size() - 1);
						}
					} else if (tmp.isJsonArray()) {
						/*make2D(new Object[unique.size()], cur, tmp.getAsJsonArray(), path + arrIndex + "//");*/
						make2D(new Object[unique.size()], cur, tmp.getAsJsonArray(), path+"["+arrIndex+"]");
					}
				}
				arrIndex++;
			}
		}
		return cur;
	}

	/**
	 * This method checks whether object inside an array contains an array or
	 * not.
	 * 
	 * @param ele
	 *            it a Json object inside an array
	 * @return it returns true if Json object inside an array contains an array
	 *         or else false
	 */
	private boolean isInnerArray(JsonElement ele) {

		for (Map.Entry<String, JsonElement> entry : ele.getAsJsonObject().entrySet()) {
			if (entry.getValue().isJsonArray()) {
				if (entry.getValue().getAsJsonArray().size() > 0)

					for (JsonElement checkPrimitive : entry.getValue().getAsJsonArray()) {

						if (checkPrimitive.isJsonObject()) {
							return true;
						}
					}
			}

		}
		return false;
	}

	/**
	 * This method returns the sheet matrix.
	 * 
	 * @return List<Object>
	 */
	public List<Object[]> getJsonAsSheet() {
		return this.sheetMatrix;
	}

	/**
	 * This method returns unique fields of the json
	 * 
	 * @return List<String>
	 */
	public List<String> getUniqueFields() {
		return this.unique;
	}

	/**
	 * This method writes the 2D representation in csv format with ',' as
	 * default delimiter.
	 * 
	 * @param destination
	 *            it takes the destination path for the csv file.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination) throws FileNotFoundException, UnsupportedEncodingException {
		this.write2csv(destination, ',');
	}

	/**
	 * This method writes the 2D representation in csv format with custom
	 * delimiter set by user.
	 * 
	 * @param destination
	 *            it takes the destination path for the csv file.
	 * @param delimiter
	 *            it represents the delimiter set by user.
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	public void write2csv(String destination, char delimiter)
			throws FileNotFoundException, UnsupportedEncodingException {
		PrintWriter writer = new PrintWriter(new File(destination), "UTF-8");
		boolean comma = false;
		for (Object[] o : this.sheetMatrix) {
			comma = false;
			for (Object t : o) {
				if (t == null) {
					writer.print(comma == true ? delimiter : "");
				} else {
					writer.print(comma == true ? delimiter + t.toString() : t.toString());
				}
				if (comma == false)
					comma = true;
			}
			writer.println();
		}
		writer.close();
	}
}