package WillHero;


import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;


public class Setting {

    public void start(Stage stage) throws IOException {
        URL toScene = getClass().getResource("setting.fxml");
        StaticFunction.setScene(stage, toScene, "Settings");
    }

    public void exit(MouseEvent mouseEvent) throws IOException {
        Stage stage=StaticFunction.getStage(mouseEvent);
        URL toScene = getClass().getResource("MainMenu.fxml");
        StaticFunction.setScene(stage, toScene, "MainMenu");
    }

    public  void setSounds(){
        StaticFunction.setPlay(!StaticFunction.getPlay());
        StaticFunction.setSounds();
    }




    public  void getHelp(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "", ButtonType.OK);
        alert.setTitle("Help");
        alert.setHeaderText("Help");
        alert.setContentText("""
                1) You have to Press space bar to move the hero in Forward direction.
                2) You can collect the coins along the way to increase your Reward.
                3) Collected coins can be used to resurrect your hero avatar.
                4) AS you proceed in the forward direction the location will be increasedYou need to fight with the orc you can also escape but legend don't do that :)
                5) Try to hit the chest to get reward or a weapon to make yourself more efficient.
                6) Try not fall into the abyss.
                """);

        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
    }

    public void ChangeBackground() {
        StaticFunction.setFt(!StaticFunction.getFt());
    }
}