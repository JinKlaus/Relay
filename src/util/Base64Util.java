package util;

import java.io.FileOutputStream;
import java.io.OutputStream;

import com.alipay.api.internal.util.codec.Base64;

import sun.misc.BASE64Decoder;



public class Base64Util {
	
	static BASE64Decoder decoder = new BASE64Decoder();
	  /** 
     * @param bytes 
     * @return 
     */  
    public static byte[] decode(final byte[] bytes) {  
        return Base64.decodeBase64(bytes);  
    }  
  
    /** 
     * 二进制数据编码为BASE64字符串 
     * 
     * @param bytes 
     * @return 
     * @throws Exception 
     */  
    public static String encode(final byte[] bytes) {  
        return new String(Base64.encodeBase64(bytes));  
    }  
    
    public static String decodeString(String s) throws Exception{
   
    	
			return new String(decoder.decodeBuffer(s), "gbk");
		
    }
    /**
     * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片
     * @author 
     * @Date 
     * @param base64 图片Base64数据
     * @param path 图片路径
     * @return
     */
    public static boolean base64ToImage(String base64, String path) {// 对字节数组字符串进行Base64解码并生成图片
        if (base64 == null){ // 图像数据为空
            return false;
        }
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            // Base64解码
            byte[] bytes = decoder.decodeBuffer(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {// 调整异常数据
                    bytes[i] += 256;
                }
            }
            // 生成jpeg图片
            OutputStream out = new FileOutputStream(path);
            out.write(bytes);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
}
