package error;

import java.io.Writer;

import org.beetl.core.ErrorHandler;
import org.beetl.core.exception.BeetlException;

public class WebErrorHandler implements ErrorHandler {

	@Override
	public void processExcption(BeetlException arg0, Writer arg1) {
		
			throw arg0;
			
		
	}

}
