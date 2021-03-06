package application;

public class Tet_T extends Tetrimino{
	private static final TetLocation instanceLoc = new TetLocation(1, 1, 2, 0, 2, 1, 2, 2);
	private static final TetLocation[] posLoc = { new TetLocation(1, 1, -1, 1, 0, 0, 1, -1), // Location for Rotation calculation, there are 4 different position
			new TetLocation(1, -1, 1, 1, 0, 0, -1, -1), new TetLocation(-1, -1, 1, -1, 0, 0, -1, 1),
			new TetLocation(-1, 1, -1, -1, 0, 0, 1, 1) };
	
	public Tet_T(boolean[][] mat) {
		super(mat,posLoc,instanceLoc);
		loc = new TetLocation(0, 4, 1, 3, 1, 4, 1, 5);
		setUILocation();
		setImage("T.png");
	}
	
	public Tet_T getInstanceOfObject() {
		return new Tet_T(mat);
	}

}
