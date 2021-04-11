package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Dimension implements Cloneable {
	
	private final int width;
	private final int height;

	public Dimension() {
		this(0, 0);
	}
}
