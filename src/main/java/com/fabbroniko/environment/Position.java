package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
public class Position implements Cloneable {

	private int x;
	private int y;
	
	public Position() {
		this(0, 0);
	}

	public void setPosition(final Position position) {
		this.setPosition(position.getX(), position.getY());
	}

	public void setPosition(final int x, final int y) {
		this.x = x;
		this.y = y;
	}

	@SneakyThrows
	@Override
	public Position clone() {
		return (Position) super.clone();
	}
}
