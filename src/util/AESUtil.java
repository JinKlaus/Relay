package util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;



public class AESUtil {
	
	 private static final String ALGORITHM = "AES"; 
	
	 /*
	     * 解密
	     * 解密过程：
	     * 1.同加密1-4步
	     * 2.将加密后的字符串反纺成byte[]数组
	     * 3.将加密内容解密
	     */
	    public static String decode(String sSrc,String sKey){
	    	try {
	            // 判断Key是否正确
	            if (sKey == null) {
	                System.out.print("Key为空null");
	                return null;
	            }
	            // 判断Key是否为16位
	            if (sKey.length() != 16) {
	                System.out.print("Key长度不是16位");
	                return null;
	            }
	            byte[] raw = sKey.getBytes("utf-8");
	            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
	            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
	            cipher.init(Cipher.DECRYPT_MODE, skeySpec);
	            byte[] encrypted1 = Base64Util.decode(sSrc.getBytes());//先用base64解密
	            try {
	                byte[] original = cipher.doFinal(encrypted1);
	                String originalString = new String(original,"utf-8");
	                return originalString;
	            } catch (Exception e) {
	                System.out.println(e.toString());
	                return "";
	            }
	        } catch (Exception ex) {
	            System.out.println(ex.toString());
	            return "";
	        }
        
	    }
	
	 public static SecretKey getKey(String strKey) {
         try {         
            KeyGenerator _generator = KeyGenerator.getInstance( "AES" );
             SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG" );
            secureRandom.setSeed(strKey.getBytes());
            _generator.init(128,secureRandom);
                return _generator.generateKey();
        }  catch (Exception e) {
             throw new RuntimeException( " 初始化密钥出现异常 " );
        }
      } 

}
