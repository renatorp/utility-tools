package tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.simple.JSONObject;

/**
 * @author renato.ribeiro
 * Input: 
 * teste.a,testea
 * teste.b,testeb
 * OutPut: "teste": {"a": "testea", "b": "testeb"}
 */
public class JSONUnflattener {
	private static final String URL = "/home/likewise-open/SEDE/renato.ribeiro/Área de Trabalho/i18ns";
	private static final String FILE_NAME = "i18n-messages-pt";

	public static void main(String[] args) {

		Map<String, String> map = getCsvEntries();
		
		JSONObject json = unflattenJson(map);
	
		String result = StringEscapeUtils.unescapeJava(json.toString());

		System.out.println(result);
		
	}

	private static JSONObject unflattenJson(Map<String, String> map) {
		JSONObject json = new JSONObject();
		
		for (String key : map.keySet()) {
			obterJsonEntry(json, key, map.get(key));
		}
		return json;
	}


	private static Map<String, String> getCsvEntries() {
		File file = new File(getCsvUrl());
		Map<String, String> map = new HashMap<String, String>();
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String s = null;

			while ((s = reader.readLine()) != null) {
				map.put(s.split("[,].*")[0], s.replace(s.split("[,].*")[0]+",", ""));
			}
			reader.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}


	private static String getCsvUrl() {
		return URL + "/" + FILE_NAME + ".csv";
	}
	
	@SuppressWarnings("unchecked")
	private static void obterJsonEntry(JSONObject parent, String key, String value) {
		
		JSONObject json = null;
		
		if (key.contains(".")) {
			String firstKey = key.split("\\.")[0];
			json = (JSONObject)parent.get(firstKey);
			if (json == null) {
				json = new JSONObject();
			}
			obterJsonEntry(json, key.substring(firstKey.length() + 1, key.length()), value);
			parent.put(firstKey, json);
		} else {
			parent.put(key, value);
		}
		
	}

}
