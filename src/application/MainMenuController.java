package application;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class MainMenuController implements Initializable{
//	private final AppModel model ;
	@FXML
	private Button mainStartBtn;
	@FXML
	private Button highScoresBtn;
	@FXML
	private Button quitBtn;
	private String l = "level ";
	ObservableList<String> levels = FXCollections.observableArrayList(l+"00",l+"01",l+"02",l+"03",l+"04",l+"05",l+"06",l+"07",l+"08",l+"09",l+"10");
	@FXML
	private ChoiceBox<String> levelSelector;
	
//	public MainMenuController(AppModel model) {
//		this.model = model;
//	}
	
	@FXML
	private void mainStartBtnHandler(ActionEvent e)throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("Applicaiton.fxml"));
		Parent gameParent = (Parent) loader.load();
		GameController gc = loader.getController();
		System.out.println("ASDASDASDAS"+levelSelector.getSelectionModel().getSelectedIndex());
		gc.startGame(levelSelector.getSelectionModel().getSelectedIndex());
		
		Scene gameScene = new Scene(gameParent);
		Stage gameStage = (Stage) ((Node) e.getSource()).getScene().getWindow();
		
		gameStage.setScene(gameScene);
		gameStage.show();
		
	}
	
	@FXML
	private void highScoreBtnHandler(ActionEvent e) {
		
	}
	
	@FXML
	private void quitBtnHandler(ActionEvent e) {
		Stage stage = (Stage) quitBtn.getScene().getWindow();
	    stage.close();
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		levelSelector.setValue("level 03");
		levelSelector.setItems(levels);
		
		
	}

}
