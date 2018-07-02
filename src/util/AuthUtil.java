package util;

import java.util.HashMap;

import com.alipay.api.domain.SsdataDataserviceRiskRainscoreQueryModel;

import server.Controller;
import server.ControllerContext;

public class AuthUtil {
	
//	public static HashMap<String, String> login_user(Controller controller) {
//		return 
//	}
	
	public static int judgeAuth(Controller controller) {
		HashMap res = controller.getSession(new HashMap().getClass());
		return (int) res.get("isadmin");
	}

}
