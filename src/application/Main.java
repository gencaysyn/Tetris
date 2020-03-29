package application;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import sun.security.x509.IssuingDistributionPointExtension;

public class Main extends Application {

	// Main components
	private Group root;
	private ObservableList<Node> list;
	private Pane outerPane;
	private Pane innerPane;
	
	// Game management variables
	private Game game;
	private Tetrimino tetrimino;
	private Rectangle[][] board = new Rectangle[Config.ROW][Config.COLUMN]; // to reach and modify rectangles that in GUI
	private Timer fall;
	private TimerTask task;
	private boolean isPaused = true; // to manage pause button
	
	// Label and button Components
	private Label gameOverLabel;
	private Label overScoreLabel;
	private Label scoreLabel;
	private Label levelLabel;
	private Label lineLabel;
	private Label selectLevelTextLabel;
	private Label instructorLabel;
	private Button restartButton;
	private Button pauseButton;
	private Button startButton;
	private ChoiceBox<String> levelSelector;
	private int selectedLevel;
	private SoundManager soundManager;
	private EventHandler<KeyEvent> keyControl;
	private boolean taskRunning = false;
	private boolean taskRunningCleaner = false;
	// Main GUI thread
	@Override
	public void start(Stage primaryStage) {
		try {
			root = new Group();
			list = root.getChildren();
			list.add(buildBoard());
			soundManager = new SoundManager();
			soundManager.autoPlay("menu");
			soundManager.play("start");
			configureTextLabels();
			configureButtons();
			list.addAll(lineLabel,selectLevelTextLabel,levelSelector,instructorLabel);
			list.addAll(levelLabel,startButton,pauseButton,scoreLabel,restartButton);
			list.addAll(gameOverLabel, overScoreLabel);
			keyControl = new EventHandler<KeyEvent>() {
				@Override
				public void handle(KeyEvent e) {
					if (!isPaused &&!tetrimino.isDropped) {
						if (e.getCode() == KeyCode.RIGHT) {
							tetrimino.moveRight();
						} else if (e.getCode() == KeyCode.LEFT) {
							tetrimino.moveLeft();
						} else if (e.getCode() == KeyCode.UP) {
							tetrimino.rotateRight();
						} else if (e.getCode() == KeyCode.DOWN) {
							if(!isMoveDown() && !tetrimino.isDropped) {
								dropTetrimino();
								checkLines();
							}
						} else if (e.getCode() == KeyCode.Z) {
							tetrimino.rotateLeft();
						}
						
					}
				}
			};

			Scene scene = new Scene(root, Config.WINDOW_PANE_WIDTH, Config.WINDOW_PANE_HEIGHT);
			scene.addEventHandler(KeyEvent.KEY_PRESSED, keyControl);
			
			primaryStage.setTitle("Tetris");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.sizeToScene();
			primaryStage.show();
			primaryStage.setOnCloseRequest( event -> {
				Platform.exit();
				System.exit(0);
				});
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// The Main game thread
	public TimerTask createNewTask() {
		return new TimerTask() {
			public synchronized void run() {
				Platform.runLater(new Runnable() {
					public synchronized void run() {
						if(!taskRunning && !taskRunningCleaner){
							taskRunning = true;
							if (!isPaused && !isMoveDown()) {
								if(!tetrimino.isDropped) {
									dropTetrimino();
									checkLines();
								}
									
								addTetrimino();
								if (!game.isAvaible()) {
									finishGame();
									pause();
								}
								updateGame();
							}
							taskRunning = false;
						}else {
						}
						
					}
					
				});
			}
		};
	}


	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}

	// Creates tetromito and adds to GUI
	public synchronized void addTetrimino() {
		tetrimino = game.createTetromino();
		innerPane.getChildren().addAll(tetrimino.getAllRectangles());
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
				innerPane.getChildren().remove(board[row][j]);
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

	// Checks if move down current tetromino
	// if it can returns true, otherwise return false and drops it
	public synchronized boolean isMoveDown() {
		if (!tetrimino.moveDown()) {
			return false;
		} else {
			return true;
		}
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
			fall.schedule(createNewTask(), Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]); // this scheduler not re-programmable we need recreate it each time
			isPaused = false;
		}	
	}
	
	public void resetBoard() {
		innerPane.getChildren().removeAll(tetrimino.getAllRectangles()); // the last tetromino that did not add to board
		                                                                 // info: this matrix not for displaying components, just for way too easy reach
		for (int i = 0; i < Config.ROW; i++) {
			for (int j = 0; j < Config.COLUMN; j++) {
				if (board[i][j] != null) {
					innerPane.getChildren().remove(board[i][j]);
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
		
		pauseButton.setDisable(true);;
		gameOverLabel.setVisible(true);
		overScoreLabel.setVisible(true);
		overScoreLabel.setText("Score :"+game.getScore());
		restartButton.setVisible(true);
		outerPane.setOpacity(0.5);
	}
	
	public synchronized void startGame() {
		// GUI configurations //////////////
		outerPane.setOpacity(1);
		selectLevelTextLabel.setVisible(false);
		levelSelector.setVisible(false);
		gameOverLabel.setVisible(false);
		restartButton.setVisible(false);
		overScoreLabel.setVisible(false);
		startButton.setDisable(true);
		pauseButton.setDisable(false);
		
		soundManager.stop("menu");
		soundManager.stop("gameOver");
		soundManager.stop("start");
		soundManager.autoPlay("inGame");
		//soundManager.mute("inGame");
		//////////////////////////////////////
		
		game = new Game(tetrimino); 
		game.setLevel(selectedLevel); // set level that selected from choice box
		levelLabel.setText("level "+ selectedLevel); // initial level text
		addTetrimino(); // adds tetromino to GUI
		// Move down thread configuration
		
		fall = new Timer(); 
		task = createNewTask();
		fall.schedule(task, Config.STARTING_DELAY, Config.DELAYS[game.getLevel()]);
		isPaused = false;
		
		
	}
	
	// deletes all rectangles from board and deletes game object
	public  void resetGame() {
		scoreLabel.setText("Score: 0");
		resetBoard();
		game = null;
		try {
			finalize();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
		game.update();
		scoreLabel.setText("Score: "+game.getScore());
		levelLabel.setText("Level "+game.getLevel());
		lineLabel.setText("Line "+game.getLineCounter());
	}
	
	// Mesh board builder
	private Pane buildBoard() {
		innerPane = new Pane();
		innerPane.setLayoutX(Config.INNER_PANE_ALIGNMENT_X);
		innerPane.setLayoutY(Config.INNER_PANE_ALIGNMENT_Y);

		for (int i = 0; i <= Config.ROW; i++) {
			if (i <= Config.COLUMN) {
				innerPane.getChildren().add(new Line(i * Config.RECTANGLE_EDGE, 0, i * Config.RECTANGLE_EDGE, Config.INNER_PANE_HEIGHT));
			}
			innerPane.getChildren().add(new Line(0, i * Config.RECTANGLE_EDGE, Config.INNER_PANE_WIDTH, i * Config.RECTANGLE_EDGE));
		}

		outerPane = new Pane();
		outerPane.setLayoutX(Config.OUTER_PANE_ALIGNMENT_X);
		outerPane.setLayoutY(Config.OUTER_PANE_ALIGNMENT_Y);
		outerPane.setBorder(new Border(
		new BorderStroke(Color.BLUE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(5))));
		outerPane.minHeight(650);
		outerPane.minWidth(650);
		outerPane.getChildren().add(innerPane);

		return outerPane;

	}
	
	//////////////////// Buttons and Labels Configurations /////////////////////////////
	public void configureButtons() {
		startButton = new Button("Start Game");
		startButton.setTranslateX(400);
		startButton.setTranslateY(50);
		
		pauseButton = new Button("Pause");
		pauseButton.setTranslateX(400);
		pauseButton.setTranslateY(100);
		pauseButton.setDisable(true);
		
		restartButton = new Button("Restart");
		restartButton.setTranslateX(178);
		restartButton.setTranslateY(350);
		restartButton.setVisible(false);
		
		levelSelector = new ChoiceBox<String>();
		levelSelector.getItems().addAll("Level 00","Level 01", "Level 02","Level 03","Level 04","Level 05","Level 06","Level 07","Level 08","Level 09","Level 10");
		levelSelector.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
			
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				selectedLevel = newValue.intValue();
			}
			
		});
		levelSelector.setTranslateX(160);
		levelSelector.setTranslateY(160);
		selectLevelTextLabel = new Label("TETRIS\nSelect Level");
		selectLevelTextLabel.setTranslateX(124);
		selectLevelTextLabel.setTranslateY(70);
		selectLevelTextLabel.setStyle("-fx-font-weight: bold");
		selectLevelTextLabel.setFont(new Font(30));
		selectLevelTextLabel.setTextAlignment(TextAlignment.CENTER);
		outerPane.setOpacity(0.5);
		
		startButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				startGame();
			}
		});
		
		restartButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				soundManager.autoPlayAfterThis("restart", "inGame");
				resetGame();
				startGame();
			}
		});

		pauseButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (!isPaused) {
					pause();
					pauseButton.setText("Resume");
				} else {
					resume();
					pauseButton.setText("Pause");
				}
			}
		});
	}
	
	public void configureTextLabels() {
		gameOverLabel = new Label("Game Over");
		overScoreLabel = new Label("Score:");
		gameOverLabel.setTranslateX(62);
		gameOverLabel.setTranslateY(200);
		overScoreLabel.setTranslateX(150);
		overScoreLabel.setTranslateY(283);
		gameOverLabel.setStyle("-fx-font-weight: bold");
		gameOverLabel.setFont(new Font(56));
		gameOverLabel.setTextFill(Color.web("#ff0000", 1));
		overScoreLabel.setFont(new Font(35));
		overScoreLabel.setTextAlignment(TextAlignment.CENTER);
		gameOverLabel.setTextAlignment(TextAlignment.CENTER);
		gameOverLabel.setVisible(false);
		overScoreLabel.setVisible(false);

		scoreLabel = new Label("Score: 0");
		scoreLabel.setTranslateX(400);
		scoreLabel.setTranslateY(150);
		
		levelLabel = new Label();
		levelLabel.setTranslateX(200);
		levelLabel.setTranslateY(10);
		
		lineLabel = new Label("Line ");
		lineLabel.setTranslateX(400);
		lineLabel.setTranslateY(200);
		
		instructorLabel = new Label("Instructor\n Right Arrow Key : Move Right\n Left Arrow Key : Move Left\n Up Arrow Key: Rotate Right \n Down Arrow Key: Soft Drop\n Z : Rotate Left");
		instructorLabel.setTranslateX(380);
		instructorLabel.setTranslateY(450);
		instructorLabel.setTextAlignment(TextAlignment.CENTER);
	}
}
