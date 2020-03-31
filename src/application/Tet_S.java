package application;

public class Tet_S extends Tetrimino{
	private static final TetLocation instanceLoc = new TetLocation(1, 1, 1, 2, 2, 0, 2, 1);
	private static final TetLocation[] posLoc = { new TetLocation(1, 1, 2, 0, -1, 1, 0, 0), // Location for Rotation calculation, there are 4 different position
	        new TetLocation(1, -1, 0, -2, 1, 1, 0, 0), new TetLocation(-1, -1, -2, 0, 1, -1, 0, 0),
			new TetLocation(-1, 1, 0,  2, -1, -1, 0, 0) };
	
	public Tet_S(boolean[][] mat) {
		super(mat, posLoc,instanceLoc);
		loc = new TetLocation(0, 4, 0, 5, 1, 3, 1, 4);
		setUILocation();
		setImage("S.png");
	}
	
	public Tet_S getInstanceOfObject() {
		return new Tet_S(mat);
	}

}
