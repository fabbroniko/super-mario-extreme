package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
public class Vector2D implements Cloneable, Position {

	private double x;
	private double y;

	public Vector2D() {
		this(0, 0);
	}

	@Override
	public void setPosition(final Position position) {
		this.setPosition(position.getX(), position.getY());
	}

	@Override
	public void setPosition(final double x, final double y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int getRoundedX() {
		return (int)Math.round(x);
	}

	@Override
	public int getRoundedY() {
		return (int)Math.round(y);
	}

	@SneakyThrows
	@Override
	public Vector2D clone() {
		return (Vector2D) super.clone();
	}
}
