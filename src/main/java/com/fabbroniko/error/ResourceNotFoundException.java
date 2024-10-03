package com.fabbroniko.error;

public class ResourceNotFoundException extends RuntimeException {

	public ResourceNotFoundException(final String resourcePath) {
		super("File located %s was not found.".formatted(resourcePath));
	}
}
