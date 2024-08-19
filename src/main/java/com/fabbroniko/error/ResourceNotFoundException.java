package com.fabbroniko.error;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(final String resourcePath) {
		super("Unable to find file at " + resourcePath);
	}
}
