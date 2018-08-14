package controller.v1;

import annotation.action;
import server.Controller;
import server.ControllerContext;

import javax.xml.ws.Action;
import java.util.HashMap;

/**
 * @Description TODO
 * @Author 小云网络jxl
 * @Date 2018-08-11  17:40
 * @Version 1.0
 **/
public class LoginOutController extends Controller {
    public LoginOutController(ControllerContext context) {
        super(context);
    }

    @action
    public void exit(){
        String id=I("get.id").toString();
        try{
            M("session").where("id="+id).delete();
            redirect("/v1/access/login");
        }catch (Exception e){
            error("退出登录失败");
        }

    }
}
