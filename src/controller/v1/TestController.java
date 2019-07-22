package controller.v1;

import annotation.action;
import server.Controller;
import server.ControllerContext;

import java.util.HashMap;


/**
 * @Description TODO
 * @Author 小云网络jxl
 * @Date 2018-07-26  16:35
 * @Version 1.0
 **/
public class TestController extends Controller {
    public TestController(ControllerContext context) {
        super(context);
    }

    @action
    public void test(){
        toHtml("admin_tpl/HD-100demo");
    }

    @action
    public void camera(){
        toHtml("admin_tpl/camera");
    }

    @action
    public void test1(){
        try {
            success(M("ss").execute("select * from sss"));
        } catch (Exception e) {
            e.printStackTrace();
            success(1);
        }

    }

    @action
    public void test2(){
        success(M("teacher").select());
    }
}
