package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
public class Position implements Cloneable {

	private double x;
	private double y;
	
	public Position() {
		this(0, 0);
	}

	public void setPosition(final Position position) {
		this.setPosition(position.getX(), position.getY());
	}

	public void setPosition(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	public int getRoundedX() {
		return (int)Math.round(x);
	}

	public int getRoundedY() {
		return (int)Math.round(y);
	}

	@SneakyThrows
	@Override
	public Position clone() {
		return (Position) super.clone();
	}
}
