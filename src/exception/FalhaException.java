package exception;

public class FalhaException extends Exception {

	private static final long serialVersionUID = 1L;

	public FalhaException() {
		super();
	}
	
	public FalhaException(String message) {
		super(message);
	}
}
