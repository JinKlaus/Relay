package config;

public class Dictionary {
	
	//personType 人员类型
	public static final int STUDENT = 10;
	public static final int TEACHER = 11;
	public static final int OTHERSTAFF = 12;
	public static final int PARENT=13;
	
	//cardTyp 卡类型
	public static final  int  UNTOUCHEDCARD=3;
	public static final  int  TOUCHEDCARD=6;
	
	//inoutType进出类型
	public static final  int ENTRANCE=1;
	public static final  int EXIT=2;
	
	//获取白名单失败错误码
	public static final String GETWHITEERROR="6010";
	
	//上报进出记录异常
	public static final String REPORTRECORDERROR="6070";
	
	//数据状态
	public static final int STATE_DELETE=0; //删除
	public static final int STATE_ADD=1;//新增
	public static final int STATE_UPDATE=2;//修改
	public static final int STATE_BLACKLIST=3;//黑名单
	
}
