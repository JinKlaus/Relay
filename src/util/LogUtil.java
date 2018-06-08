package util;

import java.util.HashMap;

public class LogUtil {
	
	public static void log(HashMap<String, Object> data){
		System.out.println(data.toString());
	}
	
	public static void log(String res){
		System.out.println(res);
	}

}
