package exception;

public class ParamsErrorException extends Exception {
	private static final long serialVersionUID = 8003;

	public ParamsErrorException(String message) {
        super(message);
        
    }
}
