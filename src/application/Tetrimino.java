package application;

import java.net.URISyntaxException;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Tetrimino extends Rectangle {
	protected int position = 0; // stores position information of tetrimino each tetrimino has 4 different position except tetrimino "O"
	protected TetLocation loc; // stores tetriminos location
	protected TetLocation instanceLoc; // location for showing next tetrimino panel
	protected Rectangle A, B, C, D; // each square of tetrimino
	protected boolean[][] mat; // matrix to manage collisions (drops,borders, etc.)
	protected TetLocation[] posLoc; // stores tetriminos positions locations to change position one to other
	protected boolean isDropped = false; // to make it trade safe if you press long to down button it trying to drop two times

			// Constructor tetrimino GUI config
	public Tetrimino(boolean[][] mat,TetLocation instanceLoc) {
		this.mat = mat;
		this.instanceLoc = instanceLoc;
		A = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		B = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		C = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		D = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		setStrokeColorAll(Color.web("#dcdde1"));
	}

	// Constructor to manage variables easily in child classes
	public Tetrimino(boolean[][] mat, TetLocation[] posLoc, TetLocation instanceLoc) {
		this.instanceLoc = instanceLoc;
		this.mat = mat;
		this.posLoc = posLoc;
		A = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		B = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		C = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		D = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		setStrokeColorAll(Color.web("#dcdde1"));
	}

	public Tetrimino(TetLocation instanceLoc) {
		A = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		B = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		C = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		D = new Rectangle(Config.RECTANGLE_EDGE, Config.RECTANGLE_EDGE);
		setStrokeColorAll(Color.web("#dcdde1"));
		loc = instanceLoc;
		setUILocation();
		setImage("S.png");
	}
	
	public Rectangle getA() {
		return A;
	}

	public Rectangle getB() {
		return B;
	}

	public Rectangle getC() {
		return C;
	}

	public Rectangle getD() {
		return D;
	}

	//////// Graphics move functions//////

	// checks if is it in border and move it left
	public boolean moveRight() {
		if ((loc.getAy() < Config.COLUMN - 1 && loc.getBy() < Config.COLUMN - 1
				&& loc.getCy() < Config.COLUMN - 1 && loc.getDy() < Config.COLUMN - 1)
				&& (mat[loc.getAx()][loc.getAy() + 1] && mat[loc.getBx()][loc.getBy() + 1]
						&& mat[loc.getCx()][loc.getCy() + 1] && mat[loc.getDx()][loc.getDy() + 1])) {
			loc.moveRight();
			A.setX(A.getX() + Config.RECTANGLE_EDGE);
			B.setX(B.getX() + Config.RECTANGLE_EDGE);
			C.setX(C.getX() + Config.RECTANGLE_EDGE);
			D.setX(D.getX() + Config.RECTANGLE_EDGE);
			return true;
		}
		return false;
	}

	// checks if is it in border and move it right
	public boolean moveLeft() {
		if ((loc.getAy() > 0 && loc.getBy() > 0 && loc.getCy() > 0 && loc.getDy() >0) && (mat[loc.getAx()][loc.getAy() - 1]
				&& mat[loc.getBx()][loc.getBy() - 1] && mat[loc.getCx()][loc.getCy() - 1] && mat[loc.getDx()][loc.getDy() - 1])) {
			loc.moveLeft();
			A.setX(A.getX() - Config.RECTANGLE_EDGE);
			B.setX(B.getX() - Config.RECTANGLE_EDGE);
			C.setX(C.getX() - Config.RECTANGLE_EDGE);
			D.setX(D.getX() - Config.RECTANGLE_EDGE);
			return true;
		}
		return false;
	}

	// checks if is it in border and move it down
	public boolean moveDown() {
		if ((loc.getAx() < Config.ROW - 1 && loc.getBx() < Config.ROW - 1 && loc.getCx() < Config.ROW - 1 && loc.getDx() < Config.ROW - 1)
				&& (mat[loc.getAx() + 1][loc.getAy()] && mat[loc.getBx() + 1][loc.getBy()] && mat[loc.getCx() + 1][loc.getCy()]
						&& mat[loc.getDx() + 1][loc.getDy()])) {
			loc.moveDown();
			A.setY(A.getY() + Config.RECTANGLE_EDGE);
			B.setY(B.getY() + Config.RECTANGLE_EDGE);
			C.setY(C.getY() + Config.RECTANGLE_EDGE);
			D.setY(D.getY() + Config.RECTANGLE_EDGE);
			return true;
		}
		return false;
	}

	///////////////////////////////////////////////////

	// changes position according to rotation way
	public boolean rotateRight() {
		if (rotate(true)) {
			position = (position + 1) % 4;
			return true;
		}
		return false;
	}

	public boolean rotateLeft() {
		if (rotate(false)) {
			position = (position == 0) ? 3 : position - 1;
			return true;
		}
		return false;
	}

	protected boolean rotate(boolean rotation) { // true means right, false means left
		int pos = (rotation) ? position : (position + 1) % 4;

		switch (position) {
		case 0:
			if (rotateCheck(posLoc[pos])) {
				setRotateLocation(posLoc[pos]);
				return true;
			}
			return false;
		case 1:
			if (rotateCheck(posLoc[pos])) {
				setRotateLocation(posLoc[pos]);
				return true;
			}
			return false;
		case 2:
			if (rotateCheck(posLoc[pos])) {
				setRotateLocation(posLoc[pos]);
				return true;
			}
			return false;
		case 3:
			if (rotateCheck(posLoc[pos])) {
				setRotateLocation(posLoc[pos]);
				return true;
			}
			return false;
		}
		return false;
	};

	public TetLocation getLoc() {
		return loc;
	}

	public void setLoc(TetLocation loc) {
		this.loc = loc;
	}

	protected void setUILocation() { // sets locations that graphics
		A.setY(Config.RECTANGLE_EDGE * (loc.getAx()));
		A.setX(Config.RECTANGLE_EDGE * (loc.getAy()));
		B.setY(Config.RECTANGLE_EDGE * (loc.getBx()));
		B.setX(Config.RECTANGLE_EDGE * (loc.getBy()));
		C.setY(Config.RECTANGLE_EDGE * (loc.getCx()));
		C.setX(Config.RECTANGLE_EDGE * (loc.getCy()));
		D.setY(Config.RECTANGLE_EDGE * (loc.getDx()));
		D.setX(Config.RECTANGLE_EDGE * (loc.getDy()));
	};

	private boolean rotateCheck(TetLocation checkLoc) { // checks if is it possible to rotate
		return (((loc.getAx() + checkLoc.getAx()) >= 0 && (loc.getAx() + checkLoc.getAx()) < Config.ROW)
				&& ((loc.getBx() + checkLoc.getBx()) >= 0
						&& (loc.getBx() + checkLoc.getBx()) < Config.ROW)
				&& ((loc.getCx() + checkLoc.getCx()) >= 0
						&& (loc.getCx() + checkLoc.getCx()) < Config.ROW)
				&& ((loc.getDx() + checkLoc.getDx()) >= 0
						&& (loc.getDx() + checkLoc.getDx()) < Config.ROW))

				&& (((loc.getAy() + checkLoc.getAy()) >= 0
						&& (loc.getAy() + checkLoc.getAy()) < Config.COLUMN)
						&& ((loc.getBy() + checkLoc.getBy()) >= 0
								&& (loc.getBy() + checkLoc.getBy()) < Config.COLUMN)
						&& ((loc.getCy() + checkLoc.getCy()) >= 0
								&& (loc.getCy() + checkLoc.getCy()) < Config.COLUMN)
						&& ((loc.getDy() + checkLoc.getDy()) >= 0
								&& (loc.getDy() + checkLoc.getDy()) < Config.COLUMN))

				&& (mat[loc.getAx() + checkLoc.getAx()][loc.getAy() + checkLoc.getAy()]
						&& mat[loc.getBx() + checkLoc.getBx()][loc.getBy() + checkLoc.getBy()]
						&& mat[loc.getCx() + checkLoc.getCx()][loc.getCy() + checkLoc.getCy()]
						&& mat[loc.getDx() + checkLoc.getDx()][loc.getDy() + checkLoc.getDy()]);
	}

	private void setRotateLocation(TetLocation newLoc) {
		loc.setAx(loc.getAx() + newLoc.getAx());
		loc.setAy(loc.getAy() + newLoc.getAy());
		loc.setBx(loc.getBx() + newLoc.getBx());
		loc.setBy(loc.getBy() + newLoc.getBy());
		loc.setCx(loc.getCx() + newLoc.getCx());
		loc.setCy(loc.getCy() + newLoc.getCy());
		loc.setDx(loc.getDx() + newLoc.getDx());
		loc.setDy(loc.getDy() + newLoc.getDy());
		setUILocation();
	}
	
	public void setStrokeColorAll(Color c) {
		A.setStroke(c);
		B.setStroke(c);
		C.setStroke(c);
		D.setStroke(c);
	}
	
	public ObservableList<Node> getAllRectangles(){
		ObservableList<Node> list;
		Group root = new Group();
		list = root.getChildren();
		list.addAll(A,B,C,D);
		return list;
	}
	
	public void setImage(String fileName) {
		String path = "/images/";
		Image image;
		try {
			image = new Image(Main.class.getResource(path+fileName).toURI().toString(),30,30,false,true);
			ImagePattern imgPat = new ImagePattern(image);
			A.setFill(imgPat);
			B.setFill(imgPat);
			C.setFill(imgPat);
			D.setFill(imgPat);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

	}
	

}
