package application;

public class Tet_I extends Tetrimino {	
	private static final TetLocation instanceLoc = new TetLocation(0, 1, 1, 1, 2, 1, 3, 1);
	private static final TetLocation[] posLoc = { new TetLocation(-1, 2, 0, 1, 1, 0, 2, -1), // Location for Rotation calculation, there are 4 different position
			new TetLocation(2, 1, 1, 0, 0, -1, -1, -2), new TetLocation(1, -2, 0, -1, -1, 0, -2, 1),
			new TetLocation(-2, -1, -1, 0, 0, 1, 1, 2) };
	public Tet_I(boolean[][] mat) {
		super(mat,posLoc,instanceLoc);
		loc = new TetLocation(1, 3, 1, 4, 1, 5, 1, 6);
		setUILocation();
		setImage("I.png");
	}
	
	public Tet_I getInstanceOfObject() {
		return new Tet_I(mat);
	}

}
