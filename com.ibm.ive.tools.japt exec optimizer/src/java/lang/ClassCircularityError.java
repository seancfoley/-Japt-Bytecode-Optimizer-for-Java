package java.lang;


public class ClassCircularityError extends LinkageError {
	public ClassCircularityError() {}
	
	public ClassCircularityError(String detailMessage) {
		super(detailMessage);
	}
}
