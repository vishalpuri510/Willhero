package WillHero;

import Exceptions.WorldNotExistException;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class World {
    /* the class is  of no use in particular but is added considering the scalability og game */
    private final ArrayList<Game> worldList = new ArrayList<>();

    public void passName(Label name) {
        worldList.add(new Game(name));
    }

    public int selectGame(Label nameLabel) {
        passName(nameLabel);

        int game;
        /* Option for taking inputs for a particular world ca be taken
            and that world will appear for user
         */
        game = 0;
        return game;
    }

    public void start(Stage stage, Label nameLabel) throws WorldNotExistException {
        int srNo = selectGame(nameLabel);
        if (srNo < worldList.size()){
            Game game  = worldList.get(srNo);

            try {
                game.start(stage);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new WorldNotExistException("Selected World not exist.");
        }
    }
}
