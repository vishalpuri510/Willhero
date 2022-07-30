package WillHero;

import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ArenaScreen implements Initializable {

    @FXML
    private ImageView mainMenuIcon;
    @FXML
    private ImageView restartIcon;
    @FXML
    private ImageView resumeIcon;
    @FXML
    private ImageView saveIcon;
    @FXML
    private Text floatingName;
    @FXML
    private Label bestLocation;
    @FXML
    private Label bestReward;

    private static Timeline tl;
    private Hero hero;
    private ArrayList<Platform> platforms;
    private ArrayList<Orc> orcs;
    private ArrayList<Chest> chests;
    private ArrayList<Obstacle> tnts;
    private ArrayList<Coin> coins;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StaticFunction.setTranslation(floatingName, 0, 100, 1000, TranslateTransition.INDEFINITE);
        StaticFunction.setRotation(resumeIcon, 1000, 2, true);
        StaticFunction.setRotation(restartIcon, 1000, 2, true);
        StaticFunction.setRotation(saveIcon, 1000, 2, true);
        StaticFunction.setRotation(mainMenuIcon, 1000, 2, true);
        StaticFunction.bestLocation(bestLocation);
        StaticFunction.bestReward(bestReward);
    }

    public void start(Timeline tl, Hero hero, ArrayList<Chest> chests, ArrayList<Orc> orcs, ArrayList<Coin> coins, ArrayList<Obstacle> tnts, ArrayList<Platform> platforms) {
        ArenaScreen.tl = tl;
        this.hero = hero;
        this.platforms = platforms;
        this.orcs = orcs;
        this.chests = chests;
        this.tnts = tnts;
        this.coins = coins;
    }


    public void Serialize(String fileName) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            out.writeObject(hero);

            for(Platform platform : platforms) {
                out.writeObject(platform);
            }

            for(Chest chest : chests) {
                out.writeObject(chest);
            }
            for(Obstacle tnt : tnts) {
                out.writeObject(tnt);
            }
            for(Coin coin : coins) {
                out.writeObject(coin);
            }
            for(Orc orc : orcs) {
                out.writeObject(orc);
            }

        } finally {
            assert out != null;
            out.close();
        }
    }


    public void SerializePlayer(String fileName) throws IOException {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(fileName));
            try {
                out.writeObject(hero);
            } catch (NullPointerException e) {
                System.out.println("HERO NOT INITIALIZED");
            }
        } finally {
            assert out != null;
            out.close();
        }
    }

    public String getFileName(String rootDirectory) throws NullPointerException {
        String playerName;
        try {
            playerName = hero.getName();
            rootDirectory = rootDirectory.concat("/");
            rootDirectory = rootDirectory.concat(playerName);
            rootDirectory = rootDirectory.concat(".txt");
            return rootDirectory;

        } catch (NullPointerException e) {
            throw new NullPointerException("HERO NOT INITIALIZED");
        }
    }

    @FXML
    public void resumeGame(MouseEvent resume) {
        StaticFunction.clickResponse(this.resumeIcon);

        Node node = (Node) resume.getSource();
        VBox vBox = (VBox) node.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);
        try {
            stackPane.getChildren().remove(stackPane.getChildren().get(1));

        } catch (NullPointerException e) {
            System.out.println("Already Deleted");
        }
    }

    public void mainMenu(MouseEvent mainMenu) {
        StaticFunction.clickResponse(this.mainMenuIcon);
///

        StaticFunction.mainMenu(mainMenu, tl);

    }

    @FXML
    public void restart(MouseEvent restart) {
        StaticFunction.clickResponse(this.restartIcon);
        tl.stop();
        Label nameLabel = new Label(hero.getName());
        World world = new World();
        world.start(StaticFunction.getStage(restart), nameLabel);
    }

    @FXML
    public void save() {
        StaticFunction.clickResponse(this.saveIcon);

        String currentDir = System.getProperty("user.dir") + "/WillHero/src/java/";

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "DO you want to save the game?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Save Prompt");
        alert.initStyle(StageStyle.UNDECORATED);
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.YES) {
            try {
                String playerFile = getFileName(currentDir + "SavedPlayers");
                String fileName = getFileName(currentDir + "SavedGames");

                SerializePlayer(playerFile);
                Serialize(fileName);

            } catch (NullPointerException | IOException  e) {
                e.printStackTrace();
            }
        }
    }
}
