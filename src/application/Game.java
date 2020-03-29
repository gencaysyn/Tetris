package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Game {

	protected boolean[][] board = new boolean[Config.ROW][Config.COLUMN];
	private Tetrimino t;
	private int score;
	private int level;
	private int lineCounter;
	private int levelConfig;
	private int lastTetrimino;
	private boolean panic = false;

	public Game(Tetrimino t) {
		this.t = t;
		this.level = 0;
		this.lineCounter = 0;
		this.score = 0;
		this.lastTetrimino = -1;
		this.levelConfig = 0;
		FillBoard();
	}

	public void addScore(int lineAmount) {
		score += (level + 1) * Config.SCORES[lineAmount - 1];
	}

	public void update() {
		if (lineCounter/Config.LINE_PER_LEVEL >levelConfig) {
			level++;
			levelConfig++;
		}
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void increaseScore(int amount) {
		score += amount * level;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void dropTetromino(Tetrimino t) {
		t.isDropped = true;
		board[t.getLoc().getAx()][t.getLoc().getAy()] = false;
		board[t.getLoc().getBx()][t.getLoc().getBy()] = false;
		board[t.getLoc().getCx()][t.getLoc().getCy()] = false;
		board[t.getLoc().getDx()][t.getLoc().getDy()] = false;
	}

	public synchronized boolean isAvaible() {
		TetLocation loc = t.loc;
		return board[loc.getAx()][loc.getAy()] && board[loc.getBx()][loc.getBy()]
				&& board[loc.getCx()][loc.getCy()] && board[loc.getDx()][loc.getDy()];
	}

	public void FillBoard() {
		for (boolean[] row : board)
			Arrays.fill(row, true);
	}

	public Tetrimino createTetromino() {
		Random r = new Random();
		int select = r.nextInt(7);
		while (select == lastTetrimino)
			select = r.nextInt(7);
		lastTetrimino = select;
		switch (select) {
		case 0:
			t = new Tet_I(board);
			return t;
		case 1:
			t = new Tet_J(board);
			return t;
		case 2:
			t = new Tet_L(board);
			return t;
		case 3:
			t = new Tet_O(board);
			return t;
		case 4:
			t = new Tet_S(board);
			return t;
		case 5:
			t = new Tet_T(board);
			return t;
		case 6:
			t = new Tet_Z(board);
			return t;
		}
		return null;
	}

	public ArrayList<Integer> checkLines() {
		ArrayList<Integer> fullLines = new ArrayList<>();

		for (int i = 0; i < Config.ROW; i++) {
			boolean flag = true;
			for (int j = 0; j < Config.COLUMN; j++) {
				if (board[i][j]) {
					flag = false;
				}
			}
			if (flag)
				fullLines.add(i);
		}

		return fullLines;
	}

	public boolean isPanic() {
		boolean currentState = false;
		for (int i = 0; i < Config.COLUMN; i++) {
			if (!board[7][i]) {
				currentState = true;
				break;
			}
		}

		if (currentState) {
			if (!panic) {
				panic = true;
				return true;
			} else {
				return false;
			}

		} else {
			panic = false;
			return false;
		}

	}

	public int getLineCounter() {
		return lineCounter;
	}

	public void setLineCounter(int lineCounter) {
		this.lineCounter = lineCounter;
	}

	public void cleanLines(ArrayList<Integer> fullLines) {
		lineCounter += fullLines.size();
		addScore(fullLines.size());
		for (Integer row : fullLines) {
			for (int i = row; i > 0; i--) {
				for (int j = 0; j < Config.COLUMN; j++) { // shifts each row down one
					board[i][j] = board[i - 1][j];
				}
				for (int k = 0; k < Config.COLUMN; k++) { // sets first line true
					board[0][k] = true; // MAYBE NECESSARY
				}
			}

		}
	}

}
