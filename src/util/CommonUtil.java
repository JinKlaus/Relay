package util;

import java.util.Map;

public class CommonUtil {
	
	public static boolean isEmpty(String res){
		if(res==null || res.equals("")) return true;
		else return false;
	}
	
	public  static <T> T getValue(Map<String,T> map,String key){
		if(map.containsKey(key)) return map.get(key);
		else return null;
	}

}
