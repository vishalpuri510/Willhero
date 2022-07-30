package WillHero;

import Exceptions.WorldNotExistException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class World {
    /* the class is  of no use in particular but is added considering the scalability og game */
    private final ArrayList<Game> worldList = new ArrayList<>();

    public World() {
        worldList.add(new Game());
    }

    public int selectGame() {
        int game;
        /* Option for taking inputs for a particular world ca be taken
            and that world will appear for user
         */
        game = 0;
        return game;
    }

    public void start(Stage stage, Label nameLabel) throws WorldNotExistException {
        int srNo = selectGame();
        if (srNo < worldList.size()){
            AnchorPane gameAnchorPane;
            StackPane stackPane = new StackPane();
            VBox vBox = new VBox();

            try {
                FXMLLoader root_gameAnchorPane = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Game.fxml")));

                gameAnchorPane = root_gameAnchorPane.load();
                gameAnchorPane.setBackground(StaticFunction.defaultBackground());
                stackPane.getChildren().add(gameAnchorPane);

                Image icon = new Image(new FileInputStream(Objects.requireNonNull(StaticFunction.class.getResource("mainIcon.png")).getPath()));
                stage.setTitle("WillHero: " + nameLabel.getText());

                Game gameController = root_gameAnchorPane.getController();
                gameController.start(stage, nameLabel.getText(), vBox, stackPane, gameAnchorPane);

                stage.getIcons().add(icon);
                vBox.getChildren().add(stackPane);
                Scene scene = new Scene(vBox);
                stage.setScene(scene);
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new WorldNotExistException("Selected World not exist.");
        }
    }
}