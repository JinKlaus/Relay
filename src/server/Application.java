package server;

import java.sql.SQLException;




public class Application {
	public Application(){
		startMysql();
	}
	
	public void startMysql(){
	
			try {
				
				new com.mysql.jdbc.Driver();
				System.out.println("mysql driver success!");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			
			}
				
			
					
					
				
				
	}
	
}
