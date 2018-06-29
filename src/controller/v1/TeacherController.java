package controller.v1;

import annotation.action;
import server.Controller;
import server.ControllerContext;

public class TeacherController extends Controller{

	public TeacherController(ControllerContext context) {
		super(context);
	}
	
	@action
	public void list() {
		toHtml("admin_tpl/teacher_list");
	}
	
	@action 
	public void add() {
		toHtml("admin_tpl/teacher_crud");
	}

}
