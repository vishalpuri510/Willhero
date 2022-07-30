package WillHero;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

public class ResumeGame {

    @FXML
    private ImageView backIcon;

    private AnchorPane anchorPane;
    private Stage stage;

    private ArrayList<String> loadGames;
    private RegenerateSprite reGenerateSprites;
    private ReGenerateHero reGenerateHero;
    private ArrayList<Hero> heroes;
    private String gameName;
    TableView<Hero> tableView = new TableView<>();
    TableColumn<Hero, String> name = new TableColumn<>("Name");
    TableColumn<Hero, Integer> reward = new TableColumn<>("Reward");
    TableColumn<Hero, Integer> location = new TableColumn<>("Location");



    private Hero generatedHero;
    private final ArrayList<Platform> generatedPlatform = new ArrayList<>();
    private final ArrayList<Coin> generatedCoin = new ArrayList<>();
    private final ArrayList<Obstacle> generatedObstacle = new ArrayList<>();
    private final ArrayList<Chest> generatedChest = new ArrayList<>();
    private final ArrayList<Orc> generatedOrc = new ArrayList<>();
    private int choice;

    public void start(Stage stage, AnchorPane anchorPane) throws IOException {
        this.anchorPane = anchorPane;
        this.stage = stage;
        this.heroes = new ArrayList<>();
        this.reGenerateHero = new ReGenerateHero();
        this.loadGames = new LoadGames().getLoadableGamesList();
        this.reGenerateSprites = new RegenerateSprite(generatedPlatform, generatedCoin,
                                        generatedObstacle, generatedChest, generatedOrc);
        this.choice = -1;
        loadHeroes();
        listGame();
    }

    private void loadHeroes() {
        for (String _game: loadGames) {
            try {
                Hero hero = reGenerateHero.getHero(_game);
                heroes.add(hero);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        StaticFunction.setHeroTable(tableView, heroes, name, reward, location);
    }

    public void listGame() {
        tableView.setPrefWidth(500);
        tableView.setPrefHeight(300);
        tableView.setLayoutX(450);
        tableView.setLayoutY(150);
        tableView.setEditable(false);
        tableView.setFocusTraversable(false);
        tableView.setStyle("-fx-background-color: #000000;");
        tableView.setStyle("-fx-text-fill: #ffffff;");
        tableView.setStyle("-fx-font-size: 20;");
        tableView.setStyle("-fx-font-family: 'Arial';");
        tableView.setStyle("-fx-font-weight: bold;");
        tableView.setStyle("-fx-border-color: #ffffff;");
        tableView.setStyle("-fx-border-width: 2;");
        tableView.setStyle("-fx-border-style: solid;");
        tableView.setStyle("-fx-border-radius: 5;");
        tableView.setStyle("-fx-background-radius: 5;");
        tableView.setStyle("-fx-background-color: #000000;");
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.getColumns().add(name);
        tableView.getColumns().add(reward);
        tableView.getColumns().add(location);
        this.anchorPane.getChildren().add(tableView);
        tableView.setRowFactory( tv -> {
            TableRow<Hero> row = new TableRow<>();
            row.setOnMouseEntered(event -> row.setStyle("-fx-background-color: #ADD8E6;"));

            row.setOnMouseExited(event -> row.setStyle("-fx-background-color: #ffffff;"));
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    Hero rowData = row.getItem();
                    generatedHero = rowData;
//                    System.out.println(generatedHero.getName()+ "  Hello");
//                    System.out.println(generatedHero.getReward());
//                    System.out.println(generatedHero.getLocation());
//                    System.out.println(rowData);
                    for(int i = 0; i < heroes.size(); i++) {
                        if(generatedHero.getName().equals(heroes.get(i).getName())) {
                            choice = i;
                        }
                    }
                    gameName = loadGames.get(choice);
                    try {
                        storeSprites();
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            });
            return row ;
        });

//        generatedHero = heroes.get(choice);





    }

    private void storeSprites() throws IOException, ClassNotFoundException {
        reGenerateSprites.regenerate(gameName);
        resumeGame();
    }

    private void resumeGame(){
//        System.out.println(generatedHero.getName());
//        System.out.println(generatedPlatform.size());
//        System.out.println(generatedChest.size());
//        System.out.println(generatedCoin.size());
//        System.out.println(generatedObstacle.size());
//        System.out.println(generatedOrc.size());

//        stage.close();


        AnchorPane gameAnchorPane;
        StackPane stackPane = new StackPane();
        VBox vBox = new VBox();

        try {
//            Stage this.stage  = new Stage();
            FXMLLoader root_gameAnchorPane = new FXMLLoader(Objects.requireNonNull(getClass().getResource("Game.fxml")));
            gameAnchorPane = root_gameAnchorPane.load();
            gameAnchorPane.setBackground(StaticFunction.defaultBackground());
            stackPane.getChildren().add(gameAnchorPane);

            Image icon = new Image(new FileInputStream(Objects.requireNonNull(StaticFunction.class.getResource("mainIcon.png")).getPath()));
            this.stage.setTitle("WillHero: " + generatedHero.getName());

            this.stage.getIcons().add(icon);
            vBox.getChildren().add(stackPane);
            Scene scene = new Scene(vBox);
            this.stage.setScene(scene);

            Game gameController = root_gameAnchorPane.getController();
            gameController.resume(this.stage, generatedHero, vBox, stackPane , gameAnchorPane,
                    generatedPlatform, generatedCoin, generatedObstacle, generatedChest, generatedOrc);

            this.stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setBack(MouseEvent backIcon) throws IOException {
        StaticFunction.clickResponse(this.backIcon);
        URL toScene = getClass().getResource("MainMenu.fxml");
        StaticFunction.setScene(StaticFunction.getStage(backIcon), toScene, "WillHero: Main Menu");
    }
}
