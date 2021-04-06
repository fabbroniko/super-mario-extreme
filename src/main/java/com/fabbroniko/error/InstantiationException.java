package com.fabbroniko.error;

import com.fabbroniko.gameobjects.AbstractGameObject;

/**
 * Exception thrown when the construction of an {@link AbstractGameObject AbstractGameObject} fails.
 * @author com.fabbroniko
 */
public class InstantiationException extends RuntimeException {

	private static final long serialVersionUID = -1L;
	private Class<? extends AbstractGameObject> objectClass;

	/**
	 * Constructs a new error.
	 * @param objectClassP Object Class to be constructed.
	 */
	public InstantiationException(final Class<? extends AbstractGameObject> objectClassP) {
		this.objectClass = objectClassP;
	}
	
	@Override
	public String toString() {
		return "Something went wrong trying to initialize a new " + objectClass.getSimpleName();
	}
}
