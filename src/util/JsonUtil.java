package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map.Entry;

import java.util.Scanner;

public class JsonUtil {
	
	static HashMap<String, String> all_config;
	
	static{
		all_config = new HashMap<>();
	}
	
	public static String getJson(String filename){
		
		//if(all_config.containsKey(filename) && !Entrance.is_debug){
		if(all_config.containsKey(filename)) {
			return all_config.get(filename);
		}
		File file = new File("src/"+filename);
		Scanner scanner = null;
		StringBuffer buffer = new StringBuffer();
		
		try {
			scanner = new Scanner(file,"utf-8");
			while (scanner.hasNextLine()) {
				buffer.append(scanner.nextLine());
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(scanner!=null){
				scanner.close();
			}
		}
		all_config.put(filename, buffer.toString());
		return all_config.get(filename);
	}
	
	/**
	 * 遍历一层map
	 * @param map
	 * @return
	 */
	public static String outSimpleMap(HashMap<String, Object> map){
		String s ="{";
		for (Entry<String, Object> entry : map.entrySet()) {
			entry.getKey();
			entry.getValue();
		}
		s+="}";
		return s;
	}

}
