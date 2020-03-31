package application;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.AudioClip;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameController implements Initializable {
	// Game management variables
	private Game game;
	private Tetrimino tetrimino;
	private Rectangle[][] board = new Rectangle[Config.ROW][Config.COLUMN]; // to reach and modify rectangles that in
																			// GUI
	private Timer fall;
	private TimerTask task;
	private boolean isPaused = true; // to manage pause button

	// javafx components
	@FXML
	private Pane game_board_pane;
	@FXML
	private Pane next_tetrimino_pane;
	@FXML
	private Label gameOverLabel;
	@FXML
	private Label overScoreLabel;
	@FXML
	private Label scoreLabel;
	@FXML
	private Label levelLabel;
	@FXML
	private Label lineLabel;
	@FXML
	private Button restartBtn;
	@FXML
	private Button pauseBtn;
	@FXML
	private Button backBtn;
	@FXML
	private ImageView img;

	private int selectedLevel;
	private SoundManager soundManager = Config.soundManager;
	private boolean taskRunning = false;
	private boolean taskRunningCleaner = false;

	@FXML
	public void keyHandler(KeyEvent e) {
		if (!isPaused && !tetrimino.isDropped) {
			if (e.getCode() == KeyCode.RIGHT) {
				tetrimino.moveRight();
			} else if (e.getCode() == KeyCode.LEFT) {
				tetrimino.moveLeft();
			} else if (e.getCode() == KeyCode.UP) {
				tetrimino.rotateRight();
			} else if (e.getCode() == KeyCode.DOWN) {
				if (!moveDown() && !tetrimino.isDropped) {
					dropTetrimino();
					checkLines();
				}
			} else if (e.getCode() == KeyCode.Z) {
				tetrimino.rotateLeft();
			}
		}
	}

	@FXML
	public void pauseBtnHandler(ActionEvent e) {
		System.out.println("askjdf");
		soundManager.playEffect("select");
		if (!isPaused) {
			pause();
			soundManager.pause("inGame");
			pauseBtn.setText("Resume");
		} else {
			resume();
			soundManager.autoPlay("inGame");
			pauseBtn.setText("Pause");
		}
	}

	@FXML
	public void restartBtnHandler(ActionEvent e) {
		soundManager.playEffect("select");
		resetGame();
		restartGame();
	}

	@FXML
	public void backBtnHandler(ActionEvent e) {
		soundManager.playEffect("select");
		pause();
		soundManager.stop("inGame");
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("MainMenu.fxml"));
			Parent mainMenuParent;
			mainMenuParent = (Parent) loader.load();
			Scene gameScene = new Scene(mainMenuParent);
			Stage gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
			gameStage.setScene(gameScene);
			gameStage.show();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	// The Main game thread
	public TimerTask createGameLoop() {
		return new TimerTask() {
			public synchronized void run() {
				Platform.runLater(new Runnable() {
					public synchronized void run() {
						if (!taskRunning && !taskRunningCleaner) {
							taskRunning = true;
							if (!isPaused && !moveDown()) {
								if (!tetrimino.isDropped) {
									dropTetrimino();
									checkLines();
								}
								updateGame();
								addNextTetrimino();
								showNextTetrimino();
								if (!game.isAvaible()) {
									finishGame();
								}
							}
							taskRunning = false;
						} else {
							System.out.println("Error: other procces running");
						}

					}

				});
			}
		};
	}

	// Creates tetrimino and adds to GUI
	public synchronized void addFirstTetrimino() {
		tetrimino = game.firstTetrimino();
		game_board_pane.getChildren().addAll(tetrimino.getAllRectangles());
	}

	public synchronized void addNextTetrimino() {
		tetrimino = game.nextTetrimino();
		game_board_pane.getChildren().addAll(tetrimino.getAllRectangles());
	}

	public void showNextTetrimino() {
		next_tetrimino_pane.getChildren().clear();
		Tetrimino next = game.getNext().getInstanceOfObject();
		next.setLoc(next.instanceLoc);
		next.setUILocation();
		next_tetrimino_pane.getChildren().addAll(next.getAllRectangles());
	}

	private void dropTetrimino() {
		game.dropTetromino(tetrimino);
		TetLocation loc = tetrimino.getLoc();
		board[loc.getAx()][loc.getAy()] = tetrimino.A;
		board[loc.getBx()][loc.getBy()] = tetrimino.B;
		board[loc.getCx()][loc.getCy()] = tetrimino.C;
		board[loc.getDx()][loc.getDy()] = tetrimino.D;
	}

	// Cleans required rows and drags others down
	private void cleanLines(ArrayList<Integer> fullLines) {
		taskRunningCleaner = true;
		for (Integer row : fullLines) {
			for (int j = 0; j < Config.COLUMN; j++) {
				game_board_pane.getChildren().remove(board[row][j]);
				board[row][j] = null;
			}
			for (int i = row; i > 0; i--) {
				for (int j = 0; j < Config.COLUMN; j++) { // shifts each row down one
					if (board[i - 1][j] != null) {
						board[i - 1][j].setY(board[i - 1][j].getY() + Config.RECTANGLE_EDGE);
					}
					board[i][j] = board[i - 1][j];
				}
			}
		}
		taskRunningCleaner = false;
	}

	// Checks if the lines are full
	private void checkLines() {
		ArrayList<Integer> fullLines = game.checkLines();
		if (fullLines.size() > 0) {
			if (fullLines.size() == 4) {
				soundManager.playEffect("tetris");
			} else {
				soundManager.playEffect("lineClean");
			}
			game.cleanLines(fullLines);
			cleanLines(fullLines);
		} else {

			soundManager.playEffect("drop");
		}
	}

	// Checks if move down current tetrimino
	// if it can returns true, otherwise return false and drops it
	public synchronized boolean moveDown() {
		return tetrimino.moveDown();
	}

	public synchronized void pause() {
		if (!isPaused) {
			fall.cancel();
			isPaused = true;
		}
	}

	// this scheduler not re-programmable we need recreate it each time
	public synchronized void resume() {
		if (isPaused) {
			fall = new Timer();
			fall.schedule(createGameLoop(), Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]); 
			isPaused = false;
		}
	}

	public void resetBoard() {
		game_board_pane.getChildren().clear(); // the last tetromino that did not add to board
												// info: this matrix not for displaying components, just for way too
												// easy reach
		for (int i = 0; i < Config.ROW; i++) {
			for (int j = 0; j < Config.COLUMN; j++) {
				if (board[i][j] != null) {
					board[i][j] = null;
				}
			}
		}
	}

	public synchronized void finishGame() {
		pause();
		soundManager.stop("inGame");
		soundManager.stopAll();
		// soundManager.playAfterThis("gameOver", "inGame");
		soundManager.play("gameOver");
		gameOverLabel.setVisible(true);
		FadeTransition ft = new FadeTransition(Duration.millis(1000), gameOverLabel);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.play();

		FadeTransition gameBoardAnim = new FadeTransition(Duration.millis(1000), game_board_pane);
		gameBoardAnim.setFromValue(1);
		gameBoardAnim.setToValue(0.5);
		gameBoardAnim.play();
		pauseBtn.setDisable(true);

		overScoreLabel.setVisible(true);
		overScoreLabel.setText("Score: " + game.getScore());
		restartBtn.setVisible(true);
	}

	public synchronized void startGame(int selectedLevel) {
		this.selectedLevel = selectedLevel;
		// GUI configurations //////////////
		gameOverLabel.setVisible(false);
		overScoreLabel.setVisible(false);
		pauseBtn.setDisable(false);
		restartBtn.setVisible(false);
		soundManager.stop("menu");
		soundManager.stop("gameOver");
		soundManager.autoPlay("inGame");
		// soundManager.mute("inGame");
		//////////////////////////////////////
		game = new Game(tetrimino, selectedLevel); // set level that selected from choice box
		levelLabel.setText("" + selectedLevel); // initial level text
		scoreLabel.setText("0");
		lineLabel.setText("0");
		addFirstTetrimino(); // adds tetrimino to GUI
		showNextTetrimino();
		fall = new Timer(); // Move down thread configuration
		task = createGameLoop();
		fall.schedule(task, Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]);
		isPaused = false;
	}

	public void restartGame() {
		// GUI configurations //////////////
		pauseBtn.setDisable(false);
		gameOverLabel.setVisible(false);
		overScoreLabel.setVisible(false);
		restartBtn.setVisible(false);
		FadeTransition gameBoardAnim = new FadeTransition(Duration.millis(1000), game_board_pane);
		gameBoardAnim.setFromValue(0.5);
		gameBoardAnim.setToValue(1);
		gameBoardAnim.play();
		soundManager.stop("gameOver");
		soundManager.autoPlay("inGame");
		//////////////////////////////////////
		game = new Game(tetrimino, selectedLevel); // set level that selected from choice box
		levelLabel.setText("" + selectedLevel); // initial level text
		lineLabel.setText("0");
		scoreLabel.setText("0");
		addFirstTetrimino(); // adds tetrimino to GUI
		fall = new Timer(); // Move down thread configuration
		task = createGameLoop();
		fall.schedule(task, Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]);
		isPaused = false;
	}

	// deletes all rectangles from board and deletes game object
	public void resetGame() {
		scoreLabel.setText("0");
		resetBoard();
		game = null;
	}

	public boolean isPaused() {
		return isPaused;
	}

	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	// Updates variables that used for game
	public synchronized void updateGame() {
		scoreLabel.setText("" + game.getScore());
		lineLabel.setText("" + game.getLineCounter());
		if (game.isPanic()) {
//			soundManager.play("panic");
		}
		if (game.update()) {
			levelLabel.setText("" + game.getLevel());
			pause();
			resume();
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		img.setImage(new Image(getClass().getResource("/images/background.jpg").toString()));
	}

}
