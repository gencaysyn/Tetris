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
	@FXML
	private Pane game_board_pane;
	
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
	private Button restartBtn;
	private Button pauseBtn;
	private Button startBtn;
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
			//Parent kok = FXMLLoader.load(getClass().getResource("Applicaiton.fxml"));
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("MainMenu.fxml"));
	        Parent kok = loader.load();
//			soundManager = new SoundManager();
//			soundManager.autoPlay("menu");
//			soundManager.play("start");
			
			Scene scene = new Scene(kok);
			
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




	public static void main(String[] args) throws InterruptedException {
		launch(args);
	}

	// Creates tetromito and adds to GUI

	

}
