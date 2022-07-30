package WillHero;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Game implements Initializable {

    @FXML
    private transient ImageView tempIl1;
    @FXML
    private transient ImageView tempIl2;
    @FXML
    private transient ImageView tempIl3;
    @FXML
    private transient ImageView tempIl4;
    @FXML
    private transient ImageView tempIl5;
    @FXML
    private transient ImageView tempIl6;
    @FXML
    private transient Text floatingName;

    @FXML
    private transient ImageView pauseIcon;
    @FXML
    private transient AnchorPane gameAnchorPane;
    @FXML
    private transient Label currLocation;
    @FXML
    private transient Label currReward;
    @FXML
    private transient Label bestLocation = new Label();
    @FXML
    private transient Label bestReward = new Label();
    @FXML
    private final transient ProgressBar progressBar = new ProgressBar();


    private Hero hero;
    private StackPane stackPane;
    private Group screenObj;
    private static ArrayList<Platform> platforms;
    private static ArrayList<Orc> orcs;
    private static ArrayList<Chest> chests;
    private static ArrayList<Obstacle> tnts;
    private ArrayList<Coin> coins;
    private ArrayList<ImageView> gate;
    private ArenaScreen arenaScreen;
    private static ArrayList<Hero> heroWrap;
    private MediaPlayer attackMedia;

    private double accumulate;
    private static double hSpeed = 0;
    private double dx = 0.3;
    private double xDist = 0;
    private Timeline tlh;

    private static Timeline tl;
    Camera camera;

    public void start(Stage stage, String name, VBox vBox, StackPane stackPane, AnchorPane gameAnchorPane) throws IOException {
        hero = new Hero(name, 0, 0, true, false, new Helmet(new Weapon1(5, 100, false, false, 0, 0), new Weapon2(3, 100, false, false,0 ,0), 0), 0, 450, 150);
        this.stackPane = stackPane;
        this.gameAnchorPane = gameAnchorPane;
        this.camera = new PerspectiveCamera();

        this.bestLocation = new Label();
        this.bestReward = new Label();
        this.screenObj = new Group();
        this.gate = new ArrayList<>();
        this.arenaScreen = new ArenaScreen();
        this.coins = new ArrayList<>();
        heroWrap = new ArrayList<>();
        heroWrap.add(hero);

        platforms = new ArrayList<>();
        orcs = new ArrayList<>();
        chests = new ArrayList<>();
        tnts = new ArrayList<>();

        stage.getScene().setCamera(camera);
        customWorld();
        gameInput(stackPane, gameAnchorPane, hero);

        loadScreen();
        Media  attackSound;


        attackSound = new Media(new File(Objects.requireNonNull(getClass().getResource("heroDie2.mp3")).getFile()).toURI().toString());
        attackMedia = new MediaPlayer(attackSound);
        attackMedia.setStartTime(Duration.millis(10));
        attackMedia.setCycleCount(1);
        attackMedia.setOnEndOfMedia(() -> {
            attackMedia.seek(Duration.ZERO);
            if(attackMedia.getStatus().equals(MediaPlayer.Status.PLAYING))
                attackMedia.pause();
        });
    }

    public void resume(Stage stage, Hero hero, VBox vBox, StackPane stackPane, AnchorPane gameAnchorPane,
                       ArrayList<Platform> generatedPlatforms, ArrayList<Coin> generatedCoins,
                       ArrayList<Obstacle> generatedObstacles, ArrayList<Chest> generatedChests,
                       ArrayList<Orc> generatedOrcs) throws IOException {
        this.hero = hero;
        this.stackPane = stackPane;
        this.gameAnchorPane = gameAnchorPane;
        this.camera = new PerspectiveCamera();

        this.bestLocation = new Label();
        this.bestReward = new Label();
        this.screenObj = new Group();
        this.coins = generatedCoins;

        platforms = generatedPlatforms;
        orcs = generatedOrcs;
        chests = generatedChests;
        tnts = generatedObstacles;
        heroWrap = new ArrayList<>();
        heroWrap.add(hero);

        gate = new ArrayList<>();
        arenaScreen = new ArenaScreen();
        stage.getScene().setCamera(camera);

        gameInput(stackPane, gameAnchorPane, hero);


        if (hero.isAlive())
            loadScreen();

        Media  attackSound;


        attackSound = new Media(new File(Objects.requireNonNull(getClass().getResource("heroDie2.mp3")).getFile()).toURI().toString());
        attackMedia = new MediaPlayer(attackSound);
        attackMedia.setStartTime(Duration.millis(10));
        attackMedia.setCycleCount(1);
        attackMedia.setOnEndOfMedia(() -> {
            attackMedia.seek(Duration.ZERO);
            if(attackMedia.getStatus().equals(MediaPlayer.Status.PLAYING))
                attackMedia.pause();
        });
    }

    private void gameInput(StackPane stackPane, AnchorPane gameAnchorPane, Hero hero) {
        buildWorld(screenObj);

        stackPane.setOnKeyPressed(event -> {

            Node node = (Node) event.getSource();
            VBox vBox = (VBox) node.getScene().getRoot();
            StackPane stackPane1 = (StackPane) vBox.getChildren().get(0);

//            System.out.println(hero.getHero().getX() + " " + hero.getHero().getY());
            if (stackPane1.getChildren().size() == 2) return;

            if (event.getCode() == KeyCode.SPACE) {
                if (tlh != null && tlh.getStatus() == Animation.Status.RUNNING) {
                    tlh.stop();
                }

                xDist = 0;
                dx = dx>=0? dx: -dx;

                hero.useWeapon();
                if(accumulate >= 80){
                    accumulate = 0;
                    hero.setLocation(hero.getLocation() + 1);
                }
                currLocation.setText((Integer.parseInt(currLocation.getText())+100)/100 + "");
                hero.getTl().pause();

                tlh = new Timeline(new KeyFrame(Duration.millis(5), e -> moveObject()));
                tlh.setCycleCount(30);
                tlh.setOnFinished(e -> {tlh.stop();
                    hero.getTl().play(); xDist = 0; dx = dx>0? dx: -dx;hSpeed=0;});
                tlh.play();
            }
        });

        tl = new Timeline(new KeyFrame(Duration.millis(5), e -> playGame(gameAnchorPane, hero)));
        tl.setCycleCount(Timeline.INDEFINITE);
        tl.play();
    }

    private void moveObject() {
        hSpeed+=dx;
        xDist+=hSpeed;
        accumulate += hSpeed;

        double maxXDist = 150;
        if(xDist > maxXDist /2){
            dx = dx>0? -dx: dx;
        }

        for (Node object : screenObj.getChildren()) {
            ImageView imageView = (ImageView) object;
            imageView.setX(imageView.getX() - hSpeed);
        }
        for (Platform platform : platforms) {
            platform.setX(platform.getX() - hSpeed);
        }
        for (Orc orc : orcs) {
            orc.setX(orc.getX() - hSpeed);
        }
        for (Chest chest : chests) {
            chest.setX(chest.getX() - hSpeed);
        }
        for (Obstacle tnt : tnts) {
            tnt.setX(tnt.getX() - hSpeed);
        }
        for (Coin coin : coins) {
            coin.setX(coin.getX() - hSpeed);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StaticFunction.setTranslation(floatingName, 0, 100, 1000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl1, 0, 25, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl2, 0, -25, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl3, 0, 25, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl4, 1, 20, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl5, 0, -25, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl6, 0, 25, 2000, TranslateTransition.INDEFINITE);
        StaticFunction.setTranslation(tempIl6, 0, 40, 1000, TranslateTransition.INDEFINITE);
        StaticFunction.setRotation(pauseIcon, 1000, 2, true);

        StaticFunction.bestLocation(bestLocation);
        StaticFunction.bestReward(bestReward);
    }


    public static ArrayList<Orc> getOrcList() {
        return orcs;
    }

    public static ArrayList<Platform> getPlatformList() {
        return platforms;
    }

    public static ArrayList<Chest> getChestList() {
        return chests;
    }

    public static ArrayList<Obstacle> getTntList() {
        return tnts;
    }

    public static double getHSpeed() {
        return hSpeed;
    }

    public static void sethSpeed(double hSpeed) {
        Game.hSpeed = hSpeed;
    }

    public static ArrayList<Hero> getHeroWrap() {
        return heroWrap;
    }

    @FXML
    public void buildWorld(Group screenObj) {
        //Initialize Game

        ImageView gat = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("gate.png"))));
        gat.setPreserveRatio(true);
        gat.setFitWidth(220);
        gat.setLayoutX(350 - hero.getLocation() * 150);
        gat.setLayoutY(135);

        ImageView gif = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("WN8R.gif"))));
        gif.setPreserveRatio(true);
        gif.setFitWidth(190);
        gif.setLayoutX(364 - hero.getLocation() * 150);
        gif.setLayoutY(160);
        gate.add(gif);
        gate.add(gat);

        ArrayList<Coin> coi = getCoinCluster(700, 260);
        coins.addAll(coi);
        ArrayList<Coin> coi2 = getCoinCluster(1000, 200);
        coins.addAll(coi2);

        for (ImageView imageView : gate) {
            screenObj.getChildren().add(imageView);
        }
        for (Platform platform : platforms) {
            screenObj.getChildren().add(platform.getIsLand());
        }
        for (Coin coin : coins) {
            screenObj.getChildren().add(coin.getCoin());
        }
        for (Chest chest : chests) {
            screenObj.getChildren().add(chest.getChest());
        }
        for (Obstacle obstacle : tnts) {
            screenObj.getChildren().add(obstacle.getObstacle());
        }
        for (Orc orc : orcs) {
            screenObj.getChildren().add(orc.getOrc());
        }

        gameAnchorPane.getChildren().addAll(screenObj);
        gameAnchorPane.getChildren().addAll(hero.getHero(), hero.getHelmet().getWeapon(0).getWeaponImage(), hero.getHelmet().getWeapon(1).getWeaponImage());
        StaticFunction.setStartit(true);
    }


    private void customWorld() {
        platforms.add(new Platform(3, 0, 350, 200, 50));
        platforms.add(new Platform(0, 300, 350, 600, 0));

        Random random = new Random();
        for (int i = 0; i < 36; i++) {
            if (i + 3 > 4) {
                int speed = 1;
                if (i * 25 % 40 == 0) {
                    speed = 50;
                }
                int x = (int) (platforms.get(platforms.size() - 1).getIsLand().getX() + platforms.get(platforms.size() - 1).getIsLand().getFitWidth() + 150);
                Platform platform = new Platform(random.nextInt(0, 5), x, random.nextInt(300, 350), random.nextInt(2, 4) * 100, speed);
                if ((i + 1) % 2 == 0) {
                    GreenOrc g = new GreenOrc(x + 40, 250);
                    orcs.add(g);
                }
                if (i % 5 == 0) {
                    ArrayList<Coin> coi = getCoinCluster(x, random.nextInt(130, 190));
                    coins.addAll(coi);
                }
                if ((i - 1) % 6 == 0 || (i - 2) % 6 == 0) {
                    Obstacle tnt = new Tnt(x + 60, 250);
                    tnts.add(tnt);
                }

                if (i % 3 == 0) {
                    if (i % 2 == 1) {
                        GreenOrc g = new GreenOrc(x, 250);
                        orcs.add(g);
                    } else {
                        Orc g = new RedOrc(x - 10, 250);
                        orcs.add(g);
                    }

                    ArrayList<Coin> coi = getCoinCluster(x, random.nextInt(130, 190));
                    coins.addAll(coi);
                }

                if (i % 4 == 0) {

                    ArrayList<Coin> coi = getCoinCluster(x, random.nextInt(260, 300));
                    coins.addAll(coi);
                    Chest c;
                    if (i % 8 == 0) {
                        c = new CoinChest(x, 200);
                    } else {
                        c = new WeaponChest(x, 200);
                    }
                    chests.add(c);
                }

                platforms.add(platform);
            }
        }

        int x = (int) (platforms.get(platforms.size() - 1).getIsLand().getX() + platforms.get(platforms.size() - 1).getIsLand().getFitWidth());
        platforms.add(new Platform(0, x + 150, 350, 500, 50));
        platforms.add(new Platform(4, x + 500 + 140, 350, 420, 50));
        Orc boss = new BossOrc(x + 200 + 150, 200);
        orcs.add(boss);

        x = (int) (platforms.get(platforms.size() - 1).getIsLand().getX() + platforms.get(platforms.size() - 1).getIsLand().getFitWidth());
        platforms.add(new Platform(4, x + 150, 350, 500, 0));
        chests.add(new CoinChest(x + 70, 200));
    }


    public void loadScreen() throws IOException {

        FXMLLoader root_screenAnchorPane = new FXMLLoader(Objects.requireNonNull(getClass().getResource("ArenaScreen.fxml")));
        AnchorPane screenAnchorPane = root_screenAnchorPane.load();
        stackPane.getChildren().add(screenAnchorPane);
        arenaScreen = root_screenAnchorPane.getController();
        arenaScreen.start(tl, hero, chests, orcs, coins, tnts, platforms);
    }


    private ArrayList<Coin> getCoinCluster(int x, int y) {
        ArrayList<Coin> coins = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Coin c1 = new Coin(x + 60 * i, y);
            Coin c2 = new Coin(x + 60 * i, y - 55);
            coins.add(c1);
            coins.add(c2);
        }
        return coins;
    }

    @FXML
    public void playGame(AnchorPane gameAnchorPane, Hero hero) {
        if(!StaticFunction.getStartit()){
            return;
        }

        entityCollision();

        garbageRemover(gameAnchorPane);
        setScoreLabel();
        try {
            if (Math.abs(hero.getHero().getX() - getOrcList().get(getOrcList().size() - 1).getOrc().getX()) <= 250) {
                if (getOrcList().get(getOrcList().size() - 1).getOrc().getX() <= hero.getHero().getX()) {
                    ((BossOrc) (getOrcList().get(getOrcList().size() - 1))).sethSpeed(0.07);
                } else {
                    ((BossOrc) (getOrcList().get(getOrcList().size() - 1))).sethSpeed(-0.07);
                }
            } else {
                ((BossOrc) (getOrcList().get(getOrcList().size() - 1))).sethSpeed(0);
            }
        }
        catch (ClassCastException | IndexOutOfBoundsException e){
            System.out.println("Boss is dead");
        }

        if (hero.getHero().getY() > 480) {
            hero.setAlive(false);

            if (hero.getHero().getY() > 700) {
                gameOver(gameAnchorPane);
            }
        }

        if (!hero.isAlive()) {

            hero.getHero().setScaleY(0.5);

            gameOver(gameAnchorPane);
        }
    }


    private void entityCollision() {
        coins.removeIf(coin -> coin.collision(hero));
        for (Chest chest : chests) chest.collision(hero);
        for (Orc orc : orcs) {
            hero.getHelmet().getWeapon(hero.getHelmet().getChoice()).collision(orc);
            if(orc.getY() > 600){
                orc.setAlive(false);
            }
            if (!orc.isAlive()) {
                if(orc instanceof BossOrc){
                    gameWin(gameAnchorPane);
                }
                orc.destroy();
                orc.increaseReward(hero);
                if (orc.getOrc().getY() > 900) {
                    orc.getOrc().setVisible(false);
                }
            }
        }
    }

    private void garbageRemover(AnchorPane pane) {
        try {

            for (Obstacle tnt : tnts) {
                if (!tnt.getObstacle().isVisible()) {
                    tnts.remove(tnt);
                    tnt = null;
                }
            }

            for (Orc orc : orcs) {
                if (!orc.getOrc().isVisible()) {
                    orcs.remove(orc);
                    orc = null;
                }
            }
            for (Coin coin : coins) {
                if (!coin.getCoin().isVisible()) {
                    coins.remove(coin);
                    coin = null;
                }
            }

        } catch (ClassCastException e) {
            System.out.println("Error");
        } catch (ConcurrentModificationException ignored) {
        }
    }


    private void setScoreLabel() {
        progressBar.setProgress(hero.getLocation() / 1400.0); ///not working
        currReward.setText("" + hero.getReward());
        currLocation.setText(hero.getLocation() + "");
    }

    public void gameWin(AnchorPane gameAnchorPane) {
        tl.pause();
        System.out.println("Game Win");
        VBox vBox = (VBox) gameAnchorPane.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);

        try {
            FXMLLoader gameOverFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource("GameWin.fxml")));
            AnchorPane gameOverPane = gameOverFXML.load();
            stackPane.getChildren().add(gameOverPane);
            GameOver gameOver = gameOverFXML.getController();
            gameOver.start(tl, hero, stackPane, gameOverPane);

        } catch (IOException e) {
            System.out.println("ArenaScreen couldn't be loaded");
            e.printStackTrace();
        }
    }


    public void gameOver(AnchorPane gameAnchorPane) {
        tl.pause();
        if(attackMedia!=null) {
            attackMedia.play();
        }

        System.out.println("Game Over");
        VBox vBox = (VBox) gameAnchorPane.getScene().getRoot();
        StackPane stackPane = (StackPane) vBox.getChildren().get(0);

        try {
            FXMLLoader gameOverFXML = new FXMLLoader(Objects.requireNonNull(getClass().getResource("GameOver.fxml")));
            AnchorPane gameOverPane = gameOverFXML.load();
            stackPane.getChildren().add(gameOverPane);
            GameOver gameOver = gameOverFXML.getController();
            gameOver.start(tl, hero, stackPane, gameOverPane);

        } catch (IOException e) {
            System.out.println("ArenaScreen couldn't be loaded");
            e.printStackTrace();
        }
    }

    @FXML
    public void pause() {
        StaticFunction.clickResponse(this.pauseIcon);
        try {
            loadScreen();

        } catch (IOException e) {
            System.out.println("ArenaScreen couldn't be loaded");
            e.printStackTrace();
        }
    }
}
