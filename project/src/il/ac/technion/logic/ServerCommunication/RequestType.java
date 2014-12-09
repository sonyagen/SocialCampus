package il.ac.technion.logic.ServerCommunication;

/**
 * Enumeration of request types.
 */
public enum RequestType
{
	GET("GET"),
	POST("POST"),
	PUT("PUT"),
	DELETE("DELETE");
	

	private final String value;

	private RequestType(String s) {
		value = s;
	}

	public String value() {
		return value;
	}

}
