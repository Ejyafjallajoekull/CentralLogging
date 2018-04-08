package central.logging.central.logging.functionality;

public class LoggingFailureException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoggingFailureException() {
		
	}

	public LoggingFailureException(String arg0) {
		super(arg0);
	}

	public LoggingFailureException(Throwable arg0) {
		super(arg0);
	}

	public LoggingFailureException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public LoggingFailureException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
	}

}
