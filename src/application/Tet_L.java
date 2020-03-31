package application;

public class Tet_L extends Tetrimino{
	private static final TetLocation instanceLoc = new TetLocation(0, 1, 1, 1, 2, 1, 2, 2);
	private static final TetLocation[] posLoc = { new TetLocation(2, 0, -1, 1, 0, 0, 1, -1), // Location for Rotation calculation, there are 4 different position
	        new TetLocation(0, -2, 1, 1, 0, 0, -1, -1), new TetLocation(-2, 0, 1, -1, 0, 0, -1, 1),
			new TetLocation(0, 2, -1, -1, 0, 0, 1, 1) };
	
	public Tet_L(boolean[][] mat) {
		super(mat,posLoc,instanceLoc);
		loc = new TetLocation(0, 5, 1, 3, 1, 4, 1, 5);
		setUILocation();
		setImage("L.png");
	}
	
	public Tet_L getInstanceOfObject() {
		return new Tet_L(mat);
	}
}
