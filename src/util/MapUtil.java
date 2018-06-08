/**
 * 
 */
package util;

import java.util.ArrayList;
import java.util.HashMap;

import org.beetl.ext.fn.GetValueFunction;

/**
 * @author liuzr
 * @date 2017年7月11日  新建  
 */
public class MapUtil {
	
	/**
	 * HashMap<String, String>转换成HashMap<String, Object>
	 * @author  liuzr
	 * @date 2017年7月11日  新建  
	 * @param map
	 * @return
	 */
	public static HashMap<String, Object> transToObjectMap(HashMap<String, String> map)  {
		
		HashMap<String, Object> rtnMap = new HashMap<>();
		
		for (String key : map.keySet()) {
			rtnMap.put(key, map.get(key)==null?"":map.get(key));
		}
		
		return rtnMap;
	}
	
	/**
	 * 将ArrayList<HashMap<String, String>>转换成ArrayList<HashMap<String, Object>>
	 * @author  liuzr
	 * @date 2017年7月11日  新建  
	 * @param array
	 * @return0
	 */
	public static ArrayList<HashMap<String, Object>> transToObjectArray(ArrayList<HashMap<String, String>> array) {
		
		ArrayList<HashMap<String, Object>> res = new ArrayList<>();
		
		for(int i = 0; i < array.size(); i++) {
			HashMap<String, Object> item = transToObjectMap(array.get(i));
			res.add(item);
		}
		
		return res;
	}
	
	public static Object getValue(HashMap<String, Object> map,String key){
		if(map.containsKey(key)){
			return map.get(key);
		}else{
			return "";
		}
	}
	
	public static int getInt(HashMap<String, Object> map,String key){
		if(map.containsKey(key)){
			return Integer.parseInt(map.get(key).toString());
		}else{
			return 0;
		}
	}
	
	
	
}
