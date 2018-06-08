package controller.v1;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javax.xml.bind.helpers.ValidationEventImpl;

import com.sun.corba.se.impl.encoding.OSFCodeSetRegistry.Entry;
import com.sun.jndi.rmi.registry.ReferenceWrapper;

import annotation.action;
import collect.CollectManager;
import server.Controller;
import server.ControllerContext;
import util.AdminUtil;
import util.FileUtil;
import util.FilterUtil;
import util.TimeUtil;

public class AdminController extends Controller{

	public AdminController(ControllerContext ctx) {
		super(ctx);
	}
	
	
	void _set_dic(String k,String v){
		M("dic").where("k='"+k+"'").save(new HashMap<String,Object>(){
			{
				put("v", v);
			}
		});
	}
	
	String _get_dic(String k){
		HashMap<String, String> res = M("dic").where("k='"+k+"'").find();
		if(res==null) return "";
		return res.get("v");
	}
	
	@action
	public void login(){
		toHtml("admin_tpl/login");
	}
	
	@action
	public void do_login(){
		String admin = I("user").toString();
		String password = I("password").toString();
		if(admin.equals("admin")&&password.equals(_get_dic("admin_password"))){
			AdminUtil.setAdmin(context.getSessionId());
			success(1);
		}else{
			error("账号或密码错误");
		}
		
	}
	
	@action
	public void index(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		toHtml("admin_tpl/index");
	}
	
	@action
	public void make_card(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		toHtml("admin_tpl/make_card");
	}
	
	@action
	public void do_card(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
        String pre = I("pre").toString();
        int num = Integer.parseInt(I("num").toString());
        int type =Integer.parseInt(I("type").toString());
        if(num==0){
        	 error("数量不对");
        	 return;
        }

        if(type==0) {
        	error("请选择类型");
        	return;
        }
        String t=(TimeUtil.getShortTimeStamp()+"").substring(4, 9);
      
        for(int i=0;i<num;i++){
            int index = i;
            HashMap<String, String> res = new HashMap<String, String>(){
            	{
            		put("card_xlh", pre+t+index);
            		put("card_num", t+""+(int)(Math.random()*1000000));
            		put("create_time", TimeUtil.getShortTimeStamp()+"");
            		put("type", type+"");
            	}
            };
           
            try {
				M("card").add(res);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
        success(1);
	}
	
	@action
	public void get_card(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
	        int page = Integer.parseInt(I("page").toString())*10;
	        String limit=page+",10";
	        int num = M("card").count();
	        
	        ArrayList<HashMap<String, String>> res = M("card").limit(limit).order("id desc").select();
	        
	       
	        for(int i=0;i<res.size();i++){
	        	res.get(i).put("create_time", TimeUtil.stampToDate(res.get(i).get("create_time")));
	            
	            int type = Integer.parseInt(res.get(i).get("type"));
	            switch (type){
	                case 1:
	                	res.get(i).put("type", "周卡");
	                    break;
	                case 2:
	                	res.get(i).put("type", "月卡");
	                    break;
	                case 3:
	                	res.get(i).put("type", "季卡");
	                    break;
	                case 4:
	                	res.get(i).put("type", "年卡");
	                    break;
	                case 5:
	                	res.get(i).put("type", "永久卡");
	                    break;
	            }
	           
	        }
	        HashMap<String, Object> rHashMap =new HashMap<>();
	        rHashMap.put("list", res);
	        rHashMap.put("num", num);
	        success(rHashMap);
	}
	
	
	@action
	public void out(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
	        int type =Integer.parseInt(I("type").toString());
	        String where ="";
	        switch (type){
	            case 1:
	            	where = " and create_time>"+(TimeUtil.getShortTimeStamp()-24*3600);
	                break;
	            case 2:
	            	where = " and is_use =0";
	                break;
	            case 3:
	            	where = " and is_use=1";
	                break;
	            case 4:
	            	where ="";
	                break;
	        }


	        ArrayList<HashMap<String, String>> res = M("card").where("true "+where).select();

	        if(res.size()==0) {
	        	error("不存在该数据");
	        	return;
	        }
	        else {
	            String tim = TimeUtil.getShortTimeStamp()+"";
	            String outfile = "download/load_"+tim+".txt";
	            String content = "";
	            for(int i=0;i<res.size();i++){
	            	content+=res.get(i).get("card_num")+"\r\n";  
	            }
	            FileUtil.saveFile(System.getProperty("user.dir")+"/assets/"+outfile, content);
	           
	            success(outfile);
	        }
	}
	
	
	@action
	  public void user(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
	       toHtml("admin_tpl/user");
	    }
	
	@action
	 public void get_user(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		int page= Integer.parseInt(I("page").toString())*10;
        Object key =I("key");
        String where="true";
        if(key!=null) where="phone like '%"+key.toString()+"%'";
        String limit=page+",10";
        int num = M("user").where(where).count();
       List<HashMap<String, String>> res = M("user").where(where).limit(limit).select();
        
        for(int i=0;i<res.size();i++){
        	res.get(i).put("create_time", TimeUtil.stampToDate(res.get(i).get("create_time")));
           long last_time = Long.parseLong(res.get(i).get("vip_time"));
           if(last_time<TimeUtil.getShortTimeStamp()){
        	   res.get(i).put("vip_time", "会员已过期");
           }else{
        	   res.get(i).put("vip_time", TimeUtil.stampToDate(res.get(i).get("vip_time")));
           }
          
        }
        HashMap<String, Object> rHashMap = new HashMap<>();
        rHashMap.put("list", res);
        rHashMap.put("num", num);
  
        success(rHashMap);
    }
	
	@action
	  public void video(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
	       toHtml("admin_tpl/pl");
	    }
	
	@action
	public void get_current(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		success(CollectManager.current);
	}
	
	@action
	public void change_current(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		int id = Integer.parseInt(I("id").toString());
		
		if(id!=CollectManager.current){
			CollectManager.collect.destory();
			try {
				CollectManager.init(id);
				success(1);
			} catch (Exception e) {
				error(e.getMessage());
				e.printStackTrace();
			}
			
			
		}else{
			error("初始化失败");
		}
		
	}
	
	@action
	public void oth(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		
	
		
		
		 List<HashMap<String, String>> data=M("dic").select();
	        HashMap<String, Object> res = new HashMap<>();
	        for(int i=0;i<data.size();i++){
	            String key = data.get(i).get("k");
	            String value = data.get(i).get("v");
	            res.put(key, value);
	        }
	       assign("api",res);
	       toHtml("admin_tpl/oth");
	  
	}
	
	@action
	public void  do_oth(){
		if(!AdminUtil.is_login(context.getSessionId())){
			redirect("/v1/admin/login");
			return;
		}
		HashMap<String, String> res=context.POST;
		for (String key : res.keySet()) {
			_set_dic(key, res.get(key));
		}
       
        redirect("oth");
    }
	
	
}
