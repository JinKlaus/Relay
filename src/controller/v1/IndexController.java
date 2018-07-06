package controller.v1;

import java.util.HashMap;


import annotation.action;
import server.Controller;
import server.ControllerContext;

public class IndexController extends AdminController{

	public IndexController(ControllerContext context) {
		super(context);
		
	}

	@action
	public  void  index(){
		String isadmin=user.get("isadmin");
		switch (isadmin) {
		case "0":
			redirect("/v1/absent/list");break;
		case "1":
			redirect("/v1/approval/list");break;
		case "2":
			redirect("/v1/student/list");break;
		case "3":
			redirect("/v1/student/list");break;
		default:
			break;
		}
	}
}
