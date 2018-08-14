package controller.v1;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import annotation.action;
import config.Dictionary;
import server.ControllerContext;
import util.StringUtil;
import util.TimeUtil;
import util.ExcelUtil.ExcelMap;

/**
 * @Description 进出记录管理
 * @Author Administrator
 * @Date 2018-07-09  15:06
 * @Version 1.0
 **/
public class IorecordController extends AdminController {

    public IorecordController(ControllerContext context) {
        super(context);
        if (admin_type < DUTY) {
            pri = false;
            return;
        }
    }

    @action
    public void list() {
        toHtml("admin_tpl/record_list");
    }

    @SuppressWarnings("deprecation")
    @action
    public void getSearchList() {
        try {
            String page = I("get.page").toString();
            String limit = Integer.parseInt(page) * 10 + ",10";
            String startdate = I("get.startdate") == "" ? ""
                    : TimeUtil.dateToStamp(URLDecoder.decode(I("get.startdate").toString()), "yyyy-MM-dd HH:mm:ss");
            String enddate = I("get.enddate") == "" ? ""
                    : TimeUtil.dateToStamp(URLDecoder.decode(I("get.enddate").toString()), "yyyy-MM-dd HH:mm:ss");
            String name = I("get.name") == "" ? "" : URLDecoder.decode(I("get.name").toString());
            StringBuffer s = new StringBuffer(
                    "select * from reportrecord where 1=1");
            StringBuffer snum = new StringBuffer(
                    "select count(*) from reportrecord where 1=1");
            if (startdate != null && startdate.length() > 0 && enddate.equals("")) {
                enddate = "9999999999";
            }
            if (startdate != null && startdate.length() > 0 && enddate != null && enddate.length() > 0) {
                if (Long.parseLong(startdate) >= Long.parseLong(enddate)) {
                    error("查询结束时间必须大于开始时间，请重试");
                    return;
                } else {
                    s.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
                    snum.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
                }
            }
            if (name != null && name.length() > 0) {
                s.append(" and name like '%" + name + "%'");
                snum.append(" and name like '%" + name + "%'");
            }
            s.append(" order by dateTime desc");
            s.append(" limit " + limit);
            String sql = s.toString();
            String sql1 = snum.toString();
            try {
                String num = M("reportrecord").query(sql1).get(0).get("count(*)");
                ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
                HashMap<String, Object> res = new HashMap<>();
                HashMap<Integer, String> personTypes = new HashMap<Integer, String>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    {
                        put(Dictionary.STUDENT, "学生");
                        put(Dictionary.TEACHER, "教师");
                        put(Dictionary.PARENT, "家长");
                    }
                };
                HashMap<Integer, String> cardTypes = new HashMap<Integer, String>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    {
                        put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
                        put(Dictionary.TOUCHEDCARD, "指静脉");
                    }
                };
                HashMap<Integer, String> inoutTypes = new HashMap<Integer, String>() {
                    /**
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    {
                        put(Dictionary.ENTRANCE, "入口");
                        put(Dictionary.EXIT, "出口");
                    }
                };

                for (int i = 0; i < list.size(); i++) {
                    String pt = personTypes.get(Integer.parseInt(list.get(i).get("personType")));
                    pt = pt != null ? pt : "其它";
                    list.get(i).put("cardType", cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
                    list.get(i).put("personType", pt);
                    list.get(i).put("dateTime",
                            TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
                    list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
                }
                res.put("num", num);
                res.put("list", list);
                success(res);
            } catch (Exception e) {
                error("查询失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            error("获取进出记录失败");
        }

    }

    @SuppressWarnings("deprecation")
    @action
    public void do_excel() throws ParseException {
        String startdate = I("startdate") == null || I("startdate").equals("") ? ""
                : TimeUtil.dateToStamp(URLDecoder.decode(I("startdate").toString()), "yyyy-MM-dd HH:mm:ss");
        String enddate = I("enddate") == null || I("enddate").equals("") ? ""
                : TimeUtil.dateToStamp(URLDecoder.decode(I("enddate").toString()), "yyyy-MM-dd HH:mm:ss");
        String name = I("name") == null || I("name").equals("") ? ""
                : URLDecoder.decode(I("name").toString());
        StringBuffer s = new StringBuffer(
                "select * from reportrecord  where 1=1");
        if (startdate != null && startdate.length() > 0 && enddate.equals(""))
            enddate = "9999999999";
        if (startdate != null && startdate.length() > 0 && enddate != null && enddate.length() > 0)
            s.append(" and dateTime BETWEEN '" + startdate + "' AND '" + enddate + "'");
        if (name != null && name.length() > 0)
            s.append(" and name like '%" + name + "%'");
        s.append(" order by dateTime desc");
        String sql = s.toString();
        try {
            ArrayList<HashMap<String, String>> list = M("reportrecord").query(sql);
            HashMap<Integer, String> personTypes = new HashMap<Integer, String>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    put(Dictionary.STUDENT, "学生");
                    put(Dictionary.TEACHER, "教师");
                    put(Dictionary.PARENT, "家长");
                }
            };
            HashMap<Integer, String> cardTypes = new HashMap<Integer, String>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    put(Dictionary.UNTOUCHEDCARD, "非接触式卡");
                    put(Dictionary.TOUCHEDCARD, "指静脉");
                }
            };
            HashMap<Integer, String> inoutTypes = new HashMap<Integer, String>() {
                /**
                 *
                 */
                private static final long serialVersionUID = 1L;

                {
                    put(Dictionary.ENTRANCE, "入口");
                    put(Dictionary.EXIT, "出口");
                }
            };

            for (int i = 0; i < list.size(); i++) {
                String pt = personTypes.get(Integer.parseInt(list.get(i).get("personType")));
                pt = pt != null ? pt : "其它";
                list.get(i).put("cardType", cardTypes.get(Integer.parseInt(list.get(i).get("cardType"))));
                list.get(i).put("personType", pt);
                list.get(i).put("dateTime", TimeUtil.stampToDate(list.get(i).get("dateTime"), "yyyy-MM-dd HH:mm:ss"));
                list.get(i).put("inoutType", inoutTypes.get(Integer.parseInt(list.get(i).get("inoutType"))));
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("id", "编号");
            map.put("name", "姓名");
            map.put("cardNo", "卡号");
            map.put("cardType", "卡类型");
            map.put("personType", "人员类型");
            map.put("dateTime", "刷卡时间");
            map.put("inoutType", "进出类型");
            try {
                String res="/resourses/iorecord_"+TimeUtil.getShortTimeStamp()+".xls";
                ExcelMap.exportXls(map, list, "assets"+res);
                success(res);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
