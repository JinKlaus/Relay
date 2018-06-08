package config;

import java.util.HashMap;

public class SchoolOpenTime {
	
	public static HashMap<String, Object> schoolopentime = new HashMap<>();
	public static HashMap<String, Object> schoolopenmonth = new HashMap<>();
	
	static{
		schoolopentime.put("1",new HashMap<String,Object>(){
			{
				put("start_time", "1441036800");
				put("end_time", "1456588800");
			}
		});
		schoolopentime.put("2",new HashMap<String,Object>(){
			{
				put("start_time", "1456070400");
				put("end_time", "1472659200");
			}
		});
		schoolopentime.put("3",new HashMap<String,Object>(){
			{
				put("start_time", "1472659200");
				put("end_time", "1486656000");
			}
		});
		schoolopentime.put("4",new HashMap<String,Object>(){
			{
				put("start_time", "1486915200");
				put("end_time", "1501516800");
			}
		});
		
		schoolopentime.put("4",new HashMap<String,Object>(){
			{
				put("start_time", "1486915200");
				put("end_time", "1501516800");
			}
		});
	
		
		schoolopentime.put("5",new HashMap<String,Object>(){
			{
				put("start_time", "1503158400");
				put("end_time", "1517414400");
			}
		});
	
		
		schoolopenmonth.put("1",new HashMap<String,Object>(){
			{
				put("start_time", "201509");
				put("end_time", "201602");
			}
		});
		schoolopenmonth.put("2",new HashMap<String,Object>(){
			{
				put("start_time", "201602");
				put("end_time", "201609");
			}
		});
		schoolopenmonth.put("3",new HashMap<String,Object>(){
			{
				put("start_time", "201609");
				put("end_time", "201702");
			}
		});
		schoolopenmonth.put("4",new HashMap<String,Object>(){
			{
				put("start_time", "201702");
				put("end_time", "201708");
			}
		});
		
		schoolopenmonth.put("5",new HashMap<String,Object>(){
			{
				put("start_time", "201709");
				put("end_time", "201802");
			}
		});
		
	}
	
	public static String getStartTime(String term_id){
		HashMap<String, String> time=(HashMap<String, String>) schoolopentime.get(term_id);
		return time.get("start_time");
	}

}
