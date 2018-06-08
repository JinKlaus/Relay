package util;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;

import com.alibaba.fastjson.JSON;

public class BsonUtil {
		
		public static ObjectId getObjectID(String idString){
			HashMap<String,String> _id = JSON.parseObject( idString,new HashMap<String,String>().getClass());
			return new ObjectId(_id.get("$oid"));
			
		}
		
		
		public static  HashMap<String, Object> removeObjectId(HashMap<String, Object> data){
			data.remove("_id");
			return data;
		}
	
}
