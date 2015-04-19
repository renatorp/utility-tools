package tools;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * @author renato.ribeiro
 * Input: "teste": {"a": "testea", "b": "testeb"}
 * Output:
 * teste.a,testea
 * teste.b,testeb
 */
public class JSONFlattener {
	private static final String URL = "/home/likewise-open/SEDE/renato.ribeiro/Área de Trabalho";
	private static final String FILE_NAME = "i18n";

	public static void main(String[] args) {
		List<String> result = flattenJson(getJsonUrl());
		printResult(result);
	}

	private static void printResult(List<String> result) {
		for (String string : result) {
			System.out.println(string);
		}
	}
	
	private static String getJsonUrl() {
		return URL + "/" + FILE_NAME + ".json";
	}		
	
	@SuppressWarnings("rawtypes")
	private static List<String> flattenJson(String url) {
		List<String> result = new ArrayList<String>();
		
		try {
			JSONParser parser = new JSONParser();
			Map json = (Map)parser.parse(new FileReader(url));

			
			System.out.println(json.toString());
			
			obterResultado(json, result, "");
			
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	@SuppressWarnings("rawtypes")
	private static void obterResultado(Map json, List<String> result, String string) {
		Iterator iter = json.entrySet().iterator();
	    while(iter.hasNext()){
	      Map.Entry entry = (Map.Entry)iter.next();
	      
	      if (entry.getValue() instanceof JSONObject) {
	    	  obterResultado((Map)entry.getValue(), result, string + entry.getKey() + ".");
	      } else {
	    	  result.add(string + entry.getKey() + "," + entry.getValue());
	      }
	    }
		
	}
}
