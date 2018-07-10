package controller.v1;

import java.util.HashMap;

import annotation.action;
import server.Controller;
import server.ControllerContext;
import util.Md5Util;
import util.TimeUtil;


/**
 * @Description 客户端权限控制
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class AuthController extends Controller {

	public AuthController(ControllerContext context) {
		super(context);
	}

	@SuppressWarnings("serial")
	@action
	public void login() {
		String username, password;
		username = I("post.user").toString();
		password = Md5Util.MD5(I("post.password").toString());
		HashMap<String, String> map = M("parent").where("tel='" + username + "' and original_pwd='" + password + "'")
				.find();
		if (map != null) {
			try {
				String token = Md5Util.MD5(map.get("id") + TimeUtil.getLongTimeStamp());
				M("parent").where("id=" + map.get("id")).save(new HashMap<String, Object>() {
					{
						put("token", token);
					}
				});
				success(token);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			error("请输入正确的用户名密码");
		}
	}

}
