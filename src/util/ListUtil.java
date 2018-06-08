package util;

import java.util.ArrayList;

public class ListUtil {
	
	public static String transferString(ArrayList<String> a){
		if(a.size()==0) return "";
		String reString="";
		for(int i=0;i<a.size();i++){
			reString+=a.get(i);
			if(i<a.size()-1) reString+=",";
		}
		return reString;
	}

}
