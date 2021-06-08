package com.fabbroniko.error;

import com.fabbroniko.gameobjects.AbstractGameObject;

public class InstantiationException extends RuntimeException {

	private final Class<? extends AbstractGameObject> objectClass;
	private final Exception originalException;

	public InstantiationException(final Class<? extends AbstractGameObject> objectClassP, final Exception exception) {
		super();
		this.objectClass = objectClassP;
		this.originalException = exception;
	}
	
	@Override
	public String toString() {
		return "Couldn't initialize a new GameObject of type " + objectClass.getSimpleName() + ". Exception is " + originalException.getMessage();
	}
}
