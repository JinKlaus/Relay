package util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonToMapList {

	/**
	 * 转换为Map 对象
	 * @param jsonString
	 * @return
	 */
	public static HashMap<String, JSONObject> getMap(String jsonString)
	{
		JSONObject sJsonObject = new JSONObject();
		return JSON.parseObject(jsonString, new HashMap<String,JSONObject>().getClass());		
		
	}

	/**
	 * 转换为list
	 * @return
	 */
	public static ArrayList<HashMap<String, JSONObject>> getList(String jsonString)
	{
		return JSON.parseObject(jsonString, new ArrayList<HashMap<String,JSONObject>>().getClass());
	}
 
	public static String toJSONString(Object object){
		String res =  JSON.toJSONString(object, SerializerFeature.DisableCircularReferenceDetect,SerializerFeature.WriteMapNullValue,SerializerFeature.WriteNullListAsEmpty,SerializerFeature.WriteNullStringAsEmpty,SerializerFeature.WriteNullBooleanAsFalse);
		return res;
	}
	


}
