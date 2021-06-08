package com.fabbroniko.error;

public class ResourceNotFoundException extends RuntimeException {

	private final String path;
	
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(final String pathP) {
		super();
		this.path = pathP;
	}
	
	@Override
	public String toString() {
		return "Unable to find file at " + path;
	}
}
