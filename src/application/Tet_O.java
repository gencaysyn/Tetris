package application;

public class Tet_O extends Tetrimino{
	
	public Tet_O(boolean[][] mat) {
		super(mat);
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

}
