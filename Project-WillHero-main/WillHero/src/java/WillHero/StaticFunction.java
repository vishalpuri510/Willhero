package WillHero;

import javafx.animation.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class StaticFunction {

    private static final ReGenerateHero reGenerateHero = new ReGenerateHero();
    private static final ArrayList<Hero> heroes = new ArrayList<>();
    private static final ArrayList<String> heroNames = new ArrayList<>();
    private static boolean startit = false;
    private static boolean ft=false;
    private static boolean play=true;
    private static MediaPlayer mediaPlayer;

    static void getPlayers(ArrayList<String> players) {

        for (String player : players) {
            try {
                Hero hero = reGenerateHero.getHero(player);
                if (!heroNames.contains(hero.getName())) {
                    heroes.add(hero);
                    heroNames.add(hero.getName());
                } else if (!(hero.getLocation() == heroes.get(heroNames.indexOf(hero.getName())).getLocation() )|| !(hero.getReward() == heroes.get(heroNames.indexOf(hero.getName())).getReward())) {
                    heroes.set(heroNames.indexOf(hero.getName()), hero);
                }
            }catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    static ArrayList<Hero> getHeroes() {
        return heroes;
    }

    static void bestReward(Label label) {
        int reward = 0;
        for (Hero hero : heroes) {
            if (hero.getReward() > reward) {
                reward = hero.getReward();
            }
        }
        label.setText(String.valueOf(reward));
    }

    static void bestLocation(Label label) {
        int location = 0;
        for (Hero hero : heroes) {
            if (hero.getLocation() > location) {
                location = hero.getLocation();
            }
        }
        label.setText(String.valueOf(location));
    }

    static void clickResponse(ImageView image) {
        ScaleTransition scale = new ScaleTransition();
        scale.setNode(image);
        scale.setDuration(Duration.millis(200));
        scale.setCycleCount(2);
        scale.setAutoReverse(true);
        scale.setByY(0.2);
        scale.setByX(0.2);
        scale.play();
    }

    static Stage getStage(MouseEvent mouseEvent) {
        Node node = (Node) mouseEvent.getSource();
        return (Stage) node.getScene().getWindow();
    }

    static void setScene(Stage stage, URL toFrame, String title) throws IOException {

        Image icon = new Image(new FileInputStream(Objects.requireNonNull(StaticFunction.class.getResource("mainIcon.png")).getPath()));
        stage.setTitle("WillHero: " + title);
        stage.getIcons().add(icon);

        FXMLLoader loader = new FXMLLoader(toFrame);
        AnchorPane root = loader.load();
        root.setBackground(defaultBackground());
        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    static void setTranslation(Node node, float xMotion, float yMotion, int duration, int cycle) {
        TranslateTransition trFloatingName = new TranslateTransition();
        trFloatingName.setNode(node);
        trFloatingName.setByX(xMotion);
        trFloatingName.setByY(yMotion);
        trFloatingName.setDuration(Duration.millis(duration));
        trFloatingName.setCycleCount(cycle);
        trFloatingName.setAutoReverse(true);
        trFloatingName.play();
    }

    static void setRotation(Node node, int duration, int cycle, boolean reverse) {
        RotateTransition trFloatingName = new RotateTransition();
        trFloatingName.setNode(node);
        trFloatingName.setAxis(Rotate.Z_AXIS);
        trFloatingName.setInterpolator(Interpolator.LINEAR);
        trFloatingName.setDuration(Duration.millis(duration));
        trFloatingName.setCycleCount(cycle);
        trFloatingName.setByAngle(360);
        trFloatingName.setAutoReverse(reverse);
        trFloatingName.play();
    }

    static void setScaling(Node node) {
        ScaleTransition scaleTransition = new ScaleTransition();
        scaleTransition.setDuration(Duration.millis(50));
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.setByX(0);
        scaleTransition.setByY(-0.1);
        scaleTransition.setNode(node);
        scaleTransition.play();
    }
  static void setSounds() {
        if(mediaPlayer!=null)
        mediaPlayer.stop();
      if(mediaPlayer==null){
          Media sound;
          sound = new Media(new File(Objects.requireNonNull(StaticFunction.class.getResource("background.mp3")).getFile()).toURI().toString());
          mediaPlayer = new MediaPlayer(sound);
          mediaPlayer.setStartTime(Duration.seconds(0));
//            mediaPlayer.setStartTime(Duration.seconds(100));
          mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);

      }
      if(!mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)){
      }

      if(play)
            mediaPlayer.play();
  }

    static void setPlay(Boolean p){
        play=p;
    }
    static Boolean getPlay(){
        return play;
    }

    static void setStartit(Boolean p){
        startit=p;
    }
    static Boolean getStartit(){
        return startit;
    }

    static Background defaultBackground() {
        String startBg = "#01D9F8FF";
        if(ft){
            startBg ="000000";

        }
        else{
            startBg ="#01D9F8FF";
        }
        String endBg = "#C4F4FEFF";
        return new Background(new BackgroundFill(
                new LinearGradient(
                        0, 0, 0, 1, true,                  //sizing
                        CycleMethod.NO_CYCLE,                 //cycling
                        new Stop(0, Color.web(startBg)),    //colors
                        new Stop(1, Color.web(endBg))
                ), CornerRadii.EMPTY, Insets.EMPTY)
        );
    }

    static void setFt(Boolean f){
        ft=f;
    }
    static Boolean getFt(){
        return ft;
    }



    static void mainMenuFn(MouseEvent mainMenu, ImageView mainMenuIcon, Timeline timeline) {
        StaticFunction.clickResponse(mainMenuIcon);

        mainMenu(mainMenu, timeline);
    }

    static void mainMenu(MouseEvent mainMenu, Timeline timeline) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"Do you want to go to MainMenu?", ButtonType.YES, ButtonType.NO);
        alert.setTitle("Back to Main Menu");
        alert.initStyle(StageStyle.UNDECORATED);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() &&  result.get() == ButtonType.YES){
            timeline.stop();
            MainMenu _mainMenu = new MainMenu();
            try {
                _mainMenu.start(StaticFunction.getStage(mainMenu));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void setHeroTable(TableView<Hero> table, ArrayList<Hero> heroes, TableColumn<Hero, String> name, TableColumn<Hero, Integer> reward, TableColumn<Hero, Integer> location) {
        ObservableList<Hero> data = table.getItems();
        data.addAll(heroes);
        table.setItems(data);

        name.setCellValueFactory(new PropertyValueFactory<>( "name" ));
        reward.setCellValueFactory(new PropertyValueFactory<>( "reward" ));
        location.setCellValueFactory(new PropertyValueFactory<>( "location" ));
    }

    static boolean bottomCollision(ImageView node1, ImageView node2, double margin) {
//        node1 ka bottom collides with node2 ka top
        return node1.getBoundsInParent().intersects(node2.getBoundsInParent()) &&
                node1.getBoundsInParent().getMaxY() >= node2.getBoundsInParent().getMinY() &&
                node1.getBoundsInParent().getMaxY() <= node2.getBoundsInParent().getMinY() + margin &&
                node1.getBoundsInParent().getMinY() <= node2.getBoundsInParent().getMinY() &&
                node1.getBoundsInParent().getMaxY() <= node2.getBoundsInParent().getMaxY() &&
                node1.getBoundsInParent().getMaxX() >= node2.getBoundsInParent().getMinX() &&
                node1.getBoundsInParent().getMinX() <= node2.getBoundsInParent().getMaxX();
    }

    static boolean topCollision(ImageView node1, ImageView node2, double margin) {
//        node1 ka top collides with node2 ka bottom
        return bottomCollision(node2, node1, margin);
    }

    static boolean leftCollision(ImageView node1, ImageView node2, double margin) {
//        node1 ka left collides with node2 ka right
        return node1.getBoundsInParent().intersects(node2.getBoundsInParent()) &&
                node1.getBoundsInParent().getMinX() <= node2.getBoundsInParent().getMaxX() &&
                node1.getBoundsInParent().getMinX() + margin >= node2.getBoundsInParent().getMaxX() &&

                node1.getBoundsInParent().getMaxX() >= node2.getBoundsInParent().getMaxX() &&
                node1.getBoundsInParent().getMinX() >= node2.getBoundsInParent().getMinX() &&
                node1.getBoundsInParent().getMaxY() >= node2.getBoundsInParent().getMinY() &&
                node1.getBoundsInParent().getMinY() <= node2.getBoundsInParent().getMaxY();
    }

    static boolean rightCollision(ImageView node1, ImageView node2, double margin) {
//        node1 ka right collides with node2 ka left
        return  leftCollision(node2, node1, margin);
    }
}