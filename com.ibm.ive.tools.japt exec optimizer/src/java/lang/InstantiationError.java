package java.lang;


public class InstantiationError extends IncompatibleClassChangeError {
	public InstantiationError () {
		super();
	}
	
	public InstantiationError (String detailMessage) {
		super(detailMessage);
	}
}
