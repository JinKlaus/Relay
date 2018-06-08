package util;

import java.util.ArrayList;

import util.FilterUtil.FilterHandler;

public class FilterUtil {
	
	public interface FilterHandler{
		public String doFilter(String res);
	}
	/**
	 * 获取html过滤器
	 * @return
	 */
	
	public static FilterHandler getHtmlHandler(){
		return new FilterHandler() {
			
			@Override
			public String doFilter(String res) {
				// TODO Auto-generated method stub
				return res;
			}
		};
	}
	
	
	
	/**
	 * 获取防sql注入过滤器
	 * @return
	 */
	
	public static FilterHandler getSqlHandler(){
		return new FilterHandler() {
			
			@Override
			public String doFilter(String res) {
				
				
				
				return res;
			}
		};
	}


	public static String filter(String param, ArrayList<FilterHandler> handlers) {
		for(int i=0;i<handlers.size();i++){
			param = handlers.get(i).doFilter(param);
		}
		return param;
	}
	

}
