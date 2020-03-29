package application;

public class Tet_J extends Tetrimino{
	private static final TetLocation[] posLoc = { new TetLocation(0, 2, -1, 1, 0, 0, 1, -1), // Location for Rotation calculation, there are 4 different position
	        new TetLocation(2, 0, 1, 1, 0, 0, -1, -1), new TetLocation(0, -2, 1, -1, 0, 0, -1, 1),
			new TetLocation(-2, 0, -1, -1, 0, 0, 1, 1) };
	
	public Tet_J(boolean[][] mat) {
		super(mat, posLoc);
		loc = new TetLocation(0, 3, 1, 3, 1, 4, 1, 5);
		setUILocation();
		setImage("J.png");
	}

}
