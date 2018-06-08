package collect;

public class CollectManager {
	
	public static int YEMEI = 1;
	public static int JINHULU = 2;
	public static int TAOHUADAO = 3;
	
	public static Collect collect;
	
	public static int current;
	
	
	public static void init(int type) throws Exception{
		current= type;
		if(type == YEMEI){
			collect = new YeMeiCollect();		
		}
		if(type==JINHULU){
			collect = new JinHuLuCollect();	
		}
		if(type==TAOHUADAO){
			collect = new TaoHuaDaoCollect();	
		}
		
	}
	
	

}
