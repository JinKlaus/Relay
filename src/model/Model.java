package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import server.ControllerContext;
import server.DbManager;
import util.LogUtil;
import util.StringUtil;

public class Model {
	String table;
	String sql;
	String where;
	String field;
	String order;
	String limit;
	public boolean isActive = true;
	

	
	HashMap<String, String> data;
	
//	private Connection connection;
	
	public void setActive(boolean is_active){
		isActive=is_active;
	}
	
	public Model(String table){
		this.table = table;
	}
	
	public static Model newInstance( HashMap<String, String> data){
		return null;
		
	}
	
	/**
	 * @return the connection
	 */
	public Connection getConnection() {
		return DbManager.getConnection();
	}
	
	public Model where(String where){
		this.where = where;
		return this;
	}
	
	public Model where (HashMap<String, String> where){
		String str = "";
		 if (where != null && where.size() > 0) {  
			 this.where = this.where==null?"true":this.where;
		        for (String key : where.keySet()) {
					this.where +=" and "+key+"="+where.get(key);
				}
		        
		        
		    }
		   
	
		return this;
	}
	
	public Model field(String fields){
		this.field = fields;
		return this;
	}
	
	public Model order(String order){
		this.order = order;
		return  this;
	}
	
	public Model limit(String limit){
		this.limit = limit;
		return this;
	}
	
	public Model field(String[] fields){
		 String str = "";
		 if (fields != null && fields.length > 0) {  
		        for (int i = 0; i < fields.length; i++) {  
		            str += fields[i] + ",";  
		        } 
		        field = str.substring(0, str.length() - 1); 
		        
		    }
		   
		return this;
	}
	
	public ArrayList<HashMap<String, String>> query(String sql){
		LogUtil.log("mysql query sql--"+sql);
		this.sql = sql;
		ArrayList<HashMap<String, String>> res = new ArrayList<>();
		try {
			Connection connection = getConnection();
			java.sql.Statement stmt = connection.createStatement();
			ResultSet resultset = stmt.executeQuery(sql);
			ResultSetMetaData rsm = resultset.getMetaData();
			while (resultset.next()) {
				HashMap<String, String> map = new HashMap<>();
				for (int i = 0; i < rsm.getColumnCount(); i++) {					
			        String columnName = rsm.getColumnName( (i + 1));
			        map.put(columnName, resultset.getString(columnName));
			      }
				res.add(map);
				
			}
			resultset.close();
			stmt.close();
			connection.close();
		} catch (SQLException e) {
			
			e.printStackTrace();
			System.out.println("mysql execute error:"+e.getMessage());
		}
		
		return res;
	}
	
	public ArrayList<HashMap<String, String>> select(){
		sql = createSql();
		clear();
		ArrayList<HashMap<String, String>> list = query(sql);
		return list;
	}
	
	public int count(){
		sql = createCountSql();
		LogUtil.log(sql);
		clear();
		ArrayList<HashMap<String, String>> list = query(sql);
		return Integer.parseInt(list.get(0).get("count(*)"));
	}
	
	public HashMap<String,String> find(){
		limit = "1";
		sql = createSql();
		ArrayList<HashMap<String, String>> list = query(sql);
		LogUtil.log("mysql slect query--"+sql);
		clear();
		if(list.size() == 0) return null;
		else return list.get(0);
	}
	
	private String createSql(){
		sql="";
		if(table==null) return "";
		if(field == null) sql+="select * from "+table;
		else sql += "select "+field+" from "+table;
		if(where != null) sql+=" where "+where;
		if(order != null) sql +=" order by "+order;
		if(limit!=null) sql+=" limit "+limit;
		return sql;
	}
	
	private String createCountSql(){
		
		sql="";
		if(table==null) return "";
		if(field == null) sql+="select count(*) from "+table;
		else sql += "select "+field+" from "+table;
		if(where != null) sql+=" where "+where;
		if(order != null) sql +=" order by "+order;
		if(limit!=null) sql+=" limit "+limit;
		return sql;
	}
	
	
	public int execute(String sql){
		LogUtil.log("mysql execute query--"+sql);
		try {
			
			Connection connection=getConnection();
			java.sql.Statement stmt = connection.createStatement();
			int result = stmt.executeUpdate(sql);
			stmt.close();
			connection.close();
			return result;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}
		
		
	}
	
	public void add_s(HashMap<String, String> data) throws SQLException{
		ArrayList<String> fields = new ArrayList<>();
		ArrayList<String> neg = new ArrayList<>();

		ArrayList<Object> values = new ArrayList<>();

		for(String key:data.keySet()){
			fields.add(key);
			neg.add("?");
			values.add(data.get(key));
		}
		String sql = "INSERT INTO "+table+" ("+StringUtil.listToString(fields)+") VALUES ("+StringUtil.listToString(values)+");";
		
		LogUtil.log("mysql insert query--"+sql);
		
		  Connection connection=getConnection();
		PreparedStatement pstmt =   (PreparedStatement) connection.createStatement();
		 pstmt.executeQuery(sql);
		  pstmt.close();
		  connection.close();

	}
	
	public long add(HashMap<String, String> data) throws Exception{
		ArrayList<String> fields = new ArrayList<>();
		ArrayList<String> neg = new ArrayList<>();

		ArrayList<Object> values = new ArrayList<>();

		for(String key:data.keySet()){
			fields.add(key);
			neg.add("?");
			values.add(data.get(key));
		}
		String sql = "INSERT INTO "+table+" ("+StringUtil.listToString(fields)+") VALUES ("+StringUtil.listToString(neg)+");";
		
		LogUtil.log("mysql insert query--"+sql);
		
		  Connection connection=getConnection();
		PreparedStatement pstmt = (PreparedStatement) connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
		  
		  for(int j=0;j<values.size();j++){
			  pstmt.setString(j+1,(String) values.get(j)); 
		  }
		  pstmt.executeUpdate();
		   long autoInckey = -1;
		  ResultSet rs = pstmt.getGeneratedKeys(); 
		  if (rs.next()) {
		  autoInckey= rs.getLong(1);// 取得ID
		  } 
		  rs.close();
		  pstmt.close();
		  connection.close();
		return  autoInckey;
		
	}

	public boolean save(HashMap<String, Object> data){
		String sql = "update "+table+" set ";
		for ( String key: data.keySet()) {
			sql+=key+"='"+data.get(key).toString()+"',";
		}
		
		sql = sql.substring(0, sql.length()-1);
		if(where!=null)
		sql+=" where "+where;
		else{
			return false;
		}
		
		 return execute(sql)==0?false:true;
		
	}
	
	public boolean save_string(HashMap<String, String> data){
		String sql = "update "+table+" set ";
		for ( String key: data.keySet()) {
			sql+=key+"='"+data.get(key)+"',";
		}
		
		sql = sql.substring(0, sql.length()-1);
		if(where!=null)
		sql+=" where "+where;
		else{
			return false;
		}
		
		 return execute(sql)==0?false:true;
		
	}
	
	public int delete(){
		String sql = "delete from "+table+" where "+where;
		return execute(sql);
	}
	
	
	private void clear() {
		field = null;
		where = null;
		order = null;
		limit = null;
		
	}
	
	public String  getLastsql(){
		return this.sql;
	}	
	

}
