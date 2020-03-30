package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Window;

public class MainMenuController implements Initializable{
//	private final AppModel model ;
	@FXML
	private Button mainStartBtn;
	@FXML
	private Button highScoresBtn;
	@FXML
	private Button quitBtn;
	
//	public MainMenuController(AppModel model) {
//		this.model = model;
//	}
	
	@FXML
	private void mainStartBtnHandler(ActionEvent e)throws IOException {
		System.out.println("dsasd");
		Parent gameParent = FXMLLoader.load(getClass().getResource("Applicaiton.fxml"));
		Scene gameScene = new Scene(gameParent);
		Stage gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		gameStage.setScene(gameScene);
		gameStage.show();
	}
	
	private void highScoreBtnHandler(ActionEvent e) {
		
	}
	
	@FXML
	private void quitBtnHandler(ActionEvent e) {
		Stage stage = (Stage) quitBtn.getScene().getWindow();
	    stage.close();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}

}
