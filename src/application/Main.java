package application;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	@Override
	public void start(Stage primaryStage) {
		try {
//			soundManager = new SoundManager();
//			soundManager.autoPlay("menu");
//			soundManager.play("start");
			
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			//////////////////////////////////////////LEVEL COUNTER ÇALIŞMIYOR///////////////////////////////////////////////////////
			/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			FXMLLoader loader = new FXMLLoader();
	        loader.setLocation(getClass().getResource("MainMenu.fxml"));
	        Parent root = loader.load();
			Scene scene = new Scene(root);
			
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


	

}
