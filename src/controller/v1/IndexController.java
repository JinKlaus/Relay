package controller.v1;

import java.util.HashMap;

import com.sun.org.apache.xml.internal.resolver.helpers.PublicId;

import annotation.action;
import server.Controller;
import server.ControllerContext;

public class IndexController extends Controller{

	public IndexController(ControllerContext context) {
		
		super(context);
		if(getSession(new HashMap<>().getClass())==null) {
			redirect("/v1/access/login");
			return;
		}
	}

	@action
	public  void  index(){
		
	}
}
