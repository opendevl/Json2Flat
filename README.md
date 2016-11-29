# Json2Flat
Converting JSON to flat CSV

Example
----------
```java
String str = new String(Files.readAllBytes(Paths.get("/path/to/source/file.json")));
JFlat flatMe = new JFlat(str);

List<Object[]> json2csv = flatMe.jsonToSheet(); 

PrintWriter writer = new PrintWriter("/path/to/destination/file.csv", "UTF-8");

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
|                  |                      |                    |                          |                   |                     |                     |                     |                     |                     |                  | 
|------------------|----------------------|--------------------|--------------------------|-------------------|---------------------|---------------------|---------------------|---------------------|---------------------|------------------| 
| /store/book/name | /store/book/category | /store/book/author | /store/book/title        | /store/book/price | /store/book/marks/0 | /store/book/marks/1 | /store/book/marks/2 | /store/book/marks/3 | /store/book/marks/4 | /store/book/isbn | 
| "dasd"           | "reference"          | "Nigel Rees"       | "Sayings of the Century" | 8.95              | 3                   | 99                  | 89                  |                     |                     |                  | 
|                  | "fiction"            | "Evelyn Waugh"     | "Sword of Honour"        | 12.99             | 3                   | 99                  | 89                  | 34                  | 67567               |                  | 
|                  | "fiction"            | "Herman Melville"  | "Moby Dick"              | 8.99              | 3                   | 99                  | 89                  |                     |                     | "0-553-21311-3"  | 
|                  | "fiction"            | "J. R. R. Tolkien" | "The Lord of the Rings"  | 22.99             |                     |                     |                     |                     |                     | "0-395-19395-8"  | 
