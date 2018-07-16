package controller.v1;

import annotation.action;
import server.ControllerContext;
import util.Md5Util;

import java.util.HashMap;

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

    @action
    public void list(){
        toHtml("admin_tpl/teacher_change_selfpwd");
    }

    @action
    public void change(){
        try {
            String new_pwd = I("post.new_pwd").toString();
            HashMap<String,String> map=new HashMap<>();
            map.put("original_pwd",Md5Util.MD5(new_pwd));
            M("teacher").where("id="+user.get("id")).save_string(map);
            success("修改登录密码成功");
        }catch (Exception e){
            error("修改登录密码失败");
        }
    }
}
