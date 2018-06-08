package server;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import config.MysqlDB;

public class DbManager  {
	
	private static ComboPooledDataSource cpds  = new ComboPooledDataSource();
	
	   
    static{    
    	
    	try {
			cpds.setDriverClass("com.mysql.jdbc.Driver");
			cpds.setMaxPoolSize(20);
			cpds.setMaxIdleTime(60);
			cpds.setIdleConnectionTestPeriod(600);  //没60检查有没有空连接
			
		} catch (PropertyVetoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	cpds.setJdbcUrl("jdbc:mysql://"+MysqlDB.host+":"+MysqlDB.port+"/"+MysqlDB.name+"?user="+MysqlDB.user+"&password="+MysqlDB.pasword+"&characterEncoding=UTF-8&useOldAliasMetadataBehavior=true&serverTimezone=GMT%2B8");
    }    
    

        
    
        
    public static Connection  getConnection(){    
        try {
			return cpds.getConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
        return null;
 
    }    
	

}
