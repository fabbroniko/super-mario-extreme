package fabbroniko.error;

/**
 * Called when a resource is missing. 
 * @author fabbroniko
 */
public class ResourceNotFoundError extends RuntimeException {

	private final String path;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs the error. 
	 * @param pathP Resource's path.
	 */
	public ResourceNotFoundError(final String pathP) {
		super();
		this.path = pathP;
	}
	
	@Override
	public String toString() {
		return "Unable to find the file: " + path + ".";
	}
}
