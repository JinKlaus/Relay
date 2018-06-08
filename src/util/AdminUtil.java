package util;

import java.util.HashMap;

import javax.swing.text.StyledEditorKit.BoldAction;

public class AdminUtil {
	
	static HashMap<String, Boolean> has_admin= new HashMap<>();
	
	public static void setAdmin(String key){
		has_admin.put(key, true);
	}
	
	public static boolean is_login(String key){
		if(has_admin.containsKey(key)) return has_admin.get(key);
		else return false;
	}

}
