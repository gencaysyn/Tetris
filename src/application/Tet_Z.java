package application;

public class Tet_Z extends Tetrimino{
	private static final TetLocation instanceLoc = new TetLocation(1, 0, 1, 1, 2, 1, 2, 2);
	private static final TetLocation[] posLoc = { new TetLocation(0, 2, 1, 1, 0, 0, 1, -1), // Location for Rotation calculation, there are 4 different position
	        new TetLocation(2, 0, 1, -1, 0, 0, -1, -1), new TetLocation(0, -2, -1, -1, 0, 0, -1, 1),
			new TetLocation(-2, 0, -1, 1, 0, 0, 1, 1) };
	
	public Tet_Z(boolean[][] mat) {
		super(mat, posLoc,instanceLoc);
		loc = new TetLocation(0, 4, 0, 5, 1, 5, 1, 6);
		setUILocation();
		setImage("Z.png");
	}
}
