package controller.v1;

import annotation.action;
import com.alibaba.fastjson.JSON;
import config.Dictionary;
import server.ControllerContext;
import util.Md5Util;
import util.StringUtil;
import util.TimeUtil;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @Description 家长管理
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
@SuppressWarnings("ALL")
public class ParentController extends AdminController {

    public ParentController(ControllerContext context) {
        super(context);
        if (admin_type < DUTY) {
            pri = false;
            return;
        }
    }

    @action
    public void list() {
        toHtml("admin_tpl/parent_list");
    }

    @action
    public void getList() {
        String page = I("page").toString();
        String limit = Integer.parseInt(page) * 10 + ",10";
        HashMap<String, Object> res = new HashMap<>();
        @SuppressWarnings("deprecation")
        String key = StringUtil.isEmpty(I("key")) ? "" : URLDecoder.decode(I("key").toString());
        try {
            if (key != null) {
                if (StringUtil.isNumeric(key)) {
                    String sql1 = "SELECT count(*) FROM parent where tel  like '%" + key + "%'";
                    String sql = "select a.*,c.name as stu_name,b.name as clazz_name from parent a left join student c on a.stu_id=c.id left join clazz b on c.clazz_id=b.id where tel like '%"
                            + key + "%' limit " + limit;
                    ArrayList<HashMap<String, String>> list1 = M("parent").query(sql1);
                    String num = list1.get(0).get("count(*)");
                    ArrayList<HashMap<String, String>> list = M("parent").query(sql);
                    res.put("num", num);
                    res.put("list", list);
                    success(res);
                } else {
                    String sql1 = "select count(*),a.*,c.name as stu_name from parent a left join student c on a.stu_id=c.id  where c.name like '%"
                            + key + "%'";
                    String sql = "select a.*,c.name as stu_name,b.name as clazz_name from parent a left join student c on a.stu_id=c.id left join clazz b on c.clazz_id=b.id where c.name like '%"
                            + key + "%' limit " + limit;
                    ArrayList<HashMap<String, String>> list1 = M("parent").query(sql1);
                    String num = list1.get(0).get("count(*)");
                    ArrayList<HashMap<String, String>> list = M("parent").query(sql);
                    res.put("num", num);
                    res.put("list", list);
                    success(res);
                }
            } else {
                int num = M("parent").count();
                String sql = "select a.*,c.name as stu_name,b.name as clazz_name from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id limit "
                        + limit;
                ArrayList<HashMap<String, String>> list = M("parent").query(sql);
                res.put("num", num);
                res.put("list", list);
                success(res);
            }
            return;
        } catch (Exception e) {
            error("0");
        }

    }

    @action
    public void add() {
        toHtml("admin_tpl/parent_crud");
    }

    @action
    public void do_add() {
        String veinData1, veinData2, veinData3, stu_id, par_name, address, tel, cardNo, scenePhoto;
        veinData1 = StringUtil.isEmpty(I("post.image1")) ? "" : I("post.image1").toString();
        veinData2 = StringUtil.isEmpty(I("post.image2")) ? "" : I("post.image2").toString();
        veinData3 = StringUtil.isEmpty(I("post.image3")) ? "" : I("post.image3").toString();
        cardNo = StringUtil.isEmpty(I("post.cardNo")) ? "" : I("post.cardNo").toString();
        scenePhoto = StringUtil.isEmpty(I("post.scenePhoto")) ? "" : I("post.scenePhoto").toString();
        try {
            stu_id = I("post.stu_id").toString();
            par_name = I("post.par_name").toString();
            address = I("post.address").toString();
            tel = I("post.tel").toString();
        } catch (Exception e) {
            error("0");
            return;
        }
        ArrayList<HashMap<String, String>> list = M("clazz").query(
                "select a.*,b.id as stu_id from clazz a left join student b on a.id=b.clazz_id where b.id=" + stu_id);
        HashMap<String, String> date = list.get(0);
        String startDate = date.get("start_date");
        String endDate = date.get("end_date");
        HashMap<String, String> par = new HashMap<>();
        HashMap<String, String> user = new HashMap<>();
        user.put("startDate", startDate);
        user.put("endDate", endDate);
        user.put("name", par_name);
        user.put("personType", Dictionary.PARENT + "");
        user.put("veinData1", veinData1);
        user.put("veinData2", veinData2);
        user.put("veinData3", veinData3);
        user.put("create_time", TimeUtil.getShortTimeStamp() + "");
        user.put("update_time", TimeUtil.getLongTimeStamp() + "");
        user.put("state", Dictionary.STATE_ADD + "");
        user.put("passType", "11111");
        user.put("cardNo", cardNo);
        user.put("scenePhoto", scenePhoto);
        par.put("stu_id", stu_id);
        par.put("par_name", par_name);
        par.put("address", address);
        par.put("tel", tel);
        par.put("create_time", TimeUtil.getShortTimeStamp() + "");
        try {
            long id = M("user").add(user);
            par.put("uid", id + "");
            long face_id = M("parent").add(par);
            HashMap<String, String> updateData = new HashMap<>();
            updateData.put("face_id", face_id + "");
            M("user").where("id=" + id).save_string(updateData);
            success("1");
        } catch (Exception e) {
            error("0");
        }
    }

    @action
    public void remove() {
        String id = I("get.id").toString();
        String sql = "select uid from parent where id=" + id;
        ArrayList<HashMap<String, String>> list = M("parent").query(sql);
        HashMap<String, String> map = list.get(0);
        HashMap<String, String> data = new HashMap<>();
        String uid = map.get("uid");
        data.put("state", "0");
        data.put("update_time", TimeUtil.getLongTimeStamp() + "");
        try {
            M("parent").where("id=" + id).delete();
            M("user").where("id=" + uid).save_string(data);
            success("1");
        } catch (Exception e) {
            error("0");
        }
    }

    @action
    public void edit_info() {
        String id = I("get.id").toString();
        String sql = "select a.*,b.id as clazz_id,d.cardNo from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id left join user d on a.uid=d.id  where a.id="
                + id;
        ArrayList<HashMap<String, String>> list = M("parent").query(sql);
        HashMap<String, String> res = list.get(0);
        assign("info", JSON.toJSON(res));
        toHtml("admin_tpl/parent_updateInfo");
    }

    @action
    public void edit_vena() {
        String id = I("get.id").toString();
        String sql = "select a.*,b.id as clazz_id from parent a left join student c on a.stu_id=c.id LEFT JOIN clazz b on c.clazz_id=b.id where a.id="
                + id;
        ArrayList<HashMap<String, String>> list = M("parent").query(sql);
        HashMap<String, String> res = list.get(0);
        assign("info", JSON.toJSON(res));
        toHtml("admin_tpl/parent_updateVena");
    }

    @action
    public void do_edit_info() {
        String id, stu_id, par_name, address, tel, uid, cardNo;
        try {
            id = I("post.id").toString();
            stu_id = I("post.stu_id").toString();
            par_name = I("post.par_name").toString();
            address = I("post.address").toString();
            tel = I("post.tel").toString();
            uid = I("post.uid").toString();
            cardNo = I("post.cardNo").toString();
        } catch (Exception e) {
            error("修改失败");
            return;
        }
        HashMap<String, String> par = new HashMap<>();
        HashMap<String, String> user = new HashMap<>();
        par.put("par_name", par_name);
        par.put("address", address);
        par.put("tel", tel);
        par.put("stu_id", stu_id);
        user.put("name", par_name);
        user.put("state", "2");
        user.put("update_time", TimeUtil.getLongTimeStamp() + "");
        user.put("cardNo", cardNo);
        try {
            M("parent").where("id=" + id).save_string(par);
            M("user").where("id=" + uid).save_string(user);
            success("数据库更新成功");
        } catch (Exception e) {
            error("数据库更新失败");
        }
    }

    @action
    public void do_edit_vena() {
        String veinData1, veinData2, veinData3, uid;
        try {
            veinData1 = I("post.image1").toString();
            veinData2 = I("post.image2").toString();
            veinData3 = I("post.image3").toString();
            uid = I("post.uid").toString();
        } catch (Exception e) {
            error("0");
            return;
        }
        HashMap<String, String> user = new HashMap<>();
        user.put("state", "2");
        user.put("update_time", TimeUtil.getLongTimeStamp() + "");
        user.put("veinData1", veinData1);
        user.put("veinData2", veinData2);
        user.put("veinData3", veinData3);
        try {
            M("user").where("id=" + uid).save_string(user);
            success("数据库更新成功");
        } catch (Exception e) {
            error("数据库更新失败");
        }

    }

    @action
    public void getClazzInfo() {
        String sql = "select id,name from clazz";
        ArrayList<HashMap<String, String>> list = M("clazz").query(sql);
        success(list);
    }

    @action
    public void getStudentInfo() {
        String clazz_id = I("get.clazz_id").toString();
        String sql = "select id,name from student where clazz_id=" + clazz_id;
        ArrayList<HashMap<String, String>> list = M("student").query(sql);
        success(list);
    }

}
