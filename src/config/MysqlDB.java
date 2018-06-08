package config;

import config.Entrance;

public class MysqlDB {
	public static String drive = "com.mysql.jdbc.Driver";
	public static String host = Entrance.mysql_host;
	public static String name = Entrance.mysql_name;
	public static String user = Entrance.mysql_user;
	public static String pasword = Entrance.mysql_pasword;
	public static String port = "3306";
	public static String charset = "utf8mb4";

}
