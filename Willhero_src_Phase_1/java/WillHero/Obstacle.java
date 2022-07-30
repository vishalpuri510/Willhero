package WillHero;

import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import java.util.Objects;

public class Obstacle {


}

class WindMill extends Obstacle {

    Group windMill;
    static ImageView blade1;
    static ImageView blade2;
    static ImageView blade3;
    ImageView building;


    public WindMill(double x, double y) {
        windMill = createChakki(x, y);

    }

    private Group createChakki(double x, double y){
        blade1 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("blade.png"))));
        blade2 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("blade.png"))));
        blade3 = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("blade.png"))));
        building = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("building.png"))));

        blade1.setPreserveRatio(true);
        blade2.setPreserveRatio(true);
        blade3.setPreserveRatio(true);

        blade1.setRotate(90);
        blade2.setRotate(210);
        blade3.setRotate(-30);

        imageViewSet(blade1, x-40, y+125);
        imageViewSet(blade2, x-125, y-23);
        imageViewSet(blade3, x+47, y-22);

        building.setFitWidth(100);
        building.setFitHeight(300);
        building.setX(x); building.setY(y);

        Group blades = new Group();
        blades.getChildren().addAll (blade1, blade2, blade3);
        blades.setAutoSizeChildren(true);
        setRotation(blades, x, y);

        Group chakki = new Group();
        chakki.getChildren().addAll(building,blade3,blade2,blade1);
//        chakki.getChildren().addAll(blades);

        return chakki;
    }

    public void setRotation(Node node, double x, double y){
        RotateTransition rotateTransition = new RotateTransition(javafx.util.Duration.millis(1000), node);
        rotateTransition.setNode(node);
        rotateTransition.setAxis(Rotate.Z_AXIS);

        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount(RotateTransition.INDEFINITE);
        rotateTransition.play();
    }

    private void imageViewSet(ImageView node, double x, double y) {
        node.setX(x);
        node.setY(y);
        node.setFitHeight(40);
    }

    public Group getWindMill() {
        return windMill;
    }

    public void start(Stage stage) {
        AnchorPane anchorPane = new AnchorPane();
//        anchorPane.setPrefSize(1360,768);
        WindMill windMill = new WindMill(200,200);
        Group mill = windMill.getWindMill();
        anchorPane.getChildren().add(mill);
        Scene scene = new Scene(anchorPane);

        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event)
            {
                System.out.println("blade1: " + blade3.getX() + " 3 " + blade3.getY());
                if(event.getCode() == KeyCode.RIGHT)
                    blade3.setX(blade3.getX() + 1);
                if(event.getCode() == KeyCode.LEFT)
                    blade3.setX(blade3.getX() - 1);
                if(event.getCode() == KeyCode.UP)
                    blade3.setY(blade3.getY() + 1);
                if(event.getCode() == KeyCode.DOWN)
                    blade3.setY(blade3.getY() - 1);
            }
        });


        stage.setScene(scene);
        stage.show();

    }

    //
//    public int getRotation() {
//        return rotation;
//    }
//
//    public int getSpeed() {
//        return speed;
//    }
//
//    public int getDirection() {
//        return direction;
//    }


}