package application;

public class Tet_O extends Tetrimino{
	private static final TetLocation instanceLoc = new TetLocation(1, 1, 1, 2, 2, 1, 2, 2);
	public Tet_O(boolean[][] mat) {
		super(mat,instanceLoc);
		loc = new TetLocation(0, 4, 0, 5, 1, 4, 1, 5);
		setUILocation();
		setImage("O.png");
	}

	@Override
	public boolean rotateRight() {
		return false;
	}
	
	@Override
	public boolean rotateLeft() {
		return false;
	}

	public Tet_O getInstanceOfObject() {
		return new Tet_O(mat);
	}
}
