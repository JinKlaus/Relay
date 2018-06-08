/**
 * 
 */
package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


/**
 * @author liuzr
 * @date 2017年7月3日  新建  
 */
public class TimeUtil {
	

	public static String getTime() {  
		return String.valueOf(System.currentTimeMillis()).substring(0,10);
	}
	
	//时间戳转成日期
	public static String stampToDate(String s){
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年 MM月 dd日");
		 long lt = new Long(s+"000");
	        Date date = new Date(lt);
	        String res = simpleDateFormat.format(date);
	        return res;
	}
	
	
	public static String getToday(){
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd");
		 long lt = System.currentTimeMillis();
		 Date date = new Date(lt);
		 String res = simpleDateFormat.format(date);
	        return res;
	}
	
	public static String stampToMysqlDate(String s){
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 long lt = new Long(s+"000");
	        Date date = new Date(lt);
	        String res = simpleDateFormat.format(date);
	        return res;
	}
	
	//时间戳转日期-自定义格式
	public static String stampToDate(String s,String format){
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		 long lt = new Long(s+"000");
	        Date date = new Date(lt);
	        String res = simpleDateFormat.format(date);
	        return res;
	}
	
    /* 
     * 将时间转换为时间戳
     */    
    public static String dateToStamp(String s, String format) throws ParseException{
        String res;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts).substring(0,10);
        return res;
    }
    
    public static long getLongTimeStamp(){
		return System.currentTimeMillis();
	}
	
	public static int getShortTimeStamp(){
		long long_timestamp = System.currentTimeMillis();
		return (int) (long_timestamp/1000);
	}
	/**
	 * 获取当前时分秒时间戳
	 * @return
	 */
	public static int getHourMinuteSecond(){
		Calendar now = Calendar.getInstance();  
		return now.get(Calendar.HOUR_OF_DAY)*3600+now.get(Calendar.MINUTE)*60+now.get(Calendar.SECOND);
	}
	
	
}
