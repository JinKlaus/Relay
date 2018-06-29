package util;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {

	
    /** 
     * @Description:把数组转换为一个用逗号分隔的字符串 ，以便于用in+String 查询 
     * @param String[] 数组字符串
     * @return String
     */  
    public static String converToString(String[] ig) {  
        String str = "";  
        if (ig != null && ig.length > 0) {  
            for (int i = 0; i < ig.length; i++) {  
                str += ig[i] + ",";  
            }  
        }  
        str = str.substring(0, str.length() - 1);  
        return str;  
    }  
      
    /** 
     * @Description:把list转换为一个用逗号分隔的字符串 
     */  
    public static String listToString(List list) {  
    	return listToString(list, ",");
    }
    
    public static String listToString(List list, String item_separator) {
        StringBuilder sb = new StringBuilder();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (i < list.size() - 1) {  
                    sb.append(list.get(i) + item_separator);  
                } else {  
                    sb.append(list.get(i));  
                }  
            }  
        }  
        return sb.toString(); 
    }
    
    public static String listToString2(List list) {  
        StringBuilder sb = new StringBuilder();  
        if (list != null && list.size() > 0) {  
            for (int i = 0; i < list.size(); i++) {  
                if (i < list.size() - 1) {  
                    sb.append(list.get(i) + "、");  
                } else {  
                    sb.append(list.get(i));  
                }  
            }  
        }  
        return sb.toString();  
    } 
    
    public static boolean isEmpty(String s){
    	if(s==null || s.isEmpty()) return true;
    	else return false;
    }
    
    public static boolean isLetter(char c) {   
        int k = 0x80;   
        return c / k == 0 ? true : false;   
    }  
   
 /** 
  * 判断字符串是否为空 
  * @param str 
  * @return 
  */  
 public static boolean isNull(String str){  
     if(str==null||str.trim().equals("")||str.trim().equalsIgnoreCase("null")){  
         return true;  
     }else{  
         return false;  
     }  
 }  
   
 /**  
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为2,英文字符长度为1  
     * @param String s 需要得到长度的字符串  
     * @return int 得到的字符串长度  
     */   
    public static int length(String s) {  
        if (s == null)  
            return 0;  
        char[] c = s.toCharArray();  
        int len = 0;  
        for (int i = 0; i < c.length; i++) {  
            len++;  
            if (!isLetter(c[i])) {  
                len++;  
            }  
        }  
        return len;  
    }  
   
      
    /**  
     * 得到一个字符串的长度,显示的长度,一个汉字或日韩文长度为1,英文字符长度为0.5  
     * @param String s 需要得到长度的字符串  
     * @return int 得到的字符串长度  
     */   
    public static double getLength(String s) {  
     double valueLength = 0;    
        String chinese = "[\u4e00-\u9fa5]";    
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1    
        for (int i = 0; i < s.length(); i++) {    
            // 获取一个字符    
            String temp = s.substring(i, i + 1);    
            // 判断是否为中文字符    
            if (temp.matches(chinese)) {    
                // 中文字符长度为1    
                valueLength += 1;    
            } else {    
                // 其他字符长度为0.5    
                valueLength += 0.5;    
            }    
        }    
        //进位取整    
        return  Math.ceil(valueLength);    
    } 
    
    public static boolean isNumeric(String str){
    	  for (int i = 0; i < str.length(); i++){
    	   if (!Character.isDigit(str.charAt(i))){
    	    return false;
    	   }
    	  }
    	  return true;
    	 }
    /**
     * 将文本中的英文双引号改成中文双引号
     * @author  jing
     * @date 2017年3月1日  新建  
     * @param content
     * @return
     */
    public static String processQuotationMarks(String content) {
		String regex = "\"([^\"]*)\"";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);

		String reCT = content;

		while (matcher.find()) {
			String itemMatch = "“" + matcher.group(1) + "”";
			reCT = reCT.replace("\"" + matcher.group(1) + "\"", itemMatch);
		}

		return reCT;
	}
    /**
     * 截取中文
     * @author  jing
     * @date 2017年3月9日  新建  
     * @param str
     * @param maxLength
     * @return
     */
    public static String subStringCN(final String str, final int maxLength) {
		if (str == null) {
			return str;
		}
		String suffix = "...";
		int suffixLen = suffix.length();
		
		final StringBuffer sbuffer = new StringBuffer();
		final char[] chr = str.trim().toCharArray();
		int len = 0;
		for (int i = 0; i < chr.length; i++) {
			
			if (chr[i] >= 0xa1) {
				len += 2;
			} else {
				len++;
			}
		}
		
		if(len<=maxLength){
			return str;
		}
		
		len = 0;
		for (int i = 0; i < chr.length; i++) {
 
			if (chr[i] >= 0xa1) {
				len += 2;
				if (len + suffixLen > maxLength) {
					break;
				}else {
					sbuffer.append(chr[i]);
				}
			} else {
				len++;
				if (len + suffixLen > maxLength) {
					break;
				}else {
					sbuffer.append(chr[i]);
				}
			}
		}
		sbuffer.append(suffix);
		return sbuffer.toString();
	}

    /**
     * 阿拉伯数字转换为中文数字
     * @author  jing
     * @date 2017年3月17日  新建  
     * @param intInput
     * @return
     */
    public static String ToCH(int intInput) {  
        String si = String.valueOf(intInput);  
        String sd = "";  
        if (si.length() == 1) // 個  
        {  
        	if(!si.equals("0")) {
        		sd += GetCH(intInput);  
        	}
            
            return sd;  
        } else if (si.length() == 2)// 十  
        {  
            if (si.substring(0, 1).equals("1"))
            	sd += "十";  
            else  
                sd += (GetCH(intInput / 10) + "十");  
            sd += ToCH(intInput % 10);  
        } else if (si.length() == 3)// 百  
        {  
            sd += (GetCH(intInput / 100) + "百");  
            if (String.valueOf(intInput % 100).length() < 2 && !String.valueOf(intInput % 100).equals("0"))  
                sd += "零";  
            sd += ToCH(intInput % 100);  
        } else if (si.length() == 4)// 千  
        {  
            sd += (GetCH(intInput / 1000) + "千");  
            if (String.valueOf(intInput % 1000).length() < 3 && !String.valueOf(intInput % 1000).equals("0"))  
                sd += "零";  
            if((intInput % 1000 >= 10) && (intInput % 1000 <= 19)) {
            	sd += "一";
            }
            sd += ToCH(intInput % 1000);  
        } else if (si.length() == 5)// 萬  
        {  
            sd += (GetCH(intInput / 10000) + "万");  
            if (String.valueOf(intInput % 10000).length() < 4 && !String.valueOf(intInput % 10000).equals("0"))  
                sd += "零";  
            if((intInput % 10000 >= 10) && (intInput % 10000 <= 19)) {
            	sd += "一";
            }
            sd += ToCH(intInput % 10000);  
        }  
  
        return sd;  
    }  
  
    private static String GetCH(int input) {  
        String sd = "";  
        switch (input) {  
        case 0:  
            sd = "零";  
            break; 
        case 1:  
            sd = "一";  
            break;  
        case 2:  
            sd = "二";  
            break;  
        case 3:  
            sd = "三";  
            break;  
        case 4:  
            sd = "四";  
            break;  
        case 5:  
            sd = "五";  
            break;  
        case 6:  
            sd = "六";  
            break;  
        case 7:  
            sd = "七";  
            break;  
        case 8:  
            sd = "八";  
            break;  
        case 9:  
            sd = "九";  
                break;  
            default:  
                break;  
            }  
            return sd;  
        } 
    
}
