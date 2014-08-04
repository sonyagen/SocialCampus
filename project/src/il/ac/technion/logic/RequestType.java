package il.ac.technion.logic;

/**
 * Enumeration of request types.
 */
public enum RequestType
{
	GET("GET"),
	POST("POST"),
	UPDATE("UPDATE"),
	DELETE("DELETE");

	private final String value;

	private RequestType(String s) {
		value = s;
	}

	public String value() {
		return value;
	}

}
