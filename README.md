# Json2Flat
Converting JSON to flat CSV. No POJO's required.  
This is just a beta version.  
But still if you want to use here goes the dependency for Maven. 
```xml
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.2.0</version>
</dependency>
```

Example
----------
```java
String str = new String(Files.readAllBytes(Paths.get("/path/to/source/file.json")));

JFlat flatMe = new JFlat(str);

//get the 2D representation of JSON document
List<Object[]> json2csv = flatMe.json2Sheet().getJsonAsSheet();

//write the 2D representation in csv format
flatMe.write2csv("/path/to/destination/file.json");
```
OR
```java
String str = new String(Files.readAllBytes(Paths.get("/path/to/source/file.json")));

JFlat flatMe = new JFlat(str);

//directly write the JSON document to CSV
flatMe.json2Sheet().write2csv("/path/to/destination/file.json");

//directly write the JSON document to CSV but with delimiter
flatMe.json2Sheet().write2csv("/path/to/destination/file.json", '|');
```
Input JSON
----------
```javascript
{
    "store": {
	    
	    
		"book": [
		    {
		        "name":"dasd",
		        "category": "reference",
		        "author": "Nigel Rees",
		        "title": "Sayings of the Century",
		        "price": 8.95,
		        "marks" : [3,99,89]
		    },
		    {
		        "category": "fiction",
		        "author": "Evelyn Waugh",
		        "title": "Sword of Honour",
		        "price": 12.99,
		        "marks" : [3,99,89,34,67567]
		    },
		    {
		        "category": "fiction",
		        "author": "Herman Melville",
		        "title": "Moby Dick",
		        "isbn": "0-553-21311-3",
		        "price": 8.99,
		        "marks" : [3,99,89]
		    },
		    {
		        "category": "fiction",
		        "author": "J. R. R. Tolkien",
		        "title": "The Lord of the Rings",
		        "isbn": "0-395-19395-8",
		        "price": 22.99,
		        "marks" : []
		    }
		]
	}
}
```
Output CSV
----------
| /store/book/name | /store/book/category | /store/book/author | /store/book/title      | /store/book/price | /store/book/marks/0 | /store/book/marks/1 | /store/book/marks/2 | /store/book/marks/3 | /store/book/marks/4 | /store/book/isbn |
|------------------|----------------------|--------------------|------------------------|-------------------|---------------------|---------------------|---------------------|---------------------|---------------------|------------------|
| dasd             | reference            | Nigel Rees         | Sayings of the Century | 8.95              | 3                   | 99                  | 89                  |                     |                     |                  |
|                  | fiction              | Evelyn Waugh       | Sword of Honour        | 12.99             | 3                   | 99                  | 89                  | 34                  | 67567               |                  |
|                  | fiction              | Herman Melville    | Moby Dick              | 8.99              | 3                   | 99                  | 89                  |                     |                     | 0-553-21311-3    |
|                  | fiction              | J. R. R. Tolkien   | The Lord of the Rings  | 22.99             |                     |                     |                     |                     |                     | 0-395-19395-8    |
