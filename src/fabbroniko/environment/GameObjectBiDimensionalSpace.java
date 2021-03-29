package fabbroniko.environment;

import java.awt.Rectangle;

public class GameObjectBiDimensionalSpace {
	
	private Position position;
	private Dimension dimension;
	private Rectangle rectangle;
	
	public GameObjectBiDimensionalSpace(final Position position, final Dimension dimension) {
		this.position = position;
		this.dimension = dimension;
		this.rectangle = new Rectangle();
		updateRectangle();
	} 
	
	public Position getPosition(){
		return new Position(this.position);
	}
	
	public Dimension getDimension() {
		return new Dimension(this.dimension);
	}
	
	public void setPosition(final Position position) {
		this.position = position;
		updateRectangle();
	}
	
	public void movePosition(final Position offsetPosition){
		this.position.setPosition(this.position.getX() + offsetPosition.getX(), this.position.getY() + offsetPosition.getY());
		updateRectangle();
	}
	
	public void setDimension(final Dimension dimension) {
		this.dimension = dimension;
		updateRectangle();
	}
	
	private void updateRectangle() {
		this.rectangle.setBounds(position.getX(), position.getY(), dimension.getWidth(), dimension.getHeight());
	}
	
	public Rectangle getRectangle() {
		return this.rectangle;
	}
	
	@Override
	public boolean equals(final Object o) {
		if(o == null || (!(o instanceof GameObjectBiDimensionalSpace) && !(o instanceof Rectangle))){
			return false;
		}
		if(o instanceof GameObjectBiDimensionalSpace){
			final GameObjectBiDimensionalSpace obj = (GameObjectBiDimensionalSpace)o;
			return this.getRectangle().intersects(obj.getRectangle());
		}
		
		final Rectangle obj2 = (Rectangle)o;
		return this.getRectangle().intersects(obj2);
	}

	public void setPosition(int i, int j) {
		setPosition(new Position(i, j));
	}
}