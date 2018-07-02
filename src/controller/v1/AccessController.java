package controller.v1;

import java.util.HashMap;

import annotation.action;
import server.Controller;
import server.ControllerContext;

public class AccessController extends Controller{

	public AccessController(ControllerContext context) {
		super(context);
	}
	
	@action
	public void test() {
		HashMap<String, String> res  = getSession(new HashMap().getClass());
		success(sessionID);
	}
	
	
	@action
	public void login() {
		toHtml("admin_tpl/staff_login");
	}
	
	@action
	public void do_login() {
		String user=I("post.user").toString();
		String password=I("post.password").toString();
		HashMap<String, String> map= M("teacher").where("user="+user+" and password="+password).find();
		if(map != null) {
			try {
				session(map);
				success("1");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else {
			error("请输入正确的用户名密码");
		}
	
		
		
		
	}
}
