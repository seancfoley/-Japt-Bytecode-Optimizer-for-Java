package java.lang;

public class IncompatibleClassChangeError extends LinkageError {
	public IncompatibleClassChangeError() {}
	
	public IncompatibleClassChangeError(String detailMessage) {
		super(detailMessage);
	}
}
