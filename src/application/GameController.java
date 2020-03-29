package application;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GameController {
	// Main components
		private Group root;
		private ObservableList<Node> list;
		
		@FXML
		private Pane game_board_pane;
		@FXML
		private Pane next_tetrimino_pane;

		
		
		// Game management variables
		private Game game;
		private Tetrimino tetrimino;
		private Rectangle[][] board = new Rectangle[Config.ROW][Config.COLUMN]; // to reach and modify rectangles that in GUI
		private Timer fall;
		private TimerTask task;
		private boolean isPaused = true; // to manage pause button
		
		// javafx components
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
		private Label selectLevelTextLabel;
		@FXML
		private Button restartBtn;
		@FXML
		private Button pauseBtn;
		@FXML
		private Button startBtn;
		private ChoiceBox<String> levelSelector;
		private int selectedLevel;
		private SoundManager soundManager = new SoundManager();
		private boolean taskRunning = false;
		private boolean taskRunningCleaner = false;
		
		@FXML
		public void keyHandler(KeyEvent e) {
			System.out.println("asdasd");
			if (!isPaused &&!tetrimino.isDropped) {
				if (e.getCode() == KeyCode.RIGHT) {
					tetrimino.moveRight();
				} else if (e.getCode() == KeyCode.LEFT) {
					tetrimino.moveLeft();
				} else if (e.getCode() == KeyCode.UP) {
					System.out.println("UP");
					tetrimino.rotateRight();
				} else if (e.getCode() == KeyCode.DOWN) {
					if(!moveDown() && !tetrimino.isDropped) {
						dropTetrimino();
						checkLines();
					}
				} else if (e.getCode() == KeyCode.Z) {
					tetrimino.rotateLeft();
				}
			}
		}

		@FXML
		public void startBtn(ActionEvent e) {
			startGame();
		}
		
		@FXML
		public void pauseBtn(ActionEvent e) {
			if (!isPaused) {
				pause();
				pauseBtn.setText("Resume");
			} else {
				resume();
				pauseBtn.setText("Pause");
			}
		}
		
		@FXML
		public void restartBtn(ActionEvent e) {
			resetGame();
			restartGame();
		}
		
		// The Main game thread
		public TimerTask createGameLoop() {
			return new TimerTask() {
				public synchronized void run() {
					Platform.runLater(new Runnable() {
						public synchronized void run() {
							if(!taskRunning && !taskRunningCleaner){
								taskRunning = true;
								if (!isPaused && !moveDown()) {
									if(!tetrimino.isDropped) {
										dropTetrimino();
										checkLines();
									}
										
									addTetrimino();
									if (!game.isAvaible()) {
										finishGame();
									}
									updateGame();
								}
								taskRunning = false;
							}else {
								System.out.println("Error: other procces running");
							}
							
						}
						
					});
				}
			};
		}

		// Creates tetrimino and adds to GUI
		public synchronized void addTetrimino() {
			System.out.println("add Tetrimino");
			tetrimino = game.createTetromino();
			game_board_pane.getChildren().addAll(tetrimino.getAllRectangles());
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
				if(fullLines.size() == 4) {
					soundManager.play("tetris");
				}else {
					soundManager.play("lineClean");
				}
				game.cleanLines(fullLines);
				cleanLines(fullLines);
			}else {
				soundManager.play("drop");
			}
		}

		// Checks if move down current tetrimino
		// if it can returns true, otherwise return false and drops it
		public synchronized boolean moveDown() {
			return tetrimino.moveDown();
		}

		public synchronized void pause() {
			if(!isPaused) {
				fall.cancel();
				isPaused = true;
			}
		}

		public synchronized void resume() {	
			if(isPaused) {
				fall = new Timer();
				fall.schedule(createGameLoop(), Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]); // this scheduler not re-programmable we need recreate it each time
				isPaused = false;
			}	
		}
		
		public void resetBoard() {
			game_board_pane.getChildren().removeAll(tetrimino.getAllRectangles()); // the last tetromino that did not add to board
			                                                                 // info: this matrix not for displaying components, just for way too easy reach
			for (int i = 0; i < Config.ROW; i++) {
				for (int j = 0; j < Config.COLUMN; j++) {
					if (board[i][j] != null) {
						game_board_pane.getChildren().remove(board[i][j]);
						board[i][j] = null;
					}
				}
			}
		}

		public synchronized void finishGame() {
			pause();
			soundManager.stop("inGame");
			soundManager.stopAll();
			//soundManager.playAfterThis("gameOver", "inGame");
			soundManager.play("gameOver");
			
			pauseBtn.setDisable(true);;
			gameOverLabel.setVisible(true);
			overScoreLabel.setVisible(true);
			overScoreLabel.setText("Score :"+game.getScore());
			restartBtn.setVisible(true);
			//outerPane.setOpacity(0.5);
		}
		
		public synchronized void startGame() {
			// GUI configurations //////////////
			//outerPane.setOpacity(1);
			//selectLevelTextLabel.setVisible(false);
			//levelSelector.setVisible(false);
			//gameOverLabel.setVisible(false);
			//overScoreLabel.setVisible(false);
			startBtn.setDisable(true);
			pauseBtn.setDisable(false);
			soundManager.stop("menu");
			soundManager.stop("gameOver");
			soundManager.stop("start");
			soundManager.autoPlay("inGame");
			//soundManager.mute("inGame");
			//////////////////////////////////////
			game = new Game(tetrimino); 
			selectedLevel = 5;
			game.setLevel(selectedLevel); // set level that selected from choice box
			//levelLabel.setText("level "+ selectedLevel); // initial level text
			addTetrimino(); // adds tetrimino to GUI
			fall = new Timer(); // Move down thread configuration
			task = createGameLoop();
			fall.schedule(task, Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]);
			isPaused = false;
		}
		
		public void restartGame() {
			// GUI configurations //////////////
			gameOverLabel.setVisible(false);
			overScoreLabel.setVisible(false);
			restartBtn.setVisible(false);
			//outerPane.setOpacity(1);
			soundManager.stop("gameOver");
			soundManager.autoPlayAfterThis("restart", "inGame");
			//////////////////////////////////////
			game = new Game(tetrimino); 
			game.setLevel(selectedLevel); // set level that selected from choice box
			levelLabel.setText("level "+ selectedLevel); // initial level text
			addTetrimino(); // adds tetrimino to GUI
			fall = new Timer(); // Move down thread configuration
			task = createGameLoop();
			fall.schedule(task, Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]);
			isPaused = false;
		}
		
		// deletes all rectangles from board and deletes game object
		public  void resetGame() {
			scoreLabel.setText("Score: 0");
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
			if(game.isPanic()) {
				soundManager.play("panic");
			}
			if(game.update()) {
				scoreLabel.setText("Score: "+game.getScore());
				levelLabel.setText("Level "+game.getLevel());
				lineLabel.setText("Line "+game.getLineCounter());
				pause();
				resume();
			}
		}
			
}
