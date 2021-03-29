package fabbroniko.error;

/**
 * Error thrown if there is an unknown tile.
 * @author fabbroniko
 */
public class TileTypeError extends RuntimeException {

	private final int selectedIndex;
	
	private static final long serialVersionUID = 1L;
	 
	/**
	 * Constructs a new error.
	 * @param selectedIndexP Tile's index.
	 */
	public TileTypeError(final int selectedIndexP) {
		super();
		this.selectedIndex = selectedIndexP;
	}
	
	@Override
	public String toString() {
		return "No such TileType with " + selectedIndex + " as index.";
	}
}
