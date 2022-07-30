package WillHero;

import javafx.animation.*;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class Game implements Initializable {

    private final Hero hero;
    public ParallelCamera camera;
    public ProgressBar progressBar;
    private VBox vBox;
    private StackPane stackPane;
    private Group screenObj;
    private final ArrayList<Platform> platforms;
    private final ArrayList<Coin> coins;
    public ImageView island11;
    public ImageView island21;
    public ImageView island211;
    public ImageView island011;
    public static Timeline tl;



    @FXML
    public static AnchorPane gameAnchorPane;
    @FXML
    private AnchorPane screenAnchorPane;
    @FXML
    private ImageView island1;
    @FXML
    private ImageView island2;
    @FXML
    private ImageView greenOrc1;
    @FXML
    private Label currLocation;
    @FXML
    private Label currReward;
    @FXML
    private ImageView pauseIcon;

    @FXML
    private Text floatingName;
    @FXML
    private ImageView mainMenuIcon;
    @FXML
    private ImageView restartIcon;
    @FXML
    private ImageView saveIcon;
    @FXML
    private ImageView resumeIcon;
    @FXML
    private Label bestLocation;
    @FXML
    private Label bestReward;


    public Game() {

        hero = new Hero(new Label(), 0);
        currLocation = new Label();
        currReward = new Label();
        bestLocation = new Label();
        bestReward = new Label();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();

    }

    public Game(Label nameLabel) {
        hero = new Hero(new Label(), 0);
        vBox  = new VBox();
        stackPane = new StackPane();
        currLocation = new Label();
        currReward = new Label();
        bestLocation = new Label();
        bestReward = new Label();
        screenAnchorPane = new AnchorPane();
        gameAnchorPane = new AnchorPane();
        screenObj = new Group();
        platforms = new ArrayList<>();
        coins = new ArrayList<>();

    }

    public void start(Stage stage) throws IOException {

        gameAnchorPane  = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Game.fxml")));
        screenAnchorPane =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ArenaScreen.fxml")));
        gameAnchorPane.setBackground(StaticFunction.defaultBackground());
        stackPane.getChildren().add(gameAnchorPane);
        stackPane.getChildren().add(screenAnchorPane);

        Image icon = new Image(new FileInputStream(Objects.requireNonNull(StaticFunction.class.getResource("mainIcon.png")).getPath()));
        stage.setTitle("WillHero: " + hero.getName().getText());
        stage.getIcons().add(icon);
        vBox.getChildren().add(stackPane);
        Scene scene = new Scene(vBox);
        buildWorld(coins, platforms,screenObj);
        stackPane.setOnKeyPressed(new EventHandler<KeyEvent> () {

            @Override
            public void handle(KeyEvent event)
            {
                Node node=(Node) event.getSource();
                VBox vBox=(VBox) node.getScene().getRoot();
                StackPane stackPane = (StackPane) vBox.getChildren().get(0);

                System.out.println(hero.getHero().getX() + " " + hero.getHero().getY());
                if(stackPane.getChildren().size() == 2) return;

                if(event.getCode() == KeyCode.RIGHT)
                    hero.getHero().setX(hero.getHero().getX() + 5);
                if(event.getCode() == KeyCode.LEFT)
                    hero.getHero().setX(hero.getHero().getX() - 5);
                if(event.getCode() == KeyCode.UP)
                    hero.getHero().setY(hero.getHero().getY() + 5);
                if(event.getCode() == KeyCode.DOWN)
                    hero.getHero().setY(hero.getHero().getY() - 5);
                if(event.getCode() == KeyCode.SPACE)
                    for (Node object : screenObj.getChildren()) {
                        ImageView imageView = (ImageView) object;
                        imageView.setX(imageView.getX() + 50 );
                    }
                if(event.getCode().isDigitKey()){
                    for (Node object : screenObj.getChildren()) {
                        ImageView imageView = (ImageView) object;
                        imageView.setX(imageView.getX() -30);
                    }
                }
            }
        });

        stage.setScene(scene);
        stage.show();
        tl = new Timeline(new KeyFrame(Duration.millis(20), e -> playGame(gameAnchorPane, hero, platforms, screenObj, currLocation, currReward)));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StaticFunction.setTranslation(floatingName, 0, 100, 1000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island2, 0, 25, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island11, 0, -25, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island211, 0, 25, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island21, 1, 20, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island011, 0, -25, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(island1, 0, 25, 2000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setTranslation(greenOrc1, 0, 40, 1000,TranslateTransition.INDEFINITE, true);
        StaticFunction.setRotation(resumeIcon,360, 1000,2, true);
        StaticFunction.setRotation(restartIcon,360, 1000, 2,true);
        StaticFunction.setRotation(pauseIcon,360, 1000, 2,true);
        StaticFunction.setRotation(saveIcon,360, 1000, 2,true);
        StaticFunction.setRotation(mainMenuIcon,360, 1000, 2,true);
        StaticFunction.bestLocation(bestLocation);
        StaticFunction.bestReward(bestReward);
    }

    @FXML
    public void buildWorld(ArrayList<Coin> coins, ArrayList<Platform> platforms, Group screenObj) {
        //Initialize Game
        platforms.add(new Platform(3, 100, 350, 200, 50));
        platforms.add( new Platform(0, 400, 350, 600, 0));
        ImageView gate = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("gate.png"))));
        gate.setPreserveRatio(true); gate.setFitWidth(220); gate.setLayoutX(350); gate.setLayoutY(150);
        ImageView gif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("WN8R.gif"))));
        gif.setPreserveRatio(true); gif.setFitWidth(190); gif.setLayoutX(364); gif.setLayoutY(160);
        screenObj.getChildren().addAll(platforms.get(0).getIsLand(), gif,gate, platforms.get(1).getIsLand());

        Group platform = getPlatforms(platforms);
//        GreenOrc g=new GreenOrc(1450,250);
        CoinChest co=new CoinChest(1850,300);
        RedOrc r=new RedOrc(2500,250);
//        Group coin = getCoinCluster(920, 300, coins);
//        Group coin2=getCoinCluster(1800, 300, coins);
//        Group coin3=new Group();
//        coin3.getChildren().addAll(coin,coin2);
        WindMill windMill = new WindMill(platforms.get(5).getIsLand().getX() + platforms.get(5).getIsLand().getBoundsInParent().getWidth() / 2, platforms.get(5).getIsLand().getY() - 300);
        screenObj.getChildren().add(r.getOrc());
        screenObj.getChildren().add(co.getChest());
//        screenObj.getChildren().addAll(coin.getChildren());
        screenObj.getChildren().addAll(platform.getChildren());
        screenObj.getChildren().addAll(windMill.getWindMill().getChildren());
//        gameAnchorPane.getChildren().add()
        gameAnchorPane.getChildren().addAll(screenObj);
        gameAnchorPane.getChildren().add( hero.getHero());
        hero.setScoreLabel(currReward,currLocation);
    }

    private Group getPlatforms(ArrayList<Platform> platforms) {
        Group group = new Group();
        int z=0;
        for(int i = 0; i < 20; i++) {

            if(i+3 > 4) {
                int n = (i+3) % 4;
                int speed = 0;
                if(i*25 % 40 == 0) {
                    speed = 50;
                }
                int x = (int) (platforms.get(platforms.size()-1).getIsLand().getX() + platforms.get(platforms.size()-1).getIsLand().getFitWidth() + 200);
                Platform platform  = new Platform(n, x, 350, n*100, speed);
                if(z%3==0) {
                    Group coi = getCoinCluster(x, 300, coins);
                    if (i%3==0){
                        GreenOrc g=new GreenOrc(x-30,250);
                        screenObj.getChildren().add(g.getOrc());
                    }
                    screenObj.getChildren().addAll(coi.getChildren());
                }
                platforms.add(platform);
                group.getChildren().addAll(platform.getIsLand());
            }
            z++;
        }
        return group;
    }

    private Group getCoinCluster(int x,int y,ArrayList<Coin> coins){
        Group group=new Group();

        for(int i=0;i<3;i++) {
            Coin c1 = new Coin(x+60*i, y);
            Coin c2=new Coin(x+60*i,y-55);
            group.getChildren().addAll(c1.getCoin(),c2.getCoin());
            coins.add(c1);
            coins.add(c2);
        }
        return group;
    }

    @FXML
    public void playGame(AnchorPane gameAnchorPane, Hero hero, ArrayList<Platform> platforms, Group screenObj, Label currReward, Label currLocation) {

        boolean gravity;

        if(hero.getHero().getY() > 480) {
            hero.setAlive(false);
            StaticFunction.setRotation(hero.getHero(), 360, 100, -1, true);

            if(hero.getHero().getY() > 700){
                System.out.println("\t/h/h/h/hgame over");
                gameOver(gameAnchorPane);
            }
        }
        gravity = true;

        for (Platform platform : platforms) {
            if(platform.collided(hero.getHero(), screenObj)) {
                gravity = false;
                hero.jump();
            }
        }

        gravity(hero.getHero(), gravity);
    }

    public void gravity(ImageView node, boolean isGravity) {
        if(isGravity) {
            System.out.println("Gravity");
            node.setY(node.getY() + 4);
        }
    }


    public void gameOver(AnchorPane gameAnchorPane) {
        tl.pause();
        gravity(hero.getHero(), false);
        System.out.println("Game Over");
        VBox vBox=(VBox)gameAnchorPane.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);

        try {
            Parent loadGameOver =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("GameOver.fxml")));
            stackPane.getChildren().add(loadGameOver);

        } catch (IOException e) {
            System.out.println("ArenaScreen couldn't be loaded");
            e.printStackTrace();
        }
    }

    @FXML
    public void pause(MouseEvent pause) {
        StaticFunction.clickResponse(this.pauseIcon);

        Node node=(Node) pause.getSource();
        VBox vBox=(VBox)node.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);

        try {
            Parent loadArena =  FXMLLoader.load(Objects.requireNonNull(getClass().getResource("ArenaScreen.fxml")));
            stackPane.getChildren().add(loadArena);

        } catch (IOException e) {
            System.out.println("ArenaScreen couldn't be loaded");
            e.printStackTrace();
        }
    }


    @FXML
    public void resumeGame(MouseEvent resume) {
        StaticFunction.clickResponse(this.resumeIcon);

        Node node=(Node) resume.getSource();
        VBox vBox=(VBox) node.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);
        try {
            stackPane.getChildren().remove(stackPane.getChildren().get(1));
            System.out.println("\nremoved ");
        } catch (NullPointerException e){
            System.out.println("Already Deleted");
        }
    }


    public void mainMenu(MouseEvent mainMenu) {
        StaticFunction.clickResponse(this.mainMenuIcon);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to go to MainMenu?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Back to Main Menu");
        alert.initStyle(StageStyle.UNDECORATED);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() &&  result.get() == ButtonType.YES){
            tl.stop();
            MainMenu _mainMenu = new MainMenu();
            try {
                _mainMenu.start(StaticFunction.getStage(mainMenu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void save(MouseEvent save)  {
        StaticFunction.clickResponse(this.saveIcon);

        Alert alert = new Alert(Alert.AlertType.INFORMATION,"DO you want to save the game?", ButtonType.OK);
        alert.setTitle("Save Prompt");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
    }


    public void restart(MouseEvent restart) {
        StaticFunction.clickResponse(this.restartIcon);
        tl.stop();
        Label nameLabel = hero.getName();
        World world = new World();
        world.start(StaticFunction.getStage(restart), nameLabel);
    }

    public void resurrect(MouseEvent resurrect) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION,"You dont have enough coins to resurrect.", ButtonType.OK);
        alert.setTitle("Resurrect");
        alert.initStyle(StageStyle.UNDECORATED);
        alert.showAndWait();
        tl.stop();
    }
}