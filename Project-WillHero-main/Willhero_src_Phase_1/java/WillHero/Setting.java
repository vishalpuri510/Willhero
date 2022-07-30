package WillHero;

import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Setting {
    @FXML
    private Button exit;
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

    }


    public void changeBackground(){

    }


    public  void getHelp(){



    }

}