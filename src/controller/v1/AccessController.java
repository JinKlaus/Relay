package controller.v1;

import java.util.HashMap;

import annotation.action;
import server.Controller;
import server.ControllerContext;
import util.Md5Util;


/**
 * @Description 后台登录
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class AccessController extends Controller {

	public AccessController(ControllerContext context) {
		super(context);
	}

	@action
	public void login() {
		toHtml("admin_tpl/staff_login");
	}

	public  void test(String s){

	}
	@action
	public void do_login() {
		String user = I("post.user").toString();
		String password = Md5Util.MD5(I("post.password").toString());
		HashMap<String, String> map = M("teacher").where("tid=" + user + " and original_pwd='" + password + "'").find();
		if (map != null) {
			try {
				session(map);
				success("1");
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			error("请输入正确的用户名密码");
		}

	}
}
