package application;

// Basic class to store tetrimino's square's locations
public class TetLocation {
	private int Ax;
	private int Ay;
	private int Bx;
	private int By;
	private int Cx;
	private int Cy;
	private int Dx;
	private int Dy;

	

	public TetLocation(int ax, int ay, int bx, int by, int cx, int cy, int dx, int dy) {
		Ax = ax;
		Ay = ay;
		Bx = bx;
		By = by;
		Cx = cx;
		Cy = cy;
		Dx = dx;
		Dy = dy;
	}
	public void moveRight() {
			Ay++;
			By++;
			Cy++;
			Dy++;
	}
	public void moveLeft() {
			Ay--;
			By--;
			Cy--;
			Dy--;
	}

	public void moveDown() {
			Ax++;
			Bx++;
			Cx++;
			Dx++;
	}

	public int getAx() {
		return Ax;
	}

	public void setAx(int Ax) {
		this.Ax = Ax;
	}

	public int getBx() {
		return Bx;
	}

	public void setBx(int Bx) {
		this.Bx = Bx;
	}

	public int getCx() {
		return Cx;
	}

	public void setCx(int Cx) {
		this.Cx = Cx;
	}

	public int getDx() {
		return Dx;
	}

	public void setDx(int Dx) {
		this.Dx = Dx;
	}

	public int getAy() {
		return Ay;
	}

	public void setAy(int Ay) {
		this.Ay = Ay;
	}

	public int getBy() {
		return By;
	}

	public void setBy(int By) {
		this.By = By;
	}

	public int getCy() {
		return Cy;
	}

	public void setCy(int Cy) {
		this.Cy = Cy;
	}

	public int getDy() {
		return Dy;
	}

	public void setDy(int Dy) {
		this.Dy = Dy;
	}

}
