/**
 * @author SargerasWang
 */
package util.ExcelUtil;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * The <code>TestExportMap</code>	
 * 
 * @author SargerasWang
 * Created at 2014年9月21日 下午4:38:42
 */
public class ExcelMap {

  public static void exportXls( HashMap<String, String> map,List<HashMap<String, String>> list,String url) throws IOException {	 
    File f= new File(url);
    OutputStream out = new FileOutputStream(f);
    ExcelUtil.exportExcel(map,list, out );
    out.close();
  }
}
