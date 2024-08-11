package com.fabbroniko.environment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.SneakyThrows;

@Data
@AllArgsConstructor
public class Vector2D implements Cloneable {

	private double x;
	private double y;

	public Vector2D() {
		this(0, 0);
	}

	public void setVector2D(final Vector2D model) {
		this.setVector2D(model.getX(), model.getY());
	}

	public void setVector2D(final double x, final double y) {
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
	public Vector2D clone() {
		return (Vector2D) super.clone();
	}
}
