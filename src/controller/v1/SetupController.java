package controller.v1;

import server.ControllerContext;

/**
 * @Description TODO
 * @Author 小云网络jxl
 * @Date 2018-07-16  18:41
 * @Version 1.0
 **/
public class SetupController extends AdminController{
    public SetupController(ControllerContext context) {
        super(context);
    }

    public void list(){
        toHtml("tpl/admin_tpl/teacher_change_selfpwd");
    }
}
